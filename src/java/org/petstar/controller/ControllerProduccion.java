package org.petstar.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.sql.Date;
import javax.servlet.http.HttpServletRequest;
import org.petstar.dao.ProduccionDAO;
import org.petstar.dto.UserDTO;
import org.petstar.model.OutputJson;
import org.petstar.model.ResponseJson;
import static org.petstar.configurations.utils.sumarFechasDias;
import org.petstar.dao.CatalogosDAO;
import org.petstar.dao.LineasDAO;
import org.petstar.dao.ProductosDAO;
import org.petstar.dto.ProduccionDTO;
import org.petstar.model.ProduccionResponseJson;
import static org.petstar.configurations.utils.getTurnoForSaveProduction;
import static org.petstar.configurations.utils.getCurrentDayByTurno;
import static org.petstar.configurations.utils.getDateFirstDay;
import static org.petstar.configurations.utils.getDateLastDay;
import static org.petstar.configurations.utils.convertSqlToDay;
import static org.petstar.configurations.utils.getCurrentDate;
import static org.petstar.configurations.utils.obtenerAnio;
import static org.petstar.configurations.utils.obtenerMes;
import org.petstar.dao.FallasDAO;
import org.petstar.dao.GposLineaDAO;
import org.petstar.dao.MetasDAO;
import org.petstar.dao.PeriodosDAO;
import org.petstar.dto.FallasDTO;
import org.petstar.dto.LineasDTO;
import org.petstar.dto.PeriodosDTO;
import org.petstar.dto.ResultInteger;

/**
 *
 * @author Tech-Pro
 */
public class ControllerProduccion {
    private static final String MSG_SUCESS = "OK";
    private static final String MSG_LOGOUT = "Inicie sesión nuevamente";
    private static final String MSG_ERROR  = "Descripción de error: ";
    private static final String TABLE_GROUP= "pet_cat_grupo";
    private static final String TABLE_TURNO= "pet_cat_turno";
    
