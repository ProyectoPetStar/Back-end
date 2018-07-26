/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.petstar.controller;

import javax.servlet.http.HttpServletRequest;
import org.petstar.dao.CatalogosDAO;
import org.petstar.dao.LineasDAO;
import org.petstar.dto.LineasDTO;
import org.petstar.dto.ResultInteger;
import org.petstar.dto.UserDTO;
import org.petstar.model.LineasDataResponseJson;
import org.petstar.model.OutputJson;
import org.petstar.model.ResponseJson;

/**
 * Controlador de Lineas
 * @author Tech-Pro
 */
public class ControllerLineas {
    private static final String MSG_SUCESS = "OK";
    private static final String MSG_LOGOUT = "Inicie sesión nuevamente";
    private static final String MSG_ERROR  = "Descripción de error: ";
    private static final String MSG_INVALID= "Valor o Descripción ya existe";
    private static final String MSG_NOEXIST= "El registro no existe";
    private static final String TABLE_NAME = "pet_cat_linea";
    
    /**
     * Consulta Genral
     * Metodo que devuelve todas las lineas 
     * @param request
     * @return 
     */
    public OutputJson getLineasData(HttpServletRequest request){
        ResponseJson response = new ResponseJson();
        OutputJson out = new OutputJson();
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        
        try{
            UserDTO sesion = autenticacion.isValidToken(request);
            if(sesion != null){
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
     * Registro de Lineas
     * Metodo para el registro de nuevas lineas
     * @param request
     * @return 
     */
    public OutputJson insertNewLinea(HttpServletRequest request){
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
         
        try {
            LineasDTO newLinea = new LineasDTO();
            newLinea.setValor(request.getParameter("valor"));
            newLinea.setDescripcion(request.getParameter("descripcion"));
            newLinea.setId_gpo_linea(Integer.parseInt(request.getParameter("id_etad")));
            newLinea.setId_gpo_linea(Integer.parseInt(request.getParameter("id_gpo_linea")));
            UserDTO sesion = autenticacion.isValidToken(request);
            
            if (sesion != null) {
                LineasDAO lineasDAO = new LineasDAO();
                ResultInteger existe = lineasDAO.validaForInsert(newLinea);
                if(existe.getResult().equals(0)){
                    lineasDAO.insertNewLinea(newLinea);
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
     * Modificación de Lineas
     * Metodo para actualizar la información de lineas
     * @param request
     * @return 
     */
    public OutputJson updateLinea(HttpServletRequest request){
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
         
        try {
            LineasDTO linea = new LineasDTO();
            linea.setId_linea(Integer.parseInt(request.getParameter("id_linea")));
            linea.setId_gpo_linea(Integer.parseInt(request.getParameter("id_etad")));
            linea.setId_gpo_linea(Integer.parseInt(request.getParameter("id_gpo_linea")));
            linea.setDescripcion(request.getParameter("descripcion"));
            linea.setValor(request.getParameter("valor"));
            
            UserDTO sesion = autenticacion.isValidToken(request);
            if (sesion != null) {
                LineasDAO lineasDAO = new LineasDAO();
                ResultInteger result = lineasDAO.validaForUpdate(linea);
                if(result.getResult().equals(0)){
                    lineasDAO.updateLinea(linea);
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
     * Cambio de Estatus
     * Metodo para Habilitar o deshabilitar una linea en especifico
     * @param request
     * @return 
     */
    public OutputJson blockLinea(HttpServletRequest request){
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
         
        try {
            int idLinea = Integer.parseInt(request.getParameter("id_linea"));
            int activo = Integer.parseInt(request.getParameter("activo"));
            UserDTO sesion = autenticacion.isValidToken(request);
             
            if (sesion != null) {
                LineasDAO lineasDAO = new LineasDAO();
                lineasDAO.blockLinea(idLinea, activo);
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
     * Consulta Especifica
     * Metodo que devuelve los datos de una linea de acuerdo al id.
     * @param request
     * @return 
     */
    public OutputJson getDataCatalogosById(HttpServletRequest request){
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
         
        try {
            int idLinea = Integer.parseInt(request.getParameter("id_linea"));
            UserDTO sesion = autenticacion.isValidToken(request);
            if (sesion != null) {
                CatalogosDAO catalogosDAO = new CatalogosDAO();
                
                ResultInteger result = catalogosDAO.validaExistID(TABLE_NAME, "id_linea", idLinea);
                if(result.getResult().equals(1)){
                    LineasDAO lineasDAO = new LineasDAO();
                    LineasDataResponseJson data = new LineasDataResponseJson();
                    data.setLineasDTO(lineasDAO.getLineasDataById(idLinea));

                    output.setData(data);
                    response.setSucessfull(true);
                    response.setMessage(MSG_SUCESS);
                }else{
                    response.setMessage(MSG_NOEXIST);
                    response.setSucessfull(false);
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
}
