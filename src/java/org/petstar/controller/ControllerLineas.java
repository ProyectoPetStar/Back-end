/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.petstar.controller;

import javax.servlet.http.HttpServletRequest;
import org.petstar.dao.CatalogosDAO;
import org.petstar.dao.LineasDAO;
import org.petstar.dto.ResultInteger;
import org.petstar.model.LineasDataResponseJson;
import org.petstar.model.LineasResponseJson;
import org.petstar.model.OutputJson;

/**
 *
 * @author Tech-Pro
 */
public class ControllerLineas {
    private static final String MSG_SUCESS = "OK";
    private static final String MSG_LOGOUT = "Inicie sesión nuevamente";
    private static final String MSG_ERROR  = "Descripción de error: ";
    private static final String MSG_INVALID= "Descripción ya existe";
    private static final String TABLE_NAME = "pet_cat_lineas";
    
    /**
     * Metodo para la consulta de lineas 
     * @param request
     * @return 
     */
    public OutputJson getLineasData(HttpServletRequest request){
        LineasResponseJson response = new LineasResponseJson();
        OutputJson out = new OutputJson();
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        
        try{
            if(autenticacion.isValidToken(request)){
                LineasDAO lineasDAO = new LineasDAO();
                LineasDataResponseJson data = new LineasDataResponseJson();
                data.setListLineasDTO(lineasDAO.getLineasData());

                out.setData(data);
                response.setSucessfull(true);
                response.setMessage(MSG_SUCESS);
                
            } else {
                response.setSucessfull(false);
                response.setMessage(MSG_LOGOUT);
            }
        } catch(Exception ex) {
            response.setSucessfull(false);
            response.setMessage(MSG_ERROR + ex.getMessage());
        }
        out.setResponse(response);
        return out;
    }
    
    /**
     * Metodo para el registro de nuevas lineas
     * @param request
     * @return 
     */
    public OutputJson insertNewLinea(HttpServletRequest request){
        int idGpoLinea = Integer.parseInt(request.getParameter("gpoLinea"));
        String descripcion = request.getParameter("descripcionLinea");
        
        LineasResponseJson response = new LineasResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
         
        try {
            if (autenticacion.isValidToken(request)) {
                CatalogosDAO catalogosDAO = new CatalogosDAO();
                ResultInteger existe = catalogosDAO.validateDescripcionInsert(TABLE_NAME, descripcion);
                if(existe.getResult() == 0){
                    LineasDAO lineasDAO = new LineasDAO();
                    lineasDAO.insertNewLinea(descripcion, idGpoLinea);
                    response.setSucessfull(true);
                    response.setMessage(MSG_SUCESS);
                }else{
                    response.setSucessfull(false);
                    response.setMessage(MSG_INVALID);
                }
            } else {
                response.setSucessfull(false);
                response.setMessage(MSG_LOGOUT);
            }
        } catch (Exception ex) {
            response.setSucessfull(false);
            response.setMessage(MSG_ERROR + ex.getMessage());
        }
        output.setResponse(response);
        
        return output;
    }
    
    /**
     * Metodo para actualizacion de lineas
     * @param request
     * @return 
     */
    public OutputJson updateLinea(HttpServletRequest request){
        int idLinea = Integer.parseInt(request.getParameter("idLinea"));
        int activo = Integer.parseInt(request.getParameter("activoLinea"));
        int idGpoLinea = Integer.parseInt(request.getParameter("gpoLinea"));
        String descripcion = request.getParameter("descripcionLinea");
        
        LineasResponseJson response = new LineasResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
         
        try {
            if (autenticacion.isValidToken(request)) {
                LineasDAO lineasDAO = new LineasDAO();
                ResultInteger result = lineasDAO.validaDescripcionUpdate(idLinea, descripcion);
                if(result.getResult().equals(0)){
                    lineasDAO.updateLinea(idLinea, descripcion, activo, idGpoLinea);
                    response.setSucessfull(true);
                    response.setMessage(MSG_SUCESS);
                }else{
                    response.setSucessfull(false);
                    response.setMessage(MSG_INVALID);
                }
            } else {
                response.setSucessfull(false);
                response.setMessage(MSG_LOGOUT);
            }
        } catch (Exception ex) {
            response.setSucessfull(false);
            response.setMessage(MSG_ERROR + ex.getMessage());
        }
        output.setResponse(response);
        
        return output;
    }
    
    /**
     * Metodo para la eliminacion de lineas
     * @param request
     * @return 
     */
    public OutputJson deleteLinea(HttpServletRequest request){
        int idLinea = Integer.parseInt(request.getParameter("idLinea"));
        
        LineasResponseJson response = new LineasResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
         
        try {
             
            if (autenticacion.isValidToken(request)) {
                LineasDAO lineasDAO = new LineasDAO();
                lineasDAO.deleteLinea(idLinea);
                response.setSucessfull(true);
                response.setMessage(MSG_SUCESS);
                
            } else {
                response.setSucessfull(false);
                response.setMessage(MSG_LOGOUT);
            }
        } catch (Exception ex) {
            response.setSucessfull(false);
            response.setMessage(MSG_ERROR + ex.getMessage());
        }
        output.setResponse(response);
        
        return output;
    }
    
    /**
     * Metodo que devuelve los datos por id de linea
     * @param request
     * @return 
     */
    public OutputJson getDataCatalogosById(HttpServletRequest request){
        int idLinea = Integer.parseInt(request.getParameter("idLinea"));
        
        LineasResponseJson response = new LineasResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
         
        try {
            if (autenticacion.isValidToken(request)) {
                LineasDAO lineasDAO = new LineasDAO();
                LineasDataResponseJson data = new LineasDataResponseJson();
                data.setLineasDTO(lineasDAO.getLineasDataById(idLinea));

                output.setData(data);
                response.setSucessfull(true);
                response.setMessage(MSG_SUCESS);
                
            } else {
                response.setSucessfull(false);
                response.setMessage(MSG_LOGOUT);
            }
        } catch (Exception ex) {
            response.setSucessfull(false);
            response.setMessage(MSG_ERROR + ex.getMessage());
        }
        output.setResponse(response);
        
        return output;
    }
}
