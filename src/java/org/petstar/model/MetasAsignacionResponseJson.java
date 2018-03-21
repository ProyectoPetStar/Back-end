/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.petstar.model;

import java.util.List;
import org.petstar.dto.MetasAsignacionDTO;

/**
 *
 * @author Tech-Pro
 */
public class MetasAsignacionResponseJson {
    private MetasAsignacionDTO metasAsignacion;
    private List<MetasAsignacionDTO> listMetasAsignacion;

    public MetasAsignacionDTO getMetasAsignacion() {
        return metasAsignacion;
    }

    public void setMetasAsignacion(MetasAsignacionDTO metasAsignacion) {
        this.metasAsignacion = metasAsignacion;
    }

    public List<MetasAsignacionDTO> getListMetasAsignacion() {
        return listMetasAsignacion;
    }

    public void setListMetasAsignacion(List<MetasAsignacionDTO> listMetasAsignacion) {
        this.listMetasAsignacion = listMetasAsignacion;
    }
}
