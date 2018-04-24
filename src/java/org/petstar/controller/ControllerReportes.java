package org.petstar.controller;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.petstar.dao.CatalogosDAO;
import org.petstar.dao.LineasDAO;
import org.petstar.dao.RazonParoDAO;
import org.petstar.dto.CatalogosDTO;
import org.petstar.dto.RazonParoDTO;
import org.petstar.model.OutputJson;
import org.petstar.model.ReportesResponseJson;
import org.petstar.model.ResponseJson;
import static org.petstar.configurations.utils.convertSqlToDay;
import static org.petstar.configurations.utils.getTotalHoras;
import static org.petstar.configurations.utils.getPorcentajeParo;
import static org.petstar.configurations.utils.getDateLastDay;
import static org.petstar.configurations.utils.getDateFirstDay;
import static org.petstar.configurations.utils.sumarFechasDias;
import static org.petstar.configurations.utils.convertStringToSql;
import static org.petstar.configurations.utils.getUltimoDiaMes;
import org.petstar.dao.PeriodosDAO;
import org.petstar.dao.ReportesDAO;
import org.petstar.dto.Fuentes;
import org.petstar.dto.LineasDTO;
import org.petstar.dto.PeriodosDTO;
import org.petstar.dto.ReporteDTO;
import org.petstar.dto.ReporteDiario;
import org.petstar.dto.ResultBigDecimal;
import org.petstar.dto.UserDTO;

/**
 * @author Tech-Pro
 */
