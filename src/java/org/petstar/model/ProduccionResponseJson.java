package org.petstar.model;

import java.util.List;
import org.petstar.dto.ProduccionDTO;

/**
 *
 * @author Tech-Pro
 */
public class ProduccionResponseJson {
    private List<ProduccionDTO> listProduccion;

    public List<ProduccionDTO> getListProduccion() {
        return listProduccion;
    }

    public void setListProduccion(List<ProduccionDTO> listProduccion) {
        this.listProduccion = listProduccion;
    }
}
