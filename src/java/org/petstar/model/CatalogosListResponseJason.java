/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.petstar.model;

import java.util.List;
import org.petstar.dto.CatalogosDTO;

/**
 *
 * @author GuillermoB
 */
public class CatalogosListResponseJason {
    private CatalogosDTO catalogosDTO;
    private List<CatalogosDTO> listCatalogosDTO;

    public CatalogosDTO getCatalogosDTO() {
        return catalogosDTO;
    }

    public void setCatalogosDTO(CatalogosDTO catalogosDTO) {
        this.catalogosDTO = catalogosDTO;
    }

    public List<CatalogosDTO> getListCatalogosDTO() {
        return listCatalogosDTO;
    }

    public void setListCatalogosDTO(List<CatalogosDTO> listCatalogosDTO) {
        this.listCatalogosDTO = listCatalogosDTO;
    }
    
}
