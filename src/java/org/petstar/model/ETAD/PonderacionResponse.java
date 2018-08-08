package org.petstar.model.ETAD;

import java.util.HashMap;
import java.util.List;
import org.petstar.dto.CatalogosDTO;
import org.petstar.dto.ETAD.PetCatObjetivoOperativo;
import org.petstar.dto.ETAD.PetPonderacionObjetivoOperativo;
import org.petstar.dto.PeriodosDTO;
import org.petstar.dto.ResultInteger;

/**
 *
 * @author Tech-Pro
 */
public class PonderacionResponse {
    private List<PetPonderacionObjetivoOperativo> listPonderacionObjetivos;
    private List<PetCatObjetivoOperativo> listObjetivosOperativos;
    private List<PeriodosDTO> listPeriodos;
    private List<ResultInteger> listYears;
    private List<ResultInteger> listYearsOP;
    private List<ResultInteger> yearsForKPI;
    private List<CatalogosDTO> listEtads;
    private List<HashMap> listData;

    public List<PetPonderacionObjetivoOperativo> getListPonderacionObjetivos() {
        return listPonderacionObjetivos;
    }

    public void setListPonderacionObjetivos(List<PetPonderacionObjetivoOperativo> listPonderacionObjetivos) {
        this.listPonderacionObjetivos = listPonderacionObjetivos;
    }

    public List<PetCatObjetivoOperativo> getListObjetivosOperativos() {
        return listObjetivosOperativos;
    }

    public void setListObjetivosOperativos(List<PetCatObjetivoOperativo> listObjetivosOperativos) {
        this.listObjetivosOperativos = listObjetivosOperativos;
    }

    public List<PeriodosDTO> getListPeriodos() {
        return listPeriodos;
    }

    public void setListPeriodos(List<PeriodosDTO> listPeriodos) {
        this.listPeriodos = listPeriodos;
    }

    public List<ResultInteger> getListYears() {
        return listYears;
    }

    public void setListYears(List<ResultInteger> listYears) {
        this.listYears = listYears;
    }

    public List<ResultInteger> getListYearsOP() {
        return listYearsOP;
    }

    public void setListYearsOP(List<ResultInteger> listYearsOP) {
        this.listYearsOP = listYearsOP;
    }

    public List<ResultInteger> getYearsForKPI() {
        return yearsForKPI;
    }

    public void setYearsForKPI(List<ResultInteger> yearsForKPI) {
        this.yearsForKPI = yearsForKPI;
    }

    public List<CatalogosDTO> getListEtads() {
        return listEtads;
    }

    public void setListEtads(List<CatalogosDTO> listEtads) {
        this.listEtads = listEtads;
    }

    public List<HashMap> getListData() {
        return listData;
    }

    public void setListData(List<HashMap> listData) {
        this.listData = listData;
    }
}
