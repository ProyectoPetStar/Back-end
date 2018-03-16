/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.petstar.model;

import java.util.List;
import org.petstar.dto.CatalogosDTO;
import org.petstar.dto.MetasDTO;

/**
 *
 * @author Tech-Pro
 */
public class MetasDataResponseJson {
    private MetasDTO metasDTO;
    private List<MetasDTO> listMetas;
    private List<CatalogosDTO> listLineas;
    private List<CatalogosDTO> listTurnos;
    private List<CatalogosDTO> listGrupos;

    public MetasDTO getMetasDTO() {
        return metasDTO;
    }

    public void setMetasDTO(MetasDTO metasDTO) {
        this.metasDTO = metasDTO;
    }

    public List<MetasDTO> getListMetas() {
        return listMetas;
    }

    public void setListMetas(List<MetasDTO> listMetas) {
        this.listMetas = listMetas;
    }

    public List<CatalogosDTO> getListLineas() {
        return listLineas;
    }

    public void setListLineas(List<CatalogosDTO> listLineas) {
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
