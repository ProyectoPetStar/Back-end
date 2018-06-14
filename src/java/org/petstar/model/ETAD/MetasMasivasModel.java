package org.petstar.model.ETAD;

import java.util.HashMap;
import java.util.List;
import org.petstar.dto.LineasDTO;
import org.petstar.dto.PeriodosDTO;

/**
 *
 * @author Tech-Pro
 */
public class MetasMasivasModel {
    private List<PeriodosDTO> listPeriodos;
    private List<LineasDTO> listLineas;
    private List<HashMap> listData;

    public List<PeriodosDTO> getListPeriodos() {
        return listPeriodos;
    }

    public void setListPeriodos(List<PeriodosDTO> listPeriodos) {
        this.listPeriodos = listPeriodos;
    }

    public List<LineasDTO> getListLineas() {
        return listLineas;
    }

    public void setListLineas(List<LineasDTO> listLineas) {
        this.listLineas = listLineas;
    }

    public List<HashMap> getListData() {
        return listData;
    }

    public void setListData(List<HashMap> listData) {
        this.listData = listData;
    }
}
