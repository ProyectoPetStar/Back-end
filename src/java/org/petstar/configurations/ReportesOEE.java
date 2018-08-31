package org.petstar.configurations;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import static org.petstar.configurations.utils.convertSqlToDay;
import static org.petstar.configurations.utils.sumarFechasDias;
import static org.petstar.configurations.utils.getNumeroMenor;
import org.petstar.dao.LineasDAO;
import org.petstar.dao.ReportesDAO;
import org.petstar.dto.LineasDTO;
import org.petstar.dto.PeriodosDTO;
import org.petstar.dto.ReporteDiario;
import org.petstar.dto.ResultBigDecimal;

/**
 *
 * @author Tech-Pro
 */
public class ReportesOEE {   
    
    public static List<HashMap> getResporteProduccionDiariaAmut(Date fechaI, Date fechaT, int idGpoLinea, PeriodosDTO periodo) throws Exception {
        ReportesDAO reportesDAO = new ReportesDAO();
        LineasDAO lineasDAO = new LineasDAO();
        
        List<LineasDTO> listLineas = lineasDAO.getLineasByGpoLinea(idGpoLinea);
        List<List<ResultBigDecimal>> listaMolidos = new ArrayList<>();
        List<ResultBigDecimal> lisTotalMolidos = new ArrayList<>();
        List<ReporteDiario> listData = reportesDAO.getReporteDiario(fechaI, fechaT, idGpoLinea);
        List<HashMap> listReporte = new ArrayList<>();

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
    
    public static List<HashMap> getResporteProduccionDiariaBuhler(Date fechaI, 
            Date fechaT, int idGpoLinea, PeriodosDTO periodo) throws Exception {
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
        List<ResultBigDecimal> resinaE1 =  reportesDAO.getMolidoByLinea(fechaI, fechaT, 3);
        List<ResultBigDecimal> resinaE2 =  reportesDAO.getMolidoByLinea(fechaI, fechaT, 4);
        List<ResultBigDecimal> resinaSSP = reportesDAO.getMolidoByLinea(fechaI, fechaT, 5);
        List<ResultBigDecimal> resina004 = reportesDAO.getResina004SSP(fechaI, fechaT);
        List<ResultBigDecimal> metasE1 = reportesDAO.getMetasByLinea(fechaI, fechaT, 3);
        List<ResultBigDecimal> metasE2 = reportesDAO.getMetasByLinea(fechaI, fechaT, 4);
        List<ResultBigDecimal> metasSSP = reportesDAO.getMetasByLinea(fechaI, fechaT, 5);
            
        int[] numeros = { resinaE1.size(), resinaE2.size(), resinaSSP.size(), diarioE1.size(), 
            resina004.size(), metasE1.size(), metasE2.size(), metasSSP.size() };
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
            totalHojaSS = totalHojaSS.add(diarioE1.get(i).getHojuela());
            totalPlasta = totalPlasta.add(diarioE1.get(i).getPlastas());
            totalPellet = totalPellet.add(diarioE1.get(i).getPellet());
            Hoj_Pla = Hoj_Pla.add(diarioE1.get(i).getPlastas().add(diarioE1.get(i).getHojuela()));
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
            row.put("hojuelaSS", diarioE1.get(i).getHojuela());
            row.put("plastas", diarioE1.get(i).getPlastas());
            row.put("pellet", diarioE1.get(i).getPellet());
            row.put("EficMat", eficienciaMat);
            listReporte.add(row);
        }

        BigDecimal totalDiferencia = acumulado.subtract(planResina);
        BigDecimal totalEficiencia = BigDecimal.ZERO;
        if(planResina.compareTo(BigDecimal.ZERO) != 0){
            totalEficiencia = acumulado.divide(planResina, RoundingMode.CEILING);
        }
        BigDecimal totalVsMetaM = totalEficiencia.subtract(new BigDecimal(100));
        BigDecimal totalDifeAmo = acumAmo.subtract(planExt);
        BigDecimal totalEficiDia = BigDecimal.ZERO;
        BigDecimal totalVsMetaE = BigDecimal.ZERO;
        if(planExt.compareTo(BigDecimal.ZERO) != 0){
            totalEficiDia = acumAmo.divide(planExt, RoundingMode.CEILING);
            totalVsMetaE = totalEficiDia.subtract(new BigDecimal(100));
        }
        BigDecimal totalEficMat = BigDecimal.ZERO;
        if(acumAmo.compareTo(BigDecimal.ZERO) != 0){
        totalEficMat = ((totalHojaSS.add(totalPlasta)).add(acumAmo)).divide(acumAmo, RoundingMode.CEILING);
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

}
