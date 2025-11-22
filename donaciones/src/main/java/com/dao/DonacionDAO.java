/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dao;

import com.modelo.DonacionDTO;
import java.util.List;


public interface DonacionDAO {
     int insertarDonacion(DonacionDTO ob);
     DonacionDTO consultarDonacion(int id);
     List<DonacionDTO> listarTodos();
     void borrar(int id);
}
