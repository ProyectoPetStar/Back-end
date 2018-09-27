package org.petstar.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.petstar.configurations.ReportesOEE;
import org.petstar.dao.LineasDAO;
import org.petstar.model.OutputJson;
import org.petstar.model.ReportesResponseJson;
import org.petstar.model.ResponseJson;
import static org.petstar.configurations.utils.convertSqlToDay;
import static org.petstar.configurations.utils.getDateLastDay;
import static org.petstar.configurations.utils.getDateFirstDay;
import static org.petstar.configurations.utils.sumarFechasDias;
import static org.petstar.configurations.utils.convertStringToSql;
import org.petstar.dao.GposLineaDAO;
import org.petstar.dao.PeriodosDAO;
import org.petstar.dao.ReportesDAO;
import org.petstar.dto.FallasDTO;
import org.petstar.dto.LineasDTO;
import org.petstar.dto.PeriodosDTO;
import org.petstar.dto.ReporteDTO;
import org.petstar.dto.ReporteDiario;
import org.petstar.dto.ResultSQLDate;
import org.petstar.dto.UserDTO;

/**
 * @author Tech-Pro
 */
public class ControllerReportes {
    private static final String MSG_SUCESS = "OK";
    private static final String MSG_LOGOUT = "Inicie sesión nuevamente";
    private static final String MSG_ERROR  = "Descripción de error: ";
    private static final String MSG_NOEXIS = "Periodo Seleccionado Incorrecto";
    private static final String MSG_NODATA = "No se encontraron registros";
    
    public OutputJson getOEEFallasByLinea(HttpServletRequest request) throws Exception{
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        
        try{
            int idLInea = Integer.valueOf(request.getParameter("id_linea"));
            int idPeriodo = Integer.valueOf(request.getParameter("id_periodo"));
            UserDTO sesion = autenticacion.isValidToken(request);
            if(sesion != null){
                PeriodosDAO periodosDAO = new PeriodosDAO();
                ReportesDAO reportesDAO = new ReportesDAO();
                LineasDAO lineasDAO = new LineasDAO();
                PeriodosDTO periodo = periodosDAO.getPeriodoById(idPeriodo, idLInea);
                LineasDTO linea = lineasDAO.getLineasDataById(idLInea);
                if(periodo != null){
                    ResultSQLDate fecIni = reportesDAO.getFirstDateofPeriodo(periodo.getMes(), periodo.getAnio(), idLInea);
                    ResultSQLDate fecTer = reportesDAO.getLastDateofPeriodo(periodo.getMes(), periodo.getAnio(), idLInea);
                    Date fechaInicio = sumarFechasDias(fecIni.getResult(), 2);
                    Date FechaTermino = sumarFechasDias(fecTer.getResult(), 2);
                    ReportesResponseJson data = new ReportesResponseJson();
                    
                    data.setListaOEEFallas(ReportesOEE.getReporteFuentePerdidas(fechaInicio, FechaTermino, periodo, linea));

                    output.setData(data);
                    response.setMessage(MSG_SUCESS);
                    response.setSucessfull(true);
                }else{
                    response.setSucessfull(false);
                    response.setMessage(MSG_NOEXIS);
                }
            }else{
                response.setMessage(MSG_LOGOUT);
                response.setSucessfull(false);
            }
        }catch (Exception ex){
            response.setSucessfull(false);
            response.setMessage(MSG_ERROR + ex.getMessage());
        }
        output.setResponse(response);
        return output;
    }
    
    public OutputJson loadCombobox(HttpServletRequest request){
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        
        try{
            UserDTO sesion = autenticacion.isValidToken(request);
            if(sesion != null){
                LineasDAO lineasDAO = new LineasDAO();
                PeriodosDAO periodosDAO = new PeriodosDAO();
                GposLineaDAO gposLineaDAO = new GposLineaDAO();
                ReportesResponseJson data = new ReportesResponseJson();
                
                data.setListLineas(lineasDAO.getLineasActive());
                data.setListPeriodos(periodosDAO.getAllPeriodos());
                data.setListGposLineas(gposLineaDAO.getGposLineaActiveForOEE());
                output.setData(data);

                response.setSucessfull(true);
                response.setMessage(MSG_SUCESS);
            }else{
                response.setSucessfull(false);
                response.setMessage(MSG_LOGOUT);
            }
        }catch(Exception ex){
            response.setSucessfull(false);
            response.setMessage(MSG_ERROR + ex.getMessage());
        }
        output.setResponse(response);
        return output;
    }
    
