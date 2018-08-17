package org.petstar.controller.ishikawa;

import com.google.gson.Gson;
import java.text.SimpleDateFormat;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONObject;
import org.petstar.controller.ControllerAutenticacion;
import org.petstar.dao.CatalogosDAO;
import org.petstar.dto.UserDTO;
import org.petstar.model.OutputJson;
import org.petstar.model.ResponseJson;
import org.petstar.model.ishikawa.IshikawaResponse;
import org.petstar.dao.PeriodosDAO;
import org.petstar.dao.ishikawa.IshikawaDAO;
import org.petstar.dto.PeriodosDTO;
import org.petstar.dto.ishikawa.PetIshikawa;
import static org.petstar.configurations.utils.getCurrentDate;
import static org.petstar.configurations.utils.convertSqlToDay;
import static org.petstar.configurations.utils.convertStringToSql;

/**
 *
 * @author Tech-Pro
 */
public class IshikawaController {
    private static final String MSG_SUCESS = "OK";
    private static final String MSG_LOGOUT = "Inicie sesión nuevamente";
    private static final String MSG_ERROR  = "Descripción de error: ";
    private static final String MSG_PERIODO= "Periodo invalido";
    
    public OutputJson loadCombobox(HttpServletRequest request){
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson responseJson = new ResponseJson();
        OutputJson outputJson = new OutputJson();
        
        try {
            UserDTO sesion = autenticacion.isValidToken(request);
            if(sesion != null){
                IshikawaResponse data = new IshikawaResponse();
                CatalogosDAO catalogosDAO = new CatalogosDAO();
                PeriodosDAO periodosDAO = new PeriodosDAO();
                
                data.setDia_actual(convertSqlToDay(getCurrentDate(), new SimpleDateFormat("dd/MM/yyyy")));
                data.setListPreguntas(catalogosDAO.getCatalogosActive("pet_cat_preguntas"));
                data.setListGrupos(catalogosDAO.getCatalogosActive("pet_cat_grupo"));
                data.setListEtads(catalogosDAO.getCatalogosActive("pet_cat_etad"));
                data.setListMs(catalogosDAO.getCatalogosActive("pet_cat_emes"));
                data.setListPeriodos(periodosDAO.getAllPeriodos());
                
                responseJson.setMessage(MSG_SUCESS);
                responseJson.setSucessfull(true);
                outputJson.setData(data);
           }else{
                responseJson.setMessage(MSG_LOGOUT);
                responseJson.setSucessfull(false);
            }
        } catch (Exception e) {
            responseJson.setMessage(MSG_ERROR + e.getMessage());
            responseJson.setSucessfull(false);
        }
        outputJson.setResponse(responseJson);
        return outputJson;
    }
    
    public OutputJson saveIshikawa(HttpServletRequest request, boolean update){
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson responseJson = new ResponseJson();
        OutputJson outputJson = new OutputJson();
        Gson gson = new Gson();
        
        try {
            UserDTO sesion = autenticacion.isValidToken(request);
            if(sesion != null){
                IshikawaDAO ishikawaDAO = new IshikawaDAO();
                
                String jsonString = request.getParameter("data");
                JSONObject jsonResponse = new JSONObject(jsonString);
                PetIshikawa ishikawa = gson.fromJson(jsonResponse.getJSONObject("ishikawa").toString(), PetIshikawa.class);
                
                if(update){
                    ishikawaDAO.deleteIshikawa(ishikawa.getId());
                }
                
                ishikawa.setFecha(convertStringToSql(ishikawa.getFecha_string()));
                ishikawa.setFecha(convertStringToSql(ishikawa.getFecha_string()));
                ishikawaDAO.saveIshikawa(ishikawa);
                
                responseJson.setMessage(MSG_SUCESS);
                responseJson.setSucessfull(true);
            }else{
                responseJson.setMessage(MSG_LOGOUT);
                responseJson.setSucessfull(false);
            }
        } catch (Exception e) {
            responseJson.setMessage(MSG_ERROR +  e.getMessage());
            responseJson.setSucessfull(false);
        }
        outputJson.setResponse(responseJson);
        return outputJson;
    }
    
    public OutputJson getAllIshikawas(HttpServletRequest request){
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson responseJson = new ResponseJson();
        OutputJson outputJson = new OutputJson();
        
        try {
            UserDTO sesion = autenticacion.isValidToken(request);
            if(sesion != null){
                IshikawaResponse data = new IshikawaResponse();
                IshikawaDAO ishikawaDAO = new IshikawaDAO();
                
                int anio = Integer.valueOf(request.getParameter("anio"));
                
                data.setListIshikawas(ishikawaDAO.getAllIshikawas(anio));

                outputJson.setData(data);
                responseJson.setMessage(MSG_SUCESS);
                responseJson.setSucessfull(true);
            }else{
                responseJson.setMessage(MSG_LOGOUT);
                responseJson.setSucessfull(false);
            }
        } catch (Exception e) {
            responseJson.setMessage(MSG_ERROR +  e.getMessage());
            responseJson.setSucessfull(false);
        }
        outputJson.setResponse(responseJson);
        return outputJson;
    }
    
    public OutputJson getIshikawaById(HttpServletRequest request){
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson responseJson = new ResponseJson();
        OutputJson outputJson = new OutputJson();
        
        try {
            UserDTO sesion = autenticacion.isValidToken(request);
            if(sesion != null){
                IshikawaResponse data = new IshikawaResponse();
                IshikawaDAO ishikawaDAO = new IshikawaDAO();
                
                int idIshikawa = Integer.valueOf(request.getParameter("id_ishikawa"));
                
                data.setIshikawa(ishikawaDAO.getIshikawaById(idIshikawa));

                outputJson.setData(data);
                responseJson.setMessage(MSG_SUCESS);
                responseJson.setSucessfull(true);
            }else{
                responseJson.setMessage(MSG_LOGOUT);
                responseJson.setSucessfull(false);
            }
        } catch (Exception e) {
            responseJson.setMessage(MSG_ERROR +  e.getMessage());
            responseJson.setSucessfull(false);
        }
        outputJson.setResponse(responseJson);
        return outputJson;
    }
    
    public OutputJson deleteIshikawa(HttpServletRequest request){
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson responseJson = new ResponseJson();
        OutputJson outputJson = new OutputJson();
        
        try {
            UserDTO sesion = autenticacion.isValidToken(request);
            if(sesion != null){
                IshikawaDAO ishikawaDAO = new IshikawaDAO();
                int idIshikawa = Integer.valueOf(request.getParameter("id_ishikawa"));
                
                ishikawaDAO.deleteIshikawa(idIshikawa);
                responseJson.setMessage(MSG_SUCESS);
                responseJson.setSucessfull(true);
            }else{
                responseJson.setMessage(MSG_LOGOUT);
                responseJson.setSucessfull(false);
            }
        } catch (Exception e) {
            responseJson.setMessage(MSG_ERROR +  e.getMessage());
            responseJson.setSucessfull(false);
        }
        outputJson.setResponse(responseJson);
        return outputJson;
    }
}
