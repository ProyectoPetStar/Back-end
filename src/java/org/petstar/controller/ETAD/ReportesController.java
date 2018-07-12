package org.petstar.controller.ETAD;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.petstar.controller.ControllerAutenticacion;
import org.petstar.dao.CatalogosDAO;
import org.petstar.dao.ETAD.FrecuenciasDAO;
import org.petstar.dao.ETAD.ObjetivosOperativosDAO;
import org.petstar.dao.ETAD.ReportesDAO;
import org.petstar.dao.PeriodosDAO;
import org.petstar.dto.ETAD.Reporte;
import org.petstar.dto.PeriodosDTO;
import org.petstar.dto.UserDTO;
import org.petstar.model.ETAD.CatalogosResponse;
import org.petstar.model.ETAD.ReportesResponse;
import org.petstar.model.OutputJson;
import org.petstar.model.ResponseJson;

/**
 *
 * @author Tech-Pro
 */
public class ReportesController {
    private static final String MSG_SUCESS = "OK";
    private static final String MSG_LOGOUT = "Inicie sesión nuevamente";
    private static final String MSG_ERROR  = "Descripción de error: ";
    private static final String MSG_EXIST  = "Ya existe un registro con estos valores.";
    private static final String MSG_NOFOUND= "Registro no encontrado.";
    private static final String TABLE_ETAD = "pet_cat_etad";
    private static final String TABLE_GROUP= "pet_cat_grupo";
    
    /**
     * Carga de Combos
     * Servicio que se encarga del llenado de las listas que se utilizaran
     * para los combos que se utilicen para generar los reportes
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
                ReportesResponse data = new ReportesResponse();
                CatalogosDAO catalogosDAO = new CatalogosDAO();
                PeriodosDAO periodosDAO = new PeriodosDAO();
                
                data.setListGrupos(catalogosDAO.getCatalogosActive(TABLE_GROUP));
                data.setListEtads(catalogosDAO.getCatalogosActive(TABLE_ETAD));
                data.setListPeriodos(periodosDAO.getPeriodos());
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
    
    public OutputJson getIndicadorClaveDesempenoByGrupo(HttpServletRequest request){
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
            
        try{
            int idPeriodo = Integer.valueOf(request.getParameter("id_periodo"));
            int idGrupo = Integer.valueOf(request.getParameter("id_grupo"));
            int idEtad = Integer.valueOf(request.getParameter("id_etad"));
            
            UserDTO session = autenticacion.isValidToken(request);
            if(session != null){
                ReportesResponse data = new ReportesResponse();
                ReportesDAO reportesDAO = new ReportesDAO();
                PeriodosDAO periodosDAO = new PeriodosDAO();
                
                PeriodosDTO periodo = periodosDAO.getPeriodoById(idPeriodo);
                
                List<HashMap> listData = new ArrayList<>();
                List<Reporte> listReporte = reportesDAO.indicadorClaveDesempenoByGrupo(
                        idPeriodo, idEtad, idGrupo, periodo.getAnio());
                
                for(Reporte row:listReporte){
                    HashMap<String, Object> mapa = new HashMap<>();
                    mapa.put("objetivo", row.getObjetivo_operativo());
                    mapa.put("kpi", row.getKpi_operativo());
                    mapa.put("meta", row.getMeta());
                    mapa.put("um", row.getUnidad_medida());
                    mapa.put("ponderacion", row.getPonderacion());
                    mapa.put("total", row.getTotal_mes());
                    mapa.put("evaluacion", row.getResultado());
                    listData.add(mapa);
                }
                data.setIndicadorDesempeno(listData);
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
}
