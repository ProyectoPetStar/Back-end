package org.petstar.controller.ETAD;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.petstar.configurations.ReportesETAD;
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
import org.petstar.dao.UsersDAO;
import org.petstar.dto.CatalogosDTO;
import org.petstar.dto.UserSonarhDTO;

/**
 *
 * @author Tech-Pro
 */
public class ReportesController {
    private static final String MSG_SUCESS = "OK";
    private static final String MSG_LOGOUT = "Inicie sesión nuevamente";
    private static final String MSG_ERROR  = "Descripción de error: ";
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
                    if(idEtad == 6 || idEtad == 7 || idEtad == 1010 || idEtad == 2010){
                        if(row.getTipo_kpi() == 0){
                            mapa.put("resBonoA", menosEsMejor(row.getMeta(), row.getGrupoa(), row.getPonderacion()));
                            mapa.put("resBonoB", 0);
                            mapa.put("resBonoC", 0);
                            mapa.put("resBonoD", 0);
                            totalBonoA = totalBonoA + menosEsMejor(row.getMeta(), row.getGrupoa(), row.getPonderacion());
                            totalBonoB = totalBonoB + 0;
                            totalBonoC = totalBonoC + 0;
                            totalBonoD = totalBonoD + 0;
                        }else{
                            mapa.put("resBonoA", masEsMejor(row.getMeta(), row.getGrupoa(), row.getPonderacion()));
                            mapa.put("resBonoB", 0);
                            mapa.put("resBonoC", 0);
                            mapa.put("resBonoD", 0);
                            totalBonoA = totalBonoA + masEsMejor(row.getMeta(), row.getGrupoa(), row.getPonderacion());
                            totalBonoB = totalBonoB + 0;
                            totalBonoC = totalBonoC + 0;
                            totalBonoD = totalBonoD + 0;
                        }
                    }else{
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
                CatalogosDAO catalogosDAO = new CatalogosDAO();
                PeriodosDAO periodosDAO = new PeriodosDAO();
                
                PeriodosDTO periodo = periodosDAO.getPeriodoById(idPeriodo);
                CatalogosDTO etad = catalogosDAO.getDescripcionById("pet_cat_etad", idEtad);
                
                data.setGraficas(ReportesETAD.getIndicadoresDesempeno(etad, idPeriodo, periodo.getMes(), periodo.getAnio()));
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
    
    public OutputJson getReporteBonos(HttpServletRequest request, boolean detallado){
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
                UsersDAO usersDAO = new UsersDAO();
                
                List<CatalogosDTO> listEtads = catalogosDAO.getCatalogosActive("pet_cat_etad");
                PeriodosDTO periodo = periodosDAO.getPeriodoById(idPeriodo);
                
                List<HashMap> listBonoDetallado = new ArrayList<>();
                List<HashMap> listData = new ArrayList<>();
                HashMap<String, Object> header = new HashMap<>();
                header.put("area", "Area");
                header.put("padre", "1");
                header.put("grupoA", "A");
                header.put("grupoB", "B");
                header.put("grupoC", "C");
                header.put("grupoD", "D");
                listData.add(header);
                
                HashMap<String, Object> head = new HashMap<>();
                head.put("area", "Area");
                head.put("padre", "1");
                head.put("grupo", "Grupo");
                head.put("bono",  "Bono");
                head.put("no","Numero de Empleado");
                head.put("empleado","Nombre de Empleado");
                listBonoDetallado.add(head);
                
                BigDecimal promedioNoEtad= BigDecimal.ZERO;
                BigDecimal promedioManto = BigDecimal.ZERO;
                BigDecimal count = BigDecimal.ZERO;
                for(CatalogosDTO etad:listEtads){
                    HashMap<String, Object> etadReport = ReportesETAD.
                            buildReportICDG(idPeriodo, periodo.getMes(), periodo.getAnio(), etad.getId());
                                        
                    if(etad.getDescripcion().equals("REFACCIONES") || etad.getId() == 6 ||
                            etad.getDescripcion().equals("CONTROL INTERNO") || etad.getId() == 7 ||
                            etad.getDescripcion().equals("PTAR") || etad.getId() == 2010 ||
                            etad.getDescripcion().equals("MANTENIMIENTO MIXTO") || etad.getId() == 1010){
                        
                        promedioNoEtad =  promedioNoEtad.add(new BigDecimal((int) etadReport.get("resBonoA")));
                        count = count.add(BigDecimal.ONE);
                    }else{
                        promedioNoEtad = promedioNoEtad.add(new BigDecimal((int) etadReport.get("resBonoA")))
                                .add(new BigDecimal((int) etadReport.get("resBonoB")))
                                .add(new BigDecimal((int) etadReport.get("resBonoC")))
                                .add(new BigDecimal((int) etadReport.get("resBonoD")));

                        count = count.add(new BigDecimal(4));
                    }
                    if(detallado){
                        Object[] grupos = {"A","B","C","D"};
                        for(Object gr:grupos){
                            List<UserSonarhDTO> listEmpleados = usersDAO.
                                    getUsersSonarhByAreaAndGrupo(etad.getValor(), gr.toString());
                            for(UserSonarhDTO empleado:listEmpleados){
                                HashMap<String, Object> mapa = new HashMap<>();
                                mapa.put("area", etad.getValor());
                                mapa.put("padre", "0");
                                mapa.put("grupo", gr.toString());
                                switch(gr.toString()){
                                    case "A":
                                        mapa.put("bono",  etadReport.get("resBonoA"));
                                    break;
                                    case "B":
                                        mapa.put("bono",  etadReport.get("resBonoB"));
                                    break;
                                    case "C":
                                        mapa.put("bono",  etadReport.get("resBonoC"));
                                    break;
                                    case "D":
                                        mapa.put("bono",  etadReport.get("resBonoD"));
                                    break;
                                }
                                mapa.put("no", empleado.getNumEmpleado());
                                mapa.put("empleado",empleado.getPaterno() + " " 
                                        + empleado.getMaterno() + " " + empleado.getNombre());
                                listBonoDetallado.add(mapa);
                            }
                        }
                    }else{
                        HashMap<String, Object> mapa = new HashMap<>();
                        mapa.put("padre", "0");
                        mapa.put("area", etad.getValor());
                        mapa.put("grupoA", etadReport.get("resBonoA"));
                        mapa.put("grupoB", etadReport.get("resBonoB"));
                        mapa.put("grupoC", etadReport.get("resBonoC"));
                        mapa.put("grupoD", etadReport.get("resBonoD"));
                        listData.add(mapa);
                    }
                }
                
                promedioNoEtad = promedioNoEtad.divide(count, 2, RoundingMode.CEILING);
                if(detallado){
                    List<UserSonarhDTO> listEmpleados = usersDAO.
                            getUsersSonarhByAreaAndGrupo("MANTENIMIENTO", "MIXTO");
                    for(UserSonarhDTO empleado:listEmpleados){
                        HashMap<String, Object> mapa = new HashMap<>();
                        mapa.put("area", "MANTENIMIENTO MIXTO");
                        mapa.put("padre", "0");
                        mapa.put("grupo", "MIXTO");
                        mapa.put("bono",  promedioManto);
                        mapa.put("no", empleado.getNumEmpleado());
                        mapa.put("empleado",empleado.getPaterno() + " "
                                + empleado.getMaterno() + " " + empleado.getNombre());
                        listBonoDetallado.add(mapa);
                    }
                    HashMap<String, Object> noEtad = new HashMap<>();
                        noEtad.put("area", "No ETAD");
                        noEtad.put("padre", "0");
                        noEtad.put("grupo", "MIXTO");
                        noEtad.put("bono",  promedioNoEtad);
                        noEtad.put("no", "");
                        noEtad.put("empleado","");
                        listBonoDetallado.add(noEtad);
                }else{
                                        
                    HashMap<String, Object> noEtad = new HashMap<>();
                    noEtad.put("padre", "0");
                    noEtad.put("area", "No ETAD");
                    noEtad.put("grupoA", promedioNoEtad);
                    noEtad.put("grupoB", "");
                    noEtad.put("grupoC", "");
                    noEtad.put("grupoD", "");
                    listData.add(noEtad);
                }
                
                if(detallado){
                    data.setBonos(listBonoDetallado);
                }else{
                    data.setBonos(listData);
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
    
    public OutputJson getGraficasPosicionTrimestral(HttpServletRequest request){
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
            
        try{
            int trimestre = Integer.valueOf(request.getParameter("trimestre"));
            int anio = Integer.valueOf(request.getParameter("anio"));
            UserDTO session = autenticacion.isValidToken(request);
            if(session != null){
                ReportesResponse data = new ReportesResponse();
                
                data.setPosicionamiento(ReportesETAD.getPosicionTrimestral(anio, trimestre));
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
    
    public OutputJson getGraficasPosicionAnual(HttpServletRequest request){
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
            
        try{
            int anio = Integer.valueOf(request.getParameter("anio"));
            UserDTO session = autenticacion.isValidToken(request);
            if(session != null){
                ReportesResponse data = new ReportesResponse();
                
                data.setPosicionamientoAnual(ReportesETAD.getPosicionAnual(anio));
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
