package org.petstar.model;

import java.util.List;
import org.petstar.dto.CatalogosDTO;
import org.petstar.dto.ForecastDTO;
import org.petstar.dto.LineasDTO;
import org.petstar.dto.PeriodosDTO;

/**
 *
 * @author Tech-Pro
 */
public class UploadMetasDataResponseJson {
    private List<ForecastDTO> listMetas;
    private List<PeriodosDTO> listPeriodos;
    private List<LineasDTO> listLineas;
    private List<CatalogosDTO> listTurnos;
    private List<CatalogosDTO> listGrupos;

    public List<ForecastDTO> getListMetas() {
        return listMetas;
    }

    public void setListMetas(List<ForecastDTO> listMetas) {
        this.listMetas = listMetas;
    }

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

    public List<CatalogosDTO> getListTurnos() {
        return listTurnos;
    }

    public void setListTurnos(List<CatalogosDTO> listTurnos) {
        this.listTurnos = listTurnos;
    }

    public List<CatalogosDTO> getListGrupos() {
        return listGrupos;
    }

    public void setListGrupos(List<CatalogosDTO> listGrupos) {
        this.listGrupos = listGrupos;
    }
    
}
