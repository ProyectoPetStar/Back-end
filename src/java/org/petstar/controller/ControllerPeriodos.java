package org.petstar.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.math.BigDecimal;
import javax.servlet.http.HttpServletRequest;
import org.petstar.dao.PeriodosDAO;
import org.petstar.dao.LineasDAO;
import org.petstar.dto.PeriodosDTO;
import org.petstar.dto.UserDTO;
import org.petstar.model.OutputJson;
import org.petstar.model.PeriodosResponseJson;
import org.petstar.model.ResponseJson;
import static org.petstar.configurations.utils.getNameOfMes;
import static org.petstar.configurations.utils.getCurrentDate;
import org.petstar.dto.ResultInteger;

/**
 *
 * @author Tech-Pro
 */
public class ControllerPeriodos {
    private static final String MSG_SUCESS = "OK";
    private static final String MSG_LOGOUT = "Inicie sesión nuevamente";
    private static final String MSG_ERROR  = "Descripción de error: ";
    private static final String MSG_NOEXIS = "El Periodo ya fue registrado";
    private static final String MSG_ERRINS = "Ocurrio un error al abrir periodo";
    
    public OutputJson openPeriodo(HttpServletRequest request){
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        
        try{
            UserDTO sesion = autenticacion.isValidToken(request);
            if(sesion != null ){
                PeriodosResponseJson data = new PeriodosResponseJson();
                LineasDAO lineasDAO = new LineasDAO();
                PeriodosDAO periodosDAO = new PeriodosDAO();
                PeriodosDTO newPeriodo = new PeriodosDTO();
                PeriodosDTO lastPeriodo = periodosDAO.getLastPeriodo();
                
                if(lastPeriodo.getMes() == 12){
                    newPeriodo.setAnio(lastPeriodo.getAnio() + 1);
                    newPeriodo.setMes(1);
                }else{
                    newPeriodo.setAnio(lastPeriodo.getAnio());
                    newPeriodo.setMes(lastPeriodo.getMes() + 1);
                }
                newPeriodo.setDescripcion_mes(getNameOfMes(newPeriodo.getMes()));
                                
                data.setPeriodo(newPeriodo);
                data.setListLineas(lineasDAO.getLineasActive());
                
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
        return  output;
    }
    
    public OutputJson saveDetailsPeriodo(HttpServletRequest request){
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        
        try{
            int year = Integer.valueOf(request.getParameter("anio"));
            int month = Integer.valueOf(request.getParameter("mes"));
            String jsonString = request.getParameter("metas_esperadas");
            
            UserDTO sesion = autenticacion.isValidToken(request);
            if(sesion != null ){
                PeriodosDAO periodosDAO = new PeriodosDAO();
                ResultInteger result = periodosDAO.validateForInsert(year, month);
                
                if(result.getResult().equals(0)){
                    PeriodosDTO newPeriodo = new PeriodosDTO();
                    newPeriodo.setAnio(year);
                    newPeriodo.setMes(month);
                    newPeriodo.setDescripcion_mes(getNameOfMes(month));
                    newPeriodo.setEstatus(0);
                    
                    periodosDAO.insertPeriodo(newPeriodo, sesion.getId_acceso(), getCurrentDate());
                    PeriodosDTO periodo = periodosDAO.getLastPeriodo();
                    if(periodo.getAnio() == newPeriodo.getAnio() && periodo.getMes() == newPeriodo.getMes()){
                        JsonParser jsonParser = new JsonParser();
                        String jsonArrayString = jsonString;
                        JsonArray arrayFromString = jsonParser.parse(jsonArrayString).getAsJsonArray();

                        for(int i=0; i<arrayFromString.size(); i++){
                            PeriodosDTO row = new PeriodosDTO();
                            JsonObject objectFromString = jsonParser.parse(arrayFromString.get(i).toString()).getAsJsonObject();
                            row.setEficiencia_teorica(new BigDecimal(objectFromString.get("eficiencia_teorica").toString()));
                            row.setDisponibilidad(new BigDecimal(objectFromString.get("disponibilidad").toString()));
                            row.setUtilizacion(new BigDecimal(objectFromString.get("utilizacion").toString()));
                            row.setId_linea(Integer.parseInt(objectFromString.get("id_linea").toString()));
                            row.setCalidad(new BigDecimal(objectFromString.get("calidad").toString()));
                            row.setOee(new BigDecimal(objectFromString.get("oee").toString()));
                            row.setId_periodo(periodo.getId_periodo());
                            periodosDAO.saveMetasPeriodo(row);
                        }
                        response.setMessage(MSG_SUCESS);
                        response.setSucessfull(true);
                    }else{
                        response.setMessage(MSG_ERRINS);
                        response.setSucessfull(false);
                    }
                }else{
                    response.setMessage(MSG_NOEXIS);
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
        return  output;
    }
    
    public OutputJson changeEstatusPeriodo(HttpServletRequest request, int estatus){
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        
        try{
            int idPeriodo = Integer.valueOf(request.getParameter("id_periodo"));
            UserDTO sesion = autenticacion.isValidToken(request);
            if(sesion != null ){
                PeriodosDAO periodosDAO = new PeriodosDAO();
                periodosDAO.changeEstatus(idPeriodo, estatus);
                
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
        return  output;
    }
    
    public OutputJson getAllPeriodos(HttpServletRequest request){
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        
        try{
            UserDTO sesion = autenticacion.isValidToken(request);
            if(sesion != null ){
                PeriodosResponseJson data = new PeriodosResponseJson();
                PeriodosDAO periodosDAO = new PeriodosDAO();
                
                data.setListPeriodos(periodosDAO.getAllPeriodos());
                
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
        return  output;
    }
    
    public OutputJson getDetailsByPeriodo(HttpServletRequest request){
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        
        try{
            int idPeriodo = Integer.valueOf(request.getParameter("id_periodo"));
            UserDTO sesion = autenticacion.isValidToken(request);
            if(sesion != null ){
                PeriodosResponseJson data = new PeriodosResponseJson();
                PeriodosDAO periodosDAO = new PeriodosDAO();
                
                data.setListDetailsPeriodo(periodosDAO.getDetailsPeriodo(idPeriodo));
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
        return  output;
    }
    
    public OutputJson updateDetailsPeriodo(HttpServletRequest request){
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        
        try{
            String jsonString = request.getParameter("metas_esperadas");
            
            UserDTO sesion = autenticacion.isValidToken(request);
            if(sesion != null ){
                PeriodosDAO periodosDAO = new PeriodosDAO();
                JsonParser jsonParser = new JsonParser();
                String jsonArrayString = jsonString;
                JsonArray arrayFromString = jsonParser.parse(jsonArrayString).getAsJsonArray();

                for(int i=0; i<arrayFromString.size(); i++){
                    PeriodosDTO row = new PeriodosDTO();
                    JsonObject objectFromString = jsonParser.parse(arrayFromString.get(i).toString()).getAsJsonObject();
                    row.setId_metas_periodo(Integer.parseInt(objectFromString.get("id_metas_periodo").toString()));
                    row.setEficiencia_teorica(new BigDecimal(objectFromString.get("eficiencia_teorica").toString()));
                    row.setDisponibilidad(new BigDecimal(objectFromString.get("disponibilidad").toString()));
                    row.setUtilizacion(new BigDecimal(objectFromString.get("utilizacion").toString()));
                    row.setCalidad(new BigDecimal(objectFromString.get("calidad").toString()));
                    row.setOee(new BigDecimal(objectFromString.get("oee").toString()));
                    periodosDAO.updateMetasPeriodo(row);
                }
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
        return  output;
    }
}
