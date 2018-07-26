package org.petstar.controller.ETAD;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONObject;
import org.petstar.controller.ControllerAutenticacion;
import org.petstar.dao.CatalogosDAO;
import org.petstar.dao.ETAD.IndicadoresDiariosDAO;
import org.petstar.dao.PeriodosDAO;
import org.petstar.dto.ETAD.PetIndicadorDiario;
import org.petstar.dto.PeriodosDTO;
import org.petstar.dto.ResultInteger;
import org.petstar.dto.UserDTO;
import org.petstar.model.ETAD.IndicadoresResponse;
import org.petstar.model.OutputJson;
import org.petstar.model.ResponseJson;
import static org.petstar.configurations.utils.convertSqlToDay;
import static org.petstar.configurations.utils.convertStringToSql;
import static org.petstar.configurations.utils.sumarFechasDias;
import static org.petstar.configurations.utils.getCurrentDate;
import static org.petstar.configurations.utils.getCurrentDayByTurno;
import static org.petstar.configurations.utils.getTurnoForSaveProduction;
import static org.petstar.configurations.utils.obtenerAnio;
import static org.petstar.configurations.utils.obtenerMes;
import org.petstar.dao.ETAD.IndicadoresMensualesDAO;
import org.petstar.dto.ETAD.PetIndicadorMensual;

/**
 *
 * @author Tech-Pro
 */
public class IndicadoresController {
    private static final String MSG_SUCESS = "OK";
    private static final String MSG_LOGOUT = "Inicie sesión nuevamente";
    private static final String MSG_ERROR  = "Descripción de error: ";
    private static final String MSG_RECORD = "Ya hay indicadores registrados.";
    private static final String MSG_EMPTY  = "No hay indicadores registrados.";
    private static final String MSG_PERIODO= "El periodo ya esta cerrado.";
    private static final String MSG_METAS  = "No hay metas registradas para el Área y Día Seleccionado";
    private static final String MSG_METAM  = "No hay metas registradas para el Área y Periodo Seleccionado";
    
