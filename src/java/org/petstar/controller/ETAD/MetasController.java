package org.petstar.controller.ETAD;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONException;
import org.json.JSONObject;
import org.petstar.controller.ControllerAutenticacion;
import org.petstar.dao.ETAD.MetasDAO;
import org.petstar.dao.PeriodosDAO;
import org.petstar.dto.UserDTO;
import org.petstar.model.ETAD.MetasResponse;
import org.petstar.model.OutputJson;
import org.petstar.model.ResponseJson;
import static org.petstar.configurations.utils.getCurrentDate;
import org.petstar.dao.CatalogosDAO;
import org.petstar.dto.ETAD.PetMetaKpi;
import org.petstar.dto.ResultInteger;

/**
 *
 * @author Tech-Pro
 */
public class MetasController {
    private static final String MSG_SUCESS = "OK";
    private static final String MSG_LOGOUT = "Inicie sesión nuevamente";
    private static final String MSG_ERROR  = "Descripción de error: ";
    private static final String MSG_EXIST  = "Ya existe una meta con estos valores.";
    
    public OutputJson loadCombobox(HttpServletRequest request){
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
            
        try{
            UserDTO session = autenticacion.isValidToken(request);
            if(session != null){
                MetasResponse data = new MetasResponse();
                PeriodosDAO periodosDAO = new PeriodosDAO();
                CatalogosDAO catalogosDAO = new CatalogosDAO();
                
                data.setListEtads(catalogosDAO.getCatalogosActive("pet_cat_etad"));
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
        return output;
    }
    
    public OutputJson getAllMetas(HttpServletRequest request){
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
            
        try{
            int idPeriodo = Integer.valueOf(request.getParameter("id_periodo"));
            int idEtad = Integer.valueOf(request.getParameter("id_etad"));
            UserDTO session = autenticacion.isValidToken(request);
            if(session != null){
                MetasResponse metasResponse= new MetasResponse();
                PeriodosDAO periodosDAO = new PeriodosDAO();
                MetasDAO metasDAO = new MetasDAO();
                
                List<PetMetaKpi> list = metasDAO.getMetasKPIByEtadAndPeriodo(idEtad, idPeriodo);
                if(!list.isEmpty()){
                    metasResponse.setListMetasKpiOperativos(list);
                }else{
                    list = metasDAO.getKpisWithoutMeta(idEtad);
                    for(PetMetaKpi meta:list){
                        meta.setPeriodo(periodosDAO.getPeriodoById(idPeriodo));
                    }
                    metasResponse.setListMetasKpiOperativos(list);
                }
                output.setData(metasResponse);
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
    
    public OutputJson insertMetas(HttpServletRequest request) throws Exception{
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        Gson gson = new Gson();
        
        try{
            int idPeriodo = Integer.valueOf(request.getParameter("id_periodo"));
            int idEtad = Integer.valueOf(request.getParameter("id_etad"));
            String jsonString = request.getParameter("meta");
            UserDTO session = autenticacion.isValidToken(request);
            if(session != null){
                JSONObject jsonResponse = new JSONObject(jsonString);
                TypeToken<List<PetMetaKpi>> token = new TypeToken<List<PetMetaKpi>>(){};
                List<PetMetaKpi> listMetaKpis = gson.
                        fromJson(jsonResponse.getJSONArray("metas").toString(), token.getType());
                MetasDAO metasDAO = new MetasDAO();
                
                ResultInteger result = metasDAO.validateForInsertKPIOperativo(idEtad, idPeriodo);
                if(result.getResult().equals(0)){
                    metasDAO.insertKPIOperativosAnual(listMetaKpis, idPeriodo, session.getId_acceso(), getCurrentDate());
                    response = messageForSave(true, MSG_SUCESS);
                }else{
                    response = messageForSave(false, MSG_EXIST);
                }
            }else{
                response.setMessage(MSG_LOGOUT);
                response.setSucessfull(false);
            }
        }catch(JsonSyntaxException | JSONException ex){
            response.setMessage(MSG_ERROR + ex.getMessage());
            response.setSucessfull(false);
        }
        
        output.setResponse(response);
        return output;
    }
    
    public OutputJson updateMeta(HttpServletRequest request) throws Exception{
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        Gson gson = new Gson();
        
        try{
            String jsonString = request.getParameter("meta");
            UserDTO session = autenticacion.isValidToken(request);
            if(session != null){
                JSONObject jsonResponse = new JSONObject(jsonString);
                TypeToken<List<PetMetaKpi>> token = new TypeToken<List<PetMetaKpi>>(){};
                List<PetMetaKpi> listMetaKpis = gson.
                        fromJson(jsonResponse.getJSONArray("metas").toString(), token.getType());
                MetasDAO metasDAO = new MetasDAO();
                
                metasDAO.updateKPIOperativos(listMetaKpis, session.getId_acceso(), getCurrentDate());
                response = messageForSave(true, MSG_SUCESS);        
            }else{
                response.setMessage(MSG_LOGOUT);
                response.setSucessfull(false);
            }
        }catch(JsonSyntaxException | JSONException ex){
            response.setMessage(MSG_ERROR + ex.getMessage());
            response.setSucessfull(false);
        }
        
        output.setResponse(response);
        return output;
    }
    
    public ResponseJson messageForSave(boolean result, String reply){
        ResponseJson response = new ResponseJson();
        if(result){
            response.setMessage(reply);
            response.setSucessfull(true);
        }else{
            response.setMessage(reply);
            response.setSucessfull(false);
        }
        return response;
    }
}
