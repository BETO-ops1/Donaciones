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

public class DonacionDaoPostgre implements DonacionDAO {

    static final String URL = "jdbc:postgresql://localhost:5432/donaciones";
    private static final String USER = "postgres";
    private static final String PASSWORD = "123456789";

    int res = 0;

    public DonacionDaoPostgre() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

   
    public boolean autenticar(String correo, String password) {
        
        String sql = "SELECT * FROM usuarios WHERE correo = ? AND password = ?";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, correo);
            ps.setString(2, password); // <- en la vida real debe ir encriptada

            ResultSet rs = ps.executeQuery();

            return rs.next(); // si encontró un usuario, autenticación exitosa

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // -------------------------------------------------------------------------
    //   INSERTAR DONACIÓN
    // -------------------------------------------------------------------------
    @Override
    public int insertarDonacion(DonacionDTO ob) {
        String sql = "INSERT INTO registrodonaciones (tipoDonacion, nombre, correo, numeroContacto, tipoIdentificacion, identificacion, entidadBancaria, monto, mensaje) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, ob.getTipoDonacion());
            ps.setString(2, ob.getNombre());
            ps.setString(3, ob.getCorreo());
            ps.setString(4, ob.getNumeroContacto());
            ps.setString(5, ob.getTipoIdentificacion());
            ps.setString(6, ob.getIdentificacion());
            ps.setString(7, ob.getEntidadBancaria());
            ps.setDouble(8, ob.getMonto());
            ps.setString(9, ob.getMensaje());

            res = ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error en insertarDonacion...");
        }
        return res;
    }


    @Override
    public DonacionDTO consultarDonacion(int id) {
        String sql = "SELECT * FROM registrodonaciones WHERE registrodonaciones_id = ?";
        DonacionDTO dto = null;

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                dto = new DonacionDTO(
                        rs.getString("tipoDonacion"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("numeroContacto"),
                        rs.getString("tipoIdentificacion"),
                        rs.getString("identificacion"),
                        rs.getString("entidadBancaria"),
                        rs.getDouble("monto"),
                        rs.getString("mensaje")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error en consultarDonacion...");
        }

        return dto;
    }

    @Override
    public List<DonacionDTO> listarTodos() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void borrar(int id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
