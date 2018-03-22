/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.petstar.controller;

import java.math.BigDecimal;
import javax.servlet.http.HttpServletRequest;
import org.petstar.dao.CatalogosDAO;
import org.petstar.dao.MetasDAO;
import org.petstar.dto.ResultInteger;
import org.petstar.model.MetasAsignacionResponseJson;
import org.petstar.model.MetasDataResponseJson;
import org.petstar.model.OutputJson;
import org.petstar.model.ResponseJson;

/**
 *
 * @author Tech-Pro
 */
public class ControllerMetas {
    private static final String TABLE_GRUPOS = "pet_cat_grupos";
    private static final String TABLE_LINEAS = "pet_cat_lineas";
    private static final String TABLE_TURNOS = "pet_cat_turnos";
    private static final String MSG_SUCESS = "OK";
    private static final String MSG_LOGOUT = "Inicie sesión nuevamente";
    private static final String MSG_ERROR  = "Descripción de error: ";
    private static final String MSG_INVALID= "Ya existe una meta con esos valores.";
    private static final String MSG_NO_EXIST= "La meta no existe.";
    
    /**
     * Metodo que devuelve la lista de metas en DB
     * @param request
     * @return 
     */
    public OutputJson getMetasDataCarga(HttpServletRequest request){
        
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion controllerAutenticacion = new ControllerAutenticacion();
        
        try{
            if(controllerAutenticacion.isValidToken(request)){
                MetasDAO metasDAO = new MetasDAO();
                MetasDataResponseJson data = new MetasDataResponseJson();
                data.setListMetas(metasDAO.getMetasCarga());
                
                output.setData(data);
                response.setMessage(MSG_SUCESS);
                response.setSucessfull(true);
            }else{
                response.setMessage(MSG_LOGOUT);
                response.setSucessfull(false);
            }
        }catch (Exception ex){
            response.setSucessfull(false);
            response.setMessage(MSG_ERROR + ex.getMessage());
        }
        output.setResponse(response);
        return output;
    }
    
    /**
     * Metodo que devuelve datos de la meta de acuerdo al id enviado
     * @param request
     * @return 
     */
    public OutputJson getMetasDataCargaById(HttpServletRequest request){
        int idMeta = Integer.parseInt(request.getParameter("idMeta"));
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion controllerAutenticacion = new ControllerAutenticacion();
        
        try{
            if(controllerAutenticacion.isValidToken(request)){
                MetasDAO metasDAO = new MetasDAO();
                ResultInteger result = metasDAO.validaIfExistMetaCarga(idMeta);
                if(result.getResult().equals(1)){
                    MetasDataResponseJson data = new MetasDataResponseJson();
                    data.setMetasDTO(metasDAO.getMetasCargaById(idMeta));

                    output.setData(data);
                    response.setMessage(MSG_SUCESS);
                    response.setSucessfull(true);
                }else{
                    response.setMessage(MSG_NO_EXIST);
                    response.setSucessfull(false);
                }
            }else{
                response.setMessage(MSG_LOGOUT);
                response.setSucessfull(false);
            }
        }catch (Exception ex){
            response.setSucessfull(false);
            response.setMessage(MSG_ERROR + ex.getMessage());
        }
        output.setResponse(response);
        return output;
    }
    
