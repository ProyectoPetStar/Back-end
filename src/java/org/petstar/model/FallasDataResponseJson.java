/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.petstar.model;

import java.util.List;
import org.petstar.dto.CatalogosDTO;
import org.petstar.dto.EquiposDTO;
import org.petstar.dto.FallasDTO;
import org.petstar.dto.LineasDTO;
import org.petstar.dto.MetasDTO;
import org.petstar.dto.RazonParoDTO;

/**
 *
 * @author Tech-Pro
 */
public class FallasDataResponseJson {
    private FallasDTO fallasDTO;
    private MetasDTO metasDTO;
    private List<FallasDTO> listFallas;
    private List<CatalogosDTO> listFuentesParo;
    private List<EquiposDTO> listEquipos;
    private List<RazonParoDTO> listRazonesParo;
    private List<CatalogosDTO> listTurnos;
    private List<CatalogosDTO> listGrupos;
    private List<LineasDTO> listLineas;

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

    public List<EquiposDTO> getListEquipos() {
        return listEquipos;
    }

    public void setListEquipos(List<EquiposDTO> listEquipos) {
        this.listEquipos = listEquipos;
    }

    public List<RazonParoDTO> getListRazonesParo() {
        return listRazonesParo;
    }

    public void setListRazonesParo(List<RazonParoDTO> listRazonesParo) {
        this.listRazonesParo = listRazonesParo;
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

    public List<LineasDTO> getListLineas() {
        return listLineas;
    }

    public void setListLineas(List<LineasDTO> listLineas) {
        this.listLineas = listLineas;
    }
}
