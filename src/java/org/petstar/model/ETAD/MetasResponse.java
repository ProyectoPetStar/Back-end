package org.petstar.model.ETAD;

import java.util.List;
import org.petstar.dto.CatalogosDTO;
import org.petstar.dto.ETAD.PetMetaKpi;
import org.petstar.dto.PeriodosDTO;

/**
 *
 * @author Tech-Pro
 */
public class MetasResponse {
   
    private List<PetMetaKpi> listMetasKpiOperativos;
    private List<PeriodosDTO> listPeriodos;
    private List<CatalogosDTO> listEtads;

    public List<PetMetaKpi> getListMetasKpiOperativos() {
        return listMetasKpiOperativos;
    }

    public void setListMetasKpiOperativos(List<PetMetaKpi> listMetasKpiOperativos) {
        this.listMetasKpiOperativos = listMetasKpiOperativos;
    }

    public List<PeriodosDTO> getListPeriodos() {
        return listPeriodos;
    }

    public void setListPeriodos(List<PeriodosDTO> listPeriodos) {
        this.listPeriodos = listPeriodos;
    }

    public List<CatalogosDTO> getListEtads() {
        return listEtads;
    }

    public void setListEtads(List<CatalogosDTO> listEtads) {
        this.listEtads = listEtads;
    }
}
