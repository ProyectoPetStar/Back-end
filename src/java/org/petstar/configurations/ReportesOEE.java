package org.petstar.configurations;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import static org.petstar.configurations.utils.convertSqlToDay;
import static org.petstar.configurations.utils.getDateFirstDay;
import static org.petstar.configurations.utils.getDateLastDay;
import static org.petstar.configurations.utils.getNumeroMenor;
import static org.petstar.configurations.utils.getPorcentajeParo;
import static org.petstar.configurations.utils.getTotalHoras;
import static org.petstar.configurations.utils.getUltimoDiaMes;
import static org.petstar.configurations.utils.sumarFechasDias;
import org.petstar.dao.CatalogosDAO;
import org.petstar.dao.LineasDAO;
import org.petstar.dao.PeriodosDAO;
import org.petstar.dao.RazonParoDAO;
import org.petstar.dao.ReportesDAO;
import org.petstar.dto.CatalogosDTO;
import org.petstar.dto.Fuentes;
import org.petstar.dto.LineasDTO;
import org.petstar.dto.PeriodosDTO;
import org.petstar.dto.RazonParoDTO;
import org.petstar.dto.ReporteDTO;
import org.petstar.dto.ReporteDiario;
import org.petstar.dto.ResultBigDecimal;

/**
 *
 * @author Tech-Pro
 */
public class ReportesOEE {   
    private static final String TABLE_FUENTES = "pet_cat_fuentes_paro";
    
    public static List<HashMap> getResporteProduccionDiariaAmut(Date fechaI, Date fechaT, int idGpoLinea, PeriodosDTO periodo) throws Exception {
        ReportesDAO reportesDAO = new ReportesDAO();
        LineasDAO lineasDAO = new LineasDAO();
        PeriodosDAO periodosDAO = new PeriodosDAO();
        
        List<LineasDTO> listLineas = lineasDAO.getLineasByGpoLinea(idGpoLinea);
        List<List<ResultBigDecimal>> listaMolidos = new ArrayList<>();
        List<ResultBigDecimal> lisTotalMolidos = new ArrayList<>();
        List<ReporteDiario> listData = reportesDAO.getReporteDiario(fechaI, fechaT, idGpoLinea);
        List<HashMap> listReporte = new ArrayList<>();
        periodo = periodosDAO.getPeriodoById(periodo.getId_periodo(), listLineas.get(0).getId_linea());

        HashMap<String, Object> encabezado = new HashMap<>();
        encabezado.put("padre", 1);
        encabezado.put("dia", "Dia");
        for (int y = 0; y < listLineas.size(); y++) {
            encabezado.put("molido" + (y + 1), "Molido " + (y + 1));
            encabezado.put("hojuela" + (y + 1), "Hojuela " + (y + 1));
        }
        encabezado.put("totalMolido", "Total molido");
        encabezado.put("acumulado", "Acumulado");
        encabezado.put("metaMolido", "Plan Molido");
        encabezado.put("difMolido", "Diferencia Molido");
        encabezado.put("eficiencia", "Eficiencia/dia");
        encabezado.put("vsMetaM", "+/- vs meta M");
        encabezado.put("eficTeorica", "Efic teorica");
        encabezado.put("totalHoj", "Total Hojuela");
        encabezado.put("acumHoju", "Acum. Hojuela");
        encabezado.put("planHoju", "Plan Hojuela");
        encabezado.put("difeHoju", "Diferencia Hoj");
        encabezado.put("eficiDia", "Eficiencia/dia H");
        encabezado.put("vsMetaH", "+/- vs meta H");
        listReporte.add(encabezado);

        for (int y = 0; y < listLineas.size(); y++) {
            List<ResultBigDecimal> molido = reportesDAO.getMolidoByLinea(fechaI, fechaT, listLineas.get(y).getId_linea());
            ResultBigDecimal result = reportesDAO.getTotalMolidoByLinea(fechaI, fechaT, listLineas.get(y).getId_linea());
            listaMolidos.add(molido);
            lisTotalMolidos.add(result);
        }
        BigDecimal totalTotalMolido = BigDecimal.ZERO;
        BigDecimal totalTotalHojuela = BigDecimal.ZERO;
        BigDecimal totalTotalPlanHojuela = BigDecimal.ZERO;
        BigDecimal totalTotalPlanMolido = BigDecimal.ZERO;

        BigDecimal acumulado = BigDecimal.ZERO;
        BigDecimal acumHojuela = BigDecimal.ZERO;
        BigDecimal planMolido = BigDecimal.ZERO;

        for (int i = 0; i < listData.size(); i++) {
            planMolido = planMolido.add(listData.get(i).getPlan_molido());
            HashMap<String, Object> row = new HashMap<>();
            row.put("padre", 0);
            row.put("dia", convertSqlToDay(sumarFechasDias(listData.get(i).getDia(), 2)));
            BigDecimal totalMolido = BigDecimal.ZERO;
            BigDecimal totalHojuela = BigDecimal.ZERO;
            for (int y = 0; y < listaMolidos.size(); y++) {
                BigDecimal molido = listaMolidos.get(y).get(i).getResult();
                row.put("molido" + (y + 1), molido);
                BigDecimal hojuela = molido.multiply(periodo.getEficiencia_teorica());
                int compare = hojuela.compareTo(BigDecimal.ZERO);
                if (compare == 0) {
                    hojuela = BigDecimal.ZERO;
                } else {
                    hojuela = hojuela.divide(new BigDecimal(100));
                }
                row.put("hojuela" + (y + 1), hojuela);
                totalMolido = totalMolido.add(molido);
                totalHojuela = totalHojuela.add(hojuela);
                //suma.add(molido);
            }
            row.put("totalMolido", totalMolido);
            totalTotalMolido = totalTotalMolido.add(totalMolido);
            acumulado = acumulado.add(totalMolido);
            row.put("acumulado", acumulado);
            row.put("metaMolido", planMolido);
            totalTotalPlanMolido = planMolido;
            BigDecimal diferenciaMolido = acumulado.subtract(planMolido);
            row.put("difMolido", diferenciaMolido);
            int compare = planMolido.compareTo(BigDecimal.ZERO);
            BigDecimal eficiencia = BigDecimal.ZERO;
            if (compare != 0) {
                eficiencia = acumulado.divide(planMolido, RoundingMode.CEILING);
            }
            eficiencia = eficiencia.multiply(new BigDecimal(100));
            row.put("eficiencia", eficiencia);
            row.put("vsMetaM", eficiencia.subtract(new BigDecimal(100)));
            row.put("eficTeorica", periodo.getEficiencia_teorica());
            row.put("totalHoj", totalHojuela);
            totalTotalHojuela = totalTotalHojuela.add(totalHojuela);
            acumHojuela = acumHojuela.add(totalHojuela);
            row.put("acumHoju", acumHojuela);
            BigDecimal planHojuela = planMolido.multiply(periodo.getEficiencia_teorica());
            compare = planHojuela.compareTo(BigDecimal.ZERO);
            if (compare == 0) {
                planHojuela = BigDecimal.ZERO;
            } else {
                planHojuela = planHojuela.divide(new BigDecimal(100));
            }
            row.put("planHoju", planHojuela);
            row.put("difeHoju", acumHojuela.subtract(planHojuela));
            BigDecimal eficienciaDia = BigDecimal.ZERO;
            compare = planHojuela.compareTo(BigDecimal.ZERO);
            if (compare != 0) {
                eficienciaDia = acumHojuela.divide(planHojuela, RoundingMode.CEILING);
            }
            totalTotalPlanHojuela = planHojuela;
            eficienciaDia = eficienciaDia.multiply(new BigDecimal(100));
            row.put("eficiDia", eficienciaDia);
            row.put("vsMetaH", eficienciaDia.subtract(new BigDecimal(100)));
            listReporte.add(row);
        }

        HashMap<String, Object> totales = new HashMap<>();
        totales.put("padre", 2);
        totales.put("dia", "Total");

        for (int y = 0; y < listLineas.size(); y++) {
            BigDecimal calculo = lisTotalMolidos.get(y).getResult().multiply(periodo.getEficiencia_teorica());
            BigDecimal totalHojuela = calculo.divide(new BigDecimal(100), RoundingMode.CEILING);
            totales.put("molido" + (y + 1), lisTotalMolidos.get(y).getResult());
            totales.put("hojuela" + (y + 1), totalHojuela);
            //totalTotalHojuela = totalTotalHojuela.add(totalHojuela);
        }
        totales.put("totalMolido", totalTotalMolido);
        totales.put("acumulado", acumulado);
        totales.put("metaMolido", totalTotalPlanMolido);
        BigDecimal totalTotalDifMolido = acumulado.subtract(totalTotalPlanMolido);
        totales.put("difMolido", totalTotalDifMolido);
        int compare = totalTotalPlanMolido.compareTo(BigDecimal.ZERO);
        BigDecimal TotalEficienciaDia = BigDecimal.ZERO;
        if (compare != 0) {
            TotalEficienciaDia = acumulado.divide(totalTotalPlanMolido, RoundingMode.CEILING);
        }
        totales.put("eficiencia", TotalEficienciaDia);
        totales.put("vsMetaM", TotalEficienciaDia.subtract(BigDecimal.ONE));
        totales.put("eficTeorica", periodo.getEficiencia_teorica());
        totales.put("totalHoj", totalTotalHojuela);
        totales.put("acumHoju", acumHojuela);
        totales.put("planHoju", totalTotalPlanHojuela);
        BigDecimal totalTotalDifHojuela = acumHojuela.subtract(totalTotalPlanHojuela);
        totales.put("difeHoju", totalTotalDifHojuela);
        compare = totalTotalPlanHojuela.compareTo(BigDecimal.ZERO);
        BigDecimal totalEficienciaHojuela = BigDecimal.ZERO;
        if (compare != 0) {
            totalEficienciaHojuela = acumHojuela.divide(totalTotalPlanHojuela, RoundingMode.CEILING);
        }
        totales.put("eficiDia", totalEficienciaHojuela);
        totales.put("vsMetaH", totalEficienciaHojuela.subtract(BigDecimal.ONE));
        listReporte.add(totales);

        return listReporte;
    }
    
