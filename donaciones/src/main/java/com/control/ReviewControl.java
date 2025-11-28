/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.control;



import com.dao.FabricaDaoDonaciones;
import com.dao.FabricaDao;
import com.dao.DonacionDaoPostgre;
import com.modelo.DonacionDTO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/ReviewControl")
public class ReviewControl extends HttpServlet {
    
    

    public ReviewControl() {
        
    }
   
    
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       
        try {
            String tipoDonacion = (String) request.getAttribute("TipoDonacion");
            String nombre = (String) request.getAttribute("Nombre");
            String correo = (String) request.getAttribute("Correo");
            String numeroContacto = (String) request.getAttribute("NumeroContacto");
            String tipoIdentificacion = (String) request.getAttribute("TipoIdentificacion");
            String identificacion = (String) request.getAttribute("Identificacion");
            String entidadBancaria = (String) request.getAttribute("EntidadBancaria");
            double monto= (double) request.getAttribute("Monto");
            String mensaje = (String) request.getAttribute("Mensaje");
            
            
            
            
            FabricaDao fab=new FabricaDaoDonaciones();
            DonacionDaoPostgre DonacionDAO = (DonacionDaoPostgre) fab.crearDonacionDao("POSTGRE");
            // DonacionDaoMongoDB DonacionDAO = (DonacionDaoMongoDB) fab.crearDonacionDao()
            DonacionDTO ob=new DonacionDTO(tipoDonacion, nombre, correo, numeroContacto, tipoIdentificacion, identificacion, entidadBancaria, monto, mensaje);
            int num= DonacionDAO.insertarDonacion(ob);
            
            
            
            if (num > 0) {
                request.setAttribute("mensaje", "Donación registrada con éxito.");
                request.getRequestDispatcher("exito.jsp").forward(request, response);
            } else {
                request.setAttribute("mensaje", "Ocurrió un error al registrar la donación.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
            }
        } catch (Exception ex) {
            Logger.getLogger(ReviewControl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
