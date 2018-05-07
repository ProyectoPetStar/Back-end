package org.petstar.controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONException;
import static org.petstar.configurations.utils.convertSqlToDay;
import org.petstar.dao.ProduccionDAO;
import org.petstar.dto.UserDTO;
import org.petstar.model.OutputJson;
import org.petstar.model.ResponseJson;
import static org.petstar.configurations.utils.obtenerAnio;
import static org.petstar.configurations.utils.obtenerMes;
import static org.petstar.configurations.utils.sumarFechasDias;
import org.petstar.dto.ProduccionDTO;
import org.petstar.model.ProduccionResponseJson;

/**
 *
 * @author Tech-Pro
 */
public class ControllerProduccion {
    private static final String MSG_SUCESS = "OK";
    private static final String MSG_LOGOUT = "Inicie sesión nuevamente";
    private static final String MSG_ERROR  = "Descripción de error: ";
    private static final String MSG_INVALID= "Valor o Descripción ya existe";
    private static final String TABLE_NAME = "pet_cat_equipos";
    private static final String MSG_NOEXIT = "El registro no existe";
    
    public OutputJson getProduccionByPeriodo(HttpServletRequest request){
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        
        try{
            UserDTO sesion = autenticacion.isValidToken(request);
            if(sesion != null){
                ProduccionResponseJson data = new ProduccionResponseJson();
                ProduccionDAO produccionDAO = new ProduccionDAO();
                String[] perfiles = sesion.getPerfiles().split(",");
                java.util.Date day = new Date();
                
                if (perfiles[0].equals("1") || perfiles[0].equals("2") || 
                        perfiles[0].equals("3") || perfiles[0].equals("6")) {
                    data.setListProduccion(produccionDAO.getProduccionByPeriodo(
                            obtenerMes(day), obtenerAnio(day)));
                }else if(perfiles[0].equals("4") || perfiles[0].equals("5")){
                    data.setListProduccion(produccionDAO.getProduccionByPeriodoAndLinea(
                            obtenerMes(day), obtenerAnio(day), sesion.getId_linea()));
                }
                
                for(ProduccionDTO produccion:data.getListProduccion()){
                    produccion.setDia(sumarFechasDias(produccion.getDia(), 2));
                    produccion.setDiaString(convertSqlToDay(produccion.getDia(), new SimpleDateFormat("dd/MM/yyyy")));
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
    
    public OutputJson insertProduccion(HttpServletRequest request) throws Exception{
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        
        try{
            int idMeta=Integer.valueOf(request.getParameter("id_meta"));

            String jsonString = request.getParameter("productos");
            UserDTO sesion = autenticacion.isValidToken(request);
            if(sesion != null){
                ProduccionDAO produccionDAO = new ProduccionDAO();
                JsonParser jsonParser = new JsonParser();
                String jsonArrayString = jsonString;
                JsonArray arrayFromString = jsonParser.parse(jsonArrayString).getAsJsonArray();

                for(int i=0; i<arrayFromString.size(); i++){
                    JsonObject objectFromString = jsonParser.parse(arrayFromString.get(i).toString()).getAsJsonObject();
                    int idProducto = Integer.parseInt(objectFromString.get("id_producto").toString());
                    BigDecimal valor = new BigDecimal(objectFromString.get("valor").toString());
                    produccionDAO.insertProduccion(idMeta, idProducto, valor,sesion.getId_acceso());
                }
                
                response.setMessage(MSG_SUCESS);
                response.setSucessfull(true);
            }else{
                response.setMessage(MSG_LOGOUT);
                response.setSucessfull(false);
            }
        } catch(JsonSyntaxException ex){
            response.setMessage(MSG_ERROR + ex.getMessage());
            response.setSucessfull(false);
        }
        
        output.setResponse(response);
        return output;
    }
    
}
