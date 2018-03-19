/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.petstar.controller;

import javax.servlet.http.HttpServletRequest;
import org.petstar.dao.FallasDAO;
import org.petstar.model.FallasDataResponseJson;
import org.petstar.model.OutputJson;
import org.petstar.model.ResponseJson;

/**
 *
 * @author Tech-Pro
 */
public class ControllerFallas {
    private static final String MSG_SUCESS = "OK";
    private static final String MSG_LOGOUT = "Inicie sesión nuevamente";
    private static final String MSG_ERROR  = "Descripción de error: ";
    
    public OutputJson getAllFallas(HttpServletRequest request){
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        
        try{
            if(autenticacion.isValidToken(request)){
                FallasDAO fallasDAO = new FallasDAO();
                FallasDataResponseJson data = new FallasDataResponseJson();
                
                data.setListFallas(fallasDAO.getAllDataFallas());
                response.setSucessfull(true);
                response.setMessage(MSG_SUCESS);
            }else{
                response.setSucessfull(false);
                response.setMessage(MSG_LOGOUT);
            }
        } catch( Exception ex){
            response.setSucessfull(false);
            response.setMessage(MSG_ERROR);
        }
        output.setResponse(response);
        return output;
    }
}
