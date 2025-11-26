/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gateway;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class ProxyHandler implements HttpHandler {

    private final List<Route> routes;
    // rate limiting: simple token bucket per IP
    private final Map<String, TokenBucket> buckets = new ConcurrentHashMap<>();
    private final int RATE_LIMIT_PER_MINUTE = 60; // ajustar (requests por minuto)

    // JWT secret (HMAC) - en producción guardarlo en variables de entorno
    private final String HMAC_SECRET = "cambia_este_secreto_por_produccion";

    public ProxyHandler(List<Route> routes) {
        this.routes = routes;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String clientIp = exchange.getRemoteAddress().getAddress().getHostAddress();
        if (!allowRequest(clientIp)) {
            sendResponse(exchange, 429, "Rate limit exceeded");
            return;
        }

        String path = exchange.getRequestURI().getPath();
        Route matched = findRoute(path);
        if (matched == null) {
            sendResponse(exchange, 404, "No route configured for: " + path);
            return;
        }

        // Auth (si aplica)
        if (matched.isRequireAuth()) {
            List<String> authHeaders = exchange.getRequestHeaders().getOrDefault("Authorization", Collections.emptyList());
            if (authHeaders.isEmpty()) {
                sendResponse(exchange, 401, "Missing Authorization header");
                return;
            }
            String auth = authHeaders.get(0);
            if (!auth.startsWith("Bearer ")) {
                sendResponse(exchange, 401, "Invalid Authorization header");
                return;
            }
            String token = auth.substring(7);
            try {
                // Validación del token (usa JWTUtils - si no usas java-jwt, cambiar por validación ligera)
                if (!JWTUtils.verifyToken(token, HMAC_SECRET)) {
                    sendResponse(exchange, 401, "Invalid token");
                    return;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                sendResponse(exchange, 401, "Error validating token");
                return;
            }
        }

        // Construir URL destino: targetBase + pathRest + query
        String pathRest = path.substring(matched.getPrefix().length());
        if (pathRest.isEmpty()) pathRest = "/";
        String targetUrl = matched.getTargetBaseUrl() + pathRest;
        String query = exchange.getRequestURI().getQuery();
        if (query != null && !query.isEmpty()) targetUrl += "?" + query;

        // Reenviar petición (proxy)
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(targetUrl).openConnection();
            conn.setRequestMethod(exchange.getRequestMethod());
            // copiar headers (except Host)
            for (Map.Entry<String, List<String>> h : exchange.getRequestHeaders().entrySet()) {
                String name = h.getKey();
                if ("Host".equalsIgnoreCase(name)) continue;
                for (String v : h.getValue()) {
                    conn.addRequestProperty(name, v);
                }
            }
            conn.setDoInput(true);

            // si hay body, enviarlo
            InputStream reqBody = exchange.getRequestBody();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            copyStream(reqBody, baos);
            byte[] bodyBytes = baos.toByteArray();
            if (bodyBytes.length > 0) {
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Length", String.valueOf(bodyBytes.length));
                OutputStream os = conn.getOutputStream();
                os.write(bodyBytes);
                os.flush();
                os.close();
            }

            // obtener respuesta
            int status = conn.getResponseCode();
            InputStream responseStream = status >= 400 ? conn.getErrorStream() : conn.getInputStream();
            ByteArrayOutputStream respBaos = new ByteArrayOutputStream();
            if (responseStream != null) copyStream(responseStream, respBaos);
            byte[] respBytes = respBaos.toByteArray();

            // copiar headers de respuesta
            HeadersUtils.copyResponseHeaders(conn, exchange);

            // enviar al cliente
            exchange.sendResponseHeaders(status, respBytes.length);
            OutputStream out = exchange.getResponseBody();
            out.write(respBytes);
            out.flush();
            out.close();
            conn.disconnect();

            System.out.println(String.format("[%s] %s -> %s (%d)", clientIp, exchange.getRequestMethod(), targetUrl, status));
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(exchange, 502, "Bad gateway: " + e.getMessage());
        }
    }

    private Route findRoute(String path) {
        // buscar la ruta con prefijo más largo coincidente
        Route best = null;
        for (Route r : routes) {
            if (path.startsWith(r.getPrefix())) {
                if (best == null || r.getPrefix().length() > best.getPrefix().length()) {
                    best = r;
                }
            }
        }
        return best;
    }

    private boolean allowRequest(String ip) {
        TokenBucket bucket = buckets.computeIfAbsent(ip, k -> new TokenBucket(RATE_LIMIT_PER_MINUTE, 1, TimeUnit.MINUTES));
        return bucket.tryConsume();
    }

    private void sendResponse(HttpExchange ex, int code, String msg) throws IOException {
        byte[] b = msg.getBytes("UTF-8");
        ex.getResponseHeaders().add("Content-Type", "text/plain; charset=utf-8");
        ex.sendResponseHeaders(code, b.length);
        OutputStream o = ex.getResponseBody();
        o.write(b);
        o.close();
    }

    private static void copyStream(InputStream in, OutputStream out) throws IOException {
        if (in == null) return;
        byte[] buffer = new byte[8192];
        int len;
        while ((len = in.read(buffer)) != -1) out.write(buffer, 0, len);
    }

    // --- Helpers inner classes ---

    // token bucket simple
    private static class TokenBucket {
        private final int maxTokens;
        private double tokens;
        private final long refillIntervalMillis;
        private final int refillAmount;
        private long lastRefillTimestamp;

        public TokenBucket(int capacityPerInterval, int refillAmount, TimeUnit unit) {
            this.maxTokens = capacityPerInterval;
            this.tokens = capacityPerInterval;
            this.refillAmount = refillAmount;
            this.refillIntervalMillis = unit.toMillis(1);
            this.lastRefillTimestamp = System.currentTimeMillis();
        }

        public synchronized boolean tryConsume() {
            refill();
            if (tokens >= 1) {
                tokens -= 1;
                return true;
            } else {
                return false;
            }
        }

        private void refill() {
            long now = System.currentTimeMillis();
            long elapsed = now - lastRefillTimestamp;
            if (elapsed <= 0) return;
            double intervals = (double) elapsed / refillIntervalMillis;
            if (intervals >= 1.0) {
                double added = intervals * refillAmount;
                tokens = Math.min(maxTokens, tokens + added);
                lastRefillTimestamp = now;
            }
        }
    }

    // small helper to copy response headers
    static class HeadersUtils {
        static void copyResponseHeaders(HttpURLConnection conn, HttpExchange ex) {
            Map<String, List<String>> map = conn.getHeaderFields();
            if (map == null) return;
            map.forEach((k, v) -> {
                if (k == null) return;
                for (String val : v) {
                    ex.getResponseHeaders().add(k, val);
                }
            });
        }
    }
}
