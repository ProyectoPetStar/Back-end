/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.petstar.model;

import java.util.List;
import org.petstar.dto.CatalogosDTO;
import org.petstar.dto.RolesDTO;

/**
 * Modelo de JSON para Catalogos
 * @author Tech-Pro
 */
public class CatalogosListResponseJason {
    private CatalogosDTO catalogosDTO;
    private List<RolesDTO> listAllRolles;
    private List<CatalogosDTO> listCatalogosDTO;
    private List<CatalogosDTO> rolesByPerfil;

    public CatalogosDTO getCatalogosDTO() {
        return catalogosDTO;
    }

    public void setCatalogosDTO(CatalogosDTO catalogosDTO) {
        this.catalogosDTO = catalogosDTO;
    }

    public List<CatalogosDTO> getListCatalogosDTO() {
        return listCatalogosDTO;
    }

    public List<RolesDTO> getListAllRolles() {
        return listAllRolles;
    }

    public void setListAllRolles(List<RolesDTO> listAllRolles) {
        this.listAllRolles = listAllRolles;
    }

    public void setListCatalogosDTO(List<CatalogosDTO> listCatalogosDTO) {
        this.listCatalogosDTO = listCatalogosDTO;
    }

    public List<CatalogosDTO> getRolesByPerfil() {
        return rolesByPerfil;
    }

    public void setRolesByPerfil(List<CatalogosDTO> rolesByPerfil) {
        this.rolesByPerfil = rolesByPerfil;
    }
}
