package org.petstar.controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.petstar.configurations.ReportesETAD;
import org.petstar.configurations.ReportesOEE;
import org.petstar.configurations.utils;
import static org.petstar.configurations.utils.getDateFirstDay;
import static org.petstar.configurations.utils.getDateLastDay;
import org.petstar.dao.CatalogosDAO;
import org.petstar.dao.LineasDAO;
import org.petstar.dao.PeriodosDAO;
import org.petstar.dao.ReportesDAO;
import org.petstar.dto.CatalogosDTO;
import org.petstar.dto.LineasDTO;
import org.petstar.dto.PeriodosDTO;
import org.petstar.dto.ResultSQLDate;
import org.petstar.model.OutputJson;
import org.petstar.model.ResponseJson;
import org.petstar.model.VideoWallResponse;

/**
 *
 * @author Tech-Pro 
*/
public class ControllerVideoWall {

    private static final String MSG_SUCESS = "OK";
    private static final String MSG_ERROR  = "Descripción de error: ";
    
    public OutputJson generateVideoWall(HttpServletRequest request){
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        
        try{
            Date currentDate = utils.getCurrentDate();
            currentDate = utils.sumarFechasDias(currentDate, -1);
            int anio = utils.obtenerAnio(currentDate);
            int mes = utils.obtenerMes(currentDate);
            int trimestre = utils.getQuarter(mes);
            
            VideoWallResponse data = new VideoWallResponse();
            CatalogosDAO catalogosDAO = new CatalogosDAO();
            PeriodosDAO periodosDAO = new PeriodosDAO();
            ReportesDAO reportesDAO = new ReportesDAO();
            LineasDAO lineasDAO = new LineasDAO();
            List<LineasDTO> listLineas = lineasDAO.getLineasActive();
            List<List<HashMap>> listReportesETAD = new ArrayList<>();
            List<List<HashMap>> listReportesOEE = new ArrayList<>();
            
            PeriodosDTO periodo = periodosDAO.getPeriodoByMesAndAnio(mes, anio);
            for(LineasDTO li : listLineas){
                periodo = periodosDAO.getPeriodoById(periodo.getId_periodo(), li.getId_linea());
                if(periodo != null){
                    Date fechaInicio = getDateFirstDay(periodo.getAnio(), periodo.getMes());
                    Date FechaTermino = getDateLastDay(periodo.getAnio(), periodo.getMes());
                    listReportesOEE.add(ReportesOEE.getReporteFuentePerdidas(fechaInicio, FechaTermino, periodo, li));

                    ResultSQLDate fecI = reportesDAO.getFirstDateofPeriodo(periodo.getMes(), periodo.getAnio(), li.getId_linea());
                    ResultSQLDate fecT = reportesDAO.getLastDateofPeriodo(periodo.getMes(), periodo.getAnio(), li.getId_linea());
                    
                    Date fechaI = getDateFirstDay(periodo.getAnio(), periodo.getMes());
                    Date fechaT = getDateLastDay(periodo.getAnio(), periodo.getMes());
                    if(fecI != null && fecT != null){
                        fechaI = utils.sumarFechasDias(fecI.getResult(), 2);
                        fechaT = utils.sumarFechasDias(fecT.getResult(), 2);
                    }
                    listReportesOEE.add(ReportesOEE.getReporteDisponibilidad(fechaI, fechaT, periodo, li));
                    listReportesOEE.add(ReportesOEE.getReporteOEE(fechaI, fechaT, periodo, li));
                    
                    listReportesOEE.add(ReportesOEE.getReportDesempenoDiario(fechaInicio, FechaTermino, li));
                    List<List<HashMap>> produccionRealPlan = ReportesOEE.getReportePlanVsRealMensual("byDays", periodo, li);
                    List<List<HashMap>> velocidadPromedio = ReportesOEE.getReporteVelocidad(fechaInicio, FechaTermino, li);
                    listReportesOEE.add(produccionRealPlan.get(1));
                    listReportesOEE.add(produccionRealPlan.get(0));
                    listReportesOEE.add(velocidadPromedio.get(0));
                    if(li.getId_gpo_linea() == 1){
                        listReportesOEE.add(ReportesOEE.getReportDesempenoDiarioPO(fechaInicio, FechaTermino, li, periodo.getId_periodo()));
                    }
                }
            }
            
            periodo = periodosDAO.getPeriodoByMesAndAnio(mes, anio);
            List<CatalogosDTO> listEtads = catalogosDAO.getCatalogosActive("pet_cat_etad");
            for(CatalogosDTO etad : listEtads){
                List<HashMap> listData = ReportesETAD.getIndicadoresDesempeno(etad, periodo.getId_periodo(), periodo.getMes(), periodo.getAnio());
                if(!listData.isEmpty())
                    listReportesETAD.add(listData);
            }
            
            data.setPosicionTrimestral(ReportesETAD.getPosicionTrimestral(periodo.getAnio(), trimestre).get(2));
            data.setPosicionAnual(ReportesETAD.getPosicionAnual(periodo.getAnio()));
            data.setETAD(listReportesETAD);
            data.setOEE(listReportesOEE);
            output.setData(data);
            
            output.setData(data);
            response.setSucessfull(true);
            response.setMessage(MSG_SUCESS);
        }catch(Exception ex){
            response.setSucessfull(false);
            response.setMessage(MSG_ERROR + ex.getMessage());
        }
        output.setResponse(response);
        return output;
    }
}
