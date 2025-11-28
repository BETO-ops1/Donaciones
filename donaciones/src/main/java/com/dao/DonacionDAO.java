/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dao;

import com.modelo.DonacionDTO;
import java.util.List;

public interface DonacionDAO {
    List<DonacionDTO> listarDonaciones() throws Exception;
    DonacionDTO obtenerDonacionPorId(int id) throws Exception;
    int insertarDonacion(DonacionDTO donacion) throws Exception;
    int actualizarDonacion(DonacionDTO donacion) throws Exception;
    int eliminarDonacion(int id) throws Exception;   // <-- id como int
    DonacionDTO consultarDonacion(int id) throws Exception;
}
