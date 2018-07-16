package org.petstar.controller.ETAD;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.petstar.controller.ControllerAutenticacion;
import org.petstar.dao.CatalogosDAO;
import org.petstar.dao.ETAD.ReportesDAO;
import org.petstar.dao.PeriodosDAO;
import org.petstar.dto.ETAD.Reporte;
import org.petstar.dto.PeriodosDTO;
import org.petstar.dto.UserDTO;
import org.petstar.model.ETAD.ReportesResponse;
import org.petstar.model.OutputJson;
import org.petstar.model.ResponseJson;
import static org.petstar.configurations.utils.masEsMejor;
import static org.petstar.configurations.utils.menosEsMejor;
import org.petstar.dto.CatalogosDTO;

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
    
    public OutputJson getIndicadorClaveDesempenoGlobal(HttpServletRequest request){
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
            
        try{
            int idPeriodo = Integer.valueOf(request.getParameter("id_periodo"));
            int idEtad = Integer.valueOf(request.getParameter("id_etad"));
            
            UserDTO session = autenticacion.isValidToken(request);
            if(session != null){
                ReportesResponse data = new ReportesResponse();
                ReportesDAO reportesDAO = new ReportesDAO();
                PeriodosDAO periodosDAO = new PeriodosDAO();
                
                PeriodosDTO periodo = periodosDAO.getPeriodoById(idPeriodo);
                
                List<HashMap> listData = new ArrayList<>();
                List<Reporte> listReporte = reportesDAO.indicadorClaveDesempenoGlobal(
                        idPeriodo, idEtad, periodo.getMes(), periodo.getAnio());
                
                int totalBonoA = 0;
                int totalBonoB = 0;
                int totalBonoC = 0;
                int totalBonoD = 0;
                for(Reporte row:listReporte){
                    HashMap<String, Object> mapa = new HashMap<>();
                    mapa.put("total", 0);
                    mapa.put("objetivo", row.getObjetivo_operativo());
                    mapa.put("kpi", row.getKpi_operativo());
                    mapa.put("meta", row.getMeta());
                    mapa.put("um", row.getUnidad_medida());
                    mapa.put("ponderacion", row.getPonderacion());
                    mapa.put("frecuencia", (row.getId_frecuencia() == 1)?"Turno":"Mensual");
                    mapa.put("resKpiA", row.getGrupoa());
                    mapa.put("resKpiB", row.getGrupob());
                    mapa.put("resKpiC", row.getGrupoc());
                    mapa.put("resKpiD", row.getGrupod());
                    if(row.getTipo_kpi() == 0){
                        mapa.put("resBonoA", menosEsMejor(row.getMeta(), row.getGrupoa(), row.getPonderacion()));
                        mapa.put("resBonoB", menosEsMejor(row.getMeta(), row.getGrupob(), row.getPonderacion()));
                        mapa.put("resBonoC", menosEsMejor(row.getMeta(), row.getGrupoc(), row.getPonderacion()));
                        mapa.put("resBonoD", menosEsMejor(row.getMeta(), row.getGrupod(), row.getPonderacion()));
                        totalBonoA = totalBonoA + menosEsMejor(row.getMeta(), row.getGrupoa(), row.getPonderacion());
                        totalBonoB = totalBonoB + menosEsMejor(row.getMeta(), row.getGrupob(), row.getPonderacion());
                        totalBonoC = totalBonoC + menosEsMejor(row.getMeta(), row.getGrupoc(), row.getPonderacion());
                        totalBonoD = totalBonoD + menosEsMejor(row.getMeta(), row.getGrupod(), row.getPonderacion());

                    }else if(row.getTipo_kpi() == 1){
                        mapa.put("resBonoA", masEsMejor(row.getMeta(), row.getGrupoa(), row.getPonderacion()));
                        mapa.put("resBonoB", masEsMejor(row.getMeta(), row.getGrupob(), row.getPonderacion()));
                        mapa.put("resBonoC", masEsMejor(row.getMeta(), row.getGrupoc(), row.getPonderacion()));
                        mapa.put("resBonoD", masEsMejor(row.getMeta(), row.getGrupod(), row.getPonderacion()));
                        totalBonoA = totalBonoA + masEsMejor(row.getMeta(), row.getGrupoa(), row.getPonderacion());
                        totalBonoB = totalBonoB + masEsMejor(row.getMeta(), row.getGrupob(), row.getPonderacion());
                        totalBonoC = totalBonoC + masEsMejor(row.getMeta(), row.getGrupoc(), row.getPonderacion());
                        totalBonoD = totalBonoD + masEsMejor(row.getMeta(), row.getGrupod(), row.getPonderacion());
                    }
                    listData.add(mapa);
                }
                HashMap<String, Object> mapa = new HashMap<>();
                mapa.put("total", 1);
                mapa.put("resBonoA", totalBonoA);
                mapa.put("resBonoB", totalBonoB);
                mapa.put("resBonoC", totalBonoC);
                mapa.put("resBonoD", totalBonoD);
                listData.add(mapa);
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
    
    public OutputJson getGraficasByEtad(HttpServletRequest request){
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
            
        try{
            int idPeriodo = Integer.valueOf(request.getParameter("id_periodo"));
            int idEtad = Integer.valueOf(request.getParameter("id_etad"));
            
            UserDTO session = autenticacion.isValidToken(request);
            if(session != null){
                ReportesResponse data = new ReportesResponse();
                ReportesDAO reportesDAO = new ReportesDAO();
                PeriodosDAO periodosDAO = new PeriodosDAO();
                
                PeriodosDTO periodo = periodosDAO.getPeriodoById(idPeriodo);
                
                List<HashMap> listData = new ArrayList<>();
                List<Reporte> listReporte = reportesDAO.indicadorClaveDesempenoGlobal(
                        idPeriodo, idEtad, periodo.getMes(), periodo.getAnio());
                
                for(Reporte row:listReporte){
                    HashMap<String, Object> mapa = new HashMap<>();
                    mapa.put("kpi", row.getKpi_operativo());
                    mapa.put("metaA", row.getMeta());
                    mapa.put("metaB", row.getMeta());
                    mapa.put("metaC", row.getMeta());
                    mapa.put("metaD", row.getMeta());
                    mapa.put("resultadoA", row.getGrupoa());
                    mapa.put("resultadoB", row.getGrupob());
                    mapa.put("resultadoC", row.getGrupoc());
                    mapa.put("resultadoD", row.getGrupod());
                    listData.add(mapa);
                }
                data.setGraficas(listData);
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
    
    public OutputJson getReporteBonos(HttpServletRequest request){
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
            
        try{
            int idPeriodo = Integer.valueOf(request.getParameter("id_periodo"));
            
            UserDTO session = autenticacion.isValidToken(request);
            if(session != null){
                ReportesResponse data = new ReportesResponse();
                CatalogosDAO catalogosDAO = new CatalogosDAO();
                PeriodosDAO periodosDAO = new PeriodosDAO();
                
                List<CatalogosDTO> listEtads = catalogosDAO.getCatalogosActive("pet_cat_etad");
                PeriodosDTO periodo = periodosDAO.getPeriodoById(idPeriodo);
                
                List<HashMap> listData = new ArrayList<>();
                HashMap<String, Object> header = new HashMap<>();
                header.put("area", "Area");
                header.put("padre", "1");
                header.put("grupoA", "A");
                header.put("grupoB", "B");
                header.put("grupoC", "C");
                header.put("grupoD", "D");
                listData.add(header);
                
                BigDecimal promedioNoEtad= BigDecimal.ZERO;
                BigDecimal promedioManto = BigDecimal.ZERO;
                BigDecimal count = BigDecimal.ZERO;
                for(CatalogosDTO etad:listEtads){
                    HashMap<String, Object> mapa = new HashMap<>();
                    HashMap<String, Object> etadReport = this.
                            buildReportICDG(idPeriodo, periodo.getMes(), periodo.getAnio(), etad.getId());
                    
                    if(etad.getDescripcion().equals("MANTENIMIENTO") || etad.getId() == 5){
                        promedioManto = (BigDecimal) etadReport.get("promedio");
                    }
                    mapa.put("padre", "0");
                    mapa.put("area", etad.getValor());
                    mapa.put("grupoA", etadReport.get("resBonoA"));
                    mapa.put("grupoB", etadReport.get("resBonoB"));
                    mapa.put("grupoC", etadReport.get("resBonoC"));
                    mapa.put("grupoD", etadReport.get("resBonoD"));
                    listData.add(mapa);
                    
                    if(etad.getDescripcion().equals("REFACCIONES") || etad.getId() == 6 || 
                            etad.getDescripcion().equals("CONTROL INTERNO") || etad.getId() == 7){
                        
                        promedioNoEtad =  promedioNoEtad.add(
                                new BigDecimal((int) etadReport.get("resBonoA")));
                        count = count.add(BigDecimal.ONE);
                    }else{
                        promedioNoEtad = promedioNoEtad.add(
                                new BigDecimal((int) etadReport.get("resBonoA")))
                                .add(new BigDecimal((int) etadReport.get("resBonoB")))
                                .add(new BigDecimal((int) etadReport.get("resBonoC")))
                                .add(new BigDecimal((int) etadReport.get("resBonoD")));
                        
                        count = count.add(new BigDecimal(4));
                    }
                }
                
                HashMap<String, Object> mtto = new HashMap<>();
                mtto.put("padre", "0");
                mtto.put("area", "MANTENIMIENTO MIXTO");
                mtto.put("grupoA", promedioManto);
                mtto.put("grupoB", "");
                mtto.put("grupoC", "");
                mtto.put("grupoD", "");
                listData.add(mtto);
                
                promedioNoEtad = promedioNoEtad.divide(count, 2, RoundingMode.CEILING);
                HashMap<String, Object> noEtad = new HashMap<>();
                noEtad.put("padre", "0");
                noEtad.put("area", "No ETAD");
                noEtad.put("grupoA", promedioNoEtad);
                noEtad.put("grupoB", "");
                noEtad.put("grupoC", "");
                noEtad.put("grupoD", "");
                listData.add(noEtad);
                    
                data.setBonos(listData);
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
    
    public HashMap<String,Object> buildReportICDG(int idPeriodo, int mes, int anio, int idEtad) throws Exception{
        ReportesDAO reportesDAO = new ReportesDAO();
        List<Reporte> listReporte = reportesDAO.indicadorClaveDesempenoGlobal(idPeriodo, idEtad, mes, anio);
        
        int totalBonoA = 0;
        int totalBonoB = 0;
        int totalBonoC = 0;
        int totalBonoD = 0;
        for(Reporte row:listReporte){
            if(row.getTipo_kpi() == 0){
                totalBonoA = totalBonoA + menosEsMejor(row.getMeta(), row.getGrupoa(), row.getPonderacion());
                totalBonoB = totalBonoB + menosEsMejor(row.getMeta(), row.getGrupob(), row.getPonderacion());
                totalBonoC = totalBonoC + menosEsMejor(row.getMeta(), row.getGrupoc(), row.getPonderacion());
                totalBonoD = totalBonoD + menosEsMejor(row.getMeta(), row.getGrupod(), row.getPonderacion());
            }else if(row.getTipo_kpi() == 1){
                totalBonoA = totalBonoA + masEsMejor(row.getMeta(), row.getGrupoa(), row.getPonderacion());
                totalBonoB = totalBonoB + masEsMejor(row.getMeta(), row.getGrupob(), row.getPonderacion());
                totalBonoC = totalBonoC + masEsMejor(row.getMeta(), row.getGrupoc(), row.getPonderacion());
                totalBonoD = totalBonoD + masEsMejor(row.getMeta(), row.getGrupod(), row.getPonderacion());
            }
        }
        
        HashMap<String, Object> mapa = new HashMap<>();
        BigDecimal promedio = BigDecimal.ZERO;
        promedio.add(new BigDecimal(totalBonoA)).add(new BigDecimal(totalBonoB))
                .add(new BigDecimal(totalBonoC)).add(new BigDecimal(totalBonoD));
        promedio.divide(new BigDecimal(4), 2, RoundingMode.CEILING);
        mapa.put("promedio", promedio);
        mapa.put("resBonoA", totalBonoA);
        mapa.put("resBonoB", totalBonoB);
        mapa.put("resBonoC", totalBonoC);
        mapa.put("resBonoD", totalBonoD);
        return mapa;
    }
}
