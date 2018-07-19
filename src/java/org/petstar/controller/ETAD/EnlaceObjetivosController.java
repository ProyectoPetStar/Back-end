package org.petstar.controller.ETAD;

import com.google.gson.Gson;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONObject;
import org.petstar.controller.ControllerAutenticacion;
import org.petstar.dao.ETAD.EnlaceObjetivosDAO;
import org.petstar.dao.PeriodosDAO;
import org.petstar.dto.ETAD.PetReporteEnlace;
import org.petstar.dto.ResultInteger;
import org.petstar.dto.UserDTO;
import org.petstar.model.ETAD.EnlaceObjetivosResponse;
import org.petstar.model.OutputJson;
import org.petstar.model.ResponseJson;

/**
 *
 * @author Tech-Pro
 */
public class EnlaceObjetivosController {
    private static final String MSG_SUCESS = "OK";
    private static final String MSG_LOGOUT = "Inicie sesión nuevamente";
    private static final String MSG_ERROR  = "Descripción de error: ";
    private static final String MSG_EXIST  = "Ya existen datos para este periodo.";
    
    /**
     * Carga de Combos
     * Servicio que se encarga del llenado de las listas que se utilizaran
     * para los combos que se utilicen en el registro de catalogos
     * @param request
     * @return 
     */
    public OutputJson loadCombobox(HttpServletRequest request){
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
            
        try{
            UserDTO session = autenticacion.isValidToken(request);
            if(session != null){
                EnlaceObjetivosResponse data = new EnlaceObjetivosResponse();
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
        return output;
    }
    
    public OutputJson getConfiguracion(HttpServletRequest request){
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
            
        try{
            int idPeriodo = Integer.valueOf(request.getParameter("id_periodo"));
            UserDTO session = autenticacion.isValidToken(request);
            if(session != null){
                EnlaceObjetivosResponse data = new EnlaceObjetivosResponse();
                EnlaceObjetivosDAO eObjetivosDAO = new EnlaceObjetivosDAO();
                
                data.setReporteEnlace(eObjetivosDAO.getConfiguracionByPeriodo(idPeriodo));
                if(data.getReporteEnlace() == null){
                    PetReporteEnlace reporteEnlace = new PetReporteEnlace(idPeriodo);
                    data.setReporteEnlace(reporteEnlace);
                }
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
    
    public OutputJson insertConfiguracion(HttpServletRequest request){
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        Gson gson = new Gson();
            
        try{
            String jsonString = request.getParameter("record");
            JSONObject jsonResponse = new JSONObject(jsonString);
            UserDTO session = autenticacion.isValidToken(request);
            if(session != null){
                EnlaceObjetivosDAO objetivosDAO = new EnlaceObjetivosDAO();
                PetReporteEnlace reporteEnlace = gson.fromJson(jsonResponse.
                                getJSONObject("record").toString(), PetReporteEnlace.class);
                
                ResultInteger result = objetivosDAO.validateExistConfiguracionEnlace(reporteEnlace.getId_periodo());
                if(result.getResult().equals(0)){
                    objetivosDAO.insertConfiguracionEnlace(reporteEnlace);
                    response.setMessage(MSG_SUCESS);
                    response.setSucessfull(true);
                }else{
                    response.setMessage(MSG_SUCESS);
                    response.setSucessfull(true);
                }
            }else{
                response.setMessage(MSG_EXIST);
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
