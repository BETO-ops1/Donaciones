/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.servicio;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.dao.DonacionDAO;
import com.dao.FabricaDaoDonaciones;
import com.modelo.DonacionDTO;
import java.util.List;

public class DonacionServicio {

    private final Gson gson = new Gson();
    private final DonacionDAO dao;

    public DonacionServicio() {
        this.dao = new FabricaDaoDonaciones().crearDonacionDao("POSTGRE");
    }

    // ================= LISTAR ===================

    public JsonObject listar() {
        JsonObject res = new JsonObject();
        JsonArray arr = new JsonArray();

        try {
            List<DonacionDTO> lista = dao.listarDonaciones();
            for (DonacionDTO d : lista) {
                arr.add(gson.toJsonTree(d));
            }

            res.addProperty("success", true);
            res.add("data", arr);

        } catch (Exception e) {
            res.addProperty("success", false);
            res.addProperty("mensaje", "Error al listar: " + e.getMessage());
        }
        return res;
    }

    // ================= GET POR ID ===================

    public JsonObject obtener(int id) {
        JsonObject res = new JsonObject();
        try {
            DonacionDTO dto = dao.consultarDonacion(id);

            if (dto == null) {
                res.addProperty("success", false);
                res.addProperty("mensaje", "No existe la donación");
            } else {
                res.addProperty("success", true);
                res.add("data", gson.toJsonTree(dto));
            }

        } catch (Exception e) {
            res.addProperty("success", false);
            res.addProperty("mensaje", "Error al obtener: " + e.getMessage());
        }
        return res;
    }

    // ================= INSERTAR ===================

    public JsonObject insertar(JsonObject json) {
        JsonObject res = new JsonObject();

        try {
            DonacionDTO d = gson.fromJson(json, DonacionDTO.class);
            int r = dao.insertarDonacion(d);

            if (r > 0) {
                res.addProperty("success", true);
                res.addProperty("mensaje", "Donación registrada");
            } else {
                res.addProperty("success", false);
                res.addProperty("mensaje", "No se pudo registrar");
            }

        } catch (Exception e) {
            res.addProperty("success", false);
            res.addProperty("mensaje", "Error al insertar: " + e.getMessage());
        }

        return res;
    }

    // ================= ELIMINAR ===================

    public JsonObject eliminar(int id) {
        JsonObject res = new JsonObject();
        try {
            int r = dao.eliminarDonacion(id);
            if (r > 0) {
                res.addProperty("success", true);
                res.addProperty("mensaje", "Donación eliminada");
            } else {
                res.addProperty("success", false);
                res.addProperty("mensaje", "No se encontró");
            }

        } catch (Exception e) {
            res.addProperty("success", false);
            res.addProperty("mensaje", "Error al eliminar: " + e.getMessage());
        }
        return res;
    }
}
