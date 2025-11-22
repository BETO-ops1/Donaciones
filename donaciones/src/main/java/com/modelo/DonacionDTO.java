/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.modelo;


public class DonacionDTO {
 
    private final String tipoDonacion;
    private final String nombre;
    private final String correo;
    private final String numeroContacto;
    private final String tipoIdentificacion;
    private final String identificacion;
    private final String entidadBancaria; // null o "" si Especie
    private final double monto; // 0 si Especie
    private final String mensaje;

    

    public DonacionDTO(String tipoDonacion, String nombre, String correo, String numeroContacto, String tipoIdentificacion, String identificacion, String entidadBancaria, double monto, String mensaje) {
       
        this.tipoDonacion = tipoDonacion;
        this.nombre = nombre;
        this.correo = correo;
        this.numeroContacto = numeroContacto;
        this.tipoIdentificacion = tipoIdentificacion;
        this.identificacion = identificacion;
        this.entidadBancaria = entidadBancaria;
        this.monto = monto;
        this.mensaje = mensaje;
    }

    public String getTipoDonacion() {
        return tipoDonacion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public String getNumeroContacto() {
        return numeroContacto;
    }

    public String getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public String getEntidadBancaria() {
        return entidadBancaria;
    }

    public double getMonto() {
        return monto;
    }

    public String getMensaje() {
        return mensaje;
    }

    
   
    
}