    /**
     * Metodo que registra nuevas Metas
     * @param request
     * @return 
     */
    public OutputJson insertNewMetaCarga(HttpServletRequest request){
        int idLinea = Integer.parseInt(request.getParameter("idLinea"));
        String meta = request.getParameter("meta");
        String tipoMedida = request.getParameter("tipoMedida");
        
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion controllerAutenticacion = new ControllerAutenticacion();
        
        try{
            if(controllerAutenticacion.isValidToken(request)){
                MetasDAO metasDAO = new MetasDAO();
                ResultInteger result = metasDAO.validaDataForInsertCarga(idLinea, meta);
                if(result.getResult().equals(0)){
                    metasDAO.insertMetaCarga(idLinea, meta, tipoMedida);
                
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
        }catch (Exception ex){
            response.setSucessfull(false);
            response.setMessage(MSG_ERROR + ex.getMessage());
        }
        output.setResponse(response);
        return output;
    }
    
    /**
     * Metodo para modificar una meta
     * @param request
     * @return 
     */
    public OutputJson updateMetaCarga(HttpServletRequest request){
        int idMeta = Integer.parseInt(request.getParameter("idMeta"));
        int idLinea = Integer.parseInt(request.getParameter("idLinea"));
        int activo = Integer.parseInt(request.getParameter("activo"));
        int posicion = Integer.parseInt(request.getParameter("posicion"));
        String meta = request.getParameter("meta");
        String tipoMedida = request.getParameter("tipoMedida");
        
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion controllerAutenticacion = new ControllerAutenticacion();
        
        try{
            if(controllerAutenticacion.isValidToken(request)){
                MetasDAO metasDAO = new MetasDAO();
                ResultInteger result = metasDAO.validaDataForUpdateCarga(idMeta, idLinea, meta);
                if(result.getResult().equals(0)){
                    metasDAO.updateMetaCarga(idMeta, idLinea, meta, tipoMedida, posicion, activo);
                
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
        }catch (Exception ex){
            response.setSucessfull(false);
            response.setMessage(MSG_ERROR + ex.getMessage());
        }
        output.setResponse(response);
        return output;
    }

    /**
     * Metodo que devuelve los datos para los combos
     * @param request
     * @return 
     */
    public OutputJson loadCombosCatalogs(HttpServletRequest request){
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion controllerAutenticacion = new ControllerAutenticacion();
        
        try{
            if(controllerAutenticacion.isValidToken(request)){
                MetasDAO metasDAO = new MetasDAO();
                CatalogosDAO catalogosDAO = new CatalogosDAO();
                MetasDataResponseJson data = new MetasDataResponseJson();
                
                data.setListMetas(metasDAO.getMetasCarga());
                data.setListGrupos(catalogosDAO.getCatalogos(TABLE_GRUPOS));
                data.setListLineas(catalogosDAO.getCatalogos(TABLE_LINEAS));
                data.setListTurnos(catalogosDAO.getCatalogos(TABLE_TURNOS));
                
                output.setData(data);
                response.setMessage(MSG_SUCESS);
                response.setSucessfull(true);
            }else{
                response.setMessage(MSG_LOGOUT);
                response.setSucessfull(false);
            }
        }catch (Exception ex){
            response.setSucessfull(false);
            response.setMessage(MSG_ERROR + ex.getMessage());
        }
        output.setResponse(response);
        return output;
    }
    
    /**
     * Metodo para asignar metas
     * @param request
     * @return 
     */
    public OutputJson registraAsignacionMeta(HttpServletRequest request){
        int idGrupo = Integer.parseInt(request.getParameter("id_grupo"));
        int idTurno = Integer.parseInt(request.getParameter("id_turno"));
        int idMeta =  Integer.parseInt(request.getParameter("id_meta"));
        String fecha = request.getParameter("dia_meta");
        BigDecimal valor = BigDecimal.valueOf(Double.parseDouble(request.getParameter("valor_meta")));
        
        String[] strings = fecha.split("/");
        String year = strings[2];
        String mont = strings[1];
        String day = strings[0];
        String diaMeta =  year + "/" + mont+ "/"+ day;
                
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion controllerAutenticacion = new ControllerAutenticacion();
        
        try{
            if(controllerAutenticacion.isValidToken(request)){
                MetasDAO metasDAO = new MetasDAO();
                ResultInteger result = metasDAO.validaDataForAsignacion(idMeta, idTurno, diaMeta);
                if(result.getResult().equals(0)){
                    metasDAO.registraAsignacion(idGrupo, idTurno, idMeta, diaMeta, valor);
                    response.setMessage(MSG_SUCESS);
                    response.setSucessfull(true);
                }else{
                    response.setMessage("Ya se ha registrado el turno " + idTurno);
                    response.setSucessfull(false);
                }
            }else{
                response.setMessage(MSG_LOGOUT);
                response.setSucessfull(false);
            }
        }catch (Exception ex){
            response.setSucessfull(false);
            response.setMessage(MSG_ERROR + ex.getMessage());
        }
        output.setResponse(response);
        return output;
    }
    
    /**
     * Metodo que devuelve las asignaciones del año
     * @param request
     * @return 
     */
    public OutputJson getAllAsignacionesByYear(HttpServletRequest request){
        int year = Integer.parseInt(request.getParameter("year"));
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        
        try{
            if(autenticacion.isValidToken(request)){
                MetasDAO metasDAO = new MetasDAO();
                MetasAsignacionResponseJson marj = new MetasAsignacionResponseJson();
                        
                marj.setListMetasAsignacion(metasDAO.getAllAsignacionesByYear(year));
                output.setData(marj);
                response.setSucessfull(true);
                response.setMessage(MSG_SUCESS);
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
    
    public OutputJson getAsignacionById(HttpServletRequest request){
        int idAsignacion = Integer.parseInt(request.getParameter("id_pro_metas"));
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        
        try{
            if(autenticacion.isValidToken(request)){
                MetasDAO metasDAO = new MetasDAO();
                ResultInteger result = metasDAO.validaIfExistAsignacion(idAsignacion);
                if(result.getResult().equals(1)){
                    MetasAsignacionResponseJson marj = new MetasAsignacionResponseJson();
                        
                    marj.setMetasAsignacion(metasDAO.getAsignacionById(idAsignacion));
                    output.setData(marj);
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
    
    public OutputJson deleteAsignacionMeta(HttpServletRequest request){
        int idAsignacion = Integer.parseInt(request.getParameter("id_pro_meta"));
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        
        try{
            if(autenticacion.isValidToken(request)){
                MetasDAO metasDAO = new MetasDAO();
                
                ResultInteger result = metasDAO.validaIfExistAsignacion(idAsignacion);
                if(result.getResult().equals(1)){
                    metasDAO.deleteAsignacionMeta(idAsignacion);
                    response.setMessage(MSG_SUCESS);
                    response.setSucessfull(true);
                }else{
                    response.setMessage(MSG_NO_EXIST);
                    response.setSucessfull(true);
                }
            }else{
                response.setMessage(MSG_LOGOUT);
                response.setSucessfull(false);
            }
        }catch (Exception ex){
            response.setSucessfull(false);
            response.setMessage(MSG_ERROR + ex.getMessage());
        }
        output.setResponse(response);
        return output;
    }
    
    public OutputJson updateAsignacionMeta(HttpServletRequest request){
        int idAsignacion = Integer.parseInt(request.getParameter("id_pro_meta"));
        int idGrupo = Integer.parseInt(request.getParameter("id_grupo"));
        int idTurno = Integer.parseInt(request.getParameter("id_turno"));
        int idMeta =  Integer.parseInt(request.getParameter("id_meta"));
        int borrar = Integer.parseInt(request.getParameter("borrar"));
        String fecha = request.getParameter("dia_meta");
        BigDecimal valor = BigDecimal.valueOf(Double.parseDouble(request.getParameter("valor_meta")));
        
        String[] strings = fecha.split("/");
        String year = strings[2];
        String mont = strings[1];
        String day = strings[0];
        String diaMeta =  year + "/" + mont+ "/"+ day;
                
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion controllerAutenticacion = new ControllerAutenticacion();
        
        try{
            if(controllerAutenticacion.isValidToken(request)){
                MetasDAO metasDAO = new MetasDAO();
                    metasDAO.updateAsignacionMeta(idAsignacion, idTurno, idGrupo, idMeta, diaMeta, valor, borrar);
                    response.setMessage(MSG_SUCESS);
                    response.setSucessfull(true);
            }else{
                response.setMessage(MSG_LOGOUT);
                response.setSucessfull(false);
            }
        }catch (Exception ex){
            response.setSucessfull(false);
            response.setMessage(MSG_ERROR + ex.getMessage());
        }
        output.setResponse(response);
        return output;
    }
}