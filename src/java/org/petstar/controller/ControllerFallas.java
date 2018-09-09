package org.petstar.controller;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.petstar.dao.FallasDAO;
import org.petstar.model.FallasDataResponseJson;
import org.petstar.model.OutputJson;
import org.petstar.model.ResponseJson;
import static org.petstar.configurations.utils.convertStringToDate;
import static org.petstar.configurations.utils.getTiempoParo;
import static org.petstar.configurations.utils.getTurnoForSaveProduction;
import static org.petstar.configurations.utils.getCurrentDayByTurno;
import static org.petstar.configurations.utils.convertSqlToDay;
import static org.petstar.configurations.utils.sumarFechasDias;
import static org.petstar.configurations.utils.getCurrentDate;
import static org.petstar.configurations.utils.getDateFirstDay;
import static org.petstar.configurations.utils.getDateLastDay;
import org.petstar.dao.CatalogosDAO;
import org.petstar.dao.EquiposDAO;
import org.petstar.dao.GposLineaDAO;
import org.petstar.dao.LineasDAO;
import org.petstar.dao.MetasDAO;
import org.petstar.dao.PeriodosDAO;
import org.petstar.dao.RazonParoDAO;
import org.petstar.dto.FallasDTO;
import org.petstar.dto.MetasDTO;
import org.petstar.dto.PeriodosDTO;
import org.petstar.dto.ResultInteger;
import org.petstar.dto.UserDTO;

/**
 *
 * @author Tech-Pro
 */
public class ControllerFallas {
    private static final String TABLE_FUENTES = "pet_cat_fuentes_paro";
    private static final String TABLE_TURNOS = "pet_cat_turno";
    private static final String TABLE_GRUPOS = "pet_cat_grupo";
    private static final String MSG_SUCESS = "OK";
    private static final String MSG_LOGOUT = "Inicie sesión nuevamente";
    private static final String MSG_ERROR  = "Descripción de error: ";
    
    public OutputJson loadCombobox(HttpServletRequest request){
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        
        try{
            ControllerAutenticacion autenticacion = new ControllerAutenticacion();
            UserDTO sesion = autenticacion.isValidToken(request);
            
            if(sesion != null){
                CatalogosDAO catalogosDAO = new CatalogosDAO();
                GposLineaDAO gposLineaDAO = new GposLineaDAO();
                EquiposDAO equiposDAO = new EquiposDAO();
                RazonParoDAO paroDAO = new RazonParoDAO();
                PeriodosDAO periodosDAO = new PeriodosDAO();
                LineasDAO lineasDAO = new LineasDAO();
                FallasDataResponseJson data = new FallasDataResponseJson();
                
                int idLinea = sesion.getId_linea();
                
                data.setListFuentesParo(catalogosDAO.getCatalogosActive(TABLE_FUENTES));
                data.setListGrupos(catalogosDAO.getCatalogosActive(TABLE_GRUPOS));
                data.setListTurnos(catalogosDAO.getCatalogosActive(TABLE_TURNOS));
                data.setListEquipos(equiposDAO.getAllEquiposByIdLinea(idLinea));
                data.setListGposLineas(gposLineaDAO.getGposLineaActiveForOEE());
                data.setListRazonesParo(paroDAO.getAllRazonesActive());
                data.setListPeriodos(periodosDAO.getAllPeriodos());
                data.setListLineas(lineasDAO.getLineasActive());

                output.setData(data);
                response.setSucessfull(true);
                response.setMessage(MSG_SUCESS);
            }else{
                response.setSucessfull(false);
                response.setMessage(MSG_LOGOUT);
            }
        } catch(Exception ex){
            response.setSucessfull(false);
            response.setMessage(MSG_ERROR + ex.getMessage());
        }
        
        output.setResponse(response);
        return output;
    }
    