    public OutputJson getReporteEficiencia(HttpServletRequest request) throws Exception{
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ReportesResponseJson data = new ReportesResponseJson();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        
        try{
            int idPeriodo = Integer.valueOf(request.getParameter("id_periodo"));
            int idLInea = Integer.valueOf(request.getParameter("id_linea"));
            UserDTO sesion = autenticacion.isValidToken(request);
            if(sesion != null){
                PeriodosDAO periodosDAO = new PeriodosDAO();
                LineasDAO lineasDAO = new LineasDAO();
                PeriodosDTO periodo = periodosDAO.getPeriodoById(idPeriodo, idLInea);
                LineasDTO linea = lineasDAO.getLineasDataById(idLInea);
                if(periodo != null){
                    ReportesDAO reportesDAO = new ReportesDAO();
                    ResultSQLDate fecIni = reportesDAO.getFirstDateofPeriodo(periodo.getMes(), periodo.getAnio(), idLInea);
                    ResultSQLDate fecTer = reportesDAO.getLastDateofPeriodo(periodo.getMes(), periodo.getAnio(), idLInea);
                    
                    if(fecIni != null && fecTer != null){
                        Date fechaInicio = sumarFechasDias(fecIni.getResult(), 2);
                        Date fechaTermino = sumarFechasDias(fecTer.getResult(), 2);
                        
                        data.setReporteDisponibilidad(ReportesOEE.getReporteDisponibilidad(fechaInicio, fechaTermino, periodo, linea));
                        data.setDatosProduccion(ReportesOEE.getDatosProduccion(fechaInicio, fechaTermino, periodo, linea));
                        data.setReporteOEE(ReportesOEE.getReporteOEE(fechaInicio, fechaTermino, periodo, linea));
                        output.setData(data);
                        response.setMessage(MSG_SUCESS);
                        response.setSucessfull(true);
                    }else{
                        response.setSucessfull(false);
                        response.setMessage("El Periodo no cuenta con datos para graficar");
                    }
                }else{
                    response.setSucessfull(false);
                    response.setMessage(MSG_NOEXIS);
                }
            }else{
                response.setMessage(MSG_LOGOUT);
                response.setSucessfull(false);
            }
        }catch (Exception ex){
            response.setSucessfull(false);
            response.setMessage(MSG_ERROR + ex.getMessage());
        }
        output.setResponse(response);
        return output;
    }
    
