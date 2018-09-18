package org.petstar.configurations;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import static org.petstar.configurations.utils.masEsMejor;
import static org.petstar.configurations.utils.menosEsMejor;
import org.petstar.dao.CatalogosDAO;
import org.petstar.dao.ETAD.ReportesDAO;
import org.petstar.dao.PeriodosDAO;
import org.petstar.dto.CatalogosDTO;
import org.petstar.dto.ETAD.EvaluacionConcentrada;
import org.petstar.dto.ETAD.Posiciones;
import org.petstar.dto.ETAD.Reporte;
import org.petstar.dto.PeriodosDTO;

/**
 *
 * @author Tech-Pro
 */
public class ReportesETAD {

    public static List<HashMap> getIndicadoresDesempeno(CatalogosDTO etad, int idPeriodo, int mes, int anio) throws Exception {
        ReportesDAO reportesDAO = new ReportesDAO();
        List<HashMap> listData = new ArrayList<>();
        int idEtad = etad.getId();
        List<Reporte> listReporte = reportesDAO.indicadorClaveDesempenoGlobal(idPeriodo, idEtad, mes, anio);

        for (Reporte row : listReporte) {
            HashMap<String, Object> mapa = new HashMap<>();
            mapa.put("grafica", "KPI");
            mapa.put("id_etad", etad.getId());
            mapa.put("etad", etad.getValor());
            mapa.put("titulo_grafica", row.getKpi_operativo() + " " + etad.getDescripcion());
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
        return listData;
    }

    public static List<List<Posiciones>> getPosicionTrimestral(int anio, int trimestre) throws Exception {
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

        for (CatalogosDTO etad : listEtads) {
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
                    periodo1 = buildReportICDG(listPeriodo.get(0).getId_periodo(),
                            listPeriodo.get(0).getMes(), anio, etad.getId());
                    periodo2 = emptyMap;
                    periodo3 = emptyMap;
                    break;
                case 2:
                    periodo1 = buildReportICDG(listPeriodo.get(0).getId_periodo(),
                            listPeriodo.get(0).getMes(), anio, etad.getId());
                    periodo2 = buildReportICDG(listPeriodo.get(1).getId_periodo(),
                            listPeriodo.get(1).getMes(), anio, etad.getId());
                    periodo3 = emptyMap;
                    break;
                case 3:
                    periodo1 = buildReportICDG(listPeriodo.get(0).getId_periodo(),
                            listPeriodo.get(0).getMes(), anio, etad.getId());
                    periodo2 = buildReportICDG(listPeriodo.get(1).getId_periodo(),
                            listPeriodo.get(1).getMes(), anio, etad.getId());
                    periodo3 = buildReportICDG(listPeriodo.get(2).getId_periodo(),
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

            if (!etad.getValor().equals("CONTROL INTERNO") && etad.getId() != 7
                    && !etad.getValor().equals("REFACCIONES") && etad.getId() != 6) {
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
        for (EvaluacionConcentrada row : listEC) {
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
            if (resultado != 0) {
                return resultado;
            }
            return resultado;
        };

        Collections.sort(listPeriodo1, comparator);
        Collections.sort(listPeriodo2, comparator);
        Collections.sort(listPeriodo3, comparator);

        listGeneral.add(listPeriodo1);
        listGeneral.add(listPeriodo2);
        listGeneral.add(listPeriodo3);
        
        return listGeneral;
    }

    public static List<Posiciones> getPosicionAnual(int anio)throws Exception{
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
                            HashMap<String, Object> mapa = buildReportICDG(
                                    periodo.getId_periodo(), periodo.getMes(), anio, etad.getId());
                            int indexA = buscarIndex(listEC, etad.getValor(), "A");
                            int indexB = buscarIndex(listEC, etad.getValor(), "B");
                            int indexC = buscarIndex(listEC, etad.getValor(), "C");
                            int indexD = buscarIndex(listEC, etad.getValor(), "D");
                            
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
                return listGeneral;
    }
    
    public static HashMap<String, Object> buildReportICDG(int idPeriodo, int mes, int anio, int idEtad) throws Exception {
        ReportesDAO reportesDAO = new ReportesDAO();
        List<Reporte> listReporte = reportesDAO.indicadorClaveDesempenoGlobal(idPeriodo, idEtad, mes, anio);

        int totalBonoA = 0;
        int totalBonoB = 0;
        int totalBonoC = 0;
        int totalBonoD = 0;
        for (Reporte row : listReporte) {
            if (row.getTipo_kpi() == 0) {
                totalBonoA = totalBonoA + menosEsMejor(row.getMeta(), row.getGrupoa(), row.getPonderacion());
                totalBonoB = totalBonoB + menosEsMejor(row.getMeta(), row.getGrupob(), row.getPonderacion());
                totalBonoC = totalBonoC + menosEsMejor(row.getMeta(), row.getGrupoc(), row.getPonderacion());
                totalBonoD = totalBonoD + menosEsMejor(row.getMeta(), row.getGrupod(), row.getPonderacion());
            } else if (row.getTipo_kpi() == 1) {
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
        if (idEtad != 6 && idEtad != 7) {
            mapa.put("resBonoB", totalBonoB);
            mapa.put("resBonoC", totalBonoC);
            mapa.put("resBonoD", totalBonoD);
        } else {
            mapa.put("resBonoB", "");
            mapa.put("resBonoC", "");
            mapa.put("resBonoD", "");
        }
        return mapa;
    }
    
    private static int buscarIndex(List<EvaluacionConcentrada> lista,String etad, String grupo) {
        int index = -1;
        for(int y=0; y<lista.size(); y++) {
            if(lista.get(y).getEtad().equals(etad) && lista.get(y).getGrupo().equals(grupo)){
                index = y;
            }
        }
        return index;
    }
}
