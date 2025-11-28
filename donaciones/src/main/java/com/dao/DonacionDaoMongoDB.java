/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dao;

import com.modelo.DonacionDTO;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class DonacionDaoMongoDB implements DonacionDAO {

    private static MongoClient mongoClient;
    private static MongoDatabase database;
    private static MongoCollection<Document> coleccion;

    // -------------------------------------------------------------
    // CONSTRUCTOR: inicia conexión una sola vez
    // -------------------------------------------------------------
    public DonacionDaoMongoDB() {
        if (mongoClient == null) {
            mongoClient = MongoClients.create("mongodb://localhost:27017");
            database = mongoClient.getDatabase("donaciones");
            coleccion = database.getCollection("registrodonaciones"); // nombre de tu colección
            System.out.println("Conectado a MongoDB correctamente.");
        }
    }

    // -------------------------------------------------------------
    // INSERTAR
    // -------------------------------------------------------------
    @Override
    public int insertarDonacion(DonacionDTO ob) {

        try {
            Document doc = new Document("tipoDonacion", ob.getTipoDonacion())
                    .append("nombre", ob.getNombre())
                    .append("correo", ob.getCorreo())
                    .append("numeroContacto", ob.getNumeroContacto())
                    .append("tipoIdentificacion", ob.getTipoIdentificacion())
                    .append("identificacion", ob.getIdentificacion())
                    .append("entidadBancaria", ob.getEntidadBancaria())
                    .append("monto", ob.getMonto())
                    .append("mensaje", ob.getMensaje())
                    .append("fecha", new Date());

            coleccion.insertOne(doc);
            return 1;

        } catch (Exception e) {
            System.out.println("ERROR INSERTANDO: " + e.getMessage());
            return 0;
        }
    }

    // -------------------------------------------------------------
    // CONSULTAR (Mongo NO usa int, así que este método no aplica)
    // -------------------------------------------------------------
    
    public DonacionDTO consultarDonacion(int id) {
        System.out.println("MongoDB no utiliza enteros como ID. Utilice consultarPorObjectId().");
        return null;
    }

    // Consulta real con ObjectId
    public DonacionDTO consultarPorObjectId(String objectId) {

        try {
            Document doc = coleccion.find(eq("_id", new ObjectId(objectId))).first();

            if (doc == null) return null;

            return new DonacionDTO(
                    doc.getString("tipoDonacion"),
                    doc.getString("nombre"),
                    doc.getString("correo"),
                    doc.getString("numeroContacto"),
                    doc.getString("tipoIdentificacion"),
                    doc.getString("identificacion"),
                    doc.getString("entidadBancaria"),
                    doc.getDouble("monto"),
                    doc.getString("mensaje")
            );

        } catch (Exception e) {
            System.out.println("ERROR CONSULTANDO: " + e.getMessage());
            return null;
        }
    }

    // -------------------------------------------------------------
    // LISTAR TODOS
    // -------------------------------------------------------------
    
    public List<DonacionDTO> listarTodos() {

        List<DonacionDTO> lista = new ArrayList<>();

        try {
            for (Document doc : coleccion.find()) {

                DonacionDTO d = new DonacionDTO(
                        doc.getString("tipoDonacion"),
                        doc.getString("nombre"),
                        doc.getString("correo"),
                        doc.getString("numeroContacto"),
                        doc.getString("tipoIdentificacion"),
                        doc.getString("identificacion"),
                        doc.getString("entidadBancaria"),
                        doc.getDouble("monto"),
                        doc.getString("mensaje")
                );

                lista.add(d);
            }

        } catch (Exception e) {
            System.out.println("ERROR LISTANDO: " + e.getMessage());
        }

        return lista;
    }

    // -------------------------------------------------------------
    // BORRAR
    // -------------------------------------------------------------
    
    public void borrar(int id) {
        System.out.println("MongoDB no usa int como ID. Use borrarPorObjectId().");
    }

    public void borrarPorObjectId(String objectId) {
        try {
            coleccion.deleteOne(eq("_id", new ObjectId(objectId)));
            System.out.println("Donación eliminada correctamente.");
        } catch (Exception e) {
            System.out.println("ERROR BORRANDO: " + e.getMessage());
        }
    }

    @Override
    public List<DonacionDTO> listarDonaciones() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public DonacionDTO obtenerDonacionPorId(int id) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int actualizarDonacion(DonacionDTO donacion) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int eliminarDonacion(int id) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
