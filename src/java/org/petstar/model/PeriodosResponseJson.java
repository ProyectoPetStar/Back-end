package org.petstar.model;

import java.util.List;
import org.petstar.dto.LineasDTO;
import org.petstar.dto.PeriodosDTO;

/**
 *
 * @author Tech-Pro
 */
public class PeriodosResponseJson {
    private List<PeriodosDTO> listPeriodos;
    private PeriodosDTO periodo;
    private List<LineasDTO> listLineas;
    private List<PeriodosDTO> listDetailsPeriodo;

    public List<PeriodosDTO> getListPeriodos() {
        return listPeriodos;
    }

    public void setListPeriodos(List<PeriodosDTO> listPeriodos) {
        this.listPeriodos = listPeriodos;
    }

    public PeriodosDTO getPeriodo() {
        return periodo;
    }

    public void setPeriodo(PeriodosDTO periodo) {
        this.periodo = periodo;
    }

    public List<LineasDTO> getListLineas() {
        return listLineas;
    }

    public void setListLineas(List<LineasDTO> listLineas) {
        this.listLineas = listLineas;
    }

    public List<PeriodosDTO> getListDetailsPeriodo() {
        return listDetailsPeriodo;
    }

    public void setListDetailsPeriodo(List<PeriodosDTO> listDetailsPeriodo) {
        this.listDetailsPeriodo = listDetailsPeriodo;
    }
}
