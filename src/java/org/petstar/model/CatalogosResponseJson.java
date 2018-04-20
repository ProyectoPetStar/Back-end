/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.petstar.model;

import org.petstar.dto.CatalogosDTO;
import java.util.List;

/**
 * Modelo de JSON para Catalogos
 * @author Tech-Pro
 */
public class CatalogosResponseJson extends ResponseJson{
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
