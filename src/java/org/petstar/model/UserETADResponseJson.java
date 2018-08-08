/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.petstar.model;

import java.util.List;
import org.petstar.dto.CatalogosDTO;
import org.petstar.dto.LineasDTO;
import org.petstar.dto.UserDTO;

/**
 * Modelado de JSON para usuarios
 * @author Tech-Pro
 */
public class UserETADResponseJson {
    private UserDTO userETAD;
    private List<UserDTO> listUserETAD;
    private List<LineasDTO> listLineas;
    private List<CatalogosDTO> listEtads;
    private List<CatalogosDTO> listGrupos;
    private List<CatalogosDTO> ListPerfiles;

    public UserDTO getUserETAD() {
        return userETAD;
    }

    public void setUserETAD(UserDTO userETAD) {
        this.userETAD = userETAD;
    }

    public List<UserDTO> getListUserETAD() {
        return listUserETAD;
    }

    public void setListUserETAD(List<UserDTO> listUserETAD) {
        this.listUserETAD = listUserETAD;
    }

    public List<LineasDTO> getListLineas() {
        return listLineas;
    }

    public void setListLineas(List<LineasDTO> listLineas) {
        this.listLineas = listLineas;
    }

    public List<CatalogosDTO> getListEtads() {
        return listEtads;
    }

    public void setListEtads(List<CatalogosDTO> listEtads) {
        this.listEtads = listEtads;
    }

    public List<CatalogosDTO> getListGrupos() {
        return listGrupos;
    }

    public void setListGrupos(List<CatalogosDTO> listGrupos) {
        this.listGrupos = listGrupos;
    }

    public List<CatalogosDTO> getListPerfiles() {
        return ListPerfiles;
    }

    public void setListPerfiles(List<CatalogosDTO> ListPerfiles) {
        this.ListPerfiles = ListPerfiles;
    }
}
