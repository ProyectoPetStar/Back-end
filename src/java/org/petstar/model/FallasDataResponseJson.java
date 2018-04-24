/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.petstar.model;

import java.util.List;
import org.petstar.dto.CatalogosDTO;
import org.petstar.dto.FallasDTO;
import org.petstar.dto.MetasDTO;

/**
 *
 * @author Tech-Pro
 */
public class FallasDataResponseJson {
    private FallasDTO fallasDTO;
    private MetasDTO metasDTO;
    private List<FallasDTO> listFallas;
    private List<CatalogosDTO> listFuentesParo;
    private List<CatalogosDTO> listEquipos;
    private List<CatalogosDTO> listRazonesParo;

   public FallasDTO getFallasDTO() {
        return fallasDTO;
    }

    public void setFallasDTO(FallasDTO fallasDTO) {
        this.fallasDTO = fallasDTO;
    }

     public MetasDTO getMetasDTO() {
        return metasDTO;
    }

    public void setMetasDTO(MetasDTO metasDTO) {
        this.metasDTO = metasDTO;
    }

    public List<FallasDTO> getListFallas() {
        return listFallas;
    }

    public void setListFallas(List<FallasDTO> listFallas) {
        this.listFallas = listFallas;
    }

    public List<CatalogosDTO> getListFuentesParo() {
        return listFuentesParo;
    }

    public void setListFuentesParo(List<CatalogosDTO> listFuentesParo) {
        this.listFuentesParo = listFuentesParo;
    }

    public List<CatalogosDTO> getListEquipos() {
        return listEquipos;
    }

    public void setListEquipos(List<CatalogosDTO> listEquipos) {
        this.listEquipos = listEquipos;
    }

    public List<CatalogosDTO> getListRazonesParo() {
        return listRazonesParo;
    }

    public void setListRazonesParo(List<CatalogosDTO> listRazonesParo) {
        this.listRazonesParo = listRazonesParo;
    }
}
