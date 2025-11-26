/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gateway;

import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class ApiGateway {

    public static void main(String[] args) throws Exception {
        int port = 3000;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        // Configura rutas
        List<Route> routes = new ArrayList<>();
        // prefix, targetBaseUrl, requireAuth
        routes.add(new Route("/api/usuarios", "http://localhost:8081", true));
        routes.add(new Route("/api/donaciones", "http://localhost:8082", true));
        routes.add(new Route("/api/auth", "http://localhost:8083", false)); // login/registro no requieren token

        // Register handlers: single handler that inspects path and forwards
        ProxyHandler handler = new ProxyHandler(routes);

        server.createContext("/", handler);
        server.setExecutor(java.util.concurrent.Executors.newCachedThreadPool());
        server.start();

        System.out.println("API Gateway iniciado en http://localhost:" + port);
    }
}
