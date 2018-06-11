package org.petstar.controller;

import javax.servlet.http.HttpServletRequest;
import org.petstar.dao.CatalogosDAO;
import org.petstar.dao.EquiposDAO;
import org.petstar.dto.EquiposDTO;
import org.petstar.dto.ResultInteger;
import org.petstar.dto.UserDTO;
import org.petstar.model.EquiposResponseJson;
import org.petstar.model.OutputJson;
import org.petstar.model.ResponseJson;

/**
 *
 * @author Tech-Pro
 */
public class ControllerEquipos {
    private static final String MSG_SUCESS = "OK";
    private static final String MSG_LOGOUT = "Inicie sesión nuevamente";
    private static final String MSG_ERROR  = "Descripción de error: ";
    private static final String MSG_INVALID= "Valor o Descripción ya existe";
    private static final String TABLE_NAME = "pet_cat_equipos";
    private static final String MSG_NOEXIT = "El registro no existe";
    
    /**
     * Consulta General
     * Metodo que consulta todos los equipos que se encuentran en base de datos
     * @param request
     * @return 
     */
    public OutputJson getAllEquipos(HttpServletRequest request){
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        
        try{
            UserDTO sesion = autenticacion.isValidToken(request);
            if(sesion != null){
                EquiposResponseJson data = new EquiposResponseJson();
                EquiposDAO equiposDAO = new EquiposDAO();
                
                data.setListEquipos(equiposDAO.getAllEquipos());
                output.setData(data);
                response.setMessage(MSG_SUCESS);
                response.setSucessfull(true);
            }else{
                response.setMessage(MSG_LOGOUT);
                response.setSucessfull(false);
            }
        }catch(Exception ex){
            response.setMessage(MSG_ERROR + ex.getMessage());
            response.setSucessfull(false);
        }
        
        output.setResponse(response);
        return output;
    }
    
    /**
     * Consulta Especiifica
     * Metodo que se encarga de la busqueda por ID
     * @param request
     * @return 
     */
    public OutputJson getEquipoById(HttpServletRequest request){
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        
        try{
            int idEquipo = Integer.parseInt(request.getParameter("id_equipo"));
            UserDTO sesion = autenticacion.isValidToken(request);
            if(sesion != null){
                CatalogosDAO catalogosDAO = new CatalogosDAO();
                
                ResultInteger result = catalogosDAO.validaExistID(TABLE_NAME, "id_equipos", idEquipo);
                if(result.getResult().equals(1)){
                    EquiposResponseJson data = new EquiposResponseJson();
                    EquiposDAO equiposDAO = new EquiposDAO();
                
                    data.setEquipo(equiposDAO.getEquipoById(idEquipo));
                    output.setData(data);
                    response.setMessage(MSG_SUCESS);
                    response.setSucessfull(true);
                }else{
                    response.setMessage(MSG_NOEXIT);
                    response.setSucessfull(false);
                }
            }else{
                response.setMessage(MSG_LOGOUT);
                response.setSucessfull(false);
            }
        }catch(Exception ex){
            response.setMessage(MSG_ERROR + ex.getMessage());
            response.setSucessfull(false);
        }
        
        output.setResponse(response);
        return output;
    }
    
    /**
     * Alta Equipo
     * Metodo encargado de validar y registrar un nuevo equipo
     * @param request
     * @return 
     */
    public OutputJson insertEquipo(HttpServletRequest request){
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        
        try{
            EquiposDTO newEquipo = new EquiposDTO();
            newEquipo.setDescripcion(request.getParameter("descripcion"));
            newEquipo.setValor(request.getParameter("valor"));
            
            UserDTO sesion = autenticacion.isValidToken(request);
            if(sesion != null){
                CatalogosDAO catalogosDAO = new CatalogosDAO();
                
                ResultInteger result = catalogosDAO.validateDescripcionInsert(
                        TABLE_NAME, newEquipo.getValor(), newEquipo.getDescripcion());
                
                if(result.getResult().equals(0)){
                    EquiposDAO equiposDAO = new EquiposDAO();
                    
                    equiposDAO.inserEquipo(newEquipo);
                    
                    response.setMessage(MSG_SUCESS);
                    response.setSucessfull(true);
                }else{
                    response.setMessage(MSG_INVALID);
                    response.setSucessfull(false);
                }
            }else{
                response.setMessage(MSG_LOGOUT);
                response.setSucessfull(false);
            }
        }catch(Exception ex){
            response.setMessage(MSG_ERROR + ex.getMessage());
            response.setSucessfull(false);
        }
        
        output.setResponse(response);
        return output;
    }
    
    /**
     * Modificación Equipo
     * Metodo que se encarga de la validación y actualizacion de datos de un equipo
     * @param request
     * @return 
     */
    public OutputJson updateEquipo(HttpServletRequest request){
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        
        try{
            EquiposDTO equipo = new EquiposDTO();
            equipo.setId_equipos(Integer.valueOf(request.getParameter("id_equipo")));
            equipo.setDescripcion(request.getParameter("descripcion"));
            equipo.setValor(request.getParameter("valor"));
            
            UserDTO sesion = autenticacion.isValidToken(request);
            if(sesion != null){
                EquiposDAO equiposDAO = new EquiposDAO();
                
                ResultInteger result = equiposDAO.validaForUpdate(equipo);
                if(result.getResult().equals(0)){
                    equiposDAO.updateEquipo(equipo);
                    
                    response.setMessage(MSG_SUCESS);
                    response.setSucessfull(true);
                }else{
                    response.setMessage(MSG_INVALID);
                    response.setSucessfull(false);
                }
            }else{
                response.setMessage(MSG_LOGOUT);
                response.setSucessfull(false);
            }
        }catch(Exception ex){
            response.setMessage(MSG_ERROR + ex.getMessage());
            response.setSucessfull(false);
        }
        
        output.setResponse(response);
        return output;
    }
    
    /** 
     * Cambio de estatus
     * Metodo que se encarga de habilitar o deshabilitar el equipo
     * @param request
     * @return 
     */
    public OutputJson blockEquipo(HttpServletRequest request){
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        
        try{
            int idEquipo = Integer.valueOf(request.getParameter("id_equipo"));
            int activo = Integer.valueOf(request.getParameter("activo"));
            
            UserDTO sesion = autenticacion.isValidToken(request);
            if(sesion != null){
                EquiposDAO equiposDAO = new EquiposDAO();
                
                equiposDAO.blockEquipo(idEquipo, activo);
                response.setMessage(MSG_SUCESS);
                response.setSucessfull(true);
            }else{
                response.setMessage(MSG_LOGOUT);
                response.setSucessfull(false);
            }
        }catch(Exception ex){
            response.setMessage(MSG_ERROR + ex.getMessage());
            response.setSucessfull(false);
        }
        
        output.setResponse(response);
        return output;
    }
}
