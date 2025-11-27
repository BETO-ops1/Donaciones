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

import com.servicio.DonacionServicio;

@WebServlet(name = "DonacionControl", urlPatterns = {"/donaciones/*"})
public class DonacionControl extends HttpServlet {

    private final Gson gson = new Gson();
    private final DonacionServicio servicio = new DonacionServicio();

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
    private JsonObject leerJson(HttpServletRequest request, HttpServletResponse response) throws IOException {
        StringBuilder sb = new StringBuilder();

        try (BufferedReader br = request.getReader()) {
            String linea;
            while ((linea = br.readLine()) != null) {
                sb.append(linea);
            }
        }

        if (sb.length() == 0) {
            JsonObject error = new JsonObject();
            error.addProperty("success", false);
            error.addProperty("mensaje", "Cuerpo vacío");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return error;
        }

        try {
            return gson.fromJson(sb.toString(), JsonObject.class);
        } catch (JsonSyntaxException e) {
            JsonObject error = new JsonObject();
            error.addProperty("success", false);
            error.addProperty("mensaje", "JSON inválido");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
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
        JsonObject respuesta;

        if (path == null || "/".equals(path)) {
            respuesta = servicio.listar();
        } else if (path.startsWith("/id/")) {
            String id = path.substring(4);
            respuesta = servicio.obtenerPorId(id);
        } else {
            respuesta = new JsonObject();
            respuesta.addProperty("success", false);
            respuesta.addProperty("mensaje", "Endpoint GET no encontrado");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }

        enviar(response, respuesta);
    }


    // -------------------- POST ------------------------
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        setCorsHeaders(response);
        response.setContentType("application/json;charset=UTF-8");

        JsonObject json = leerJson(request, response);
        String path = request.getPathInfo();
        JsonObject respuesta;

        if (path == null || "/".equals(path)) {
            respuesta = servicio.crear(json);
            response.setStatus(HttpServletResponse.SC_CREATED);
        } else {
            respuesta = new JsonObject();
            respuesta.addProperty("success", false);
            respuesta.addProperty("mensaje", "Endpoint POST no encontrado");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }

        enviar(response, respuesta);
    }


    // -------------------- PUT ------------------------
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        setCorsHeaders(response);
        response.setContentType("application/json;charset=UTF-8");

        JsonObject json = leerJson(request, response);
        JsonObject respuesta = servicio.actualizar(json);

        enviar(response, respuesta);
    }


    // -------------------- DELETE ------------------------
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        setCorsHeaders(response);
        response.setContentType("application/json;charset=UTF-8");

        JsonObject json = leerJson(request, response);
        JsonObject respuesta = servicio.eliminar(json);

        enviar(response, respuesta);
    }
}
