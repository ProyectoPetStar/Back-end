/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.petstar.model;

import java.util.List;
import org.petstar.dto.EquipoAmutDTO;

/**
 * Clase de Modelo del response
 * @author Tech-Pro
 */
public class EquipoAmutResponseJson extends ResponseJson{
    private EquipoAmutDTO equipoAmut;
    private List<EquipoAmutDTO> listEquipoAmut;

    public EquipoAmutDTO getEquipoAmut() {
        return equipoAmut;
    }

    public void setEquipoAmut(EquipoAmutDTO equipoAmut) {
        this.equipoAmut = equipoAmut;
    }

    public List<EquipoAmutDTO> getListEquipoAmut() {
        return listEquipoAmut;
    }

    public void setListEquipoAmut(List<EquipoAmutDTO> listEquipoAmut) {
        this.listEquipoAmut = listEquipoAmut;
    }
}
