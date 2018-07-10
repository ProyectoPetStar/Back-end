package org.petstar.controller.ETAD;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONObject;
import org.petstar.controller.ControllerAutenticacion;
import org.petstar.dao.ETAD.PonderacionDAO;
import org.petstar.dto.ETAD.PetPonderacionObjetivoOperativo;
import org.petstar.dto.ResultInteger;
import org.petstar.dto.UserDTO;
import org.petstar.model.OutputJson;
import org.petstar.model.ResponseJson;
import static org.petstar.configurations.utils.getCurrentDate;
import org.petstar.dao.ETAD.KPIOperativosDAO;
import org.petstar.dto.ETAD.PetCatKpiOperativo;
import org.petstar.dto.ETAD.PetEtadKpi;
import org.petstar.dto.ETAD.PetPonderacionKpiOperativo;
import org.petstar.model.ETAD.PonderacionResponse;

/**
 *
 * @author Tech-Pro
 */
public class PonderacionController {
    private static final String MSG_SUCESS = "OK";
    private static final String MSG_LOGOUT = "Inicie sesión nuevamente";
    private static final String MSG_ERROR  = "Descripción de error: ";
    private static final String MSG_EXIST  = "Ya existe ponderacion para este año";
    private static final String MSG_EMPTY  = "No hay registros para este año";
    