    public OutputJson loadComboboxAndMeta(HttpServletRequest request){
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        
        try{
            ControllerAutenticacion autenticacion = new ControllerAutenticacion();
            UserDTO sesion = autenticacion.isValidToken(request);
            
            if(sesion != null){
                CatalogosDAO catalogosDAO = new CatalogosDAO();
                MetasDAO metasDAO = new MetasDAO();
                EquiposDAO equiposDAO = new EquiposDAO();
                PeriodosDAO periodosDAO = new PeriodosDAO();
                RazonParoDAO paroDAO = new RazonParoDAO();
                LineasDAO lineasDAO = new LineasDAO();
                FallasDataResponseJson data = new FallasDataResponseJson();
                int turno = getTurnoForSaveProduction();
                java.sql.Date dia = getCurrentDayByTurno(turno);
                int idGrupo = sesion.getId_grupo();
                int idLinea = sesion.getId_linea();
                ResultInteger idMeta = metasDAO.getIdMeta(dia, turno, idGrupo, idLinea);

                data.setListFuentesParo(catalogosDAO.getCatalogosActive(TABLE_FUENTES));
                data.setListGrupos(catalogosDAO.getCatalogosActive(TABLE_GRUPOS));
                data.setListTurnos(catalogosDAO.getCatalogosActive(TABLE_TURNOS));
                data.setListEquipos(equiposDAO.getAllEquiposByIdLinea(idLinea));
                data.setListRazonesParo(paroDAO.getAllRazonesActive());
                data.setListPeriodos(periodosDAO.getAllPeriodos());
                data.setListLineas(lineasDAO.getLineasActive());

                if(null != idMeta){
                    MetasDTO metasDTO = metasDAO.getMetaById(idMeta.getResult());
                    metasDTO.setDia(sumarFechasDias(metasDTO.getDia(), 2));
                    metasDTO.setDia_string(convertSqlToDay(metasDTO.getDia(), new SimpleDateFormat("dd/MM/yyyy")));
                    data.setMetasDTO(metasDTO);
                    
                    response.setSucessfull(true);
                    response.setMessage(MSG_SUCESS);
                }else{
                    response.setSucessfull(false);
                    response.setMessage("No existe Meta");
                } 
                output.setData(data);
            }else{
                response.setSucessfull(false);
                response.setMessage(MSG_LOGOUT);
            }
        } catch(Exception ex){
            response.setSucessfull(false);
            response.setMessage(MSG_ERROR + ex.getMessage());
        }
        
        output.setResponse(response);
        return output;
    }
    
    public OutputJson getAllFallasByDays(HttpServletRequest request){
        int idPeriodo = Integer.valueOf(request.getParameter("id_periodo"));
        int idLinea = Integer.parseInt(request.getParameter("id_linea"));
        int idGrupo = Integer.parseInt(request.getParameter("id_grupo"));
        int idTurno = Integer.parseInt(request.getParameter("id_turno"));
        int idGpoLn = Integer.parseInt(request.getParameter("id_gpo_linea"));
        
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        
        try{
            UserDTO sesion = autenticacion.isValidToken(request);
            if(sesion != null){
                PeriodosDAO periodosDAO = new PeriodosDAO();
                PeriodosDTO periodo = periodosDAO.getPeriodoById(idPeriodo);
                java.sql.Date fechaI = getDateFirstDay(periodo.getAnio(), periodo.getMes());
                java.sql.Date fechaT = getDateLastDay(periodo.getAnio(), periodo.getMes());
                FallasDAO fallasDAO = new FallasDAO();
                FallasDataResponseJson data = new FallasDataResponseJson();
                
                String[] perfiles = sesion.getPerfiles().split(",");
                if(perfiles[0].equals("4")){
                    data.setListFallas(fallasDAO.getAllFallasByDaysAndGpoLn(idGpoLn, idGrupo, idTurno, fechaI, fechaT));
                }else{
                    data.setListFallas(fallasDAO.getAllFallasByDays(idLinea, idGrupo, idTurno, fechaI, fechaT));
                }
                data.setEstatusPeriodo(periodo.getEstatus()==0);
                
                for(FallasDTO falla:data.getListFallas()){
                    falla.setDia(sumarFechasDias(falla.getDia(), 2));
                    falla.setDiaString(convertSqlToDay(
                            falla.getDia(), new SimpleDateFormat("dd/MM/yyyy")));
                    if(falla.getEstatus_validado() == 1){
                        falla.setValidado(true);
                    }else{
                        falla.setValidado(false);
                    }
                }
                
                output.setData(data);
                response.setSucessfull(true);
                response.setMessage(MSG_SUCESS);
            }else{
                response.setSucessfull(false);
                response.setMessage(MSG_LOGOUT);
            }
        } catch( Exception ex){
            response.setSucessfull(false);
            response.setMessage(MSG_ERROR + ex.getMessage());
        }
        output.setResponse(response);
        return output;
    }
    
    public OutputJson insertFalla(HttpServletRequest request) throws ParseException{
        FallasDTO fallasDTO = new FallasDTO();
        fallasDTO.setDescripcion(request.getParameter("descripcion"));
        fallasDTO.setHora_inicio(request.getParameter("hora_inicio"));
        fallasDTO.setHora_final(request.getParameter("hora_final"));
        fallasDTO.setId_razon(Integer.parseInt(request.getParameter("id_razon")));
        fallasDTO.setId_equipo(Integer.parseInt(request.getParameter("id_equipo")));
        fallasDTO.setId_meta(Integer.parseInt(request.getParameter("id_meta")));
        fallasDTO.setTiempo_paro(new BigDecimal(request.getParameter("tiempo_paro")));
                
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        
        try{
            UserDTO sesion = autenticacion.isValidToken(request);
            if(sesion != null){
                fallasDTO.setId_usuario_registro(sesion.getId_acceso());
                FallasDAO fallasDAO = new FallasDAO();
                fallasDAO.insertNewFalla(fallasDTO);

                response.setSucessfull(true);
                response.setMessage(MSG_SUCESS);
            }else{
                response.setSucessfull(false);
                response.setMessage(MSG_LOGOUT);
            }
        } catch(Exception ex){
            response.setSucessfull(false);
            response.setMessage(MSG_ERROR + ex.getMessage());
        }
        
        output.setResponse(response);
        return output;
    }
    
