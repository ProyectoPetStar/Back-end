/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.petstar.controller;

import javax.servlet.http.HttpServletRequest;
import org.petstar.dao.CatalogosDAO;
import org.petstar.dto.ResultInteger;
import org.petstar.dto.UserDTO;
import org.petstar.model.CatalogosListResponseJason;
import org.petstar.model.OutputJson;
import org.petstar.model.ResponseJson;

/**
 * Controlador de Catalogos
 * @author Tech-Pro
 */
public class ControllerCatalogos {
    private static final String MSG_SUCESS = "OK";
    private static final String MSG_LOGOUT = "Inicie sesión nuevamente";
    private static final String MSG_ERROR  = "Descripción de error: ";
    private static final String MSG_INVALID= "Valor o Descripción ya existe";
    
    /**
     * Consulta General
     * Metodo que devuelve un JSON con la lista de de Datos según el catalogo
     * @param request
     * @return 
     */
    public OutputJson getCatalogosData(HttpServletRequest request) {
        String tableName = request.getParameter("tableName");
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
         
        try {
            UserDTO sesion = autenticacion.isValidToken(request);
            if (sesion != null) {
                CatalogosDAO catalogosDAO = new CatalogosDAO();
                CatalogosListResponseJason catalogosListResponseJason = new CatalogosListResponseJason();
                catalogosListResponseJason.setListCatalogosDTO(catalogosDAO.getCatalogos(tableName));
                
                output.setData(catalogosListResponseJason);
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
     * Registro de Catalogos
     * Metodo que se encarga en insertar nuevos registros de catalogos
     * @param request
     * @return 
     */
    public OutputJson insertNewCatalogo(HttpServletRequest request){
        String tableName = request.getParameter("tableName");
        String valor = request.getParameter("valor");
        String descripcion = request.getParameter("descripcion");
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
         
        try {
            UserDTO sesion = autenticacion.isValidToken(request);
            if (sesion != null) {
                CatalogosDAO catalogosDAO = new CatalogosDAO();
                
                ResultInteger existe = catalogosDAO.validateDescripcionInsert(tableName, valor, descripcion);
                
                if(existe.getResult() == 0){
                    catalogosDAO.insertCatalogos(tableName, valor, descripcion);
                    response.setSucessfull(true);
                    response.setMessage(MSG_SUCESS);
                }else {
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
     * Modificación de Catalogos
     * Metodo que se encarga de actualizar los registros de catalogos
     * @param request
     * @return 
     */
    public OutputJson updateCatalogo(HttpServletRequest request){
        String tableName = request.getParameter("tableName");
        int id = Integer.parseInt(request.getParameter("idCatalogo"));
        int activo = Integer.parseInt(request.getParameter("activoCatalogo"));
        String descripcion = request.getParameter("descripcion");
        String valor = request.getParameter("valor");
        
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
         
        try {
             UserDTO sesion = autenticacion.isValidToken(request);
            if (sesion != null) {
                CatalogosDAO catalogosDAO = new CatalogosDAO();
                ResultInteger existe = catalogosDAO.validateDescripcionUpdate(tableName, id, valor, descripcion);
                
                if(existe.getResult().equals(0)){
                    catalogosDAO.updateCatalogos(id, valor, descripcion, activo, tableName);
                    response.setSucessfull(true);
                    response.setMessage(MSG_SUCESS);
                }else {
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
     * Bloqueo de Catalogos
     * Metodo generico para habilitar y deshabilitar registros de catalogos
     * @param request
     * @return 
     */
    public OutputJson blockCatalogo(HttpServletRequest request){
        String tableName = request.getParameter("tableName");
        int id = Integer.parseInt(request.getParameter("idCatalogo"));
        int activo = Integer.parseInt(request.getParameter("activoCatalogo"));
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
         
        try {
            UserDTO sesion = autenticacion.isValidToken(request);
            if (sesion != null) {
                CatalogosDAO catalogosDAO = new CatalogosDAO();
                catalogosDAO.blockCatalogo(id, tableName,activo);
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
     * Consulta Especifica de Catalogos
     * Metodo Generico para la obtención de valores de acuerdo al id del catalogos
     * @param request
     * @return 
     */
    public OutputJson getDataByIdCatalogo(HttpServletRequest request){
        String tableName = request.getParameter("tableName");
        int id = Integer.parseInt(request.getParameter("idCatalogo"));
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
         
        try {
            UserDTO sesion = autenticacion.isValidToken(request);
            if (sesion != null) {
                CatalogosDAO catalogosDAO = new CatalogosDAO();
                CatalogosListResponseJason catalogosListResponseJason = new CatalogosListResponseJason();
                catalogosListResponseJason.setCatalogosDTO(catalogosDAO.getDescripcionById(tableName, id));
                
                output.setData(catalogosListResponseJason);
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
