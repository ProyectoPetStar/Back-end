package org.petstar.model.ETAD;

import java.util.HashMap;
import java.util.List;
import org.petstar.dto.CatalogosDTO;
import org.petstar.dto.PeriodosDTO;

/**
 *
 * @author Tech-Pro
 */
public class MetasMasivasModel {
    private List<PeriodosDTO> listPeriodos;
    private List<CatalogosDTO> listEtad;
    private List<HashMap> listData;

    public List<PeriodosDTO> getListPeriodos() {
        return listPeriodos;
    }

    public void setListPeriodos(List<PeriodosDTO> listPeriodos) {
        this.listPeriodos = listPeriodos;
    }

    public List<CatalogosDTO> getListEtad() {
        return listEtad;
    }

    public void setListEtad(List<CatalogosDTO> listEtad) {
        this.listEtad = listEtad;
    }

    public List<HashMap> getListData() {
        return listData;
    }

    public void setListData(List<HashMap> listData) {
        this.listData = listData;
    }
}
