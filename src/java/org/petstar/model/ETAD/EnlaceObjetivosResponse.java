package org.petstar.model.ETAD;

import java.util.List;
import org.petstar.dto.ETAD.PetReporteEnlace;
import org.petstar.dto.PeriodosDTO;

/**
 *
 * @author Tech-Pro
 */
public class EnlaceObjetivosResponse {
    private PetReporteEnlace reporteEnlace;
    private List<PeriodosDTO> listPeriodos;

    public PetReporteEnlace getReporteEnlace() {
        return reporteEnlace;
    }

    public void setReporteEnlace(PetReporteEnlace reporteEnlace) {
        this.reporteEnlace = reporteEnlace;
    }

    public List<PeriodosDTO> getListPeriodos() {
        return listPeriodos;
    }

    public void setListPeriodos(List<PeriodosDTO> listPeriodos) {
        this.listPeriodos = listPeriodos;
    }
}
