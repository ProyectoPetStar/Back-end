package org.petstar.controller;

import java.math.BigDecimal;
import java.sql.Date;
import javax.servlet.http.HttpServletRequest;
import org.petstar.dao.CatalogosDAO;
import org.petstar.dao.MetasDAO;
import org.petstar.model.MetasDataResponseJson;
import org.petstar.model.OutputJson;
import org.petstar.model.ResponseJson;
import static org.petstar.configurations.utils.convertStringToSql;
import static org.petstar.configurations.utils.getCurrentDate;
import static org.petstar.configurations.utils.sumarFechasDias;
import static org.petstar.configurations.utils.convertSqlToString;
import org.petstar.dao.LineasDAO;
import org.petstar.dao.PeriodosDAO;
import org.petstar.dto.MetasDTO;
import org.petstar.dto.PeriodosDTO;
import org.petstar.dto.ResultInteger;

/**
 * Controlador de Metas
 * @author Tech-Pro
 */
public class ControllerMetas {
    private static final String TABLE_GRUPOS = "pet_cat_grupo";
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
     * @throws java.lang.Exception 
     */
    public OutputJson getAllMetas(HttpServletRequest request) throws Exception{
        int idPeriodo = Integer.parseInt(request.getParameter("id_periodo"));
        int idLinea = Integer.parseInt(request.getParameter("id_linea"));
        
        PeriodosDAO periodosDAO = new PeriodosDAO();
        PeriodosDTO periodosDTO = periodosDAO.getPeriodoById(idPeriodo);
        
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
//        ControllerAutenticacion controllerAutenticacion = new ControllerAutenticacion();
        
        try{
//            if(controllerAutenticacion.isValidToken(request)){
                MetasDAO metasDAO = new MetasDAO();
                MetasDataResponseJson data = new MetasDataResponseJson();
                data.setListMetas(metasDAO.getAllMetas(periodosDTO.getMes(), periodosDTO.getAnio(), idLinea));
                
                for(MetasDTO meta:data.getListMetas()){
                    meta.setDia(sumarFechasDias(meta.getDia(), 2));
                    meta.setDia_string(convertSqlToString(meta.getDia()));
                    if(null != meta.getFecha_modificacion()){
                        meta.setFecha_modificacion(sumarFechasDias(meta.getFecha_modificacion(), 2));
                    } 
                }
                
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
    public OutputJson getMetaById(HttpServletRequest request){
        int idMeta = Integer.parseInt(request.getParameter("id_meta"));
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
//        ControllerAutenticacion controllerAutenticacion = new ControllerAutenticacion();
        
        try{
//            if(controllerAutenticacion.isValidToken(request)){
                MetasDAO metasDAO = new MetasDAO();
                ResultInteger result = metasDAO.validaIfExistMeta(idMeta);
                if(result.getResult().equals(1)){
                    MetasDataResponseJson data = new MetasDataResponseJson();
                    data.setMetasDTO(metasDAO.getMetaById(idMeta));
                    data.getMetasDTO().setDia(sumarFechasDias(data.getMetasDTO().getDia(), 2));
                    data.getMetasDTO().setDia_string(convertSqlToString(data.getMetasDTO().getDia()));
                    if(null != data.getMetasDTO().getFecha_modificacion()){
                        data.getMetasDTO().setFecha_modificacion(sumarFechasDias(data.getMetasDTO().getFecha_modificacion(), 2));
                    } 
                    
                    output.setData(data);
                    response.setMessage(MSG_SUCESS);
                    response.setSucessfull(true);
                }else{
                    response.setMessage(MSG_NO_EXIST);
                    response.setSucessfull(false);
                }
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
                ResultInteger result = metasDAO.validaDataForInsertMeta(convertStringToSql(dia), idTurno, idGrupo, idLinea);
                if(result.getResult().equals(0)){
                    metasDAO.insertNewMeta(convertStringToSql(dia), meta, tmp, vel, idTurno, idGrupo, idLinea);
                
                    response.setMessage(MSG_SUCESS);
                    response.setSucessfull(true);
                }else{
                    response.setMessage(MSG_INVALID);
                    response.setSucessfull(false);
                }
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
    public OutputJson updateMeta(HttpServletRequest request){
        String dia = request.getParameter("dia");
        BigDecimal meta = new BigDecimal(request.getParameter("meta"));
        BigDecimal tmp = new BigDecimal(request.getParameter("tmp"));
        BigDecimal vel = new BigDecimal(request.getParameter("velocidad"));
        int idMeta = Integer.parseInt(request.getParameter("id_meta"));
        int idTurno = Integer.parseInt(request.getParameter("id_turno"));
        int idGrupo = Integer.parseInt(request.getParameter("id_grupo"));
        int idLinea = Integer.parseInt(request.getParameter("id_linea"));
        int idFiles = Integer.parseInt(request.getParameter("id_file"));
        int estatus = Integer.parseInt(request.getParameter("estatus"));
                
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
//        ControllerAutenticacion controllerAutenticacion = new ControllerAutenticacion();
        
        try{
//            if(controllerAutenticacion.isValidToken(request)){
                MetasDAO metasDAO = new MetasDAO();
                ResultInteger result = metasDAO.validaDataForUpdateMeta(idMeta, convertStringToSql(dia), idTurno, idGrupo, idLinea);
                if(result.getResult().equals(0)){
                    Date fechaMod = getCurrentDate();
                    metasDAO.updateMeta(idMeta, convertStringToSql(dia), meta, tmp, vel, idTurno, idGrupo, idLinea, idFiles, 1, fechaMod, estatus);

                    response.setMessage(MSG_SUCESS);
                    response.setSucessfull(true);
                }else{
                    response.setMessage(MSG_INVALID);
                    response.setSucessfull(false);
                }
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

    public OutputJson deleteMeta(HttpServletRequest request){
        int idMeta = Integer.parseInt(request.getParameter("id_meta"));
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
//        ControllerAutenticacion controllerAutenticacion = new ControllerAutenticacion();
        
        try{
//            if(controllerAutenticacion.isValidToken(request)){
                MetasDAO metasDAO = new MetasDAO();
                metasDAO.deleteMeta(idMeta);
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
}