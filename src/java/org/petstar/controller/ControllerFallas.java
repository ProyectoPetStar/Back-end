package org.petstar.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.petstar.dao.FallasDAO;
import org.petstar.model.FallasDataResponseJson;
import org.petstar.model.OutputJson;
import org.petstar.model.ResponseJson;
import static org.petstar.configurations.utils.convertStringToSql;
import static org.petstar.configurations.utils.convertStringToDate;
import static org.petstar.configurations.utils.getHoursMinutes;
import static org.petstar.configurations.utils.getTurno;
import static org.petstar.configurations.utils.getCurrentDayByTurno;
import static org.petstar.configurations.utils.convertSqlToDay;
import static org.petstar.configurations.utils.sumarFechasDias;
import org.petstar.dao.CatalogosDAO;
import org.petstar.dao.EquiposDAO;
import org.petstar.dao.MetasDAO;
import org.petstar.dao.RazonParoDAO;
import org.petstar.dto.FallasDTO;
import org.petstar.dto.MetasDTO;
import org.petstar.dto.ResultInteger;

/**
 *
 * @author Tech-Pro
 */
public class ControllerFallas {
    private static final String TABLE_FUENTES = "pet_cat_fuentes_paro";
    private static final String MSG_SUCESS = "OK";
    private static final String MSG_LOGOUT = "Inicie sesión nuevamente";
    private static final String MSG_ERROR  = "Descripción de error: ";
    
    public OutputJson loadCombobox(HttpServletRequest request){
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        
        try{
            CatalogosDAO catalogosDAO = new CatalogosDAO();
            MetasDAO metasDAO = new MetasDAO();
            EquiposDAO equiposDAO = new EquiposDAO();
            RazonParoDAO paroDAO = new RazonParoDAO();
            FallasDataResponseJson data = new FallasDataResponseJson();
            int turno = getTurno();
            java.sql.Date dia = getCurrentDayByTurno(turno);
            int idGrupo = 3;
            int idLinea = 2;
            ResultInteger idMeta = metasDAO.getIdMeta(dia, turno, idGrupo, idLinea);
            
            data.setListFuentesParo(catalogosDAO.getCatalogos(TABLE_FUENTES));
            data.setListEquipos(equiposDAO.getAllEquiposByIdLinea(idLinea));
            data.setListRazonesParo(paroDAO.getAllRazones());
                
            if(null != idMeta){
                MetasDTO metasDTO = new MetasDTO();
                metasDTO = metasDAO.getMetaById(idMeta.getResult());
                metasDTO.setDia(sumarFechasDias(metasDTO.getDia(), 2));
                metasDTO.setDia_string(convertSqlToDay(metasDTO.getDia(), new SimpleDateFormat("dd/MM/yyyy")));
                data.setMetasDTO(metasDTO);
                
            } 
            
            output.setData(data);
            response.setSucessfull(true);
            response.setMessage(MSG_SUCESS);
        } catch(Exception ex){
            response.setSucessfull(false);
            response.setMessage(MSG_ERROR + ex.getMessage());
        }
        
        output.setResponse(response);
        return output;
    }
    
    public OutputJson getAllFallasByDays(HttpServletRequest request){
        String fechaIn = request.getParameter("fecha_inicio");
        String fechaTe = request.getParameter("fecha_termino");
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
//        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        
        try{
//            if(autenticacion.isValidToken(request)){
                FallasDAO fallasDAO = new FallasDAO();
                FallasDataResponseJson data = new FallasDataResponseJson();
                
                data.setListFallas(fallasDAO.getAllFallasByDays(
                        convertStringToSql(fechaIn),
                        convertStringToSql(fechaTe)));
                response.setSucessfull(true);
                response.setMessage(MSG_SUCESS);
//            }else{
//                response.setSucessfull(false);
//                response.setMessage(MSG_LOGOUT);
//            }
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
        fallasDTO.setId_usuario_registro(1);
        
        SimpleDateFormat formato = new SimpleDateFormat("HH:mm");
        Date horaInicio = convertStringToDate(fallasDTO.getHora_inicio(), formato);
        Date horaFinal = convertStringToDate(fallasDTO.getHora_final(), formato);
        
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        
        try{
            if(horaInicio.before(horaFinal)){
                fallasDTO.setTiempo_paro(getHoursMinutes(horaInicio, horaFinal));
                FallasDAO fallasDAO = new FallasDAO();
            
                fallasDAO.insertNewFalla(fallasDTO);

                response.setSucessfull(true);
                response.setMessage(MSG_SUCESS);
            }else{
                response.setSucessfull(false);
                response.setMessage("Horas incorrectas");
            }
        } catch(Exception ex){
            response.setSucessfull(false);
            response.setMessage(MSG_ERROR + ex.getMessage());
        }
        
        output.setResponse(response);
        return output;
    }
}
