package org.petstar.model.ETAD;

import java.util.HashMap;
import java.util.List;
import org.petstar.dto.CatalogosDTO;
import org.petstar.dto.ETAD.EvaluacionConcentrada;
import org.petstar.dto.ETAD.Posiciones;
import org.petstar.dto.PeriodosDTO;

/**
 *
 * @author Tech-Pro
 */
public class ReportesResponse {
    private List<List<Posiciones>> posicionamiento;
    private List<PeriodosDTO> listPeriodos;
    private List<CatalogosDTO> listGrupos;
    private List<CatalogosDTO> listEtads;
    private List<HashMap> graficas;
    private List<HashMap> bonos;
    private List<HashMap> indicadorDesempeno;

    public List<List<Posiciones>> getPosicionamiento() {
        return posicionamiento;
    }

    public void setPosicionamiento(List<List<Posiciones>> posicionamiento) {
        this.posicionamiento = posicionamiento;
    }
    
    public List<PeriodosDTO> getListPeriodos() {
        return listPeriodos;
    }

    public void setListPeriodos(List<PeriodosDTO> listPeriodos) {
        this.listPeriodos = listPeriodos;
    }

    public List<CatalogosDTO> getListGrupos() {
        return listGrupos;
    }

    public void setListGrupos(List<CatalogosDTO> listGrupos) {
        this.listGrupos = listGrupos;
    }

    public List<CatalogosDTO> getListEtads() {
        return listEtads;
    }

    public void setListEtads(List<CatalogosDTO> listEtads) {
        this.listEtads = listEtads;
    }

    public List<HashMap> getGraficas() {
        return graficas;
    }

    public void setGraficas(List<HashMap> graficas) {
        this.graficas = graficas;
    }

    public List<HashMap> getBonos() {
        return bonos;
    }

    public void setBonos(List<HashMap> bonos) {
        this.bonos = bonos;
    }

    public List<HashMap> getIndicadorDesempeno() {
        return indicadorDesempeno;
    }

    public void setIndicadorDesempeno(List<HashMap> indicadorDesempeno) {
        this.indicadorDesempeno = indicadorDesempeno;
    }
}
