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
import java.util.ArrayList;
import java.util.List;

public class DonacionDaoPostgre implements DonacionDAO {

    static final String URL = "jdbc:postgresql://localhost:5432/donaciones";
    private static final String USER = "postgres";
    private static final String PASSWORD = "123456789";

    public DonacionDaoPostgre() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    @Override
    public List<DonacionDTO> listarDonaciones() throws Exception {
        List<DonacionDTO> lista = new ArrayList<>();
        String sql = "SELECT * FROM registrodonaciones";

        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                lista.add(new DonacionDTO(
                        rs.getString("tipoDonacion"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("numeroContacto"),
                        rs.getString("tipoIdentificacion"),
                        rs.getString("identificacion"),
                        rs.getString("entidadBancaria"),
                        rs.getDouble("monto"),
                        rs.getString("mensaje")
                ));
            }
        }
        return lista;
    }

    @Override
    public DonacionDTO obtenerDonacionPorId(int id) throws Exception {
        return consultarDonacion(id);
    }

    @Override
    public int insertarDonacion(DonacionDTO ob) throws Exception {
        String sql = "INSERT INTO registrodonaciones (tipoDonacion, nombre, correo, numeroContacto, tipoIdentificacion, identificacion, entidadBancaria, monto, mensaje) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, ob.getTipoDonacion());
            ps.setString(2, ob.getNombre());
            ps.setString(3, ob.getCorreo());
            ps.setString(4, ob.getNumeroContacto());
            ps.setString(5, ob.getTipoIdentificacion());
            ps.setString(6, ob.getIdentificacion());
            ps.setString(7, ob.getEntidadBancaria());
            ps.setDouble(8, ob.getMonto());
            ps.setString(9, ob.getMensaje());

            return ps.executeUpdate();
        }
    }

    @Override
    public int actualizarDonacion(DonacionDTO ob) throws Exception {
        throw new UnsupportedOperationException("No implementado");
    }

    @Override
    public int eliminarDonacion(int id) throws Exception {
        String sql = "DELETE FROM registrodonaciones WHERE registrodonaciones_id = ?";

        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, id);
            return pst.executeUpdate();
        }
    }

    @Override
    public DonacionDTO consultarDonacion(int id) throws Exception {

        String sql = "SELECT * FROM registrodonaciones WHERE registrodonaciones_id = ?";
        DonacionDTO dto = null;

        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

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
        }

        return dto;
    }
}
