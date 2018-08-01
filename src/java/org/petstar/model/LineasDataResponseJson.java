/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.petstar.model;

import java.util.List;
import org.petstar.dto.CatalogosDTO;
import org.petstar.dto.LineasDTO;

/**
 * Modelado de JSON para las lineas
 * @author Tech-Pro
 */
public class LineasDataResponseJson {
    private LineasDTO lineasDTO;
    private List<LineasDTO> listLineasDTO;
    private List<CatalogosDTO> listEtads;
    private List<CatalogosDTO> listGposLinea;

    public LineasDTO getLineasDTO() {
        return lineasDTO;
    }

    public void setLineasDTO(LineasDTO lineasDTO) {
        this.lineasDTO = lineasDTO;
    }

    public List<LineasDTO> getListLineasDTO() {
        return listLineasDTO;
    }

    public void setListLineasDTO(List<LineasDTO> listLineasDTO) {
        this.listLineasDTO = listLineasDTO;
    }

    public List<CatalogosDTO> getListEtads() {
        return listEtads;
    }

    public void setListEtads(List<CatalogosDTO> listEtads) {
        this.listEtads = listEtads;
    }

    public List<CatalogosDTO> getListGposLinea() {
        return listGposLinea;
    }

    public void setListGposLinea(List<CatalogosDTO> listGposLinea) {
        this.listGposLinea = listGposLinea;
    }
}
