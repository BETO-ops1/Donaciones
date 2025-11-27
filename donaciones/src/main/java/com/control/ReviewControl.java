/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.control;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.*;
import java.io.BufferedReader;
import java.io.IOException;

import com.servicio.ReviewServicio;

@WebServlet(name = "ReviewControl", urlPatterns = {"/reviews/*"})
public class ReviewControl extends HttpServlet {

    private final Gson gson = new Gson();
    private final ReviewServicio servicio = new ReviewServicio();

    // -------------------- CORS ------------------------
    private void setCorsHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, Accept");
        response.setHeader("Access-Control-Max-Age", "3600");
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse res) throws IOException {
        setCorsHeaders(res);
        res.setStatus(HttpServletResponse.SC_OK);
    }

    // -------------------- JSON Helpers ------------------------
    private JsonObject leerJson(HttpServletRequest req, HttpServletResponse res) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = req.getReader()) {
            String linea;
            while ((linea = br.readLine()) != null) sb.append(linea);
        }

        if (sb.length() == 0) {
            JsonObject error = new JsonObject();
            error.addProperty("success", false);
            error.addProperty("mensaje", "Cuerpo vacío");
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return error;
        }

        try {
            return gson.fromJson(sb.toString(), JsonObject.class);
        } catch (JsonSyntaxException e) {
            JsonObject error = new JsonObject();
            error.addProperty("success", false);
            error.addProperty("mensaje", "JSON inválido");
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return error;
        }
    }

    private void enviar(HttpServletResponse response, JsonObject data) throws IOException {
        response.getWriter().write(gson.toJson(data));
    }


    // -------------------- GET ------------------------
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        setCorsHeaders(response);
        response.setContentType("application/json;charset=UTF-8");

        String path = request.getPathInfo();
        JsonObject r;

        if (path == null || "/".equals(path)) {
            r = servicio.listar();
        } else if (path.startsWith("/id/")) {
            String id = path.substring(4);
            r = servicio.obtenerPorId(id);
        } else if (path.startsWith("/donacion/")) {
            String idDonacion = path.substring(10);
            r = servicio.listarPorDonacion(idDonacion);
        } else {
            r = new JsonObject();
            r.addProperty("success", false);
            r.addProperty("mensaje", "Endpoint GET no encontrado");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }

        enviar(response, r);
    }


    // -------------------- POST ------------------------
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        setCorsHeaders(response);
        response.setContentType("application/json;charset=UTF-8");

        JsonObject json = leerJson(request, response);

        JsonObject r = servicio.crear(json);
        response.setStatus(HttpServletResponse.SC_CREATED);

        enviar(response, r);
    }


    // -------------------- PUT ------------------------
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        setCorsHeaders(response);
        JsonObject json = leerJson(request, response);

        JsonObject r = servicio.actualizar(json);
        enviar(response, r);
    }


    // -------------------- DELETE ------------------------
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        setCorsHeaders(response);
        JsonObject json = leerJson(request, response);

        JsonObject r = servicio.eliminar(json);
        enviar(response, r);
    }
}
