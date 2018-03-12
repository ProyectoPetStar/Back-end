/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.petstar.controller;

import javax.servlet.http.HttpServletRequest;
import org.petstar.dao.CatalogosDAO;
import org.petstar.model.CatalogosResponseJson;
import org.petstar.model.CatalogosListResponseJason;
import org.petstar.model.OutputJson;

/**
 *
 * @author GuillermoB
 */
public class ControllerCatalogos {
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
    
    public OutputJson insertNewCatalogo(HttpServletRequest request){
        String tableName = request.getParameter("tableName");
        String descripcion = request.getParameter("descripcion");
        CatalogosResponseJson response = new CatalogosResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion controllerAutenticacion = new ControllerAutenticacion();
         
        try {
             
            if (controllerAutenticacion.isValidToken(request)) {
                CatalogosDAO catalogosDAO = new CatalogosDAO();
                catalogosDAO.insertCatalogos(tableName, descripcion);
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
