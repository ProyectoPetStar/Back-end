package org.petstar.controller.ETAD;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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

/**
 *
 * @author Tech-Pro
 */
public class PonderacionController {
    private static final String MSG_SUCESS = "OK";
    private static final String MSG_LOGOUT = "Inicie sesión nuevamente";
    private static final String MSG_ERROR  = "Descripción de error: ";
    private static final String MSG_EXIST  = "Ya existe ponderacion para este año";
    private static final String MSG_INVALID= "El archivo contiene errores";
    
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