    public OutputJson getReporteDiarioProduccion(HttpServletRequest request){
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        
        try{
            int idGpoLinea = Integer.valueOf(request.getParameter("id_gpo_linea"));
            int idPeriodo = Integer.valueOf(request.getParameter("id_periodo"));
            UserDTO sesion = autenticacion.isValidToken(request);
            if(sesion != null){
                PeriodosDAO periodosDAO = new PeriodosDAO();
                LineasDAO lineasDAO = new LineasDAO();
                
                List<LineasDTO> listLineas = lineasDAO.getLineasByGpoLinea(idGpoLinea);
                PeriodosDTO periodo = periodosDAO.getPeriodoById(idPeriodo,listLineas.get(0).getId_linea());
                if(periodo != null){
                    ReportesResponseJson data = new ReportesResponseJson();
                    List<HashMap> listReporte = new ArrayList<>();
                    Date fechaI = getDateFirstDay(periodo.getAnio(), periodo.getMes());
                    Date fechaT = getDateLastDay(periodo.getAnio(), periodo.getMes());
                    
                    if(idGpoLinea==2){
                        listReporte = ReportesOEE.getResporteProduccionDiariaBuhler(fechaI, fechaT, idGpoLinea, periodo);
                    }else{
                        listReporte = ReportesOEE.getResporteProduccionDiariaAmut(fechaI, fechaT, idGpoLinea, periodo);
                    }
                    
                    data.setReporteDiario(listReporte);
                    output.setData(data);
                    response.setMessage(MSG_SUCESS);
                    response.setSucessfull(true);
                }else{
                    response.setMessage(MSG_NOEXIS);
                    response.setSucessfull(false);
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
    
    public OutputJson getReportDailyPerformance(HttpServletRequest request){
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        
        try{
            int idPeriodo = Integer.valueOf(request.getParameter("id_periodo"));
            int idGpoLinea = Integer.valueOf(request.getParameter("id_gpo_linea"));
            UserDTO sesion = autenticacion.isValidToken(request);
            if(sesion != null){
                PeriodosDAO periodosDAO = new PeriodosDAO();
                PeriodosDTO periodo = periodosDAO.getPeriodoById(idPeriodo);
                if(periodo != null){
                    Date fechaI = getDateFirstDay(periodo.getAnio(), periodo.getMes());
                    Date fechaT = getDateLastDay(periodo.getAnio(), periodo.getMes());
                    
                    ReportesResponseJson data = new ReportesResponseJson();
                    data.setReporteDailyPerformance(ReportesOEE.getReportDailyPerformance(fechaI, fechaT, idGpoLinea, idPeriodo));
                    output.setData(data);
                    response.setSucessfull(true);
                    response.setMessage(MSG_SUCESS);
                }else{
                    response.setSucessfull(false);
                    response.setMessage(MSG_NOEXIS);
                }
            }else{
                response.setSucessfull(false);
                response.setMessage(MSG_LOGOUT);
            }
        }catch(Exception ex){
            response.setSucessfull(false);
            response.setMessage(MSG_ERROR + ex.getMessage());
        }
        output.setResponse(response);
        return output;
    }
    
    public OutputJson getReporteJUCODI(HttpServletRequest request){
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        
        try{
            Date dia = convertStringToSql(request.getParameter("dia"));
            int idGpoLinea = Integer.valueOf(request.getParameter("id_gpo_linea"));
            UserDTO sesion = autenticacion.isValidToken(request);
            if(sesion != null){
                ReportesResponseJson data = new ReportesResponseJson();
                ReportesDAO reportesDAO = new ReportesDAO();
                List<ReporteDiario> listData = reportesDAO.getJUCODIproduccion(dia, idGpoLinea);
                List<ReporteDiario> dataParo = reportesDAO.getJUCODIparos(dia, idGpoLinea);
                    
                List<HashMap> listReporte = new ArrayList<>();
                List<HashMap> reporteTiempoParo = new ArrayList<>();
                HashMap<String, Object> encabezado = new HashMap<>();
                encabezado.put("padre", 1);
                encabezado.put("linea","Linea");
                encabezado.put("dia","Dia");
                encabezado.put("produccion","Produccion");
                encabezado.put("meta","Meta");
                encabezado.put("desempeno","% Desempeño");
                encabezado.put("icon", "");
                listReporte.add(encabezado);
                HashMap<String, Object> encabezados = new HashMap<>();
                encabezados.put("padre", 1);
                encabezados.put("linea","Linea");
                encabezados.put("dia","Dia");
                encabezados.put("tmpReal","TMP Real");
                encabezados.put("tmpMeta","TMP Meta");
                encabezados.put("desempeno","% Desempeño");
                encabezados.put("icon", "");
                reporteTiempoParo.add(encabezados);
                
                BigDecimal totalProduccion = BigDecimal.ZERO;
                BigDecimal totalMeta = BigDecimal.ZERO;
                for(ReporteDiario row:listData){
                    HashMap<String, Object> body = new HashMap<>();
                    body.put("padre", 0);
                    body.put("linea", row.getDescripcion());
                    body.put("dia", convertSqlToDay(sumarFechasDias(row.getDia(), 2)));
                    body.put("produccion", row.getProduccion());
                    totalProduccion=totalProduccion.add(row.getProduccion());
                    body.put("meta", row.getMeta());
                    totalMeta = totalMeta.add(row.getMeta());
                    int compare = row.getProduccion().compareTo(BigDecimal.ZERO);
                    int compara = row.getMeta().compareTo(BigDecimal.ZERO);
                    BigDecimal desempeno = BigDecimal.ZERO;
                    if(compare != 0 && compara != 0){
                        desempeno = (row.getProduccion().divide(row.getMeta(), RoundingMode.CEILING)).multiply(new BigDecimal(100));
                    } 
                    body.put("desempeno", desempeno);
                    body.put("icon", row.getProduccion().compareTo(row.getMeta()));
                    listReporte.add(body);
                }
                
                BigDecimal totalDesempeno = BigDecimal.ZERO;
                int compare = totalMeta.compareTo(BigDecimal.ZERO);
                int compara = totalProduccion.compareTo(BigDecimal.ZERO);
                if(compara != 0 && compare != 0){
                    totalDesempeno=(totalProduccion.divide(totalMeta, RoundingMode.CEILING)).multiply(new BigDecimal(100));
                }
                
                HashMap<String, Object> totales = new HashMap<>();
                totales.put("padre", 2);
                totales.put("linea","Total");
                totales.put("dia","");
                totales.put("produccion",totalProduccion);
                totales.put("meta",totalMeta);
                totales.put("desempeno",totalDesempeno);
                totales.put("icon", totalProduccion.compareTo(totalMeta));
                listReporte.add(totales);
                    
                BigDecimal totalTMPr = BigDecimal.ZERO;
                BigDecimal totalTMPm = BigDecimal.ZERO;
                for(ReporteDiario row:dataParo){
                    totalTMPr=totalTMPr.add(row.getTmp_real());
                    totalTMPm=totalTMPm.add(row.getTmp_meta());
                    compare = row.getTmp_real().compareTo(BigDecimal.ZERO);
                    compara = row.getTmp_meta().compareTo(BigDecimal.ZERO);
                    BigDecimal desempeno = BigDecimal.ZERO;
                    if(compare != 0 && compara != 0){
                        desempeno = row.getTmp_real().divide(row.getTmp_meta(), RoundingMode.CEILING);
                        desempeno = desempeno.multiply(new BigDecimal(100));
                    }
                    HashMap<String, Object> body = new HashMap<>();
                    body.put("padre", 0);
                    body.put("linea", row.getDescripcion());
                    body.put("dia", convertSqlToDay(sumarFechasDias(row.getDia(), 2)));
                    body.put("tmpReal", row.getTmp_real());
                    body.put("tmpMeta", row.getTmp_meta());
                    body.put("desempeno", desempeno);
                    body.put("icon", row.getTmp_meta().compareTo(row.getTmp_real()));
                    reporteTiempoParo.add(body);
                }
                
                BigDecimal desempenoTotal = BigDecimal.ZERO;
                compare = totalTMPr.compareTo(BigDecimal.ZERO);
                compara = totalTMPm.compareTo(BigDecimal.ZERO);
                if(compare != 0 && compara != 0){
                    desempenoTotal = totalTMPr.divide(totalTMPm, RoundingMode.CEILING);
                }
                desempenoTotal = desempenoTotal.multiply(new BigDecimal(100));
                HashMap<String, Object> total = new HashMap<>();
                total.put("padre", 2);
                total.put("linea","Total");
                total.put("dia","");
                total.put("tmpReal",totalTMPr);
                total.put("tmpMeta",totalTMPm);
                total.put("desempeno",desempenoTotal);
                total.put("icon", totalTMPm.compareTo(totalTMPr));
                reporteTiempoParo.add(total);
                
                data.setReporteMap(reporteTiempoParo);
                data.setReporteDesempeno(listReporte);
                output.setData(data);
                response.setSucessfull(true);
                response.setMessage(MSG_SUCESS);
                
            }else{
                response.setSucessfull(false);
                response.setMessage(MSG_LOGOUT);
            }
        }catch(Exception ex){
            response.setSucessfull(false);
            response.setMessage(MSG_ERROR + ex.getMessage());
        }
        output.setResponse(response);
        return output;
    }
    
    public OutputJson getReporteSubproductos(HttpServletRequest request){
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        
        try{
            int idPeriodo = Integer.valueOf(request.getParameter("id_periodo"));
            int idLinea = Integer.valueOf(request.getParameter("id_linea"));
            UserDTO sesion = autenticacion.isValidToken(request);
            if(sesion != null){
                PeriodosDAO periodosDAO = new PeriodosDAO();
                PeriodosDTO periodo = periodosDAO.getPeriodoById(idPeriodo);
                if(periodo != null){
                    Date fechaI = getDateFirstDay(periodo.getAnio(), periodo.getMes());
                    Date fechaT = getDateLastDay(periodo.getAnio(), periodo.getMes());
                    
                    ReportesResponseJson data = new ReportesResponseJson();
                    ReportesDAO reportesDAO = new ReportesDAO();
                    List<ReporteDTO> dataSubproductos = reportesDAO.getReporteSubproducto(fechaI, fechaT, idLinea);
                    
                    List<HashMap> reporteSubpro = new ArrayList<>();
                    HashMap<String, Object> head = new HashMap<>();
                    head.put("padre", 1);
                    head.put("dia",   "Dia");
                    head.put("turno", "Turno");
                    head.put("grupo", "Grupo");
                    head.put("valor", "Subproducto");
                    reporteSubpro.add(head);
                    
                    for(ReporteDTO row:dataSubproductos){
                        HashMap<String, Object> body = new HashMap<>();
                        body.put("padre", 0);
                        body.put("dia",   convertSqlToDay(sumarFechasDias(row.getDia(), 2)));
                        body.put("turno", row.getId_turno());
                        body.put("grupo", row.getValor_grupo());
                        body.put("valor", row.getSubproductos().setScale(3, RoundingMode.FLOOR));
                        reporteSubpro.add(body);
                    }
                    
                    data.setReporteMap(reporteSubpro);
                    output.setData(data);
                    response.setSucessfull(true);
                    response.setMessage(MSG_SUCESS);
                }else{
                    response.setSucessfull(false);
                    response.setMessage(MSG_NOEXIS);
                }
            }else{
                response.setSucessfull(false);
                response.setMessage(MSG_LOGOUT);
            }
        }catch(Exception ex){
            response.setSucessfull(false);
            response.setMessage(MSG_ERROR + ex.getMessage());
        }
        output.setResponse(response);
        return output;
    }
    
    public OutputJson getReporteVelocidadPromedio(HttpServletRequest request){
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        
        try{
            int idPeriodo = Integer.valueOf(request.getParameter("id_periodo"));
            int idGpoLinea = Integer.valueOf(request.getParameter("id_linea"));
            UserDTO sesion = autenticacion.isValidToken(request);
            if(sesion != null){
                PeriodosDAO periodosDAO = new PeriodosDAO();
                LineasDAO lineasDAO = new LineasDAO();
                PeriodosDTO periodo = periodosDAO.getPeriodoById(idPeriodo);
                LineasDTO linea = lineasDAO.getLineasDataById(idGpoLinea);
                
                if(periodo != null){
                    Date fechaI = getDateFirstDay(periodo.getAnio(), periodo.getMes());
                    Date fechaT = getDateLastDay(periodo.getAnio(), periodo.getMes());
                    
                    ReportesResponseJson data = new ReportesResponseJson();
                    List<List<HashMap>> datos = ReportesOEE.getReporteVelocidad(fechaI, fechaT, linea);
                    data.setGraficaMap(datos.get(0));
                    data.setReporteMap(datos.get(1));
                    output.setData(data);
                    response.setSucessfull(true);
                    response.setMessage(MSG_SUCESS);
                }else{
                    response.setSucessfull(false);
                    response.setMessage(MSG_NOEXIS);
                }
            }else{
                response.setSucessfull(false);
                response.setMessage(MSG_LOGOUT);
            }
        }catch(Exception ex){
            response.setSucessfull(false);
            response.setMessage(MSG_ERROR + ex.getMessage());
        }
        output.setResponse(response);
        return output;
    }
    
    public OutputJson getReportePerformance(HttpServletRequest request){
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        
        try{
            int idLinea = Integer.valueOf(request.getParameter("id_linea"));
            String report = request.getParameter("report");
            UserDTO sesion = autenticacion.isValidToken(request);
            if(sesion != null){
                ReportesResponseJson data = new ReportesResponseJson();
                PeriodosDAO periodosDAO = new PeriodosDAO();
                LineasDAO lineasDAO = new LineasDAO();
                LineasDTO linea = lineasDAO.getLineasDataById(idLinea);
                
                if(report.equals("byMonths")){
                    int anio = Integer.valueOf(request.getParameter("anio"));
                    List<PeriodosDTO> periodos = periodosDAO.getPeriodosByAnio(anio);
                    if(!periodos.isEmpty()){
                        List<List<HashMap>> datos = ReportesOEE.getReportePlanVsRealAnual(anio, linea);
                        data.setGraficaMap(datos.get(0));
                        data.setReporteMap(datos.get(1));
                        output.setData(data);
                        response.setSucessfull(true);
                        response.setMessage(MSG_SUCESS);
                    }else{
                        response.setSucessfull(false);
                        response.setMessage(MSG_NODATA);
                    }
                }else{
                    int idPeriodo = Integer.valueOf(request.getParameter("id_periodo"));
                    PeriodosDTO periodo = periodosDAO.getPeriodoById(idPeriodo);
                    if(periodo != null){
                        List<List<HashMap>> datos = ReportesOEE.getReportePlanVsRealMensual(report, periodo, linea);
                        data.setGraficaMap(datos.get(0));
                        data.setReporteMap(datos.get(1));
                        output.setData(data);
                        response.setSucessfull(true);
                        response.setMessage(MSG_SUCESS);
                    }else{
                        response.setSucessfull(false);
                        response.setMessage(MSG_NODATA);
                    }
                }
            }else{
                response.setSucessfull(false);
                response.setMessage(MSG_LOGOUT);
            }
        }catch(Exception ex){
            response.setSucessfull(false);
            response.setMessage(MSG_ERROR + ex.getMessage());
        }
        output.setResponse(response);
        return output;
    }
    
    public OutputJson getReporteFallas(HttpServletRequest request){
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        
        try{
            int idPeriodo = Integer.valueOf(request.getParameter("id_periodo"));
            int idLinea = Integer.valueOf(request.getParameter("id_linea"));
            UserDTO sesion = autenticacion.isValidToken(request);
            
            if(sesion != null){
                PeriodosDAO periodosDAO = new PeriodosDAO();
                PeriodosDTO periodo = periodosDAO.getPeriodoById(idPeriodo);
                
                if(periodo != null){
                    ReportesResponseJson data = new ReportesResponseJson();
                    ReportesDAO reportesDAO = new ReportesDAO();
                    
                    Date fechaI = getDateFirstDay(periodo.getAnio(), periodo.getMes());
                    Date fechaT = getDateLastDay(periodo.getAnio(), periodo.getMes());
                    data.setListFallas(reportesDAO.getFallasByPeriodo(fechaI, fechaT, idLinea));
                    
                    for(FallasDTO falla:data.getListFallas()){
                        falla.setDia(sumarFechasDias(falla.getDia(), 2));
                        falla.setDiaString(convertSqlToDay(falla.getDia()));
                    }
                    
                    output.setData(data);
                    response.setMessage(MSG_SUCESS);
                    response.setSucessfull(true);
                }else{
                    response.setMessage(MSG_NOEXIS);
                    response.setSucessfull(false);
                }
            }else{
                response.setMessage(MSG_LOGOUT);
                response.setSucessfull(false);
            }
        } catch(Exception ex){
            response.setMessage(MSG_ERROR + ex.getMessage());
            response.setSucessfull(false);
        }
        
        output.setResponse(response);
        return output;
    }
}