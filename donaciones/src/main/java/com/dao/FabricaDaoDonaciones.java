/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dao;


public class FabricaDaoDonaciones extends FabricaDao{

    @Override
    public DonacionDAO crearDonacionDao(String tipo) {
        if(tipo.equals("POSTGRE"))
            return new DonacionDaoPostgre();
        else
            
        return (DonacionDAO) new DonacionDaoMongoDB();
        
    }
    
}