    /**
     * llenado de listas
     * Servicio que llena las listas que seran utilizadas en los combos
     * @param request
     * @return 
     */
    public OutputJson loadCombobox(HttpServletRequest request){
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        
        try{
            UserDTO sesion = autenticacion.isValidToken(request);
            if(sesion != null){
                ProduccionResponseJson data = new ProduccionResponseJson();
                ProduccionDAO produccionDAO = new ProduccionDAO();
                GposLineaDAO gposLineaDAO = new GposLineaDAO();
                ProductosDAO productosDAO = new ProductosDAO();
                CatalogosDAO catalogosDAO = new CatalogosDAO();
                PeriodosDAO periodosDAO = new PeriodosDAO();
                LineasDAO lineasDAO = new LineasDAO();
                MetasDAO metasDAO = new MetasDAO();
                
                int turno = getTurnoForSaveProduction();
                Date dia = getCurrentDayByTurno(turno);
                data.setMeta(metasDAO.getMeta(dia, turno, sesion.getId_grupo(), sesion.getId_linea()));
                data.setListPeriodos(periodosDAO.getAllPeriodos());
                data.setListProductos(productosDAO.getProductosByLinea(sesion.getId_linea()));
                data.setListGposLinea(gposLineaDAO.getGposLineaActiveForOEE());
                data.setListGrupos(catalogosDAO.getCatalogosActive(TABLE_GROUP));
                data.setListTurnos(catalogosDAO.getCatalogosActive(TABLE_TURNO));
                data.setListLineas(lineasDAO.getLineasActive());
                        
                if(data.getMeta() != null){
                    data.setListDetalle(produccionDAO.getProduccionByIdMeta(data.getMeta().getId_meta()));
                    if(data.getListDetalle().isEmpty()){
                        data.getMeta().setDia(sumarFechasDias(data.getMeta().getDia(), 2));
                        data.getMeta().setDia_string(convertSqlToDay(data.getMeta().getDia(),
                                            new SimpleDateFormat("dd/MM/yyyy")));
                        
                        response.setMessage(MSG_SUCESS);
                        response.setSucessfull(true);
                    }else{
                        response.setMessage("-1");
                        response.setSucessfull(true);
                    }
                }else{
                    response.setMessage("0");
                    response.setSucessfull(true);
                }
                output.setData(data);
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
    
    /**
     * Produccion por Periodo
     * Servicio para obtener los dias que se tienen produccion del periodo actual
     * @param request
     * @return 
     */
    public OutputJson getProduccionByPeriodo(HttpServletRequest request){
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        
        try{
            int idPeriodo = Integer.valueOf(request.getParameter("id_periodo"));
            int idLinea = Integer.valueOf(request.getParameter("id_linea"));
            int idGpoLn = Integer.valueOf(request.getParameter("id_gpo_linea"));
            UserDTO sesion = autenticacion.isValidToken(request);
            if(sesion != null){
                ProduccionResponseJson data = new ProduccionResponseJson();
                ProduccionDAO produccionDAO = new ProduccionDAO();
                PeriodosDAO periodosDAO = new PeriodosDAO();
                MetasDAO metasDAO = new MetasDAO();
                
                PeriodosDTO periodo = periodosDAO.getPeriodoById(idPeriodo);
                String[] perfiles = sesion.getPerfiles().split(",");
                
                if (perfiles[0].equals("1") || perfiles[0].equals("2") || 
                        perfiles[0].equals("3") || perfiles[0].equals("6")) {
                    
                    data.setListProduccion(produccionDAO.getProduccionByPeriodo(periodo.getMes(), periodo.getAnio(), idLinea));
                    
                }else if(perfiles[0].equals("5")){
                    
                    data.setListProduccion(produccionDAO.getProduccionByPeriodoAndLinea(
                            periodo.getMes(), periodo.getAnio(),
                            sesion.getId_linea(), sesion.getId_grupo()));
                    
                    int turno = getTurnoForSaveProduction();
                    Date dia = getCurrentDayByTurno(turno);
                    data.setMeta(metasDAO.getMeta(dia, turno, sesion.getId_grupo(), sesion.getId_linea()));
                    
                    if(data.getMeta() != null){
                        data.getMeta().setDia(sumarFechasDias(data.getMeta().getDia(), 2));
                        data.getMeta().setDia_string(
                                convertSqlToDay(data.getMeta().getDia(),
                                        new SimpleDateFormat("dd/MM/yyyy")));
                        data.setListDetalle(produccionDAO.getProduccionByIdMeta(data.getMeta().getId_meta()));
                    }
                }else if(perfiles[0].equals("4")){
                    data.setListProduccion(produccionDAO.getProduccionByPeriodoAndGpoln(
                            periodo.getMes(), periodo.getAnio(),
                            idGpoLn, sesion.getId_grupo()));
                    
                    int turno = getTurnoForSaveProduction();
                    Date dia = getCurrentDayByTurno(turno);
                    data.setMeta(metasDAO.getMeta(dia, turno, sesion.getId_grupo(), sesion.getId_linea()));
                    
                    if(data.getMeta() != null){
                        data.getMeta().setDia(sumarFechasDias(data.getMeta().getDia(), 2));
                        data.getMeta().setDia_string(
                                convertSqlToDay(data.getMeta().getDia(),
                                        new SimpleDateFormat("dd/MM/yyyy")));
                        data.setListDetalle(produccionDAO.getProduccionByIdMeta(data.getMeta().getId_meta()));
                    }
                }
                
                for(ProduccionDTO produccion:data.getListProduccion()){
                    produccion.setDia(sumarFechasDias(produccion.getDia(), 2));
                    produccion.setDiaString(convertSqlToDay(produccion.getDia(),
                            new SimpleDateFormat("dd/MM/yyyy")));
                }
                
                data.setEstatusPeriodo(periodo.getEstatus()==0);
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
    
    /**
     * Registro de Produccion
     * Servicio para el registrar la produccion de un turno
     * @param request
     * @return
     * @throws Exception 
     */
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
                ResultInteger result = produccionDAO.validateForSaveProduccion(idMeta);
                if(result.getResult().equals(0)){
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
                    response.setMessage("Ya existe producción registrada.");
                    response.setSucessfull(false);
                }
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
    
    /**
     * Detalles de Produccion y Fallas
     * Servicio que devuelve la produccion y fallas registradas previamente
     * @param request
     * @return 
     */
    public OutputJson getDetailsByIdMeta(HttpServletRequest request){
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        
        try{
            int idMeta = Integer.valueOf(request.getParameter("id_meta"));
            UserDTO sesion = autenticacion.isValidToken(request);
            if(sesion != null){
                ProduccionResponseJson data = new ProduccionResponseJson();
                ProduccionDAO produccionDAO = new ProduccionDAO();
                CatalogosDAO catalogosDAO = new CatalogosDAO();
                PeriodosDAO periodosDAO = new PeriodosDAO();
                LineasDAO lineasDAO = new LineasDAO();
                FallasDAO fallasDAO = new FallasDAO();
                MetasDAO metasDAO = new MetasDAO();
                
                data.setMeta(metasDAO.getMetaById(idMeta));
                data.setListFallas(fallasDAO.getFallasByIdMeta(idMeta));
                data.setListProduccion(produccionDAO.getProduccionByIdMeta(idMeta));
                data.setListGrupos(catalogosDAO.getCatalogosActive(TABLE_GROUP));
                data.setListTurnos(catalogosDAO.getCatalogosActive(TABLE_TURNO));
                data.setListLineas(lineasDAO.getLineasActive());
                
                for(FallasDTO falla: data.getListFallas()){
                    falla.setDia(sumarFechasDias(falla.getDia(), 2));
                    falla.setDiaString(convertSqlToDay(falla.getDia(), new SimpleDateFormat("dd/MM/yyyy")));
                }
                data.getMeta().setDia(sumarFechasDias(data.getMeta().getDia(), 2));
                PeriodosDTO periodo = periodosDAO.getPeriodoByMesAndAnio(
                        obtenerMes(data.getMeta().getDia()), obtenerAnio(data.getMeta().getDia()));
                data.setEstatusPeriodo(periodo.getEstatus()==0);
                data.getMeta().setDia_string(convertSqlToDay(data.getMeta().getDia(),new SimpleDateFormat("dd/MM/yyyy")));
                
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
    
    /**
     * Detalles de Produccion
     * Servicio que devuelve la lista con la produccion de un dia en especifico
     * @param request
     * @return 
     */
    public OutputJson getDetailsProducion(HttpServletRequest request){
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        
        try{
            int idMeta = Integer.valueOf(request.getParameter("id_meta"));
            UserDTO sesion = autenticacion.isValidToken(request);
            
            if(sesion != null){
                ProduccionResponseJson data = new ProduccionResponseJson();
                ProduccionDAO produccionDAO = new ProduccionDAO();
                CatalogosDAO catalogosDAO = new CatalogosDAO();
                PeriodosDAO periodosDAO = new PeriodosDAO();
                LineasDAO lineasDAO = new LineasDAO();
                MetasDAO metasDAO = new MetasDAO();
                
                data.setListGrupos(catalogosDAO.getCatalogosActive(TABLE_GROUP));
                data.setListTurnos(catalogosDAO.getCatalogosActive(TABLE_TURNO));
                data.setListLineas(lineasDAO.getLineasActive());
                data.setListDetalle(produccionDAO.getProduccionByIdMeta(idMeta));
                data.setMeta(metasDAO.getMetaById(idMeta));
                if(data.getMeta() != null){
                    data.getMeta().setDia(sumarFechasDias(data.getMeta().getDia(), 2));
                    data.getMeta().setDia_string(convertSqlToDay(data.getMeta().getDia(), new SimpleDateFormat("dd/MM/yyyy")));
                }
                
                PeriodosDTO periodo = periodosDAO.getPeriodoByMesAndAnio(
                        obtenerMes(data.getMeta().getDia()),
                        obtenerAnio(data.getMeta().getDia()));
                data.setEstatusPeriodo(periodo.getEstatus()==0);
                
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
    
    /**
     * Modificación de Produccion
     * Servicio para la modificación de la produccion
     * @param request
     * @return
     * @throws Exception 
     */
    public OutputJson updateProduccion(HttpServletRequest request) throws Exception{
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        
        try{
            String jsonString = request.getParameter("productos");
            UserDTO sesion = autenticacion.isValidToken(request);
            if(sesion != null){
                ProduccionDAO produccionDAO = new ProduccionDAO();
                JsonParser jsonParser = new JsonParser();
                String jsonArrayString = jsonString;
                JsonArray arrayFromString = jsonParser.parse(jsonArrayString).getAsJsonArray();
                Date fecha = getCurrentDate();

                for(int i=0; i<arrayFromString.size(); i++){
                    JsonObject objectFromString = jsonParser.parse(arrayFromString.get(i).toString()).getAsJsonObject();
                    int idProduccion = Integer.parseInt(objectFromString.get("id_produccion").toString());
                    BigDecimal valor = new BigDecimal(objectFromString.get("valor").toString());
                    produccionDAO.updateProduccion(idProduccion, valor, sesion.getId_acceso(), fecha);
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
    
    public OutputJson getProduccionForLiberar(HttpServletRequest request){
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        
        try{
            UserDTO sesion = autenticacion.isValidToken(request);
            if(sesion != null){
                ProduccionDAO produccionDAO = new ProduccionDAO();
                ProduccionResponseJson data = new ProduccionResponseJson();
                
                String[] perfiles = sesion.getPerfiles().split(",");
                
                if (perfiles[0].equals("1") || perfiles[0].equals("2") || 
                        perfiles[0].equals("3") || perfiles[0].equals("6")) {
                    
                    data.setListProduccion(produccionDAO.getProduccionForLiberar(0, 0));
                }else if(perfiles[0].equals("4") || perfiles[0].equals("5")){
                    LineasDAO lineasDAO = new LineasDAO();
                    LineasDTO linea = lineasDAO.getLineasDataById(sesion.getId_linea());
                    data.setListProduccion(produccionDAO.getProduccionForLiberar(
                            linea.getId_gpo_linea(), sesion.getId_grupo()));
                }
                
                for(ProduccionDTO prod:data.getListProduccion()){
                    prod.setDia(sumarFechasDias(prod.getDia(), 2));
                    prod.setDiaString(convertSqlToDay(prod.getDia(), new SimpleDateFormat("dd/MM/yyyy")));
                }
                
                output.setData(data);
                response.setMessage(MSG_SUCESS);
                response.setSucessfull(true);
            }else{
                response.setMessage(MSG_LOGOUT);
                response.setSucessfull(false);
            }
        }catch(Exception ex){
            response.setMessage(MSG_ERROR +  ex.getMessage());
            response.setSucessfull(false);
        }
        
        output.setResponse(response);
        return output;
    }
    
    public OutputJson getProduccionLiberada(HttpServletRequest request){
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        
        try{
            int idPeriodo = Integer.valueOf(request.getParameter("id_periodo"));
            int idLinea = Integer.valueOf(request.getParameter("id_linea"));
            UserDTO sesion = autenticacion.isValidToken(request);
            if(sesion != null){
                PeriodosDAO periodosDAO = new PeriodosDAO();
                ProduccionDAO produccionDAO = new ProduccionDAO();
                ProduccionResponseJson data = new ProduccionResponseJson();
                
                //String[] perfiles = sesion.getPerfiles().split(",");
                PeriodosDTO periodo = periodosDAO.getPeriodoById(idPeriodo);
                Date fechaI = getDateFirstDay(periodo.getAnio(), periodo.getMes());
                Date fechaT = getDateLastDay(periodo.getAnio(), periodo.getMes());
                
                //if (perfiles[0].equals("1") || perfiles[0].equals("2") || perfiles[0].equals("3") || perfiles[0].equals("6")) {
                //    data.setListProduccion(produccionDAO.getProduccionLiberada(0,fechaI, fechaT));
                //}else if(perfiles[0].equals("4") || perfiles[0].equals("5")){
                data.setListProduccion(produccionDAO.getProduccionLiberada(idLinea, fechaI, fechaT));
                //}
                
                for(ProduccionDTO prod:data.getListProduccion()){
                    prod.setDia(sumarFechasDias(prod.getDia(), 2));
                    prod.setDiaString(convertSqlToDay(prod.getDia(), new SimpleDateFormat("dd/MM/yyyy")));
                }
                data.setEstatusPeriodo(periodo.getEstatus()==0);
                
                output.setData(data);
                response.setMessage(MSG_SUCESS);
                response.setSucessfull(true);
            }else{
                response.setMessage(MSG_LOGOUT);
                response.setSucessfull(false);
            }
        }catch(Exception ex){
            response.setMessage(MSG_ERROR +  ex.getMessage());
            response.setSucessfull(false);
        }
        
        output.setResponse(response);
        return output;
    }
    
    public OutputJson liberarDatos(HttpServletRequest request){
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        
        try{
            int idMeta = Integer.valueOf(request.getParameter("id_meta"));
            int estatus = Integer.valueOf(request.getParameter("estatus"));
            UserDTO sesion = autenticacion.isValidToken(request);
            if(sesion != null){
                ProduccionDAO produccionDAO = new ProduccionDAO();
                
                produccionDAO.liberarDatos(idMeta,estatus);
                response.setMessage(MSG_SUCESS);
                response.setSucessfull(true);
            }else{
                response.setMessage(MSG_LOGOUT);
                response.setSucessfull(false);
            }
        }catch(Exception ex){
            response.setMessage(MSG_ERROR +  ex.getMessage());
            response.setSucessfull(false);
        }
        
        output.setResponse(response);
        return output;
    }
}