    public OutputJson loadCombobox(HttpServletRequest request){
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
            
        try{
            UserDTO session = autenticacion.isValidToken(request);
            if(session != null){
                IndicadoresResponse data = new IndicadoresResponse();
                CatalogosDAO catalogosDAO = new CatalogosDAO();
                PeriodosDAO periodosDAO = new PeriodosDAO();
                
                data.setDia_string(convertSqlToDay(getCurrentDayByTurno(
                        getTurnoForSaveProduction()), new SimpleDateFormat("dd/MM/yyyy")));
                data.setListPeriodos(periodosDAO.getAllPeriodos());
                data.setListEtads(catalogosDAO.getCatalogosActive("pet_cat_etad"));
                data.setListGrupos(catalogosDAO.getCatalogosActive("pet_cat_grupo"));
                
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
    
    public OutputJson getAllIndicadores(HttpServletRequest request){
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
            
        try{
            int idPeriodo = Integer.valueOf(request.getParameter("id_periodo"));
            int idEtad = Integer.valueOf(request.getParameter("id_etad"));
            String frecuencia = request.getParameter("frecuencia");
            UserDTO session = autenticacion.isValidToken(request);
            if(session != null){
                IndicadoresResponse data = new IndicadoresResponse();
                PeriodosDAO periodosDAO = new PeriodosDAO();
                PeriodosDTO periodo = periodosDAO.getPeriodoById(idPeriodo);
                
                if(frecuencia.equals("mensual")){
                    IndicadoresMensualesDAO mensualesDAO = new IndicadoresMensualesDAO();
                    
                    data.setListIndicadorMensuales(mensualesDAO.getIndicadoresExtract(idPeriodo, idEtad));
                    if(!data.getListIndicadorMensuales().isEmpty()){
                        output.setData(data);
                        response = message(true, MSG_SUCESS);
                    }else{
                        response = message(false, MSG_METAM);
                    }
                }else if (frecuencia.equals("diario")){
                    IndicadoresDiariosDAO diariosDAO = new IndicadoresDiariosDAO();
                    
                    data.setListIndicadorDiarios(diariosDAO.getIndicadoresExtract(periodo, idEtad));
                    if(!data.getListIndicadorDiarios().isEmpty()){
                        for(PetIndicadorDiario row:data.getListIndicadorDiarios()){
                            row.setDia(sumarFechasDias(row.getDia(), 2));
                            row.setDia_string(convertSqlToDay(row.getDia(), new SimpleDateFormat("dd/MM/yyyy")));
                        }
                        output.setData(data);
                        response = message(true, MSG_SUCESS);
                    }else{
                        response = message(false, MSG_METAS);
                    }
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
    
    public OutputJson viewKpiForSave(HttpServletRequest request){
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
            
        try{
            int idEtad = Integer.valueOf(request.getParameter("id_etad"));
            String frecuencia = request.getParameter("frecuencia");
            UserDTO session = autenticacion.isValidToken(request);
            if(session != null){
                IndicadoresResponse data = new IndicadoresResponse();
                
                if(frecuencia.equals("mensual")){
                    int idPeriodo = Integer.valueOf(request.getParameter("id_periodo"));
                    IndicadoresMensualesDAO mensualesDAO = new IndicadoresMensualesDAO();
                    data.setListIndicadorMensuales(mensualesDAO.getKPIforIndicadores(idPeriodo, idEtad));
                    
                    if(!data.getListIndicadorMensuales().isEmpty()){
                        output.setData(data);
                        response = message(true, MSG_SUCESS);
                    }else{
                        response = message(false, MSG_METAM);
                    }
                }else if (frecuencia.equals("diario")){
                    String dia = request.getParameter("dia");
                    IndicadoresDiariosDAO diariosDAO = new IndicadoresDiariosDAO();
                    Date day = convertStringToSql(dia);
                    data.setListIndicadorDiarios(diariosDAO.getKPIforIndicadores(day, idEtad));
                    if(!data.getListIndicadorDiarios().isEmpty()){
                        output.setData(data);
                        response = message(true, MSG_SUCESS);
                    }else{
                        response = message(false, MSG_METAS);
                    }
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
    
    public OutputJson insertIndicadores(HttpServletRequest request){
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        Gson gson = new Gson();
            
        try{
            UserDTO session = autenticacion.isValidToken(request);
            if(session != null){
                int idEtad = Integer.valueOf(request.getParameter("id_etad"));
                String frecuencia = request.getParameter("frecuencia");
                String jsonString = request.getParameter("datos");
                
                JSONObject jsonResponse = new JSONObject(jsonString);
                PeriodosDAO periodosDAO = new PeriodosDAO();
                
                if(frecuencia.equals("mensual")){
                    IndicadoresMensualesDAO mensualesDAO = new IndicadoresMensualesDAO();
                    
                    TypeToken<List<PetIndicadorMensual>> token = new TypeToken<List<PetIndicadorMensual>>(){};
                    List<PetIndicadorMensual> listIndicadorM = gson.fromJson(
                            jsonResponse.getJSONArray("indicadores").toString(), token.getType());
                    PeriodosDTO periodo = periodosDAO.getPeriodoById(listIndicadorM.get(0).getId_periodo());
                    
                    if(periodo.getEstatus() == 0){
                        ResultInteger result = mensualesDAO.validaRecordsForPeriodoAndEtad(
                                listIndicadorM.get(0).getId_periodo(), idEtad, listIndicadorM.get(0).getId_grupo());

                        if(result.getResult().equals(0)){
                            mensualesDAO.insertIndicadoresDiarios(listIndicadorM, getCurrentDate(), session.getId_acceso());
                            response = message(true, MSG_SUCESS);
                        }else{
                            response = message(false, MSG_RECORD);
                        }
                    }else{
                        response = message(false, MSG_PERIODO);
                    }
                }else if(frecuencia.equals("diario")){
                    IndicadoresDiariosDAO diariosDAO = new IndicadoresDiariosDAO();
                    String dia = request.getParameter("dia");
                    Date day = convertStringToSql(dia);
                    PeriodosDTO periodo = periodosDAO.getPeriodoByMesAndAnio(obtenerMes(day), obtenerAnio(day));
                    
                    if(periodo.getEstatus() == 0){
                        TypeToken<List<PetIndicadorDiario>> token = new TypeToken<List<PetIndicadorDiario>>(){};
                        List<PetIndicadorDiario> listIndicadorD = gson.fromJson(
                                jsonResponse.getJSONArray("indicadores").toString(), token.getType());
                        ResultInteger result = diariosDAO.validaRecordsForDayAndEtad(
                                day, idEtad, listIndicadorD.get(0).getId_grupo());

                        if(result.getResult().equals(0)){
                            diariosDAO.insertIndicadoresDiarios(listIndicadorD, day, session.getId_acceso());
                            response = message(true, MSG_SUCESS);
                        }else{
                            response = message(false, MSG_RECORD);
                        }
                    }else{
                        response = message(false, MSG_PERIODO);
                    }
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
    
    public OutputJson getDetailIndicadores(HttpServletRequest request){
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
            
        try{
            int idGrupo = Integer.valueOf(request.getParameter("id_grupo"));
            int idEtad = Integer.valueOf(request.getParameter("id_etad"));
            String frecuencia = request.getParameter("frecuencia");
            UserDTO session = autenticacion.isValidToken(request);
            if(session != null){
                IndicadoresResponse data = new IndicadoresResponse();
                
                if(frecuencia.equals("mensual")){
                    int idPeriodo = Integer.valueOf(request.getParameter("id_periodo"));
                    IndicadoresMensualesDAO mensualesDAO = new IndicadoresMensualesDAO();
                    
                    data.setListIndicadorMensuales(mensualesDAO.getIndicadoresByPeriodoAndEtadAndGrupo(idPeriodo, idEtad, idGrupo));
                    if(!data.getListIndicadorMensuales().isEmpty()){
                        output.setData(data);
                        response = message(true, MSG_SUCESS);
                    }else{
                        response = message(false, MSG_EMPTY);
                    }
                }else if (frecuencia.equals("diario")){
                    String dia = request.getParameter("dia");
                    Date day = convertStringToSql(dia);
                    IndicadoresDiariosDAO diariosDAO = new IndicadoresDiariosDAO();
                    
                    data.setListIndicadorDiarios(diariosDAO.getIndicadoresByDiaAndEtadAndGrupo(day, idEtad, idGrupo));
                    if(!data.getListIndicadorDiarios().isEmpty()){
                        for(PetIndicadorDiario row:data.getListIndicadorDiarios()){
                            row.setDia(sumarFechasDias(row.getDia(), 2));
                            row.setDia_string(convertSqlToDay(row.getDia(), new SimpleDateFormat("dd/MM/yyyy")));
                        }
                        output.setData(data);
                        response = message(true, MSG_SUCESS);
                    }else{
                        response = message(false, MSG_EMPTY);
                    }
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
    
    public OutputJson updateIndicadores(HttpServletRequest request){
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        Gson gson = new Gson();
            
        try{
            UserDTO session = autenticacion.isValidToken(request);
            if(session != null){
                String frecuencia = request.getParameter("frecuencia");
                String jsonString = request.getParameter("datos");
                
                JSONObject jsonResponse = new JSONObject(jsonString);
                IndicadoresDiariosDAO diariosDAO = new IndicadoresDiariosDAO();
                IndicadoresMensualesDAO mensualesDAO = new IndicadoresMensualesDAO();
                if(frecuencia.equals("mensual")){
                    TypeToken<List<PetIndicadorMensual>> token = new TypeToken<List<PetIndicadorMensual>>(){};
                    List<PetIndicadorMensual> listIndicadorM = gson.fromJson(
                            jsonResponse.getJSONArray("indicadores").toString(), token.getType());
                    
                    mensualesDAO.updateIndicadoresMensuales(listIndicadorM, getCurrentDate(), session.getId_acceso());
                    response = message(true, MSG_SUCESS);  
                }else if(frecuencia.equals("diario")){
                    TypeToken<List<PetIndicadorDiario>> token = new TypeToken<List<PetIndicadorDiario>>(){};
                    List<PetIndicadorDiario> listIndicadorD = gson.fromJson(
                            jsonResponse.getJSONArray("indicadores").toString(), token.getType());
                    
                    diariosDAO.updateIndicadoresDiarios(listIndicadorD, getCurrentDate(), session.getId_acceso());
                    response = message(true, MSG_SUCESS);                    
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
    
    public OutputJson changeEstatusIndicadoresDiarios(HttpServletRequest request, int estatus){
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
            
        try{
            int idGrupo = Integer.valueOf(request.getParameter("id_grupo"));
            int idEtad = Integer.valueOf(request.getParameter("id_etad"));
            String dia = request.getParameter("dia");
            Date day = convertStringToSql(dia);
            UserDTO session = autenticacion.isValidToken(request);
            if(session != null){
                IndicadoresDiariosDAO diariosDAO = new IndicadoresDiariosDAO();
                
                diariosDAO.changeEstatusIndicadoresDiarios(day, idGrupo, idEtad, estatus);
                response = message(true, MSG_SUCESS);
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
    
    public ResponseJson message(boolean boo, String mensaje){
        ResponseJson responseJson = new ResponseJson();
        responseJson.setMessage(mensaje);
        responseJson.setSucessfull(boo);
        return responseJson;
    }
}
