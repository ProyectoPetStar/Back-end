package org.petstar.controller.ETAD;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import org.petstar.dao.UsersDAO;
import org.petstar.dto.CatalogosDTO;
import org.petstar.dto.ETAD.EvaluacionConcentrada;
import org.petstar.dto.ETAD.Posiciones;
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
                    HashMap<String, Object> etadReport = this.
                            buildReportICDG(idPeriodo, periodo.getMes(), periodo.getAnio(), etad.getId());
                    
                    if(etad.getDescripcion().equals("MANTENIMIENTO") || etad.getId() == 5){
                        promedioManto = (BigDecimal) etadReport.get("promedio");
                    }
                    if(etad.getDescripcion().equals("REFACCIONES") || etad.getId() == 6 ||
                            etad.getDescripcion().equals("CONTROL INTERNO") || etad.getId() == 7){
                        
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
                    HashMap<String, Object> mtto = new HashMap<>();
                    mtto.put("padre", "0");
                    mtto.put("area", "MANTENIMIENTO MIXTO");
                    mtto.put("grupoA", promedioManto);
                    mtto.put("grupoB", "");
                    mtto.put("grupoC", "");
                    mtto.put("grupoD", "");
                    listData.add(mtto);
                    
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
                CatalogosDAO catalogosDAO = new CatalogosDAO();
                PeriodosDAO periodosDAO = new PeriodosDAO();
                
                List<PeriodosDTO> listPeriodo = periodosDAO.getPeriodosByTrimestre(anio, trimestre);
                List<CatalogosDTO> listEtads = catalogosDAO.getCatalogosActive("pet_cat_etad");
                List<EvaluacionConcentrada> listEC = new ArrayList<>();
                
                HashMap<String, Object> emptyMap = new HashMap<>();
                emptyMap.put("promedio", 0);
                emptyMap.put("resBonoA", 0);
                emptyMap.put("resBonoB", 0);
                emptyMap.put("resBonoC", 0);
                emptyMap.put("resBonoD", 0);
                        
                for(CatalogosDTO etad:listEtads){
                    HashMap<String, Object> periodo1 = new HashMap<>();
                    HashMap<String, Object> periodo2 = new HashMap<>();
                    HashMap<String, Object> periodo3 = new HashMap<>();
                    switch (listPeriodo.size()) {
                        case 0:
                            periodo1 = emptyMap;
                            periodo2 = emptyMap;
                            periodo3 = emptyMap;
                            break;
                        case 1:
                            periodo1 = this.buildReportICDG(listPeriodo.get(0).getId_periodo(),
                                    listPeriodo.get(0).getMes(), anio, etad.getId());
                            periodo2 = emptyMap;
                            periodo3 = emptyMap;
                            break;
                        case 2:
                            periodo1 = this.buildReportICDG(listPeriodo.get(0).getId_periodo(),
                                    listPeriodo.get(0).getMes(), anio, etad.getId());
                            periodo2 = periodo2 = this.buildReportICDG(listPeriodo.get(1).getId_periodo(),
                                    listPeriodo.get(1).getMes(), anio, etad.getId());
                            periodo3 = emptyMap;
                            break;
                        case 3:
                            periodo1 = this.buildReportICDG(listPeriodo.get(0).getId_periodo(),
                                    listPeriodo.get(0).getMes(), anio, etad.getId());
                            periodo2 = periodo2 = this.buildReportICDG(listPeriodo.get(1).getId_periodo(),
                                    listPeriodo.get(1).getMes(), anio, etad.getId());
                            periodo3 = this.buildReportICDG(listPeriodo.get(2).getId_periodo(),
                                    listPeriodo.get(2).getMes(), anio, etad.getId());
                            break;
                    }
                    
                    EvaluacionConcentrada grupoA = new EvaluacionConcentrada();
                    EvaluacionConcentrada grupoB = new EvaluacionConcentrada();
                    EvaluacionConcentrada grupoC = new EvaluacionConcentrada();
                    EvaluacionConcentrada grupoD = new EvaluacionConcentrada();
                    
                    grupoA.setEtad(etad.getValor());
                    grupoA.setGrupo("A");
                    grupoA.setMes1((int) periodo1.get("resBonoA"));
                    grupoA.setMes2((int) periodo2.get("resBonoA"));
                    grupoA.setMes3((int) periodo3.get("resBonoA"));
                    listEC.add(grupoA);
                    
                    if(!etad.getValor().equals("CONTROL INTERNO") && etad.getId() != 7 &&
                            !etad.getValor().equals("REFACCIONES") && etad.getId() != 6){
                        grupoB.setEtad(etad.getValor());
                        grupoB.setGrupo("B");
                        grupoB.setMes1((int) periodo1.get("resBonoB"));
                        grupoB.setMes2((int) periodo2.get("resBonoB"));
                        grupoB.setMes3((int) periodo3.get("resBonoB"));
                        grupoC.setEtad(etad.getValor());
                        grupoC.setGrupo("C");
                        grupoC.setMes1((int) periodo1.get("resBonoC"));
                        grupoC.setMes2((int) periodo2.get("resBonoC"));
                        grupoC.setMes3((int) periodo3.get("resBonoC"));
                        grupoD.setEtad(etad.getValor());
                        grupoD.setGrupo("D");
                        grupoD.setMes1((int) periodo1.get("resBonoD"));
                        grupoD.setMes2((int) periodo2.get("resBonoD"));
                        grupoD.setMes3((int) periodo3.get("resBonoD"));
                        
                        listEC.add(grupoB);
                        listEC.add(grupoC);
                        listEC.add(grupoD);
                    }
                }
                List<List<Posiciones>> listGeneral = new ArrayList<>();
                List<Posiciones> listPeriodo1 = new ArrayList<>();
                List<Posiciones> listPeriodo2 = new ArrayList<>();
                List<Posiciones> listPeriodo3 = new ArrayList<>();
                for(EvaluacionConcentrada row:listEC){
                    Posiciones mapa1 = new Posiciones();
                    mapa1.setName(row.getEtad() + " " + row.getGrupo());
                    mapa1.setValor(new BigDecimal(row.getMes1()));
                    listPeriodo1.add(mapa1);
                    
                    Posiciones mapa2 = new Posiciones();
                    BigDecimal promedio = new BigDecimal(row.getMes1() + row.getMes2())
                            .divide(new BigDecimal(2), 2, RoundingMode.CEILING);
                    mapa2.setName(row.getEtad() + " " + row.getGrupo());
                    mapa2.setValor(promedio);
                    listPeriodo2.add(mapa2);
                    
                    Posiciones mapa3 = new Posiciones();
                    BigDecimal average = new BigDecimal(row.getMes1() + row.getMes2() + row.getMes3())
                            .divide(new BigDecimal(3), 2, RoundingMode.CEILING);
                    mapa3.setName(row.getEtad() + " " + row.getGrupo());
                    mapa3.setValor(average);
                    listPeriodo3.add(mapa3);
                }
                
                Comparator<Posiciones> comparator = (Posiciones a, Posiciones b) -> {
                    int resultado = b.getValor().compareTo(a.getValor());
                    if ( resultado != 0 ) { return resultado; }
                    return resultado;
                };
                
                Collections.sort( listPeriodo1, comparator );
                Collections.sort( listPeriodo2, comparator );
                Collections.sort( listPeriodo3, comparator );
                
                listGeneral.add(listPeriodo1);
                listGeneral.add(listPeriodo2);
                listGeneral.add(listPeriodo3);
                data.setPosicionamiento(listGeneral);
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
                CatalogosDAO catalogosDAO = new CatalogosDAO();
                PeriodosDAO periodosDAO = new PeriodosDAO();
                
                List<PeriodosDTO> listPeriodo = periodosDAO.getPeriodosByAnio(anio);
                List<CatalogosDTO> listEtads = catalogosDAO.getCatalogosActive("pet_cat_etad");
                List<EvaluacionConcentrada> listEC = new ArrayList<>();
                
                HashMap<String, Object> emptyMap = new HashMap<>();
                emptyMap.put("promedio", 0);
                emptyMap.put("resBonoA", 0);
                emptyMap.put("resBonoB", 0);
                emptyMap.put("resBonoC", 0);
                emptyMap.put("resBonoD", 0);
                        
                for(CatalogosDTO etad:listEtads){
                    EvaluacionConcentrada grupoA = new EvaluacionConcentrada();
                    grupoA.setEtad(etad.getValor());
                    grupoA.setGrupo("A");
                    listEC.add(grupoA);
                    
                    if(!etad.getValor().equals("CONTROL INTERNO") && etad.getId() != 7 &&
                            !etad.getValor().equals("REFACCIONES") && etad.getId() != 6){
                        
                        EvaluacionConcentrada grupoB = new EvaluacionConcentrada();
                        grupoB.setEtad(etad.getValor());
                        grupoB.setGrupo("B");
                        EvaluacionConcentrada grupoC = new EvaluacionConcentrada();
                        grupoC.setEtad(etad.getValor());
                        grupoC.setGrupo("C");
                        EvaluacionConcentrada grupoD = new EvaluacionConcentrada();
                        grupoD.setEtad(etad.getValor());
                        grupoD.setGrupo("D");
                        listEC.add(grupoB);
                        listEC.add(grupoC);
                        listEC.add(grupoD);
                    }
                }
                
                for(CatalogosDTO etad:listEtads){
                    if(!listPeriodo.isEmpty()){
                        for(PeriodosDTO periodo:listPeriodo){
                            HashMap<String, Object> mapa = this.buildReportICDG(
                                    periodo.getId_periodo(), periodo.getMes(), anio, etad.getId());
                            int indexA = this.buscarIndex(listEC, etad.getValor(), "A");
                            int indexB = this.buscarIndex(listEC, etad.getValor(), "B");
                            int indexC = this.buscarIndex(listEC, etad.getValor(), "C");
                            int indexD = this.buscarIndex(listEC, etad.getValor(), "D");
                            
                            switch(periodo.getMes()){
                                case 1 :
                                    if(indexA != -1){
                                        listEC.get(indexA).setMes1((int) mapa.get("resBonoA"));
                                    }
                                    if(indexB != -1){
                                        listEC.get(indexB).setMes1((int) mapa.get("resBonoB"));
                                    }
                                    if(indexC != -1){
                                        listEC.get(indexC).setMes1((int) mapa.get("resBonoC"));
                                    }
                                    if(indexD != -1){
                                        listEC.get(indexD).setMes1((int) mapa.get("resBonoD"));
                                    }
                                    break;
                                case 2 :
                                    if(indexA != -1){
                                        listEC.get(indexA).setMes2((int) mapa.get("resBonoA"));
                                    }
                                    if(indexB != -1){
                                        listEC.get(indexB).setMes2((int) mapa.get("resBonoB"));
                                    }
                                    if(indexC != -1){
                                        listEC.get(indexC).setMes2((int) mapa.get("resBonoC"));
                                    }
                                    if(indexD != -1){
                                        listEC.get(indexD).setMes2((int) mapa.get("resBonoD"));
                                    }
                                    break;
                                case 3 :
                                    if(indexA != -1){
                                        listEC.get(indexA).setMes3((int) mapa.get("resBonoA"));
                                    }
                                    if(indexB != -1){
                                        listEC.get(indexB).setMes3((int) mapa.get("resBonoB"));
                                    }
                                    if(indexC != -1){
                                        listEC.get(indexC).setMes3((int) mapa.get("resBonoC"));
                                    }
                                    if(indexD != -1){
                                        listEC.get(indexD).setMes3((int) mapa.get("resBonoD"));
                                    }
                                    break;
                                case 4 :
                                    if(indexA != -1){
                                        listEC.get(indexA).setMes4((int) mapa.get("resBonoA"));
                                    }
                                    if(indexB != -1){
                                        listEC.get(indexB).setMes4((int) mapa.get("resBonoB"));
                                    }
                                    if(indexC != -1){
                                        listEC.get(indexC).setMes4((int) mapa.get("resBonoC"));
                                    }
                                    if(indexD != -1){
                                        listEC.get(indexD).setMes4((int) mapa.get("resBonoD"));
                                    }
                                    break;
                                case 5 :
                                    if(indexA != -1){
                                        listEC.get(indexA).setMes5((int) mapa.get("resBonoA"));
                                    }
                                    if(indexB != -1){
                                        listEC.get(indexB).setMes5((int) mapa.get("resBonoB"));
                                    }
                                    if(indexC != -1){
                                        listEC.get(indexC).setMes5((int) mapa.get("resBonoC"));
                                    }
                                    if(indexD != -1){
                                        listEC.get(indexD).setMes5((int) mapa.get("resBonoD"));
                                    }
                                    break;
                                case 6 :
                                    if(indexA != -1){
                                        listEC.get(indexA).setMes6((int) mapa.get("resBonoA"));
                                    }
                                    if(indexB != -1){
                                        listEC.get(indexB).setMes6((int) mapa.get("resBonoB"));
                                    }
                                    if(indexC != -1){
                                        listEC.get(indexC).setMes6((int) mapa.get("resBonoC"));
                                    }
                                    if(indexD != -1){
                                        listEC.get(indexD).setMes6((int) mapa.get("resBonoD"));
                                    }
                                    break;
                                case 7 :
                                    if(indexA != -1){
                                        listEC.get(indexA).setMes7((int) mapa.get("resBonoA"));
                                    }
                                    if(indexB != -1){
                                        listEC.get(indexB).setMes7((int) mapa.get("resBonoB"));
                                    }
                                    if(indexC != -1){
                                        listEC.get(indexC).setMes7((int) mapa.get("resBonoC"));
                                    }
                                    if(indexD != -1){
                                        listEC.get(indexD).setMes7((int) mapa.get("resBonoD"));
                                    }
                                    break;
                                case 8 :
                                    if(indexA != -1){
                                        listEC.get(indexA).setMes8((int) mapa.get("resBonoA"));
                                    }
                                    if(indexB != -1){
                                        listEC.get(indexB).setMes8((int) mapa.get("resBonoB"));
                                    }
                                    if(indexC != -1){
                                        listEC.get(indexC).setMes8((int) mapa.get("resBonoC"));
                                    }
                                    if(indexD != -1){
                                        listEC.get(indexD).setMes8((int) mapa.get("resBonoD"));
                                    }
                                    break;
                                case 9 :
                                    if(indexA != -1){
                                        listEC.get(indexA).setMes9((int) mapa.get("resBonoA"));
                                    }
                                    if(indexB != -1){
                                        listEC.get(indexB).setMes9((int) mapa.get("resBonoB"));
                                    }
                                    if(indexC != -1){
                                        listEC.get(indexC).setMes9((int) mapa.get("resBonoC"));
                                    }
                                    if(indexD != -1){
                                        listEC.get(indexD).setMes9((int) mapa.get("resBonoD"));
                                    }
                                    break;
                                case 10 :
                                    if(indexA != -1){
                                        listEC.get(indexA).setMes10((int) mapa.get("resBonoA"));
                                    }
                                    if(indexB != -1){
                                        listEC.get(indexB).setMes10((int) mapa.get("resBonoB"));
                                    }
                                    if(indexC != -1){
                                        listEC.get(indexC).setMes10((int) mapa.get("resBonoC"));
                                    }
                                    if(indexD != -1){
                                        listEC.get(indexD).setMes10((int) mapa.get("resBonoD"));
                                    }
                                    break;
                                case 11 :
                                    if(indexA != -1){
                                        listEC.get(indexA).setMes11((int) mapa.get("resBonoA"));
                                    }
                                    if(indexB != -1){
                                        listEC.get(indexB).setMes11((int) mapa.get("resBonoB"));
                                    }
                                    if(indexC != -1){
                                        listEC.get(indexC).setMes11((int) mapa.get("resBonoC"));
                                    }
                                    if(indexD != -1){
                                        listEC.get(indexD).setMes11((int) mapa.get("resBonoD"));
                                    }
                                    break;
                                case 12 :
                                    if(indexA != -1){
                                        listEC.get(indexA).setMes12((int) mapa.get("resBonoA"));
                                    }
                                    if(indexB != -1){
                                        listEC.get(indexB).setMes12((int) mapa.get("resBonoB"));
                                    }
                                    if(indexC != -1){
                                        listEC.get(indexC).setMes12((int) mapa.get("resBonoC"));
                                    }
                                    if(indexD != -1){
                                        listEC.get(indexD).setMes12((int) mapa.get("resBonoD"));
                                    }
                                    break;
                            }
                            
                        }
                    }
                }
                List<Posiciones> listGeneral = new ArrayList<>();
                for(EvaluacionConcentrada row:listEC){
                    int sumaMeses = row.getMes1() + row.getMes2() + row.getMes3() + row.getMes4() +
                            row.getMes5() + row.getMes5() + row.getMes6() + row.getMes7() + row.getMes8() + 
                            row.getMes9() + row.getMes10() + row.getMes11() + row.getMes12();
                    BigDecimal promedio = new BigDecimal(sumaMeses).divide(new BigDecimal(12), 2, RoundingMode.CEILING);
                    Posiciones mapa = new Posiciones();
                    mapa.setName(row.getEtad()+ " " + row.getGrupo());
                    mapa.setValor(promedio);
                    listGeneral.add(mapa);
                }
                
                Comparator<Posiciones> comparator = (Posiciones a, Posiciones b) -> {
                    int resultado = b.getValor().compareTo(a.getValor());
                    if ( resultado != 0 ) { return resultado; }
                    return resultado;
                };
                
                Collections.sort( listGeneral, comparator );
                data.setPosicionamientoAnual(listGeneral);
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
    
    private HashMap<String,Object> buildReportICDG(int idPeriodo, int mes, int anio, int idEtad) throws Exception{
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
        promedio = promedio.add(new BigDecimal(totalBonoA)).add(new BigDecimal(totalBonoB))
                .add(new BigDecimal(totalBonoC)).add(new BigDecimal(totalBonoD));
        promedio = promedio.divide(new BigDecimal(4), 2, RoundingMode.CEILING);
        mapa.put("promedio", promedio);
        mapa.put("resBonoA", totalBonoA);
        if(idEtad != 6 && idEtad != 7 ){
            mapa.put("resBonoB", totalBonoB);
            mapa.put("resBonoC", totalBonoC);
            mapa.put("resBonoD", totalBonoD);
        }else{
            mapa.put("resBonoB", "");
            mapa.put("resBonoC", "");
            mapa.put("resBonoD", "");
        }
        return mapa;
    }
    
    private int buscarIndex(List<EvaluacionConcentrada> lista,String etad, String grupo) {
        int index = -1;
        for(int y=0; y<lista.size(); y++) {
            if(lista.get(y).getEtad().equals(etad) && lista.get(y).getGrupo().equals(grupo)){
                index = y;
            }
        }
        return index;
    }
}