    public OutputJson updateFalla(HttpServletRequest request) throws ParseException{
        FallasDTO fallasDTO = new FallasDTO();
        fallasDTO.setDescripcion(request.getParameter("descripcion"));
        fallasDTO.setHora_inicio(request.getParameter("hora_inicio"));
        fallasDTO.setHora_final(request.getParameter("hora_final"));
        fallasDTO.setId_falla(Integer.parseInt(request.getParameter("id_falla")));
        fallasDTO.setId_razon(Integer.parseInt(request.getParameter("id_razon")));
        fallasDTO.setId_equipo(Integer.parseInt(request.getParameter("id_equipo")));
        fallasDTO.setId_meta(Integer.parseInt(request.getParameter("id_meta")));
        fallasDTO.setTiempo_paro(new BigDecimal(request.getParameter("tiempo_paro")));
        
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        
        try{
            UserDTO sesion = autenticacion.isValidToken(request);
            if(sesion != null){
                    fallasDTO.setFecha_modificacion_registro(getCurrentDate());
                    fallasDTO.setId_usuario_modifica_registro(sesion.getId_acceso());
                    
                    FallasDAO fallasDAO = new FallasDAO();
                    fallasDAO.updateFalla(fallasDTO);

                    response.setSucessfull(true);
                    response.setMessage(MSG_SUCESS);
            }else{
                response.setSucessfull(false);
                response.setMessage(MSG_LOGOUT);
            }
        } catch(Exception ex){
            response.setSucessfull(false);
            response.setMessage(MSG_ERROR + ex.getMessage());
        }
        
        output.setResponse(response);
        return output;
    }
    
    public OutputJson deleteFalla(HttpServletRequest request) throws ParseException{       
        int idFalla = Integer.parseInt(request.getParameter("id_falla"));
        
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        
        try{
            UserDTO sesion = autenticacion.isValidToken(request);
            if(sesion != null){
                FallasDAO fallasDAO = new FallasDAO();
                fallasDAO.deleteFalla(idFalla);

                response.setSucessfull(true);
                response.setMessage(MSG_SUCESS);
            }else{
                response.setSucessfull(false);
                response.setMessage(MSG_LOGOUT);
            }
        } catch(Exception ex){
            response.setSucessfull(false);
            response.setMessage(MSG_ERROR + ex.getMessage());
        }
        
        output.setResponse(response);
        return output;
    }
    
    public OutputJson getFallaById(HttpServletRequest request) throws ParseException{       
        int idFalla = Integer.parseInt(request.getParameter("id_falla"));
        
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        FallasDataResponseJson data = new FallasDataResponseJson();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        
        try{
            UserDTO sesion = autenticacion.isValidToken(request);
            if(sesion != null){
                CatalogosDAO catalogosDAO = new CatalogosDAO();
                EquiposDAO equiposDAO = new EquiposDAO();
                RazonParoDAO paroDAO = new RazonParoDAO();
                LineasDAO lineasDAO = new LineasDAO();
                FallasDAO fallasDAO = new FallasDAO();

                data.setFallasDTO(fallasDAO.getFallaById(idFalla));
                int idLinea = data.getFallasDTO().getId_linea();
                data.getFallasDTO().setDia(sumarFechasDias(data.getFallasDTO().getDia(), 2));
                data.getFallasDTO().setDiaString(convertSqlToDay(
                        data.getFallasDTO().getDia(), 
                        new SimpleDateFormat("dd/MM/yyyy")));
                data.setListFuentesParo(catalogosDAO.getCatalogosActive(TABLE_FUENTES));
                data.setListGrupos(catalogosDAO.getCatalogosActive(TABLE_GRUPOS));
                data.setListTurnos(catalogosDAO.getCatalogosActive(TABLE_TURNOS));
                data.setListEquipos(equiposDAO.getAllEquiposByIdLinea(idLinea));
                data.setListRazonesParo(paroDAO.getAllRazonesActive());
                data.setListLineas(lineasDAO.getLineasActive());

                output.setData(data);
                response.setSucessfull(true);
                response.setMessage(MSG_SUCESS);
            }else{
                response.setSucessfull(false);
                response.setMessage(MSG_LOGOUT);
            }
        } catch(Exception ex){
            response.setSucessfull(false);
            response.setMessage(MSG_ERROR + ex.getMessage());
        }
        
        output.setResponse(response);
        return output;
    }
}