public class ControllerReportes {
    private static final String TABLE_FUENTES = "pet_cat_fuentes_paro";
    private static final String TABLE_GPOLINE = "pet_cat_gpo_linea";
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
                PeriodosDTO periodo = periodosDAO.getPeriodoById(idPeriodo, idLInea);
                if(periodo != null){
                    Date fechaInicio = getDateFirstDay(periodo.getAnio(), periodo.getMes());
                    Date FechaTermino = getDateLastDay(periodo.getAnio(), periodo.getMes());
                    
                    CatalogosDAO catalogosDAO = new CatalogosDAO();
                    RazonParoDAO razonParoDAO = new RazonParoDAO();
                    ReportesResponseJson data = new ReportesResponseJson();

                    List<CatalogosDTO> listFuentes = new ArrayList<>();
                    listFuentes = catalogosDAO.getCatalogosActive(TABLE_FUENTES);
                    List<HashMap> listOEEFallas = new ArrayList<>();
                    BigDecimal tiempoDisponible = getTotalHoras(fechaInicio, FechaTermino);
                    BigDecimal totalGeneral = new BigDecimal(BigInteger.ZERO);

                    for(CatalogosDTO fuente:listFuentes){
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("padre", 1);
                        map.put("fuente", fuente.getValor());
                        listOEEFallas.add(map);

                        List<RazonParoDTO> listRazones = new ArrayList<>();
                        listRazones = razonParoDAO.getFallasByOEE(fechaInicio, FechaTermino, idLInea, fuente.getId());
                        BigDecimal totalParcial = new BigDecimal(BigInteger.ZERO);

                        for(RazonParoDTO razon:listRazones){
                            HashMap<String, Object> raz = new HashMap<>();
                            raz.put("padre", 0);
                            raz.put("fuente", razon.getValor());
                            raz.put("hrs", razon.getSuma_tiempo_paro());
                            raz.put("porcentaje", getPorcentajeParo(
                                    razon.getSuma_tiempo_paro(), tiempoDisponible));
                            listOEEFallas.add(raz);

                            totalParcial = totalParcial.add(razon.getSuma_tiempo_paro());
                            totalGeneral = totalGeneral.add(razon.getSuma_tiempo_paro());
                            map.put("hrs", totalParcial);
                            map.put("porcentaje", getPorcentajeParo(totalParcial, tiempoDisponible));
                        }
                    }

                    HashMap<String, Object> mapa = new HashMap<>();
                    mapa.put("padre", 2);
                    mapa.put("fuente", "Total");
                    mapa.put("hrs", totalGeneral);
                    mapa.put("porcentaje", getPorcentajeParo(totalGeneral, tiempoDisponible));
                    listOEEFallas.add(mapa);
                    data.setListaOEEFallas(listOEEFallas);

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
                CatalogosDAO catalogosDAO = new CatalogosDAO();
                ReportesResponseJson data = new ReportesResponseJson();
                
                data.setListLineas(lineasDAO.getLineasActive());
                data.setListPeriodos(periodosDAO.getAllPeriodos());
                data.setListGposLineas(catalogosDAO.getCatalogosActive(TABLE_GPOLINE));
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
                PeriodosDTO periodo = periodosDAO.getPeriodoById(idPeriodo, idLInea);
                if(periodo != null){
                    Date fechaInicio = getDateFirstDay(periodo.getAnio(), periodo.getMes());
                    Date fechaTermino = getDateLastDay(periodo.getAnio(), periodo.getMes());
                    BigDecimal tiempoDisponibleTotal = getTotalHoras(fechaInicio, fechaTermino);
                    
                    ReportesDAO reportesDAO = new ReportesDAO();
                
                    List<HashMap> reporte = new ArrayList<>();
                    HashMap<String, Object> map0 = new HashMap<>();
                    map0.put("padre", 1);
                    map0.put("titulo", "Titulo");
                    map0.put("hrs", "Hrs.");
                    map0.put("porcentaje", "%");
                    reporte.add(map0);
                    HashMap<String, Object> map1 = new HashMap<>();
                    map1.put("padre", 0);
                    map1.put("titulo", "Tiempo Disponible Total");
                    map1.put("hrs", tiempoDisponibleTotal);
                    map1.put("porcentaje", 100);
                    reporte.add(map1);
                    HashMap<String, Object> map2 = new HashMap<>();
                    map2.put("padre", 0);
                    map2.put("titulo", "No Ventas");
                    map2.put("hrs", 0);
                    map2.put("porcentaje", 0);
                    reporte.add(map2);
                    HashMap<String, Object> map3 = new HashMap<>();
                    map3.put("padre", 0);
                    map3.put("titulo", "Tiempo Disponible");
                    map3.put("hrs", tiempoDisponibleTotal);
                    map3.put("porcentaje", 100);
                    reporte.add(map3);
                    BigDecimal totalHoraParo = new BigDecimal(BigInteger.ZERO);
                    BigDecimal desempenoEfec = new BigDecimal(BigInteger.ZERO);
                    List<Fuentes> listFuentes = reportesDAO.getFuentes(idLInea, fechaInicio, fechaTermino);
                    ResultBigDecimal prodBuena = reportesDAO.getProduccionBuena(idLInea, fechaInicio, fechaTermino);
                    ResultBigDecimal subProduc = reportesDAO.getSumaSubProductos(idLInea, fechaInicio, fechaTermino);
                    for(Fuentes fuente:listFuentes){
                        HashMap<String, Object> map4 = new HashMap<>();
                        map4.put("padre", 0);
                        map4.put("titulo", fuente.getValor());
                        map4.put("hrs", fuente.getHrs());
                        map4.put("porcentaje", getPorcentajeParo(fuente.getHrs(), tiempoDisponibleTotal));
                        reporte.add(map4);
                        desempenoEfec = desempenoEfec.add(fuente.getHrs());
                        if(fuente.getId() == 1 || fuente.getId() == 2){
                            totalHoraParo = totalHoraParo.add(fuente.getHrs());
                        }
                    }
                    HashMap<String, Object> map5 = new HashMap<>();
                    map5.put("padre", 2);
                    map5.put("titulo", "Desempeño Efectivo Total de Equipos");
                    map5.put("hrs", desempenoEfec);
                    map5.put("porcentaje", getPorcentajeParo(desempenoEfec, tiempoDisponibleTotal));
                    reporte.add(map5);
                    HashMap<String, Object> map6 = new HashMap<>();
                    map6.put("padre", 2);
                    map6.put("titulo", "Total Hora de Paro");
                    map6.put("hrs", totalHoraParo);
                    map6.put("porcentaje", getPorcentajeParo(totalHoraParo, tiempoDisponibleTotal));
                    reporte.add(map6);

                    List<HashMap> datosProduccion = new ArrayList<>();
                    HashMap<String, Object> map12 = new HashMap<>();
                    map12.put("padre", 1);
                    map12.put("titulo", "Datos de Producción");
                    map12.put("hrs", "");
                    map12.put("porcentaje", "");
                    datosProduccion.add(map12);
                    HashMap<String, Object> map7 = new HashMap<>();
                    map7.put("padre", 0);
                    map7.put("titulo", "Velocidad Ideal (Hora)");
                    map7.put("hrs", 3.5);
                    map7.put("porcentaje", "");
                    datosProduccion.add(map7);
                    HashMap<String, Object> map8 = new HashMap<>();
                    map8.put("padre", 0);
                    map8.put("titulo", "Capacidad Productiva (Turno)");
                    map8.put("hrs", (3.5 * 8));
                    map8.put("porcentaje", "");
                    datosProduccion.add(map8);
                    datosProduccion.add(map3);
                    HashMap<String, Object> map9 = new HashMap<>();
                    BigDecimal tiempoOperacion = tiempoDisponibleTotal.subtract(totalHoraParo);
                    map9.put("padre", 0);
                    map9.put("titulo", "Tiempo de Operación");
                    map9.put("hrs", tiempoOperacion);
                    map9.put("porcentaje", "");
                    datosProduccion.add(map9);
                    HashMap<String, Object> map10 = new HashMap<>();
                    map10.put("padre", 0);
                    map10.put("titulo", "Producción Buena");
                    map10.put("hrs", prodBuena.getResult());
                    map10.put("porcentaje", "");
                    datosProduccion.add(map10);
                    HashMap<String, Object> map11 = new HashMap<>();
                    BigDecimal produccionTotal = prodBuena.getResult().add(subProduc.getResult());
                    map11.put("padre", 0);
                    map11.put("titulo", "Producción Total");
                    map11.put("hrs", produccionTotal);
                    map11.put("porcentaje", "");
                    datosProduccion.add(map11);

                    List<HashMap> reporteOEE = new ArrayList<>();
                    HashMap<String, Object> map13 = new HashMap<>();
                    map13.put("padre", 1);
                    map13.put("titulo", "OEE");
                    map13.put("hrs", "");
                    map13.put("porcentaje", "");
                    reporteOEE.add(map13);
                    HashMap<String, Object> map14 = new HashMap<>();
                    BigDecimal pDisponibilidad = getPorcentajeParo(tiempoOperacion, tiempoDisponibleTotal);
                    map14.put("padre", 0);
                    map14.put("titulo", "Disponibilidad");
                    map14.put("hrs", tiempoOperacion);
                    map14.put("porcentaje", pDisponibilidad);
                    map14.put("meta", periodo.getDisponibilidad());
                    reporteOEE.add(map14);
                    HashMap<String, Object> map15 = new HashMap<>();
                    BigDecimal utilizacion = prodBuena.getResult().divide(tiempoOperacion, RoundingMode.CEILING);
                    BigDecimal calculo = prodBuena.getResult().divide(tiempoOperacion, RoundingMode.CEILING);
                    BigDecimal pUtilizacion = calculo.divide(new BigDecimal(3.5), RoundingMode.CEILING);
                    map15.put("padre", 0);
                    map15.put("titulo", "Utilización");
                    map15.put("hrs", utilizacion);
                    map15.put("porcentaje", pUtilizacion);
                    map15.put("meta", periodo.getUtilizacion());
                    reporteOEE.add(map15);
                    HashMap<String, Object> map16 = new HashMap<>();
                    BigDecimal pCalidad = BigDecimal.ZERO;
                    int resultado = BigDecimal.ZERO.compareTo(prodBuena.getResult());
                    if(resultado == -1){
                        pCalidad = prodBuena.getResult().divide(produccionTotal,RoundingMode.CEILING);
                    } 
                   
                    map16.put("padre", 0);
                    map16.put("titulo", "Calidad");
                    map16.put("hrs", "");
                    map16.put("porcentaje", pCalidad);
                    map16.put("meta", periodo.getCalidad());
                    reporteOEE.add(map16);
                    HashMap<String, Object> map17 = new HashMap<>();
                    BigDecimal oee = pDisponibilidad.multiply(pUtilizacion).multiply(pCalidad);
                    map17.put("padre", 0);
                    map17.put("titulo", "OEE");
                    map17.put("hrs", "");
                    map17.put("porcentaje", oee);
                    map17.put("meta", periodo.getOee());
                    reporteOEE.add(map17);
                    HashMap<String, Object> map18 = new HashMap<>();
                    BigDecimal pTEEP = getPorcentajeParo(desempenoEfec, tiempoDisponibleTotal);
                    map18.put("padre", 0);
                    map18.put("titulo", "TEEP (hrs)");
                    map18.put("hrs", desempenoEfec);
                    map18.put("porcentaje", pTEEP);
                    reporteOEE.add(map18);
                    HashMap<String, Object> map19 = new HashMap<>();
                    map19.put("padre", 0);
                    map19.put("titulo", "TEEP (hrs)");
                    map19.put("hrs", tiempoDisponibleTotal.subtract(desempenoEfec));
                    map19.put("porcentaje", BigDecimal.ONE.subtract(pTEEP));
                    reporteOEE.add(map19);

                    data.setReporteDisponibilidad(reporte);
                    data.setDatosProduccion(datosProduccion);
                    data.setReporteOEE(reporteOEE);
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
    
    public OutputJson getReporteDiarioProduccion(HttpServletRequest request){
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        
        try{
            int idGpoLinea = Integer.valueOf(request.getParameter("id_gpo_linea"));
            int idPeriodo = Integer.valueOf(request.getParameter("id_periodo"));
            UserDTO sesion = autenticacion.isValidToken(request);
            if(sesion != null){
                ReportesDAO reportesDAO = new ReportesDAO();
                PeriodosDAO periodosDAO = new PeriodosDAO();
                LineasDAO lineasDAO = new LineasDAO();
                
                List<LineasDTO> listLineas = lineasDAO.getLineasByGpoLinea(idGpoLinea);
                PeriodosDTO periodo = periodosDAO.getPeriodoById(idPeriodo,listLineas.get(0).getId_linea());
                if(periodo != null){
                    
                    Date fechaI = getDateFirstDay(periodo.getAnio(), periodo.getMes());
                    Date fechaT = getDateLastDay(periodo.getAnio(), periodo.getMes());
                    
                    List<List<ResultBigDecimal>> listaMolidos= new ArrayList<>();
                    List<ResultBigDecimal> lisTotalMolidos = new ArrayList<>();
                    List<ReporteDiario> listData = reportesDAO.getReporteDiario(fechaI, fechaT, idGpoLinea);
                    ReportesResponseJson data = new ReportesResponseJson();
                    List<HashMap> listReporte = new ArrayList<>();
                    
                    HashMap<String, Object> encabezado = new HashMap<>();
                    encabezado.put("padre", 1);
                    encabezado.put("dia","Dia");
                    for(int y=0; y<listLineas.size(); y++){
                        encabezado.put("molido"+(y+1),"Molido "+(y+1));
                        encabezado.put("hojuela"+(y+1),"Hojuela "+(y+1));
                    }
                    encabezado.put("totalMolido","Total molido");
                    encabezado.put("acumulado","Acumulado");
                    encabezado.put("metaMolido","Plan Molido");
                    encabezado.put("difMolido","Diferencia Molido");
                    encabezado.put("eficiencia","Eficiencia/dia");
                    encabezado.put("vsMetaM","+/- vs meta M");
                    encabezado.put("eficTeorica","Efic teorica");
                    encabezado.put("totalHoj","Total Hojuela");
                    encabezado.put("acumHoju","Acum. Hojuela");
                    encabezado.put("planHoju","Plan Hojuela");
                    encabezado.put("difeHoju","Diferencia Hoj");
                    encabezado.put("eficiDia","Eficiencia/dia H");
                    encabezado.put("vsMetaH","+/- vs meta H");
                    listReporte.add(encabezado);
                    
                    for (int y=0; y<listLineas.size(); y++) {
                        List<ResultBigDecimal> molido = new ArrayList<>();
                        molido = reportesDAO.getMolidoByLinea(fechaI, fechaT, listLineas.get(y).getId_linea());
                        ResultBigDecimal result = reportesDAO.getTotalMolidoByLinea(fechaI, fechaT, listLineas.get(y).getId_linea());
                        listaMolidos.add(molido);
                        lisTotalMolidos.add(result);
                    }
                    List<BigDecimal> suma = null;
                    BigDecimal totalTotalMolido = BigDecimal.ZERO;
                    BigDecimal totalTotalHojuela= BigDecimal.ZERO;
                    BigDecimal totalTotalPlanHojuela = BigDecimal.ZERO;
                    BigDecimal totalTotalPlanMolido = BigDecimal.ZERO;
                    
                    BigDecimal acumulado = BigDecimal.ZERO;
                    BigDecimal acumHojuela = BigDecimal.ZERO;
                    BigDecimal planMolido = BigDecimal.ZERO;
                    
                    for(int i=0; i<listData.size(); i++){
                        planMolido = planMolido.add(listData.get(i).getPlan_molido());
                        HashMap<String, Object> row = new HashMap<>();
                        row.put("padre",	0);
                        row.put("dia",		convertSqlToDay(sumarFechasDias(listData.get(i).getDia(),2)));
                        BigDecimal totalMolido = BigDecimal.ZERO;
                        BigDecimal totalHojuela = BigDecimal.ZERO;
                        for(int y=0; y<listaMolidos.size(); y++){
                            BigDecimal molido = listaMolidos.get(y).get(i).getResult();
                            row.put("molido"+(y+1),	molido);
                            BigDecimal hojuela = molido.multiply(periodo.getEficiencia_teorica());
                            int compare = hojuela.compareTo(BigDecimal.ZERO);
                            if(compare == 0){
                                hojuela = BigDecimal.ZERO;
                            }else{
                                hojuela = hojuela.divide(new BigDecimal(100));
                            }
                            row.put("hojuela"+(y+1), hojuela);
                            totalMolido = totalMolido.add(molido);
                            totalHojuela = totalHojuela.add(hojuela);
                            //suma.add(molido);
                        }
                        row.put("totalMolido",	totalMolido);
                        totalTotalMolido = totalTotalMolido.add(totalMolido);
                        acumulado = acumulado.add(totalMolido);
                        row.put("acumulado",	acumulado);
                        row.put("metaMolido",	planMolido);
                        totalTotalPlanMolido = planMolido;
                        BigDecimal diferenciaMolido = acumulado.subtract(planMolido);
                        row.put("difMolido",	diferenciaMolido);
                        int compare = planMolido.compareTo(BigDecimal.ZERO);
                        BigDecimal eficiencia = BigDecimal.ZERO;
                        if(compare != 0){eficiencia = acumulado.divide(planMolido, RoundingMode.CEILING);}
                        eficiencia = eficiencia.multiply(new BigDecimal(100));
                        row.put("eficiencia",	eficiencia);
                        row.put("vsMetaM",	eficiencia.subtract(new BigDecimal(100)));
                        row.put("eficTeorica",	periodo.getEficiencia_teorica());
                        row.put("totalHoj",	totalHojuela);
                        totalTotalHojuela = totalTotalHojuela.add(totalHojuela);
                        acumHojuela = acumHojuela.add(totalHojuela);
                        row.put("acumHoju",	acumHojuela);
                        BigDecimal planHojuela = planMolido.multiply(periodo.getEficiencia_teorica());
                        compare = planHojuela.compareTo(BigDecimal.ZERO);
                        if(compare == 0){ planHojuela= BigDecimal.ZERO;}else{planHojuela= planHojuela.divide(new BigDecimal(100));}
                        row.put("planHoju",	planHojuela);
                        row.put("difeHoju",     acumHojuela.subtract(planHojuela));
                        BigDecimal eficienciaDia = BigDecimal.ZERO;
                        compare = planHojuela.compareTo(BigDecimal.ZERO);
                        if(compare != 0){eficienciaDia = acumHojuela.divide(planHojuela, RoundingMode.CEILING);}
                        totalTotalPlanHojuela = planHojuela;
                        eficienciaDia = eficienciaDia.multiply(new BigDecimal(100));
                        row.put("eficiDia",	eficienciaDia);
                        row.put("vsMetaH",	eficienciaDia.subtract(new BigDecimal(100)));
                        listReporte.add(row);   
                    }
                    
                    HashMap<String, Object> totales = new HashMap<>();
                    totales.put("padre", 2);
                    totales.put("dia","Total");
                    
                    for(int y=0; y<listLineas.size(); y++){
                        BigDecimal calculo = lisTotalMolidos.get(y).getResult().multiply(periodo.getEficiencia_teorica());
                        BigDecimal totalHojuela = calculo.divide(new BigDecimal(100), RoundingMode.CEILING);
                        totales.put("molido"+(y+1),lisTotalMolidos.get(y).getResult());
                        totales.put("hojuela"+(y+1),totalHojuela);
                        //totalTotalHojuela = totalTotalHojuela.add(totalHojuela);
                    }
                    totales.put("totalMolido",totalTotalMolido);
                    totales.put("acumulado",acumulado);
                    totales.put("metaMolido",totalTotalPlanMolido);
                    BigDecimal totalTotalDifMolido = acumulado.subtract(totalTotalPlanMolido);
                    totales.put("difMolido",totalTotalDifMolido);
                    int compare = totalTotalPlanMolido.compareTo(BigDecimal.ZERO);
                    BigDecimal TotalEficienciaDia = BigDecimal.ZERO;
                    if(compare != 0){ TotalEficienciaDia = acumulado.divide(totalTotalPlanMolido,RoundingMode.CEILING);}
                    totales.put("eficiencia",TotalEficienciaDia);
                    totales.put("vsMetaM",TotalEficienciaDia.subtract(BigDecimal.ONE));
                    totales.put("eficTeorica",periodo.getEficiencia_teorica());
                    totales.put("totalHoj",totalTotalHojuela);
                    totales.put("acumHoju",acumHojuela);
                    totales.put("planHoju",totalTotalPlanHojuela);
                    BigDecimal totalTotalDifHojuela = acumHojuela.subtract(totalTotalPlanHojuela);
                    totales.put("difeHoju",totalTotalDifHojuela);
                    compare = totalTotalPlanHojuela.compareTo(BigDecimal.ZERO);
                    BigDecimal totalEficienciaHojuela = BigDecimal.ZERO;
                    if(compare != 0){ totalEficienciaHojuela = acumHojuela.divide(totalTotalPlanHojuela, RoundingMode.CEILING); }
                    totales.put("eficiDia",totalEficienciaHojuela);
                    totales.put("vsMetaH",totalEficienciaHojuela.subtract(BigDecimal.ONE));
                    listReporte.add(totales);
                    
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
                    ReportesDAO reportesDAO = new ReportesDAO();
                    LineasDAO lineasDAO = new LineasDAO();
                    
                    List<List<HashMap>> listReport = new ArrayList<>();
                    
                    HashMap<String, Object> encabezado = new HashMap<>();
                    encabezado.put("padre", 1);
                    //encabezado.put("linea","Linea");
                    encabezado.put("dia","Dia");
                    encabezado.put("a","A");
                    encabezado.put("b","B");
                    encabezado.put("c","C");
                    encabezado.put("d","D");
                    encabezado.put("meta1","Meta 1ro");
                    encabezado.put("meta2","Meta 2do");
                    encabezado.put("meta3","Meta dia");
                    
                    List<LineasDTO> listLineas = lineasDAO.getLineasByGpoLinea(idGpoLinea);
                    for(LineasDTO linea:listLineas){
                        
                        HashMap<String, Object> dataLinea = new HashMap<>();
                        dataLinea.put("Linea", linea.getValor());
                        List<ReporteDiario> listData = reportesDAO.getDailyPerformance(fechaI, fechaT, linea.getId_linea());
                        List<HashMap> listReporteLinea = new ArrayList<>();
                        listReporteLinea.add(dataLinea);
                        listReporteLinea.add(encabezado);
                        
                        for(ReporteDiario row:listData){
                            HashMap<String, Object> body = new HashMap<>();
                            body.put("padre", 0);
                            //body.put("linea", row.getDescripcion());
                            body.put("dia",convertSqlToDay(sumarFechasDias(row.getDia(), 2)));
                            body.put("a",row.getA());
                            body.put("b",row.getB());
                            body.put("c",row.getC());
                            body.put("d",row.getD());
                            body.put("meta1",row.getMeta_uno());
                            body.put("meta2",row.getMeta_dos());
                            body.put("meta3",row.getMeta_dia());
                            listReporteLinea.add(body);
                        }
                        listReport.add(listReporteLinea);
                    }
                    data.setReporteDailyPerformance(listReport);
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
                    BigDecimal desempeno = BigDecimal.ZERO;
                    if(compare == 1){
                        desempeno = (row.getProduccion().divide(row.getMeta(), RoundingMode.CEILING)).multiply(new BigDecimal(100));
                    } 
                    body.put("desempeno", desempeno);
                    body.put("icon", desempeno.compareTo(new BigDecimal(100)));
                    listReporte.add(body);
                }
                
                BigDecimal totalDesempeno = (totalProduccion.divide(totalMeta, RoundingMode.CEILING)).multiply(new BigDecimal(100));
                HashMap<String, Object> totales = new HashMap<>();
                totales.put("padre", 2);
                totales.put("linea","Total");
                totales.put("dia","");
                totales.put("produccion",totalProduccion);
                totales.put("meta",totalMeta);
                totales.put("desempeno",totalDesempeno);
                totales.put("icon", totalDesempeno.compareTo(new BigDecimal(100)));
                listReporte.add(totales);
                    
                BigDecimal totalTMPr = BigDecimal.ZERO;
                BigDecimal totalTMPm = BigDecimal.ZERO;
                for(ReporteDiario row:dataParo){
                    totalTMPr=totalTMPr.add(row.getTmp_real());
                    totalTMPm=totalTMPm.add(row.getTmp_meta());
                    int compare = row.getTmp_real().compareTo(BigDecimal.ZERO);
                    BigDecimal desempeno = BigDecimal.ZERO;
                    if(compare == 1){
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
                    body.put("icon", desempeno.compareTo(new BigDecimal(100)));
                    reporteTiempoParo.add(body);
                }
                
                BigDecimal desempenoTotal = BigDecimal.ZERO;
                int compare = totalTMPr.compareTo(BigDecimal.ZERO);
                if(compare == 1){
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
                total.put("icon", desempenoTotal.compareTo(new BigDecimal(100)));
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
                PeriodosDTO periodo = periodosDAO.getPeriodoById(idPeriodo);
                if(periodo != null){
                    Date fechaI = getDateFirstDay(periodo.getAnio(), periodo.getMes());
                    Date fechaT = getDateLastDay(periodo.getAnio(), periodo.getMes());
                    
                    ReportesResponseJson data = new ReportesResponseJson();
                    ReportesDAO reportesDAO = new ReportesDAO();
                    List<ReporteDTO> dataVelPromedio = reportesDAO.getReporteVelPromedio(fechaI, fechaT, idGpoLinea);
                    
                    List<HashMap> reporteVelPromedio = new ArrayList<>();
                    HashMap<String, Object> head = new HashMap<>();
                    head.put("padre", 1);
                    head.put("dia",   "Dia");
                    head.put("turno", "Turno");
                    head.put("grupo", "Grupo");
                    head.put("valor", "Velocidad Promedio");
                    reporteVelPromedio.add(head);
                    
                    for(ReporteDTO row:dataVelPromedio){
                        HashMap<String, Object> body = new HashMap<>();
                        body.put("padre", 0);
                        body.put("dia",   convertSqlToDay(sumarFechasDias(row.getDia(), 2)));
                        body.put("turno", row.getId_turno());
                        body.put("grupo", row.getValor_grupo());
                        body.put("valor", row.getVelocidad_promedio().setScale(3,RoundingMode.FLOOR));
                        reporteVelPromedio.add(body);
                    }
                    
                    data.setReporteMap(reporteVelPromedio);
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
            int idPeriodo = Integer.valueOf(request.getParameter("id_periodo"));
            int idLinea = Integer.valueOf(request.getParameter("id_linea"));
            String report = request.getParameter("report");
            UserDTO sesion = autenticacion.isValidToken(request);
            if(sesion != null){
                PeriodosDAO periodosDAO = new PeriodosDAO();
                PeriodosDTO periodo = periodosDAO.getPeriodoById(idPeriodo);
                if(periodo != null){                    
                    ReportesResponseJson data = new ReportesResponseJson();
                    ReportesDAO reportesDAO = new ReportesDAO();
                    List<ReporteDiario> dataRows = null;
                    
                    switch(report){
                        case"byDays":
                            Date fechaI = getDateFirstDay(periodo.getAnio(), periodo.getMes());
                            Date fechaT = getDateLastDay(periodo.getAnio(), periodo.getMes());
                            dataRows = reportesDAO.getDailyPerformance(fechaI, fechaT, idLinea);
                            break;
                        case"byWeeks":
                            dataRows = reportesDAO.getReportePerformanceByWeek(periodo.getMes(), periodo.getAnio(), idLinea);
                            break;
                        case"byMonths":
                            int lastDayFeb = getUltimoDiaMes(periodo.getAnio(), 2);
                            dataRows = reportesDAO.getReportePerformanceByMonth(periodo.getAnio(), idLinea,lastDayFeb);
                            break;
                    }
                    
                    List<HashMap> reportePerformance = new ArrayList<>();
                    HashMap<String, Object> head = new HashMap<>();
                    head.put("padre", 1);
                    head.put("periodo","Periodo");
                    head.put("a","A");
                    head.put("b","B");
                    head.put("c","C");
                    head.put("d","D");
                    head.put("meta1","Meta 1");
                    head.put("meta2","Meta 2");
                    head.put("meta3","Meta 3");
                    reportePerformance.add(head);
                    
                    if(dataRows != null){
                        for(ReporteDiario row:dataRows){
                            HashMap<String, Object> body = new HashMap<>();
                            if(report.equals("byDays")){
                                body.put("periodo",convertSqlToDay(sumarFechasDias(row.getDia(), 2)));
                            }else{
                                body.put("periodo",row.getPeriodo());
                            }
                            body.put("a",    row.getA().setScale(3, RoundingMode.FLOOR));
                            body.put("b",    row.getB().setScale(3, RoundingMode.FLOOR));
                            body.put("c",    row.getC().setScale(3, RoundingMode.FLOOR));
                            body.put("d",    row.getD().setScale(3, RoundingMode.FLOOR));
                            body.put("meta1",row.getMeta_uno().setScale(3, RoundingMode.FLOOR));
                            body.put("meta2",row.getMeta_dos().setScale(3, RoundingMode.FLOOR));
                            body.put("meta3",row.getMeta_dia().setScale(3, RoundingMode.FLOOR));
                            body.put("padre",0);
                            reportePerformance.add(body);
                        }
                        
                        data.setReporteMap(reportePerformance);
                        output.setData(data);
                        response.setSucessfull(true);
                        response.setMessage(MSG_SUCESS);
                    }else{
                        response.setSucessfull(false);
                        response.setMessage(MSG_NODATA);
                    }
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
}