    public static List<HashMap> getResporteProduccionDiariaBuhler(Date fechaI,Date fechaT, int idGpoLinea, PeriodosDTO periodo) throws Exception {
        ReportesDAO reportesDAO = new ReportesDAO();
        List<HashMap> listReporte = new ArrayList<>();

        HashMap<String, Object> encabezado = new HashMap<>();
        encabezado.put("padre", 1);
        encabezado.put("dia", "Dia");
        encabezado.put("resina", "Resina");
        encabezado.put("resina004", "Resina 004");
        encabezado.put("acumulado", "Acumulado");
        encabezado.put("planResina", "Plan Resina");
        encabezado.put("diferencia", "Diferencia");
        encabezado.put("eficiencia", "Eficiencia / dia");
        encabezado.put("vsMetaM", "+/- vs Meta");
        encabezado.put("ext1", "Extrusor 1");
        encabezado.put("ext2", "Extrusor 2");
        encabezado.put("totalAmo", "Total Amorfo");
        encabezado.put("acumAmo", "Acumulado Amorfo");
        encabezado.put("planExt", "Plan Extrusor");
        encabezado.put("difeAmo", "Diferencia Amorfo");
        encabezado.put("eficiDia", "Eficiencia/dia Ext");
        encabezado.put("vsMetaE", "+/- vs Meta E");
        encabezado.put("hojuelaSS", "Hojuela S + S");
        encabezado.put("plastas", "Plastas");
        encabezado.put("pellet", "Pellet Arranque");
        encabezado.put("EficMat", "Eficiencia / Mat");
        listReporte.add(encabezado);
        
        List<ReporteDiario> diarioE1 = reportesDAO.getReporteDiarioBuhler(fechaI, fechaT, 3);
        List<ReporteDiario> diarioE2 = reportesDAO.getReporteDiarioBuhler(fechaI, fechaT, 4);
        List<ResultBigDecimal> resinaE1 =  reportesDAO.getMolidoByLinea(fechaI, fechaT, 3);
        List<ResultBigDecimal> resinaE2 =  reportesDAO.getMolidoByLinea(fechaI, fechaT, 4);
        List<ResultBigDecimal> resinaSSP = reportesDAO.getMolidoByLinea(fechaI, fechaT, 5);
        List<ResultBigDecimal> resina004 = reportesDAO.getResina004SSP(fechaI, fechaT);
        List<ResultBigDecimal> metasE1 = reportesDAO.getMetasByLinea(fechaI, fechaT, 3);
        List<ResultBigDecimal> metasE2 = reportesDAO.getMetasByLinea(fechaI, fechaT, 4);
        List<ResultBigDecimal> metasSSP = reportesDAO.getMetasByLinea(fechaI, fechaT, 5);
            
        int[] numeros = { resinaE1.size(), resinaE2.size(), resinaSSP.size(), diarioE1.size(), 
            diarioE2.size(), resina004.size(), metasE1.size(), metasE2.size(), metasSSP.size() };
        int maxIndex = getNumeroMenor(numeros);
        
        BigDecimal totalResina = BigDecimal.ZERO;
        BigDecimal totalRes004 = BigDecimal.ZERO;
        BigDecimal totalExt1 = BigDecimal.ZERO;
        BigDecimal totalExt2 = BigDecimal.ZERO;
        BigDecimal totalTotAmo = BigDecimal.ZERO;
        BigDecimal totalHojaSS = BigDecimal.ZERO;
        BigDecimal totalPellet = BigDecimal.ZERO;
        BigDecimal totalPlasta = BigDecimal.ZERO;
        BigDecimal acumulado = BigDecimal.ZERO;
        BigDecimal planResina = BigDecimal.ZERO;
        BigDecimal acumAmo = BigDecimal.ZERO;
        BigDecimal planExt = BigDecimal.ZERO;
        BigDecimal Hoj_Pla = BigDecimal.ZERO;

        for (int i = 0; i < maxIndex; i++) {
            totalResina = totalResina.add(resinaSSP.get(i).getResult());
            totalRes004 = totalRes004.add(resina004.get(i).getResult());
            
            BigDecimal calculo = resinaSSP.get(i).getResult().subtract(resina004.get(i).getResult());
            acumulado = calculo.add(acumulado);
            planResina = planResina.add(metasSSP.get(i).getResult());
            BigDecimal diferencia = acumulado.subtract(planResina);
            BigDecimal eficiencia = acumulado.divide(planResina, RoundingMode.CEILING);
            eficiencia = eficiencia.multiply(new BigDecimal(100));
            BigDecimal vsMetaM = eficiencia.subtract(new BigDecimal(100));
            planExt = planExt.add(metasE1.get(i).getResult().add(metasE2.get(i).getResult()));
            BigDecimal totalAmo = resinaE1.get(i).getResult().add(resinaE2.get(i).getResult());
            acumAmo = acumAmo.add(totalAmo);
            totalTotAmo = totalTotAmo.add(totalAmo);
            BigDecimal difeAmo = acumAmo.subtract(planExt);
            BigDecimal eficiDia = acumAmo.divide(planExt, RoundingMode.CEILING);
            eficiDia = eficiDia.multiply(new BigDecimal(100));
            BigDecimal vsMetaE = eficiDia.subtract(new BigDecimal(100));
            totalHojaSS = totalHojaSS.add(diarioE1.get(i).getHojuela().add(diarioE2.get(i).getHojuela()));
            totalPlasta = totalPlasta.add(diarioE1.get(i).getPlastas().add(diarioE2.get(i).getPlastas()));
            totalPellet = totalPellet.add(diarioE1.get(i).getPellet().add(diarioE2.get(i).getPellet()));
            Hoj_Pla = Hoj_Pla.add(diarioE1.get(i).getPlastas().add(diarioE1.get(i).getHojuela())
                    .add(diarioE2.get(i).getPlastas()).add(diarioE2.get(i).getHojuela()));
            BigDecimal eficienciaMat = acumAmo.add(Hoj_Pla);
            if(acumAmo.compareTo(BigDecimal.ZERO) == 0){
                eficienciaMat = BigDecimal.ZERO;
            }else{
                eficienciaMat = acumAmo.divide(eficienciaMat, RoundingMode.CEILING);
                eficienciaMat = eficienciaMat.multiply(new BigDecimal(100));
            }
            totalExt1 = totalExt1.add(resinaE1.get(i).getResult());
            totalExt2 = totalExt2.add(resinaE2.get(i).getResult());
            
            HashMap<String, Object> row = new HashMap<>();
            row.put("padre", 0);
            row.put("dia", convertSqlToDay(sumarFechasDias(diarioE1.get(i).getDia(), 2)));
            row.put("resina", resinaSSP.get(i).getResult());
            row.put("resina004", resina004.get(i).getResult());
            row.put("acumulado", acumulado);
            row.put("planResina", planResina);
            row.put("diferencia", diferencia);
            row.put("eficiencia", eficiencia);
            row.put("vsMetaM", vsMetaM);
            row.put("ext1", resinaE1.get(i).getResult());
            row.put("ext2", resinaE2.get(i).getResult());
            row.put("totalAmo", totalAmo);
            row.put("acumAmo", acumAmo);
            row.put("planExt", planExt);
            row.put("difeAmo", difeAmo);
            row.put("eficiDia", eficiDia);
            row.put("vsMetaE", vsMetaE);
            row.put("hojuelaSS", diarioE1.get(i).getHojuela().add(diarioE2.get(i).getHojuela()));
            row.put("plastas", diarioE1.get(i).getPlastas().add(diarioE2.get(i).getPlastas()));
            row.put("pellet", diarioE1.get(i).getPellet().add(diarioE2.get(i).getPellet()));
            row.put("EficMat", eficienciaMat);
            listReporte.add(row);
        }

        BigDecimal totalDiferencia = acumulado.subtract(planResina);
        BigDecimal totalEficiencia = BigDecimal.ZERO;
        if(planResina.compareTo(BigDecimal.ZERO) != 0){
            totalEficiencia = acumulado.divide(planResina, RoundingMode.CEILING);
            totalEficiencia = totalEficiencia.multiply(new BigDecimal(100));
        }
        BigDecimal totalVsMetaM = totalEficiencia.subtract(new BigDecimal(100));
        BigDecimal totalDifeAmo = acumAmo.subtract(planExt);
        BigDecimal totalEficiDia = BigDecimal.ZERO;
        BigDecimal totalVsMetaE = BigDecimal.ZERO;
        if(planExt.compareTo(BigDecimal.ZERO) != 0){
            totalEficiDia = acumAmo.divide(planExt, RoundingMode.CEILING);
            totalEficiDia = totalEficiDia.multiply(new BigDecimal(100));
            totalVsMetaE = totalEficiDia.subtract(new BigDecimal(100));
        }
        BigDecimal totalEficMat = BigDecimal.ZERO;
        if(acumAmo.compareTo(BigDecimal.ZERO) != 0){
            BigDecimal toHoj_Pla = acumAmo.add(totalPlasta.add(totalHojaSS));
            totalEficMat = acumAmo.divide(toHoj_Pla, RoundingMode.CEILING);
            totalEficMat = totalEficMat.multiply(new BigDecimal(100));
        }
        
        HashMap<String, Object> totales = new HashMap<>();
        totales.put("padre", 2);
        totales.put("dia", "Total");
        totales.put("resina", totalResina);
        totales.put("resina004", totalRes004);
        totales.put("acumulado", acumulado);
        totales.put("planResina", planResina);
        totales.put("diferencia", totalDiferencia);
        totales.put("eficiencia", totalEficiencia);
        totales.put("vsMetaM", totalVsMetaM);
        totales.put("ext1", totalExt1);
        totales.put("ext2", totalExt2);
        totales.put("totalAmo", totalTotAmo);
        totales.put("acumAmo", acumAmo);
        totales.put("planExt", planExt);
        totales.put("difeAmo", totalDifeAmo);
        totales.put("eficiDia", totalEficiDia);
        totales.put("vsMetaE", totalVsMetaE);
        totales.put("hojuelaSS", totalHojaSS);
        totales.put("plastas", totalPlasta);
        totales.put("pellet", totalPellet);
        totales.put("EficMat", totalEficMat);
        listReporte.add(totales);

        return listReporte;
    }

