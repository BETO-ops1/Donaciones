/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.control;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.servicio.DonacionServicio;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

@WebServlet("/donaciones/*")
public class DonacionControl extends HttpServlet {

    private final DonacionServicio servicio = new DonacionServicio();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        setCorsHeaders(response);
        response.setContentType("application/json;charset=UTF-8");

        String path = request.getPathInfo();  
        JsonObject respuesta;

        try {
            if (path == null || path.equals("/") || path.isEmpty()) {
                respuesta = servicio.listar();         // ✔ listar()
            } else if (path.startsWith("/id/")) {
                int id = Integer.parseInt(path.substring(4));
                respuesta = servicio.obtener(id);      // ✔ obtener(id)
            } else {
                respuesta = new JsonObject();
                respuesta.addProperty("success", false);
                respuesta.addProperty("mensaje", "Ruta GET no válida");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (Exception e) {
            respuesta = new JsonObject();
            respuesta.addProperty("success", false);
            respuesta.addProperty("mensaje", "Error en GET: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        enviar(response, respuesta);
    }


    // ============================
    // POST → INSERTAR
    // ============================
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        setCorsHeaders(response);
        response.setContentType("application/json;charset=UTF-8");

        JsonObject json = leerJson(request, response);
        JsonObject respuesta;

        if (json == null) {
            respuesta = new JsonObject();
            respuesta.addProperty("success", false);
            respuesta.addProperty("mensaje", "JSON inválido");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            respuesta = servicio.insertar(json);   // ✔ llama al servicio, no al dao
        }

        enviar(response, respuesta);
    }


    // ===========================================================
    // UTILIDADES
    // ===========================================================

    private JsonObject leerJson(HttpServletRequest request, HttpServletResponse response) {
        try (BufferedReader reader = request.getReader()) {
            StringBuilder jsonBuilder = new StringBuilder();
            String linea;
            while ((linea = reader.readLine()) != null) {
                jsonBuilder.append(linea);
            }
            return gson.fromJson(jsonBuilder.toString(), JsonObject.class);
        } catch (Exception e) {
            return null;
        }
    }

    private void enviar(HttpServletResponse response, JsonObject respuesta) throws IOException {
        response.getWriter().write(respuesta.toString());
    }

    private void setCorsHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
    }
}
