/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.petstar.controller;

import javax.servlet.http.HttpServletRequest;
import org.petstar.dao.CatalogosDAO;
import org.petstar.dto.ResultInteger;
import org.petstar.model.CatalogosResponseJson;
import org.petstar.model.CatalogosListResponseJason;
import org.petstar.model.OutputJson;

/**
 *
 * @author Tech-Pro
 */
public class ControllerCatalogos {
    
    /**
     * Metodo generico que devuelve la lista de de registros en DB
     * @param request
     * @return 
     */
    public OutputJson getCatalogosData(HttpServletRequest request) {
        String tableName = request.getParameter("tableName");
        CatalogosResponseJson response = new CatalogosResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion controllerAutenticacion = new ControllerAutenticacion();
         
        try {
             
            if (controllerAutenticacion.isValidToken(request)) {
                CatalogosDAO catalogosDAO = new CatalogosDAO();
                CatalogosListResponseJason catalogosListResponseJason = new CatalogosListResponseJason();
                catalogosListResponseJason.setListCatalogosDTO(catalogosDAO.getCatalogos(tableName));
                
                output.setData(catalogosListResponseJason);
                response.setSucessfull(true);
                response.setMessage("OK");
                
            } else {
                response.setSucessfull(false);
                response.setMessage("Inicie sesión nuevamente");
            }
        } catch (Exception ex) {
            response.setSucessfull(false);
            response.setMessage("Descripcion de error: " + ex.getMessage());
        }
        output.setResponse(response);
        
        return output;
    }
    
    /**
     * Metodo Generico para insertar registros de catalogos
     * @param request
     * @return 
     */
    public OutputJson insertNewCatalogo(HttpServletRequest request){
        String tableName = request.getParameter("tableName");
        String descripcion = request.getParameter("descripcion");
        CatalogosResponseJson response = new CatalogosResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion controllerAutenticacion = new ControllerAutenticacion();
         
        try {
             
            if (controllerAutenticacion.isValidToken(request)) {
                CatalogosDAO catalogosDAO = new CatalogosDAO();
                
                ResultInteger existe = catalogosDAO.validateDescripcionInsert(tableName, descripcion);
                
                if(existe.getResult() == 0){
                    catalogosDAO.insertCatalogos(tableName, descripcion);
                    response.setSucessfull(true);
                    response.setMessage("OK");
                }else {
                    response.setSucessfull(false);
                    response.setMessage("Capturar nueva descripción.");
                }
            } else {
                response.setSucessfull(false);
                response.setMessage("Inicie sesión nuevamente");
            }
        } catch (Exception ex) {
            response.setSucessfull(false);
            response.setMessage("Descripcion de error: " + ex.getMessage());
        }
        output.setResponse(response);
        
        return output;
    }
    
    /**
     * Metodo Generico para actualizar registros de catalogos
     * @param request
     * @return 
     */
    public OutputJson updateCatalogo(HttpServletRequest request){
        String tableName = request.getParameter("tableName");
        int id = Integer.parseInt(request.getParameter("idCatalogo"));
        int activo = Integer.parseInt(request.getParameter("activoCatalogo"));
        String descripcion = request.getParameter("descripcion");
        CatalogosResponseJson response = new CatalogosResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion controllerAutenticacion = new ControllerAutenticacion();
         
        try {
             
            if (controllerAutenticacion.isValidToken(request)) {
                CatalogosDAO catalogosDAO = new CatalogosDAO();
                catalogosDAO.updateCatalogos(id, descripcion, activo, tableName);
                response.setSucessfull(true);
                response.setMessage("OK");
                
            } else {
                response.setSucessfull(false);
                response.setMessage("Inicie sesión nuevamente");
            }
        } catch (Exception ex) {
            response.setSucessfull(false);
            response.setMessage("Descripcion de error: " + ex.getMessage());
        }
        output.setResponse(response);
        
        return output;
    }
    
    /**
     * Metodo generico para la eliminacion de registros de catalogos
     * @param request
     * @return 
     */
    public OutputJson deleteCatalogo(HttpServletRequest request){
        String tableName = request.getParameter("tableName");
        int id = Integer.parseInt(request.getParameter("idCatalogo"));
        CatalogosResponseJson response = new CatalogosResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion controllerAutenticacion = new ControllerAutenticacion();
         
        try {
             
            if (controllerAutenticacion.isValidToken(request)) {
                CatalogosDAO catalogosDAO = new CatalogosDAO();
                catalogosDAO.deleteCatalogo(id, tableName);
                response.setSucessfull(true);
                response.setMessage("OK");
            } else {
                response.setSucessfull(false);
                response.setMessage("Inicie sesión nuevamente");
            }
        } catch (Exception ex) {
            response.setSucessfull(false);
            response.setMessage("Descripcion de error: " + ex.getMessage());
        }
        output.setResponse(response);
        
        return output;
    }
    
    /**
     * Metodo Generico para la obtencion de valores de acuerdo al id de los catalogos
     * @param request
     * @return 
     */
    public OutputJson getDataByIdCatalogo(HttpServletRequest request){
        String tableName = request.getParameter("tableName");
        int id = Integer.parseInt(request.getParameter("idCatalogo"));
        CatalogosResponseJson response = new CatalogosResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion controllerAutenticacion = new ControllerAutenticacion();
         
        try {
             
            if (controllerAutenticacion.isValidToken(request)) {
                CatalogosDAO catalogosDAO = new CatalogosDAO();
                CatalogosListResponseJason catalogosListResponseJason = new CatalogosListResponseJason();
                catalogosListResponseJason.setCatalogosDTO(catalogosDAO.getDescripcionById(tableName, id));
                
                output.setData(catalogosListResponseJason);
                response.setSucessfull(true);
                response.setMessage("OK");
            } else {
                response.setSucessfull(false);
                response.setMessage("Inicie sesión nuevamente");
            }
        } catch (Exception ex) {
            response.setSucessfull(false);
            response.setMessage("Descripcion de error: " + ex.getMessage());
        }
        output.setResponse(response);
        
        return output;
    }
}