    public static List<HashMap> getReporteFuentePerdidas(Date fechaI, Date fechaT, PeriodosDTO periodo, LineasDTO linea) throws Exception {
        CatalogosDAO catalogosDAO = new CatalogosDAO();
        RazonParoDAO razonParoDAO = new RazonParoDAO();
        ReportesDAO reportesDAO = new ReportesDAO();
        
        int idLinea = linea.getId_linea();
        
        List<CatalogosDTO> listFuentes = catalogosDAO.getCatalogosActive(TABLE_FUENTES);
        List<HashMap> listOEEFallas = new ArrayList<>();
        BigDecimal tiempoDisponible = getTotalHoras(fechaI, fechaT);
        BigDecimal totalGeneral = BigDecimal.ZERO;
        ResultBigDecimal subproductos = reportesDAO.getTotalSubProducto(fechaI, fechaT, idLinea);

        for (CatalogosDTO fuente : listFuentes) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("padre", 1);
            map.put("grafica", "Perdidas");
            map.put("linea", linea.getValor());
            map.put("titulo_grafica", "Fuente de Perdidas " + linea.getDescripcion());
            map.put("fuente", fuente.getValor());
            listOEEFallas.add(map);

            List<RazonParoDTO> listRazones = razonParoDAO.getFallasByOEE(fechaI, fechaT, idLinea, fuente.getId());
            BigDecimal totalParcial = new BigDecimal(BigInteger.ZERO);

            for (RazonParoDTO razon : listRazones) {
                BigDecimal subproducto = BigDecimal.ZERO;
                HashMap<String, Object> raz = new HashMap<>();
                raz.put("padre", 0);
                raz.put("fuente", razon.getValor());
                if (razon.getValor().equals("Subproductos")) {
                    if(linea.getId_linea() != 5){
                        if (subproductos.getResult().compareTo(BigDecimal.ZERO) != 0) {
                            subproducto = subproductos.getResult();
                            subproducto = subproducto.divide(periodo.getVelocidad_ideal(), RoundingMode.CEILING);
                        }
                    }
                    
                    raz.put("hrs", subproducto);
                    raz.put("porcentaje", getPorcentajeParo(subproducto, tiempoDisponible));
                } else {
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
        return listOEEFallas;
    }
    
    public static List<HashMap> getReporteDisponibilidad(Date fechaI, Date fechaT, PeriodosDTO periodo, LineasDTO linea)throws Exception{
        ReportesDAO reportesDAO = new ReportesDAO();
        List<HashMap> reporte = new ArrayList<>();
        
        int idLinea = linea.getId_linea();
        BigDecimal tiempoDisponibleTotal = getTotalHoras(fechaI, fechaT);
        
        HashMap<String, Object> map0 = new HashMap<>();
        map0.put("padre", 1);
        map0.put("grafica", "Disponibilidad");
        map0.put("linea", linea.getValor());
        map0.put("titulo_grafica", "Desempeño Global del Equipo " + linea.getDescripcion());
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
        
        BigDecimal totalHoraParo = BigDecimal.ZERO;
        BigDecimal desempenoEfec = BigDecimal.ZERO;
        
        List<Fuentes> listFuentes = reportesDAO.getFuentes(idLinea, fechaI, fechaT);
        ResultBigDecimal subproductos = reportesDAO.getTotalSubProducto(fechaI, fechaT, idLinea);
        
        for (Fuentes fuente : listFuentes) {
            HashMap<String, Object> map4 = new HashMap<>();
            map4.put("padre", 0);
            map4.put("titulo", fuente.getValor());
            BigDecimal porCalidad = BigDecimal.ZERO;
            
            if (fuente.getValor().equals("Por Calidad")) {
                if(idLinea != 5){
                    if (subproductos.getResult().compareTo(BigDecimal.ZERO) != 0) {
                        BigDecimal subproducto = subproductos.getResult();
                        subproducto = subproducto.divide(periodo.getVelocidad_ideal(), RoundingMode.CEILING);
                        porCalidad = subproducto.add(fuente.getHrs());
                    }
                }
                map4.put("hrs", porCalidad);
                map4.put("porcentaje", getPorcentajeParo(porCalidad, tiempoDisponible));
            } else {
                map4.put("hrs", fuente.getHrs());
                map4.put("porcentaje", getPorcentajeParo(fuente.getHrs(), tiempoDisponible));
            }
            reporte.add(map4);
            desempenoEfec = desempenoEfec.add(fuente.getHrs()).add(porCalidad);
            if (fuente.getId() == 1 || fuente.getId() == 2) {
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
        
        return  reporte;
    }

    public static List<HashMap> getReporteOEE(Date fechaI, Date fechaT, PeriodosDTO periodo, LineasDTO linea) throws Exception{
        ReportesDAO reportesDAO = new ReportesDAO();
        int idLinea = linea.getId_linea();
        
        
        ResultBigDecimal subproductos = reportesDAO.getTotalSubProducto(fechaI, fechaT, idLinea);
        ResultBigDecimal subProduc = reportesDAO.getSumaSubProductos(idLinea, fechaI, fechaT);
        ResultBigDecimal prodBuena = reportesDAO.getProduccionBuena(idLinea, fechaI, fechaT);
        BigDecimal produccionTotal = prodBuena.getResult().add(subProduc.getResult());
        BigDecimal tiempoDisponibleTotal = getTotalHoras(fechaI, fechaT);
        BigDecimal reduccionVelocidad = BigDecimal.ZERO;
        BigDecimal totalHoraParo = BigDecimal.ZERO;
        BigDecimal desempenoEfec = BigDecimal.ZERO;
        BigDecimal porCalidad = BigDecimal.ZERO;
        
        List<Fuentes> listFuentes = reportesDAO.getFuentes(idLinea, fechaI, fechaT);
        
        for(Fuentes fuente:listFuentes){
            if (fuente.getValor().equals("Por Calidad")) {
                if(idLinea != 5){
                    if (subproductos.getResult().compareTo(BigDecimal.ZERO) != 0) {
                        BigDecimal subproducto = subproductos.getResult();
                        subproducto = subproducto.divide(periodo.getVelocidad_ideal(), RoundingMode.CEILING);
                        porCalidad = subproducto.add(fuente.getHrs());
                    }
                }
            }
            if (fuente.getValor().equals("Reducción de velocidad") || fuente.getId() == 3){
                reduccionVelocidad = fuente.getHrs();
            }
            desempenoEfec = desempenoEfec.add(fuente.getHrs()).add(porCalidad);
            if(fuente.getId() == 1 || fuente.getId() == 2){
                totalHoraParo = totalHoraParo.add(fuente.getHrs());
            }
        }
        desempenoEfec = tiempoDisponibleTotal.subtract(desempenoEfec);
        
        BigDecimal tiempoDisponible = tiempoDisponibleTotal.subtract(periodo.getNo_ventas());
        BigDecimal tiempoOperacion = tiempoDisponible.subtract(totalHoraParo);
        
        List<HashMap> reporteOEE = new ArrayList<>();
        HashMap<String, Object> map13 = new HashMap<>();
        map13.put("padre", 1);
        map13.put("grafica", "OEE");
        map13.put("linea", linea.getValor());
        map13.put("titulo_grafica", "OEE " + linea.getDescripcion());
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
        int compare = periodo.getVelocidad_ideal().compareTo(BigDecimal.ZERO);
        BigDecimal pUtilizacion = BigDecimal.ZERO;
        if (compare != 0) {
            pUtilizacion = utilizacion.divide(periodo.getVelocidad_ideal(), RoundingMode.CEILING);
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
        if (resultado != 0) {
            pCalidad = prodBuena.getResult().divide(produccionTotal, RoundingMode.CEILING);
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
        oee = oee.divide(new BigDecimal(10000), RoundingMode.CEILING);
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
        return reporteOEE;
    }
    
    public static List<HashMap> getDatosProduccion(Date fechaI, Date fechaT, PeriodosDTO periodo, LineasDTO linea) throws Exception {
        ReportesDAO reportesDAO = new ReportesDAO();
        int idLinea = linea.getId_linea();
        
        ResultBigDecimal subProduc = reportesDAO.getSumaSubProductos(idLinea, fechaI, fechaT);
        ResultBigDecimal prodBuena = reportesDAO.getProduccionBuena(idLinea, fechaI, fechaT);
        BigDecimal tiempoDisponibleTotal = getTotalHoras(fechaI, fechaT);
        BigDecimal totalHoraParo = BigDecimal.ZERO;
       
        List<Fuentes> listFuentes = reportesDAO.getFuentes(idLinea, fechaI, fechaT);
        for(Fuentes fuente:listFuentes){
            if(fuente.getId() == 1 || fuente.getId() == 2){
                totalHoraParo = totalHoraParo.add(fuente.getHrs());
            }
        }
        
        List<HashMap> datosProduccion = new ArrayList<>();
        BigDecimal tiempoDisponible = tiempoDisponibleTotal.subtract(periodo.getNo_ventas());
        HashMap<String, Object> map3 = new HashMap<>();
        map3.put("padre", 0);
        map3.put("titulo", "Tiempo Disponible");
        map3.put("hrs", tiempoDisponible);
        map3.put("porcentaje", getPorcentajeParo(tiempoDisponible, tiempoDisponibleTotal));

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
        
        return datosProduccion;
    }
    
    /**
     * Reporte Desempeño Diario
     * Metodo que genera el reporte desempeño diario por Grupo de Linea,
     * este metodo llama a los siguientes 2
     * - getReportDesempenoDiario
     * - getReportDesempenoDiarioPO
     * @param fechaI
     * @param fechaT
     * @param idGpoLinea
     * @param idPeriodo
     * @return
     * @throws Exception 
     */
    public static List<List<HashMap>> getReportDailyPerformance(Date fechaI,Date fechaT, int idGpoLinea, int idPeriodo) throws Exception {
        LineasDAO lineasDAO = new LineasDAO();

        List<List<HashMap>> listReport = new ArrayList<>();
        List<LineasDTO> listLineas = lineasDAO.getLineasByGpoLinea(idGpoLinea);
        
        for (LineasDTO linea : listLineas) {

            HashMap<String, Object> dataLinea = new HashMap<>();
            dataLinea.put("Linea", linea.getValor());
            dataLinea.put("titulo_grafica", "Desempeño Diario de " + linea.getDescripcion());
            List<HashMap> listPO = new ArrayList<>();
            List<HashMap> listReporteLinea = new ArrayList<>();
            listReporteLinea.add(dataLinea);
            listReporteLinea.addAll(getReportDesempenoDiario(fechaI, fechaT, linea));
            listReport.add(listReporteLinea);

            if (idGpoLinea == 1) {
                HashMap<String, Object> dataLineaPO = new HashMap<>();
                dataLineaPO.put("Linea", linea.getValor());
                dataLineaPO.put("titulo_grafica", "Desempeño Diario Poliolefinas " + linea.getDescripcion());
                listPO.add(dataLineaPO);
                listPO.addAll(getReportDesempenoDiarioPO(fechaI, fechaT, linea, idPeriodo));
                listReport.add(listPO);
            }
        }
        return listReport;
    }
    
    /**
     * Reporte Desempeño Diario
     * Metodo que genera el reporte de Desempeño Diario por Linea
     * @param fechaI
     * @param fechaT
     * @param linea
     * @return
     * @throws Exception 
     */
    public static List<HashMap> getReportDesempenoDiario(Date fechaI, Date fechaT, LineasDTO linea) throws Exception {
        ReportesDAO reportesDAO = new ReportesDAO();
        List<HashMap> listReport = new ArrayList<>();

        HashMap<String, Object> encabezado = new HashMap<>();
        encabezado.put("padre", 1);
        encabezado.put("grafica", "Desempeño Diario");
        encabezado.put("linea", linea.getValor());
        encabezado.put("titulo_grafica", "Desempeño Diario de " + linea.getDescripcion());
        encabezado.put("dia", "Dia");
        encabezado.put("a", "A");
        encabezado.put("b", "B");
        encabezado.put("c", "C");
        encabezado.put("d", "D");
        encabezado.put("meta1", "Meta 1ro");
        encabezado.put("meta2", "Meta 2do");
        encabezado.put("meta3", "Meta dia");
        listReport.add(encabezado);

        List<ReporteDiario> listData = reportesDAO.getDailyPerformance(fechaI, fechaT, linea.getId_linea());

        for (ReporteDiario row : listData) {
            HashMap<String, Object> body = new HashMap<>();
            body.put("padre", 0);
            body.put("dia", convertSqlToDay(sumarFechasDias(row.getDia(), 2)));
            body.put("a", row.getA());
            body.put("b", row.getB());
            body.put("c", row.getC());
            body.put("d", row.getD());
            body.put("meta1", row.getMeta_uno());
            body.put("meta2", row.getMeta_dos());
            body.put("meta3", row.getMeta_dia());
            listReport.add(body);
        }
        return listReport;
    }
    
    /**
     * Reporte Desempeño Diario Poliolefinas
     * Metodo que genera el reporte Desempeño Diario Poliolefinas por linea
     * @param fechaI
     * @param fechaT
     * @param linea
     * @param idPeriodo
     * @return
     * @throws Exception 
     */
    public static List<HashMap> getReportDesempenoDiarioPO(Date fechaI, Date fechaT, LineasDTO linea, int idPeriodo) throws Exception {
        ReportesDAO reportesDAO = new ReportesDAO();
        PeriodosDAO periodosDAO = new PeriodosDAO();
        List<HashMap> listReport = new ArrayList<>();

        HashMap<String, Object> encabezado = new HashMap<>();
        encabezado.put("padre", 1);
        encabezado.put("grafica", "Desempeño Diario");
        encabezado.put("linea", linea.getValor());
        encabezado.put("titulo_grafica", "Desempeño Diario Poliolefinas " + linea.getDescripcion());
        encabezado.put("dia", "Dia");
        encabezado.put("a", "A");
        encabezado.put("b", "B");
        encabezado.put("c", "C");
        encabezado.put("d", "D");
        encabezado.put("meta1", "Meta 1ro");
        encabezado.put("meta2", "Meta 2do");
        encabezado.put("meta3", "Meta dia");
        listReport.add(encabezado);

        PeriodosDTO periodoLinea = periodosDAO.getPeriodoById(idPeriodo, linea.getId_linea());
        BigDecimal meta1 = periodoLinea.getVelocidad_po().multiply(new BigDecimal(8));
        BigDecimal meta2 = periodoLinea.getVelocidad_po().multiply(new BigDecimal(16));
        BigDecimal meta3 = periodoLinea.getVelocidad_po().multiply(new BigDecimal(24));

        List<ReporteDiario> listDataPO = reportesDAO.getDailyPerformancePO(fechaI, fechaT, linea.getId_linea());
        for (ReporteDiario row : listDataPO) {
            HashMap<String, Object> body = new HashMap<>();
            body.put("padre", 0);
            body.put("dia", convertSqlToDay(sumarFechasDias(row.getDia(), 2)));
            body.put("a", row.getA());
            body.put("b", row.getB());
            body.put("c", row.getC());
            body.put("d", row.getD());
            body.put("meta1", meta1);
            body.put("meta2", meta2);
            body.put("meta3", meta3);
            listReport.add(body);
        }
        return listReport;
    }
    
    public static List<List<HashMap>> getReportePlanVsRealAnual(int anio, LineasDTO linea) throws Exception {
        List<List<HashMap>> data = new ArrayList<>();
        ReportesDAO reportesDAO = new ReportesDAO();

        int lastDayFeb = getUltimoDiaMes(anio, 2);
        int idLinea = linea.getId_linea();

        List<ReporteDiario> dataRows = reportesDAO.getReportePerformanceByMonth(anio, idLinea, lastDayFeb);
        ReporteDiario grafRows = reportesDAO.getGraficaPerformanceByMonth(anio, idLinea);

        List<HashMap> reportePerformance = new ArrayList<>();
        List<HashMap> graficaPerformance = new ArrayList<>();
        HashMap<String, Object> head = new HashMap<>();
        head.put("padre", 1);
        head.put("grafica", "Producción Real vs Plan");
        head.put("linea", linea.getValor());
        head.put("titulo_grafica", "Producción Real vs Plan " + linea.getDescripcion());
        head.put("periodo", "Periodo");
        head.put("real", "Real");
        head.put("meta", "Meta");
        reportePerformance.add(head);
        HashMap<String, Object> header = new HashMap<>();
        header.put("grafica", "Desempeño por Grupo");
        header.put("linea", linea.getValor());
        header.put("titulo_grafica", "Desempeño por Grupo " + linea.getDescripcion());
        header.put("reala", "Real A");
        header.put("realb", "Real B");
        header.put("realc", "Real C");
        header.put("reald", "Real D");
        header.put("metaa", "Meta A");
        header.put("metab", "Meta B");
        header.put("metac", "Meta C");
        header.put("metad", "Meta D");
        header.put("padre", 1);
        graficaPerformance.add(header);

        for (ReporteDiario row : dataRows) {
            HashMap<String, Object> body = new HashMap<>();
            body.put("periodo", row.getPeriodo());
            BigDecimal suma = row.getA().add(row.getB()).add(row.getC()).add(row.getD());
            body.put("real", suma.setScale(3, RoundingMode.FLOOR));
            body.put("meta", row.getMeta_dia().setScale(3, RoundingMode.FLOOR));
            body.put("padre", 0);
            reportePerformance.add(body);
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("reala", grafRows.getA().setScale(3, RoundingMode.FLOOR));
        map.put("realb", grafRows.getB().setScale(3, RoundingMode.FLOOR));
        map.put("realc", grafRows.getC().setScale(3, RoundingMode.FLOOR));
        map.put("reald", grafRows.getD().setScale(3, RoundingMode.FLOOR));
        map.put("metaa", grafRows.getMeta_a().setScale(3, RoundingMode.FLOOR));
        map.put("metab", grafRows.getMeta_b().setScale(3, RoundingMode.FLOOR));
        map.put("metac", grafRows.getMeta_c().setScale(3, RoundingMode.FLOOR));
        map.put("metad", grafRows.getMeta_d().setScale(3, RoundingMode.FLOOR));
        map.put("padre", 0);
        graficaPerformance.add(map);
        
        data.add(graficaPerformance);
        data.add(reportePerformance);
        return data;
    }
    
    public static List<List<HashMap>> getReportePlanVsRealMensual(String reportBy, PeriodosDTO periodo, LineasDTO linea)throws Exception{
        List<List<HashMap>> data = new ArrayList<>();
        ReportesDAO reportesDAO = new ReportesDAO();
        List<ReporteDiario> dataRows = new ArrayList<>();
        ReporteDiario grafRows = new ReporteDiario();
        int idLinea = linea.getId_linea();
        
        switch (reportBy) {
            case "byDays":
                Date fechaI = getDateFirstDay(periodo.getAnio(), periodo.getMes());
                Date fechaT = getDateLastDay(periodo.getAnio(), periodo.getMes());
                dataRows = reportesDAO.getDailyPerformance(fechaI, fechaT, idLinea);
                grafRows = reportesDAO.getGraficaPerformanceByWeek(periodo.getAnio(), periodo.getMes(), idLinea);
                break;
            case "byWeeks":
                dataRows = reportesDAO.getReportePerformanceByWeek(periodo.getMes(), periodo.getAnio(), idLinea);
                grafRows = reportesDAO.getGraficaPerformanceByWeek(periodo.getAnio(), periodo.getMes(), idLinea);
                break;
        }

        List<HashMap> reportePerformance = new ArrayList<>();
        List<HashMap> graficaPerformance = new ArrayList<>();
        HashMap<String, Object> head = new HashMap<>();
        head.put("padre", 1);
        head.put("grafica", "Producción Real vs Plan");
        head.put("linea", linea.getValor());
        head.put("titulo_grafica", "Producción Real vs Plan " + linea.getDescripcion());
        head.put("periodo", "Periodo");
        head.put("real", "Real");
        head.put("meta", "Meta");
        reportePerformance.add(head);
        HashMap<String, Object> header = new HashMap<>();
        header.put("grafica", "Desempeño por Grupo");
        header.put("linea", linea.getValor());
        header.put("titulo_grafica", "Desempeño por Grupo " + linea.getDescripcion());
        header.put("reala", "Real A");
        header.put("realb", "Real B");
        header.put("realc", "Real C");
        header.put("reald", "Real D");
        header.put("metaa", "Meta A");
        header.put("metab", "Meta B");
        header.put("metac", "Meta C");
        header.put("metad", "Meta D");
        header.put("padre", 1);
        graficaPerformance.add(header);

        for (ReporteDiario row : dataRows) {
            HashMap<String, Object> body = new HashMap<>();
            if (reportBy.equals("byDays")) {
                body.put("periodo", convertSqlToDay(sumarFechasDias(row.getDia(), 2)));
            } else {
                body.put("periodo", row.getPeriodo());
            }
            BigDecimal suma = row.getA().add(row.getB()).add(row.getC()).add(row.getD());
            body.put("real", suma.setScale(3, RoundingMode.FLOOR));
            body.put("meta", row.getMeta_dia().setScale(3, RoundingMode.FLOOR));
            body.put("padre", 0);
            reportePerformance.add(body);
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("reala", grafRows.getA().setScale(3, RoundingMode.FLOOR));
        map.put("realb", grafRows.getB().setScale(3, RoundingMode.FLOOR));
        map.put("realc", grafRows.getC().setScale(3, RoundingMode.FLOOR));
        map.put("reald", grafRows.getD().setScale(3, RoundingMode.FLOOR));
        map.put("metaa", grafRows.getMeta_a().setScale(3, RoundingMode.FLOOR));
        map.put("metab", grafRows.getMeta_b().setScale(3, RoundingMode.FLOOR));
        map.put("metac", grafRows.getMeta_c().setScale(3, RoundingMode.FLOOR));
        map.put("metad", grafRows.getMeta_d().setScale(3, RoundingMode.FLOOR));
        map.put("padre", 0);
        graficaPerformance.add(map);
        
        data.add(graficaPerformance);
        data.add(reportePerformance);
        return data;
    }
    
    public static List<List<HashMap>> getReporteVelocidad(Date fechaI, Date fechaT, LineasDTO linea) throws Exception {
        ReportesDAO reportesDAO = new ReportesDAO();
        int idLinea = linea.getId_linea();
        
        List<ReporteDTO> dataVelPromedio = reportesDAO.getReporteVelPromedio(fechaI, fechaT, idLinea);
        List<List<HashMap>> data = new ArrayList<>();
        List<HashMap> reporteVelPromedio = new ArrayList<>();
        HashMap<String, Object> head = new HashMap<>();
        head.put("padre", 1);
        head.put("dia", "Dia");
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
        for (ReporteDTO row : dataVelPromedio) {
            HashMap<String, Object> body = new HashMap<>();
            body.put("padre", 0);
            body.put("dia", convertSqlToDay(sumarFechasDias(row.getDia(), 2)));
            body.put("turno", row.getId_turno());
            body.put("grupo", row.getValor_grupo());
            body.put("valor", row.getVelocidad_promedio().setScale(2, RoundingMode.FLOOR));
            reporteVelPromedio.add(body);

            switch (row.getValor_grupo()) {
                case "A":
                    acumuladoVelA = acumuladoVelA.add(row.getVelocidad_promedio());
                    countA = countA.add(BigDecimal.ONE);
                    break;
                case "B":
                    acumuladoVelB = acumuladoVelB.add(row.getVelocidad_promedio());
                    countB = countB.add(BigDecimal.ONE);
                    break;
                case "C":
                    acumuladoVelC = acumuladoVelC.add(row.getVelocidad_promedio());
                    countC = countC.add(BigDecimal.ONE);
                    break;
                case "D":
                    acumuladoVelD = acumuladoVelD.add(row.getVelocidad_promedio());
                    countD = countD.add(BigDecimal.ONE);
                    break;
            }
        }
        List<HashMap> graficaVelPromedio = new ArrayList<>();
        HashMap<String, Object> header = new HashMap<>();
        header.put("padre", 1);
        header.put("grafica", "Velocidad Promedio");
        header.put("linea", linea.getValor());
        header.put("titulo_grafica", "Velocidad Promedio por Grupo " + linea.getDescripcion());
        header.put("grupoa", "Grupo A");
        header.put("sppeda", "Velocidad A");
        header.put("grupob", "Grupo B");
        header.put("sppedb", "Velocidad B");
        header.put("grupoc", "Grupo C");
        header.put("sppedc", "Velocidad C");
        header.put("grupod", "Grupo D");
        header.put("sppedd", "Velocidad D");
        graficaVelPromedio.add(header);

        HashMap<String, Object> body = new HashMap<>();
        body.put("padre", 0);
        body.put("grupoa", "Grupo A");
        if (acumuladoVelA.compareTo(countA) != 0) {
            body.put("sppeda", acumuladoVelA.divide(countA, RoundingMode.CEILING).setScale(2, RoundingMode.FLOOR));
        }else{
            body.put("sppeda", BigDecimal.ZERO);
        }
        body.put("grupob", "Grupo B");
        if (acumuladoVelB.compareTo(countB) != 0) {
            body.put("sppedb", acumuladoVelB.divide(countB, RoundingMode.CEILING).setScale(2, RoundingMode.FLOOR));
        }else{
            body.put("sppedb", BigDecimal.ZERO);
        }
        body.put("grupoc", "Grupo C");
        if (acumuladoVelC.compareTo(countC) != 0) {
            body.put("sppedc", acumuladoVelC.divide(countC, RoundingMode.CEILING).setScale(2, RoundingMode.FLOOR));
        }else{
            body.put("sppedc", BigDecimal.ZERO);
        }
        body.put("grupod", "Grupo D");
        if (acumuladoVelD.compareTo(countD) != 0) {
            body.put("sppedd", acumuladoVelD.divide(countD, RoundingMode.CEILING).setScale(2, RoundingMode.FLOOR));
        }else{
            body.put("sppedd", BigDecimal.ZERO);
        }
        graficaVelPromedio.add(body);
        
        data.add(graficaVelPromedio);
        data.add(reporteVelPromedio);
        return data;
    }
}
