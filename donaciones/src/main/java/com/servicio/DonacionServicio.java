/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.servicio;

import com.dao.DonacionDaoMongoDB;
import com.modelo.DonacionDTO;

import java.util.List;

public class DonacionServicio {

    private final DonacionDaoMongoDB donacionDao;

    // ----------------------------------------------
    // CONSTRUCTOR
    // ----------------------------------------------
    public DonacionServicio() {
        this.donacionDao = new DonacionDaoMongoDB(); // crea la conexión una sola vez
    }

    // ----------------------------------------------
    // REGISTRAR DONACIÓN
    // ----------------------------------------------
    public String registrarDonacion(DonacionDTO donacion) {

        // -------- VALIDACIONES --------
        
        if (donacion.getNombre() == null || donacion.getNombre().isEmpty()) {
            return "El nombre es obligatorio.";
        }

        if (donacion.getCorreo() == null || !donacion.getCorreo().contains("@")) {
            return "Correo inválido.";
        }

        if (donacion.getNumeroContacto() == null || donacion.getNumeroContacto().length() < 7) {
            return "Número de contacto inválido.";
        }

        // Validación por tipo de donación
        if (donacion.getTipoDonacion().equalsIgnoreCase("dinero")) {
            if (donacion.getMonto() <= 0) {
                return "Debe ingresar un monto válido.";
            }
            if (donacion.getEntidadBancaria() == null || donacion.getEntidadBancaria().isEmpty()) {
                return "Debe indicar la entidad bancaria.";
            }
        } else if (donacion.getTipoDonacion().equalsIgnoreCase("especie")) {
            // donación en especie NO lleva monto ni banco
        } else {
            return "Tipo de donación inválido.";
        }

        // Guardar en BD
        int resultado = donacionDao.insertarDonacion(donacion);

        if (resultado == 1) {
            return "Donación registrada correctamente.";
        } else {
            return "Error al registrar la donación.";
        }
    }

    // ----------------------------------------------
    // CONSULTAR DONACIÓN POR ObjectId
    // ----------------------------------------------
    public DonacionDTO obtenerDonacionPorId(String objectId) {
        return donacionDao.consultarPorObjectId(objectId);
    }

    // ----------------------------------------------
    // LISTAR TODAS LAS DONACIONES
    // ----------------------------------------------
    public List<DonacionDTO> listarDonaciones() {
        return donacionDao.listarTodos();
    }

    // ----------------------------------------------
    // ELIMINAR DONACIÓN
    // ----------------------------------------------
    public String eliminarDonacion(String objectId) {
        donacionDao.borrarPorObjectId(objectId);
        return "Donación eliminada (si existía).";
    }

}
