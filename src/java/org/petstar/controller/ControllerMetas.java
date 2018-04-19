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
import static org.petstar.configurations.utils.convertStringToSql;
import org.petstar.dao.LineasDAO;

/**
 * Controlador de Metas
 * @author Tech-Pro
 */
public class ControllerMetas {
    private static final String TABLE_GRUPOS = "pet_cat_grupo";
    private static final String TABLE_LINEAS = "pet_cat_lineas";
    private static final String TABLE_TURNOS = "pet_cat_turno";
    private static final String MSG_SUCESS = "OK";
    private static final String MSG_LOGOUT = "Inicie sesión nuevamente";
    private static final String MSG_ERROR  = "Descripción de error: ";
    private static final String MSG_INVALID= "Ya existe una meta con esos valores.";
    private static final String MSG_NO_EXIST= "La meta no existe.";
    
    /**
     * Consulta de Metas
     * Metodo que devuelve la lista de metas que se encuentran en el catálogo.
     * @param request
     * @return 
     */
    public OutputJson getAllMetas(HttpServletRequest request){
        
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
//        ControllerAutenticacion controllerAutenticacion = new ControllerAutenticacion();
        
        try{
//            if(controllerAutenticacion.isValidToken(request)){
                MetasDAO metasDAO = new MetasDAO();
                MetasDataResponseJson data = new MetasDataResponseJson();
                data.setListMetas(metasDAO.getAllMetas());
                
                output.setData(data);
                response.setMessage(MSG_SUCESS);
                response.setSucessfull(true);
//            }else{
//                response.setMessage(MSG_LOGOUT);
//                response.setSucessfull(false);
//            }
        }catch (Exception ex){
            response.setSucessfull(false);
            response.setMessage(MSG_ERROR + ex.getMessage());
        }
        output.setResponse(response);
        return output;
    }
    
    /**
     * Consulta de Metas Especifica
     * Metodo que devuelve los datos de una meta de acuerdo al id enviado
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
     * Registrar Metas
     * Metodo que registra nuevas Metas.
     * @param request
     * @return 
     */
    public OutputJson insertNewMeta(HttpServletRequest request){
        String dia = request.getParameter("dia");
        BigDecimal meta = new BigDecimal(request.getParameter("meta"));
        BigDecimal tmp = new BigDecimal(request.getParameter("tmp"));
        BigDecimal vel = new BigDecimal(request.getParameter("velocidad"));
        int idTurno = Integer.parseInt(request.getParameter("id_turno"));
        int idGrupo = Integer.parseInt(request.getParameter("id_grupo"));
        int idLinea = Integer.parseInt(request.getParameter("id_linea"));
        
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
//        ControllerAutenticacion controllerAutenticacion = new ControllerAutenticacion();
        
        try{
//            if(controllerAutenticacion.isValidToken(request)){
                MetasDAO metasDAO = new MetasDAO();
//                ResultInteger result = metasDAO.validaDataForInsertCarga(idLinea, meta);
//                if(result.getResult().equals(0)){
                    metasDAO.insertNewMeta(convertStringToSql(dia), meta, tmp, vel, idTurno, idGrupo, idLinea);
                
                    response.setMessage(MSG_SUCESS);
                    response.setSucessfull(true);
//                }else{
//                    response.setMessage(MSG_INVALID);
//                    response.setSucessfull(false);
//                }
//            }else{
//                response.setMessage(MSG_LOGOUT);
//                response.setSucessfull(false);
//            }
        }catch (Exception ex){
            response.setSucessfull(false);
            response.setMessage(MSG_ERROR + ex.getMessage());
        }
        output.setResponse(response);
        return output;
    }
    
    /**
     * Modificación de Metas
     * Metodo que actualiza la información de una meta
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
     * Carga de Combos
     * Metodo que se encarga de poblar las listasnecesarias para los combobox
     * @param request
     * @return 
     */
    public OutputJson loadCombobox(HttpServletRequest request){
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
//        ControllerAutenticacion controllerAutenticacion = new ControllerAutenticacion();
        
        try{
//            if(controllerAutenticacion.isValidToken(request)){
                CatalogosDAO catalogosDAO = new CatalogosDAO();
                LineasDAO lineasDAO = new LineasDAO();
                MetasDataResponseJson data = new MetasDataResponseJson();
                
                data.setListGrupos(catalogosDAO.getCatalogos(TABLE_GRUPOS));
                data.setListTurnos(catalogosDAO.getCatalogos(TABLE_TURNOS));
                data.setListLineas(lineasDAO.getLineasData());
                
                output.setData(data);
                response.setMessage(MSG_SUCESS);
                response.setSucessfull(true);
//            }else{
//                response.setMessage(MSG_LOGOUT);
//                response.setSucessfull(false);
//            }
        }catch (Exception ex){
            response.setSucessfull(false);
            response.setMessage(MSG_ERROR + ex.getMessage());
        }
        output.setResponse(response);
        return output;
    }
    
    /**
     * Registro de Asignaciones de Metas
     * Metodo que se encarga del registro de una asignación de una Meta
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
     * Consulta de Asignaciones
     * Metodo que devuelve la lista de asignaciones de todo un año en especifico.
     * @param request
     * @return 
     */
    public OutputJson getAllAsignacionesByYear(HttpServletRequest request){
        int year = Integer.parseInt(request.getParameter("year"));
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        //ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        
        try{
            //if(autenticacion.isValidToken(request)){
                MetasDAO metasDAO = new MetasDAO();
                MetasAsignacionResponseJson marj = new MetasAsignacionResponseJson();
                        
                marj.setListMetasAsignacion(metasDAO.getAllAsignacionesByYear(year));
                output.setData(marj);
                response.setSucessfull(true);
                response.setMessage(MSG_SUCESS);
//            }else{
//                response.setSucessfull(false);
//                response.setMessage(MSG_LOGOUT);
//            }
        } catch(Exception ex){
            response.setSucessfull(false);
            response.setMessage(MSG_ERROR + ex.getMessage());
        }
        output.setResponse(response);
        return output;
    }
    
    /**
     * Consulta de Asignaciones por ID
     * Metodo que devuelve la información de una asignaciones en especifico.
     * @param request
     * @return 
     */
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
    
    /**
     * Eliminación de Asignaciones de Metas
     * Metodo que se encarga de eliminar una asignación de acuerdo al id
     * @param request
     * @return 
     */
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
    
    /**
     * Modificación de Asignaciones de Metas
     * Metodo que se encarga en la actualización de una asignación
     * @param request
     * @return 
     */
    public OutputJson updateAsignacionMeta(HttpServletRequest request){
        int idAsignacion = Integer.parseInt(request.getParameter("id_pro_metas"));
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