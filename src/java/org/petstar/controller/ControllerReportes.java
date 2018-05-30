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
import org.petstar.dto.FallasDTO;
import org.petstar.dto.Fuentes;
import org.petstar.dto.LineasDTO;
import org.petstar.dto.PeriodosDTO;
import org.petstar.dto.ReporteDTO;
import org.petstar.dto.ReporteDiario;
import org.petstar.dto.ResultBigDecimal;
import org.petstar.dto.ResultSQLDate;
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
                    ReportesDAO reportesDAO = new ReportesDAO();
                    Date fechaInicio = getDateFirstDay(periodo.getAnio(), periodo.getMes());
                    Date FechaTermino = getDateLastDay(periodo.getAnio(), periodo.getMes());
                    
                    CatalogosDAO catalogosDAO = new CatalogosDAO();
                    RazonParoDAO razonParoDAO = new RazonParoDAO();
                    ReportesResponseJson data = new ReportesResponseJson();

                    List<CatalogosDTO> listFuentes = catalogosDAO.getCatalogosActive(TABLE_FUENTES);
                    List<HashMap> listOEEFallas = new ArrayList<>();
                    BigDecimal tiempoDisponible = getTotalHoras(fechaInicio, FechaTermino);
                    BigDecimal totalGeneral = new BigDecimal(BigInteger.ZERO);
                    ResultBigDecimal subproductos = reportesDAO.getTotalSubProducto(fechaInicio, FechaTermino, idLInea);

                    for(CatalogosDTO fuente:listFuentes){
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("padre", 1);
                        map.put("fuente", fuente.getValor());
                        listOEEFallas.add(map);

                        List<RazonParoDTO> listRazones = razonParoDAO.getFallasByOEE(fechaInicio, FechaTermino, idLInea, fuente.getId());
                        BigDecimal totalParcial = new BigDecimal(BigInteger.ZERO);

                        for(RazonParoDTO razon:listRazones){
                            BigDecimal subproducto = BigDecimal.ZERO;
                            HashMap<String, Object> raz = new HashMap<>();
                            raz.put("padre", 0);
                            raz.put("fuente", razon.getValor());
                            if(razon.getValor().equals("Subproductos")){
                                if(subproductos.getResult().compareTo(BigDecimal.ZERO) != 0){
                                    subproducto = subproductos.getResult();
                                    subproducto = subproducto.divide(new BigDecimal(3.5), RoundingMode.CEILING);
                                }
                                raz.put("hrs", subproducto);
                                raz.put("porcentaje", getPorcentajeParo(subproducto, tiempoDisponible));
                            }else{
                                raz.put("hrs", razon.getSuma_tiempo_paro());
                                raz.put("porcentaje", getPorcentajeParo(
                                razon.getSuma_tiempo_paro(), tiempoDisponible));
                            }
                            listOEEFallas.add(raz);

                            totalParcial = totalParcial.add(razon.getSuma_tiempo_paro().add(subproducto));
                            totalGeneral = totalGeneral.add(razon.getSuma_tiempo_paro().add(subproducto));
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
                    ReportesDAO reportesDAO = new ReportesDAO();
                    ResultSQLDate fecIni = reportesDAO.getFirstDateofPeriodo(periodo.getMes(), periodo.getAnio(), idLInea);
                    ResultSQLDate fecTer = reportesDAO.getLastDateofPeriodo(periodo.getMes(), periodo.getAnio(), idLInea);
                    
                    if(fecIni != null && fecTer != null){
                        Date fechaInicio = sumarFechasDias(fecIni.getResult(), 2);
                        Date fechaTermino = sumarFechasDias(fecTer.getResult(), 2);
                        BigDecimal tiempoDisponibleTotal = getTotalHoras(fechaInicio, fechaTermino);
                        ResultBigDecimal subproductos = reportesDAO.getTotalSubProducto(fechaInicio, fechaTermino, idLInea);
                        
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
                        map2.put("hrs", periodo.getNo_ventas());
                        map2.put("porcentaje", getPorcentajeParo(periodo.getNo_ventas(), tiempoDisponibleTotal));
                        reporte.add(map2);
                        BigDecimal tiempoDisponible = tiempoDisponibleTotal.subtract(periodo.getNo_ventas());
                        HashMap<String, Object> map3 = new HashMap<>();
                        map3.put("padre", 0);
                        map3.put("titulo", "Tiempo Disponible");
                        map3.put("hrs", tiempoDisponible);
                        map3.put("porcentaje", getPorcentajeParo(tiempoDisponible, tiempoDisponibleTotal));
                        reporte.add(map3);
                        BigDecimal totalHoraParo = new BigDecimal(BigInteger.ZERO);
                        BigDecimal desempenoEfec = new BigDecimal(BigInteger.ZERO);
                        List<Fuentes> listFuentes = reportesDAO.getFuentes(idLInea, fechaInicio, fechaTermino);
                        ResultBigDecimal prodBuena = reportesDAO.getProduccionBuena(idLInea, fechaInicio, fechaTermino);
                        ResultBigDecimal subProduc = reportesDAO.getSumaSubProductos(idLInea, fechaInicio, fechaTermino);
                        BigDecimal reduccionVelocidad = BigDecimal.ZERO;
                        for(Fuentes fuente:listFuentes){
                            HashMap<String, Object> map4 = new HashMap<>();
                            map4.put("padre", 0);
                            map4.put("titulo", fuente.getValor());
                            BigDecimal porCalidad = BigDecimal.ZERO;
                            if(fuente.getValor().equals("Reducción de velocidad") || fuente.getId() == 3){
                                reduccionVelocidad = fuente.getHrs();
                            }
                            if(fuente.getValor().equals("Por Calidad")){
                                if(subproductos.getResult().compareTo(BigDecimal.ZERO) != 0){
                                    BigDecimal subproducto = subproductos.getResult();
                                    subproducto = subproducto.divide(new BigDecimal(3.5), RoundingMode.CEILING);
                                    porCalidad = subproducto.add(fuente.getHrs());
                                }
                                map4.put("hrs", porCalidad);
                                map4.put("porcentaje", getPorcentajeParo(porCalidad, tiempoDisponible));
                            }else{
                                map4.put("hrs", fuente.getHrs());
                                map4.put("porcentaje", getPorcentajeParo(fuente.getHrs(), tiempoDisponible));
                            }
                            reporte.add(map4);
                            desempenoEfec = desempenoEfec.add(fuente.getHrs()).add(porCalidad);
                            if(fuente.getId() == 1 || fuente.getId() == 2){
                                totalHoraParo = totalHoraParo.add(fuente.getHrs());
                            }
                        }
                        desempenoEfec = tiempoDisponibleTotal.subtract(desempenoEfec);
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
                        map7.put("hrs", periodo.getVelocidad_ideal());
                        map7.put("porcentaje", "");
                        datosProduccion.add(map7);
                        HashMap<String, Object> map8 = new HashMap<>();
                        map8.put("padre", 0);
                        map8.put("titulo", "Capacidad Productiva (Turno)");
                        map8.put("hrs", periodo.getVelocidad_ideal().multiply(new BigDecimal(8)));
                        map8.put("porcentaje", "");
                        datosProduccion.add(map8);
                        datosProduccion.add(map3);
                        HashMap<String, Object> map9 = new HashMap<>();
                        BigDecimal tiempoOperacion = tiempoDisponible.subtract(totalHoraParo);
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
                        BigDecimal pDisponibilidad = getPorcentajeParo(tiempoOperacion, tiempoDisponible);
                        map14.put("padre", 0);
                        map14.put("titulo", "Disponibilidad");
                        map14.put("hrs", tiempoOperacion);
                        map14.put("porcentaje", pDisponibilidad);
                        map14.put("meta", periodo.getDisponibilidad());
                        reporteOEE.add(map14);
                        HashMap<String, Object> map15 = new HashMap<>();
                        BigDecimal calculo = tiempoOperacion.subtract(reduccionVelocidad);
                        BigDecimal utilizacion = prodBuena.getResult().divide(calculo, RoundingMode.CEILING);
                        calculo = prodBuena.getResult().divide(tiempoOperacion, RoundingMode.CEILING);
                        int compare = periodo.getVelocidad_ideal().compareTo(BigDecimal.ZERO);
                        BigDecimal pUtilizacion = BigDecimal.ZERO;
                        if(compare != 0){
                            pUtilizacion = calculo.divide(periodo.getVelocidad_ideal(), RoundingMode.CEILING);
                        }
                        pUtilizacion = pUtilizacion.multiply(new BigDecimal(100));
                        map15.put("padre", 0);
                        map15.put("titulo", "Utilización");
                        map15.put("hrs", utilizacion);
                        map15.put("porcentaje", pUtilizacion);
                        map15.put("meta", periodo.getUtilizacion());
                        reporteOEE.add(map15);
                        HashMap<String, Object> map16 = new HashMap<>();
                        BigDecimal pCalidad = BigDecimal.ZERO;
                        int resultado = BigDecimal.ZERO.compareTo(prodBuena.getResult());
                        if(resultado != 0){
                            pCalidad = prodBuena.getResult().divide(produccionTotal,RoundingMode.CEILING);
                            pCalidad = pCalidad.multiply(new BigDecimal(100));
                        } 

                        map16.put("padre", 0);
                        map16.put("titulo", "Calidad");
                        map16.put("hrs", "");
                        map16.put("porcentaje", pCalidad);
                        map16.put("meta", periodo.getCalidad());
                        reporteOEE.add(map16);
                        HashMap<String, Object> map17 = new HashMap<>();
                        BigDecimal oee = pDisponibilidad.multiply(pUtilizacion).multiply(pCalidad);
                        oee = oee.divide(new BigDecimal(10000),RoundingMode.CEILING);
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
                        map19.put("titulo", "TMP (hrs)");
                        map19.put("hrs", tiempoDisponibleTotal.subtract(desempenoEfec));
                        map19.put("porcentaje", new BigDecimal(100).subtract(pTEEP));
                        reporteOEE.add(map19);

                        data.setReporteDisponibilidad(reporte);
                        data.setDatosProduccion(datosProduccion);
                        data.setReporteOEE(reporteOEE);
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
                        List<ResultBigDecimal> molido = reportesDAO.getMolidoByLinea(fechaI, fechaT, listLineas.get(y).getId_linea());
                        ResultBigDecimal result = reportesDAO.getTotalMolidoByLinea(fechaI, fechaT, listLineas.get(y).getId_linea());
                        listaMolidos.add(molido);
                        lisTotalMolidos.add(result);
                    }
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
                    int compara = row.getMeta().compareTo(BigDecimal.ZERO);
                    BigDecimal desempeno = BigDecimal.ZERO;
                    if(compare != 0 && compara != 0){
                        desempeno = (row.getProduccion().divide(row.getMeta(), RoundingMode.CEILING)).multiply(new BigDecimal(100));
                    } 
                    body.put("desempeno", desempeno);
                    body.put("icon", desempeno.compareTo(new BigDecimal(100)));
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
                totales.put("icon", totalDesempeno.compareTo(new BigDecimal(100)));
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
                    body.put("icon", desempeno.compareTo(new BigDecimal(100)));
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
                    
                    BigDecimal acumuladoVelA = BigDecimal.ZERO;
                    BigDecimal acumuladoVelB = BigDecimal.ZERO;
                    BigDecimal acumuladoVelC = BigDecimal.ZERO;
                    BigDecimal acumuladoVelD = BigDecimal.ZERO;
                    BigDecimal countA = BigDecimal.ZERO;
                    BigDecimal countB = BigDecimal.ZERO;
                    BigDecimal countC = BigDecimal.ZERO;
                    BigDecimal countD = BigDecimal.ZERO;
                    for(ReporteDTO row:dataVelPromedio){
                        HashMap<String, Object> body = new HashMap<>();
                        body.put("padre", 0);
                        body.put("dia",   convertSqlToDay(sumarFechasDias(row.getDia(), 2)));
                        body.put("turno", row.getId_turno());
                        body.put("grupo", row.getValor_grupo());
                        body.put("valor", row.getVelocidad_promedio().setScale(2,RoundingMode.FLOOR));
                        reporteVelPromedio.add(body);
                        
                        switch(row.getValor_grupo()){
                            case"A":
                                acumuladoVelA = acumuladoVelA.add(row.getVelocidad_promedio());
                                countA = countA.add(BigDecimal.ONE);
                                break;
                            case"B":
                                acumuladoVelB = acumuladoVelB.add(row.getVelocidad_promedio());
                                countB = countB.add(BigDecimal.ONE);
                                break;
                            case"C":
                                acumuladoVelC = acumuladoVelC.add(row.getVelocidad_promedio());
                                countC = countC.add(BigDecimal.ONE);
                                break;
                            case"D":
                                acumuladoVelD = acumuladoVelD.add(row.getVelocidad_promedio());
                                countD = countD.add(BigDecimal.ONE);
                                break;
                        }
                    }
                    List<HashMap> graficaVelPromedio = new ArrayList<>();
                    HashMap<String, Object> header = new HashMap<>();
                    header.put("padre", 1);
                    header.put("grupoa","Grupo A");
                    header.put("sppeda","Velocidad A");
                    header.put("grupob","Grupo B");
                    header.put("sppedb","Velocidad B");
                    header.put("grupoc","Grupo C");
                    header.put("sppedc","Velocidad C");
                    header.put("grupod","Grupo D");
                    header.put("sppedd","Velocidad D");
                    graficaVelPromedio.add(header);
                    
                    HashMap<String, Object> body = new HashMap<>();
                    body.put("padre", 0);
                    body.put("grupoa","Grupo A");
                    if(acumuladoVelA.compareTo(countA) != 0){
                        body.put("sppeda",acumuladoVelA.divide(countA,RoundingMode.CEILING).setScale(2,RoundingMode.FLOOR));
                    }
                    body.put("grupob","Grupo B");
                    if(acumuladoVelB.compareTo(countB) != 0){
                        body.put("sppedb",acumuladoVelB.divide(countB,RoundingMode.CEILING).setScale(2,RoundingMode.FLOOR));
                    }
                    body.put("grupoc","Grupo C");
                    if(acumuladoVelC.compareTo(countC) != 0){
                        body.put("sppedc",acumuladoVelC.divide(countC,RoundingMode.CEILING).setScale(2,RoundingMode.FLOOR));
                    }
                    body.put("grupod","Grupo D");
                    if(acumuladoVelD.compareTo(countD) != 0){
                        body.put("sppedd",acumuladoVelD.divide(countD,RoundingMode.CEILING).setScale(2,RoundingMode.FLOOR));
                    }
                    graficaVelPromedio.add(body);
                    
                    data.setGraficaMap(graficaVelPromedio);
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
                    ReporteDiario grafRows = null;
                    
                    switch(report){
                        case"byDays":
                            Date fechaI = getDateFirstDay(periodo.getAnio(), periodo.getMes());
                            Date fechaT = getDateLastDay(periodo.getAnio(), periodo.getMes());
                            dataRows = reportesDAO.getDailyPerformance(fechaI, fechaT, idLinea);
                            grafRows = reportesDAO.getGraficaPerformanceByWeek(periodo.getAnio(), periodo.getMes(), idLinea);
                            break;
                        case"byWeeks":
                            dataRows = reportesDAO.getReportePerformanceByWeek(periodo.getMes(), periodo.getAnio(), idLinea);
                            grafRows = reportesDAO.getGraficaPerformanceByWeek(periodo.getAnio(), periodo.getMes(), idLinea);
                            break;
                        case"byMonths":
                            int lastDayFeb = getUltimoDiaMes(periodo.getAnio(), 2);
                            dataRows = reportesDAO.getReportePerformanceByMonth(periodo.getAnio(), idLinea,lastDayFeb);
                            grafRows = reportesDAO.getGraficaPerformanceByMonth(periodo.getAnio(), idLinea);
                            break;
                    }
                    
                    List<HashMap> reportePerformance = new ArrayList<>();
                    List<HashMap> graficaPerformance = new ArrayList<>();
                    HashMap<String, Object> head = new HashMap<>();
                    head.put("padre", 1);
                    head.put("periodo", "Periodo");
                    head.put("real",    "Real");
                    head.put("meta",    "Meta");
                    reportePerformance.add(head);
                    HashMap<String, Object> header = new HashMap<>();
                    header.put("reala","Real A");
                    header.put("realb","Real B");
                    header.put("realc","Real C");
                    header.put("reald","Real D");
                    header.put("metaa","Meta A");
                    header.put("metab","Meta B");
                    header.put("metac","Meta C");
                    header.put("metad","Meta D");
                    header.put("padre", 1);
                    graficaPerformance.add(header);
                    
                    if(dataRows != null && grafRows != null){
                        for(ReporteDiario row:dataRows){
                            HashMap<String, Object> body = new HashMap<>();
                            if(report.equals("byDays")){
                                body.put("periodo",convertSqlToDay(sumarFechasDias(row.getDia(), 2)));
                            }else{
                                body.put("periodo",row.getPeriodo());
                            }
                            BigDecimal suma = row.getA().add(row.getB()).add(row.getC()).add(row.getD());
                            body.put("real", suma.setScale(3, RoundingMode.FLOOR));
                            body.put("meta", row.getMeta_dia().setScale(3, RoundingMode.FLOOR));
                            body.put("padre",0);
                            reportePerformance.add(body);
                        }
                        
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("reala",grafRows.getA().setScale(3, RoundingMode.FLOOR));
                        map.put("realb",grafRows.getB().setScale(3, RoundingMode.FLOOR));
                        map.put("realc",grafRows.getC().setScale(3, RoundingMode.FLOOR));
                        map.put("reald",grafRows.getD().setScale(3, RoundingMode.FLOOR));
                        map.put("metaa",grafRows.getMeta_a().setScale(3, RoundingMode.FLOOR));
                        map.put("metab",grafRows.getMeta_b().setScale(3, RoundingMode.FLOOR));
                        map.put("metac",grafRows.getMeta_c().setScale(3, RoundingMode.FLOOR));
                        map.put("metad",grafRows.getMeta_d().setScale(3, RoundingMode.FLOOR));
                        map.put("padre",0);
                        graficaPerformance.add(map);
                        
                        data.setGraficaMap(graficaPerformance);
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