    public OutputJson insertPonderacion(HttpServletRequest request){
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        Gson gson = new Gson();
            
        try{
            int tipoPond = Integer.valueOf(request.getParameter("tipo_ponderacion"));
            String jsonString = request.getParameter("meta");
            JSONObject jsonResponse = new JSONObject(jsonString);
            
            UserDTO session = autenticacion.isValidToken(request);
            if(session != null){
                PonderacionDAO ponderacionDAO = new PonderacionDAO();
                /**
                * Tipos de Ponderacion
                * 1.- Ponderacion Anual de Objetivos Operativos
                * 2.- Ponderacion Anual de KPI operativos
                */
                switch(tipoPond){
                    case 1:
                        TypeToken<List<PetPonderacionObjetivoOperativo>> token =
                                new TypeToken<List<PetPonderacionObjetivoOperativo>>(){};
                        List<PetPonderacionObjetivoOperativo> listPPOO = gson.fromJson(
                                jsonResponse.getJSONArray("ponderaciones").toString(), token.getType());
                        ResultInteger result = ponderacionDAO.validateExistRecords(listPPOO.get(0).getAnio());
                        
                        if(result.getResult().equals(0)){
                            ponderacionDAO.insertPonderacionObjetivos(
                                    listPPOO, session.getId_acceso(), getCurrentDate());
                            response.setMessage(MSG_SUCESS);
                            response.setSucessfull(true);
                        }else{
                            response.setMessage(MSG_EXIST);
                            response.setSucessfull(false);
                        }
                    break;
                    case 2:
                        int idEtad = Integer.valueOf(request.getParameter("id_etad"));
                        int year = Integer.valueOf(request.getParameter("anio"));
                        TypeToken<List<PetPonderacionKpiOperativo>> tokenPPKP =
                                new TypeToken<List<PetPonderacionKpiOperativo>>(){};
                        List<PetPonderacionKpiOperativo> listPKOO = gson.fromJson(
                                jsonResponse.getJSONArray("ponderaciones").toString(), tokenPPKP.getType());
                        ResultInteger resultKPI = ponderacionDAO.validateExistRecordsKPI(year, idEtad);
                        
                        if(resultKPI.getResult().equals(0)){
                            ponderacionDAO.insertPonderacionKPI(listPKOO, year, session.getId_acceso(), getCurrentDate());
                            response.setMessage(MSG_SUCESS);
                            response.setSucessfull(true);
                        }else{
                            response.setMessage(MSG_EXIST);
                            response.setSucessfull(false);
                        }
                    break;
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
    
    public OutputJson getPonderacion(HttpServletRequest request){
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
            
        try{
            int tipoPond = Integer.valueOf(request.getParameter("tipo_ponderacion"));
            int year = Integer.valueOf(request.getParameter("anio"));
            
            UserDTO session = autenticacion.isValidToken(request);
            if(session != null){
                PonderacionResponse ponderacionResponse = new PonderacionResponse();
                PonderacionDAO ponderacionDAO = new PonderacionDAO();
                /**
                * Tipos de Ponderacion
                * 1.- Ponderacion Anual de Objetivos Operativos
                * 2.- Ponderacion Anual de KPI operativos
                */
                switch(tipoPond){
                    case 1:
                        ponderacionResponse.setListPonderacionObjetivos(ponderacionDAO.getPonderacionObejtivos(year));
                        if(!ponderacionResponse.getListPonderacionObjetivos().isEmpty()){
                            output.setData(ponderacionResponse);
                            response.setMessage(MSG_SUCESS);
                            response.setSucessfull(true);
                        }else{
                            response.setMessage(MSG_EMPTY);
                            response.setSucessfull(false);
                        }
                    break;
                    case 2:
                        int idEtad = Integer.valueOf(request.getParameter("id_etad"));
                        KPIOperativosDAO kpioDAO = new KPIOperativosDAO();
                        List<HashMap> listHashMaps = new ArrayList<>();
                        List<PetPonderacionKpiOperativo> listPonderacion = ponderacionDAO.getPonderacionKPI(year, idEtad);
                        
                        if(!listPonderacion.isEmpty()){
                            String objetivoName = "";
                            for(PetPonderacionKpiOperativo row:listPonderacion){
                                if(objetivoName.equals(row.getPetEtadKpi().getKpiOperativo().getObjetivoOperativo().getValor())){
                                    HashMap<String, Object> hashMap = new HashMap();
                                    hashMap.put("padre", 1);
                                    hashMap.put("id_ponderacion_kpi_operativo", row.getId_ponderacion_kpi_operativo());
                                    hashMap.put("kpi",row.getPetEtadKpi().getKpiOperativo().getValor());
                                    hashMap.put("ponderacion",row.getPonderacion());
                                    listHashMaps.add(hashMap);
                                }else{
                                    PetPonderacionObjetivoOperativo obj = ponderacionDAO.getPonderacionObejtivoById(
                                            year, row.getPetEtadKpi().getKpiOperativo().getId_cat_objetivo_operativo());
                                    objetivoName = row.getPetEtadKpi().getKpiOperativo().getObjetivoOperativo().getValor();
                                    HashMap<String, Object> hashMap = new HashMap();
                                    hashMap.put("padre", 0);
                                    hashMap.put("kpi",row.getPetEtadKpi().getKpiOperativo().getObjetivoOperativo().getValor());
                                    hashMap.put("ponderacion",obj.getPonderacion());
                                    listHashMaps.add(hashMap);
                                    HashMap<String, Object> hashMap1 = new HashMap();
                                    hashMap1.put("padre", 1);
                                    hashMap1.put("id_ponderacion_kpi_operativo", row.getId_ponderacion_kpi_operativo());
                                    hashMap1.put("kpi",row.getPetEtadKpi().getKpiOperativo().getValor());
                                    hashMap1.put("ponderacion",row.getPonderacion());
                                    listHashMaps.add(hashMap1);
                                }
                            }
                        }else{
                            List<PetPonderacionObjetivoOperativo> listPonObj = ponderacionDAO.getPonderacionObejtivos(year);
                            for(PetPonderacionObjetivoOperativo row:listPonObj){
                                List<PetCatKpiOperativo> listKPIs =kpioDAO.
                                        getKPIOperativosByObjetivoAndEtad(row.getId_objetivo_operativo(), idEtad);
                                if(!listKPIs.isEmpty()){
                                    HashMap<String, Object> hashMap = new HashMap();
                                    hashMap.put("padre", 0);
                                    hashMap.put("kpi",row.getObjetivoOperativo().getValor());
                                    hashMap.put("ponderacion",row.getPonderacion());
                                    listHashMaps.add(hashMap);

                                    for(PetCatKpiOperativo field:listKPIs){
                                        PetEtadKpi etadKpi = ponderacionDAO.getEtadKpi(field.getId(),idEtad);
                                        HashMap<String, Object> map = new HashMap();
                                        map.put("padre", 1);
                                        map.put("id_kpi_etad", etadKpi.getId_kpi_etad());
                                        map.put("kpi",field.getValor());
                                        map.put("ponderacion","");
                                        listHashMaps.add(map);
                                    }
                                }
                            }
                        }
                        ponderacionResponse.setListData(listHashMaps);
                        output.setData(ponderacionResponse);
                        response.setMessage(MSG_SUCESS);
                        response.setSucessfull(true);
                    break;
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
    
    public OutputJson updatePonderacion(HttpServletRequest request){
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        Gson gson = new Gson();
            
        try{
            int tipoPond = Integer.valueOf(request.getParameter("tipo_ponderacion"));
            String jsonString = request.getParameter("meta");
            JSONObject jsonResponse = new JSONObject(jsonString);
            
            UserDTO session = autenticacion.isValidToken(request);
            if(session != null){
                PonderacionDAO ponderacionDAO = new PonderacionDAO();
                /**
                * Tipos de Ponderacion
                * 1.- Ponderacion Anual de Objetivos Operativos
                * 2.- Ponderacion Anual de KPI operativos
                */
                switch(tipoPond){
                    case 1:
                        TypeToken<List<PetPonderacionObjetivoOperativo>> token =
                                new TypeToken<List<PetPonderacionObjetivoOperativo>>(){};
                        List<PetPonderacionObjetivoOperativo> listPPOO = gson.fromJson(
                                jsonResponse.getJSONArray("ponderaciones").toString(), token.getType());
                        
                        ponderacionDAO.updatePonderacionObjetivos(
                                listPPOO, session.getId_acceso(), getCurrentDate());
                        response.setMessage(MSG_SUCESS);
                        response.setSucessfull(true);
                    break;
                    case 2:
                        TypeToken<List<PetPonderacionKpiOperativo>> tokenPPKP =
                                new TypeToken<List<PetPonderacionKpiOperativo>>(){};
                        List<PetPonderacionKpiOperativo> listPKOO = gson.fromJson(
                                jsonResponse.getJSONArray("ponderaciones").toString(), tokenPPKP.getType());
                        
                        ponderacionDAO.updatePonderacionKPI(listPKOO, session.getId_acceso(), getCurrentDate());
                        response.setMessage(MSG_SUCESS);
                        response.setSucessfull(true);
                    break;
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
}
