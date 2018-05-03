package org.petstar.model;

import java.util.List;
import org.petstar.dto.EquiposDTO;

/**
 *
 * @author Tech-Pro
 */
public class EquiposResponseJson {
    private EquiposDTO equipo;
    private List<EquiposDTO> listEquipos;

    public EquiposDTO getEquipo() {
        return equipo;
    }

    public void setEquipo(EquiposDTO equipo) {
        this.equipo = equipo;
    }

    public List<EquiposDTO> getListEquipos() {
        return listEquipos;
    }

    public void setListEquipos(List<EquiposDTO> listEquipos) {
        this.listEquipos = listEquipos;
    }
}
