/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.petstar.controller;

import javax.servlet.http.HttpServletRequest;
import org.petstar.dao.EquipoAmutDAO;
import org.petstar.dto.ResultInteger;
import org.petstar.model.EquipoAmutDataResponseJson;
import org.petstar.model.EquipoAmutResponseJson;
import org.petstar.model.OutputJson;

/**
 *
 * @author Tech-Pro
 */
public class ControllerEquipoAmut {
    private static final String MSG_SUCESS = "OK";
    private static final String MSG_LOGOUT = "Inicie sesión nuevamente";
    private static final String MSG_ERROR  = "Descripción de error: ";
    private static final String MSG_INVALID= "Clave o Nombre ya existe";
    
    /**
     * Metodo que devuelve la lista de Equipos Amut
     * @param request
     * @return 
     */
    public OutputJson getEquiposAmutList(HttpServletRequest request){
        EquipoAmutResponseJson response = new EquipoAmutResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        
        try{
            if(autenticacion.isValidToken(request)){
                EquipoAmutDAO equipoAmutDAO = new EquipoAmutDAO();
                EquipoAmutDataResponseJson dataEquipo = new EquipoAmutDataResponseJson();
                dataEquipo.setListEquipoAmut(equipoAmutDAO.getEquipoAmutData());
                
                output.setData(dataEquipo);
                response.setSucessfull(true);
                response.setMessage(MSG_SUCESS);
            }else{
                response.setSucessfull(false);
                response.setMessage(MSG_LOGOUT);
            }
        }catch(Exception ex){
            response.setSucessfull(false);
            response.setMessage(MSG_ERROR + ex.getMessage());
        }
        output.setResponse(response);
        return output;
    }
    
    public OutputJson getDataEquipoAmoutById(HttpServletRequest request){
        int idEquipo = Integer.parseInt(request.getParameter("id_equipo"));
        EquipoAmutResponseJson response = new EquipoAmutResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        
        try{
            if(autenticacion.isValidToken(request)){
                EquipoAmutDAO equipoAmutDAO = new EquipoAmutDAO();
                EquipoAmutDataResponseJson dataEquipo = new EquipoAmutDataResponseJson();
                dataEquipo.setEquipoAmut(equipoAmutDAO.getEquipoAmutById(idEquipo));
                
                output.setData(dataEquipo);
                response.setSucessfull(true);
                response.setMessage(MSG_SUCESS);
            }else{
                response.setSucessfull(false);
                response.setMessage(MSG_LOGOUT);
            }
        } catch (Exception ex){
            response.setSucessfull(false);
            response.setMessage(MSG_ERROR + ex.getMessage());
        }
        output.setResponse(response);
        return output;
    }
    
    /**
     * Metodo que permite el regitro de nuevos Equipos Amut
     * @param request
     * @return 
     */
    public OutputJson insertNewEquipoAmut(HttpServletRequest request){
        String equipoName = request.getParameter("nombre_equipo");
        String claveEquipo = request.getParameter("clave_equipo");
        EquipoAmutResponseJson response = new EquipoAmutResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        
        try{
            if(autenticacion.isValidToken(request)){
                EquipoAmutDAO equipoAmutDAO = new EquipoAmutDAO();
                ResultInteger result = equipoAmutDAO.validaDataForInsert(claveEquipo, equipoName);
                if(result.getResult().equals(0)){
                    equipoAmutDAO.insertNewEquipoAmut(claveEquipo, equipoName);
                    
                    response.setSucessfull(true);
                    response.setMessage(MSG_SUCESS);
                }else{
                    response.setSucessfull(false);
                    response.setMessage(MSG_INVALID);
                }
            }else{
                response.setSucessfull(false);
                response.setMessage(MSG_LOGOUT);
            }
        } catch(Exception ex){
            response.setMessage(MSG_ERROR + ex.getMessage());
            response.setSucessfull(false);
        }
        output.setResponse(response);
        return output;
    }
    
    /**
     * Metodo que permite la modificación de equipo amut
     * @param request
     * @return 
     */
    public OutputJson updateEquipoAmut(HttpServletRequest request){
        int idEquipoAmut = Integer.parseInt(request.getParameter("id_equipo"));
        int activo = Integer.parseInt(request.getParameter("activo"));
        String equipoName = request.getParameter("nombre_equipo");
        String claveEquipo = request.getParameter("clave_equipo");
        EquipoAmutResponseJson response = new EquipoAmutResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        
        try{
            if(autenticacion.isValidToken(request)){
                EquipoAmutDAO equipoAmutDAO = new EquipoAmutDAO();
                ResultInteger result = equipoAmutDAO.validaDataForUpdate(idEquipoAmut, claveEquipo, equipoName);
                
                if(result.getResult().equals(0)){
                    equipoAmutDAO.updateEquipoAmut(idEquipoAmut, claveEquipo, equipoName, activo);
                    
                    response.setSucessfull(true);
                    response.setMessage(MSG_SUCESS);
                }else{
                    response.setSucessfull(false);
                    response.setMessage(MSG_INVALID);
                }
            }else{
                response.setSucessfull(false);
                response.setMessage(MSG_LOGOUT);
            }
        } catch(Exception ex){
            response.setSucessfull(false);
            response.setMessage(MSG_ERROR + ex.getMessage());
        }
        output.setResponse(response);
        return output;
    }
    
    /**
     * Metodo para eliminar equipo amut
     * @param request
     * @return 
     */
    public OutputJson deleteEquipoAmut(HttpServletRequest request){
        int idEquipo = Integer.parseInt(request.getParameter("id_equipo"));
        EquipoAmutResponseJson response = new EquipoAmutResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        
        try{
            if(autenticacion.isValidToken(request)){
                EquipoAmutDAO equipoAmutDAO = new EquipoAmutDAO();
                equipoAmutDAO.deledeEquipoAmut(idEquipo);
                
                response.setSucessfull(true);
                response.setMessage(MSG_SUCESS);
            }else{
                response.setSucessfull(false);
                response.setMessage(MSG_LOGOUT);
            }
        } catch (Exception ex){
            response.setSucessfull(false);
            response.setMessage(MSG_ERROR + ex.getMessage());
        }
        output.setResponse(response);
        return output;
    }
}
