package org.petstar.model;

import java.util.HashMap;
import java.util.List;
import org.petstar.dto.LineasDTO;

/**
 * @author Tech-Pro
 */
public class ReportesResponseJson {
    private List<HashMap> listaOEEFallas;
    private List<LineasDTO> listLineas;

    public List<HashMap> getListaOEEFallas() {
        return listaOEEFallas;
    }

    public void setListaOEEFallas(List<HashMap> listaOEEFallas) {
        this.listaOEEFallas = listaOEEFallas;
    }

    public List<LineasDTO> getListLineas() {
        return listLineas;
    }

    public void setListLineas(List<LineasDTO> listLineas) {
        this.listLineas = listLineas;
    }
}
