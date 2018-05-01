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
 * Controlador de Catalogos
 * @author Tech-Pro
 */
public class ControllerCatalogos {
    
    /**
     * Consulta General
     * Metodo que devuelve un JSON con la lista de de Datos según el catalogo
     * @param request
     * @return 
     */
    public OutputJson getCatalogosData(HttpServletRequest request) {
        String tableName = request.getParameter("tableName");
        CatalogosResponseJson response = new CatalogosResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion controllerAutenticacion = new ControllerAutenticacion();
         
        try {
             
//            if (controllerAutenticacion.isValidToken(request)) {
                CatalogosDAO catalogosDAO = new CatalogosDAO();
                CatalogosListResponseJason catalogosListResponseJason = new CatalogosListResponseJason();
                catalogosListResponseJason.setListCatalogosDTO(catalogosDAO.getCatalogos(tableName));
                
                output.setData(catalogosListResponseJason);
                response.setSucessfull(true);
                response.setMessage("OK");
                
//            } else {
//                response.setSucessfull(false);
//                response.setMessage("Inicie sesión nuevamente");
//            }
        } catch (Exception ex) {
            response.setSucessfull(false);
            response.setMessage("Descripcion de error: " + ex.getMessage());
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
        String descripcion = request.getParameter("descripcion");
        CatalogosResponseJson response = new CatalogosResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion controllerAutenticacion = new ControllerAutenticacion();
         
        try {
             
//            if (controllerAutenticacion.isValidToken(request)) {
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
//            } else {
//                response.setSucessfull(false);
//                response.setMessage("Inicie sesión nuevamente");
//            }
        } catch (Exception ex) {
            response.setSucessfull(false);
            response.setMessage("Descripcion de error: " + ex.getMessage());
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
        CatalogosResponseJson response = new CatalogosResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion controllerAutenticacion = new ControllerAutenticacion();
         
        try {
             
//            if (controllerAutenticacion.isValidToken(request)) {
                CatalogosDAO catalogosDAO = new CatalogosDAO();
                ResultInteger existe = catalogosDAO.validateDescripcionUpdate(tableName, id, descripcion);
                
                if(existe.getResult().equals(0)){
                    catalogosDAO.updateCatalogos(id, descripcion, activo, tableName);
                    response.setSucessfull(true);
                    response.setMessage("OK");
                }else {
                    response.setSucessfull(false);
                    response.setMessage("Capturar nueva descripción.");
                }
//            } else {
//                response.setSucessfull(false);
//                response.setMessage("Inicie sesión nuevamente");
//            }
        } catch (Exception ex) {
            response.setSucessfull(false);
            response.setMessage("Descripcion de error: " + ex.getMessage());
        }
        output.setResponse(response);
        
        return output;
    }
    
    /**
     * Eliminación de Catalogos
     * Metodo generico para la eliminación de registros de catalogos
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
             
//            if (controllerAutenticacion.isValidToken(request)) {
                CatalogosDAO catalogosDAO = new CatalogosDAO();
                catalogosDAO.deleteCatalogo(id, tableName);
                response.setSucessfull(true);
                response.setMessage("OK");
//            } else {
//                response.setSucessfull(false);
//                response.setMessage("Inicie sesión nuevamente");
//            }
        } catch (Exception ex) {
            response.setSucessfull(false);
            response.setMessage("Descripcion de error: " + ex.getMessage());
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
        CatalogosResponseJson response = new CatalogosResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion controllerAutenticacion = new ControllerAutenticacion();
         
        try {
             
//            if (controllerAutenticacion.isValidToken(request)) {
                CatalogosDAO catalogosDAO = new CatalogosDAO();
                CatalogosListResponseJason catalogosListResponseJason = new CatalogosListResponseJason();
                catalogosListResponseJason.setCatalogosDTO(catalogosDAO.getDescripcionById(tableName, id));
                
                output.setData(catalogosListResponseJason);
                response.setSucessfull(true);
                response.setMessage("OK");
//            } else {
//                response.setSucessfull(false);
//                response.setMessage("Inicie sesión nuevamente");
//            }
        } catch (Exception ex) {
            response.setSucessfull(false);
            response.setMessage("Descripcion de error: " + ex.getMessage());
        }
        output.setResponse(response);
        
        return output;
    }
}
