/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.control;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@WebServlet("/DonacionControl")
public class DonacionControl extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       
        String TipoDonacion = request.getParameter("TipoDonacion"); 
        String Nombre = request.getParameter("Nombre");
        String Correo = request.getParameter("Correo");
        String NumeroContacto= request.getParameter("NumeroContacto");
        String TipoIdentificacion = request.getParameter("TipoIdentificacion");
        String Identificacion = request.getParameter("Identificacion");
        String EntidadBancaria = request.getParameter("EntidadBancaria");
         
        String montoStr = request.getParameter("Monto");
        double monto = 0.0;

        if (montoStr != null && !montoStr.trim().isEmpty()) {
        monto = Double.parseDouble(montoStr);
        }

        
        String mensaje = request.getParameter("Mensaje");
  
       
        request.setAttribute("TipoDonacion", TipoDonacion);
        request.setAttribute("Nombre", Nombre);
        request.setAttribute("Correo", Correo);
        request.setAttribute("NumeroContacto", NumeroContacto);
        request.setAttribute("TipoIdentificacion", TipoIdentificacion);
        request.setAttribute("Identificacion", Identificacion);
        request.setAttribute("EntidadBancaria", EntidadBancaria);
        request.setAttribute("Monto", monto);
        request.setAttribute("Mensaje", mensaje);

       
        request.getRequestDispatcher("/ReviewControl").forward(request, response);
    }
    
    
}