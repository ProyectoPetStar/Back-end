package org.petstar.model.ETAD;

import java.util.List;
import org.petstar.dto.CatalogosDTO;
import org.petstar.dto.ETAD.PetIndicadorDiario;
import org.petstar.dto.ETAD.PetIndicadorMensual;
import org.petstar.dto.PeriodosDTO;

/**
 *
 * @author Tech-Pro
 */
public class IndicadoresResponse {
    private List<CatalogosDTO> listEtads;
    private List<CatalogosDTO> listGrupos;
    private List<PeriodosDTO> listPeriodos;
    private List<PetIndicadorDiario> listIndicadorDiarios;
    private List<PetIndicadorMensual> listIndicadorMensuales;

    public List<CatalogosDTO> getListEtads() {
        return listEtads;
    }

    public void setListEtads(List<CatalogosDTO> listEtads) {
        this.listEtads = listEtads;
    }

    public List<PeriodosDTO> getListPeriodos() {
        return listPeriodos;
    }

    public List<CatalogosDTO> getListGrupos() {
        return listGrupos;
    }

    public void setListGrupos(List<CatalogosDTO> listGrupos) {
        this.listGrupos = listGrupos;
    }

    public void setListPeriodos(List<PeriodosDTO> listPeriodos) {
        this.listPeriodos = listPeriodos;
    }

    public List<PetIndicadorDiario> getListIndicadorDiarios() {
        return listIndicadorDiarios;
    }

    public void setListIndicadorDiarios(List<PetIndicadorDiario> listIndicadorDiarios) {
        this.listIndicadorDiarios = listIndicadorDiarios;
    }

    public List<PetIndicadorMensual> getListIndicadorMensuales() {
        return listIndicadorMensuales;
    }

    public void setListIndicadorMensuales(List<PetIndicadorMensual> listIndicadorMensuales) {
        this.listIndicadorMensuales = listIndicadorMensuales;
    }
}
