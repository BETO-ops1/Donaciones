/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dao;

import com.modelo.DonacionDTO;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DonacionDaoPostgre implements DonacionDAO{
     static final String URL = "jdbc:postgresql://localhost:5432/donaciones";
    private static final String USER = "postgres";
    private static final String PASSWORD = "123456789";
    int res=0;
    
    
     public DonacionDaoPostgre() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
     }
    

     @Override
    public int insertarDonacion(DonacionDTO ob) {
        String sql = "INSERT INTO registrodonaciones (tipoDonacion, nombre, correo, numeroContacto, tipoIdentificacion, identificacion, entidadBancaria, monto, mensaje) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            
           
            ps.setString(1,ob.getTipoDonacion());
            ps.setString(2,ob.getNombre());
            ps.setString(3, ob.getCorreo());
            ps.setString(4, ob.getNumeroContacto());
            ps.setString(5, ob.getTipoIdentificacion());
            ps.setString(6, ob.getIdentificacion());
            ps.setString(7, ob.getEntidadBancaria());
            ps.setDouble(8, ob.getMonto());
            ps.setString(9, ob.getMensaje());

            res= ps.executeUpdate();
            
            
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Que pasa aqui...");
        }
        return res;
    }

   @Override
    public DonacionDTO consultarDonacion(int id) {
        String sql = "SELECT * FROM registrodonaciones where registrodonaciones_id=?";
        DonacionDTO res=null;
        try (
                Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, id);
                ResultSet executeQuery = ps.executeQuery();
                while(executeQuery.next()){
                    res= new DonacionDTO(executeQuery.getString("tipoDonacion"),executeQuery.getString("nombre"),executeQuery.getString("correo"),executeQuery.getString("numeroContacto"),executeQuery.getString("tipoIdentificacion"),executeQuery.getString("identificacion"),executeQuery.getString("entidadBancaria"),executeQuery.getDouble("monto"),executeQuery.getString("mensaje"));
                    break;
                }
        }catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Que pasa aqui...");
        }
        return res;
    }

    @Override
    public List<DonacionDTO> listarTodos() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void borrar(int id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
