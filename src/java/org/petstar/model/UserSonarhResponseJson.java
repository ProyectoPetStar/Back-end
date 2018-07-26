/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.petstar.model;
import java.util.List;
import org.petstar.dto.CatalogosDTO;
import org.petstar.dto.LineasDTO;
import org.petstar.dto.UserSonarhDTO;
/**
 * Modelado de JSON para usuarios Sonarh
 * @author Tech-Pro
 */
public class UserSonarhResponseJson {
    private UserSonarhDTO usuarioSonarh;
    private List<UserSonarhDTO> listUserSonarh;
    private List<LineasDTO> listLineas;
    private List<CatalogosDTO> listEtads;
    private List<CatalogosDTO> listGrupos;
    private List<CatalogosDTO> listPerfiles;

    public UserSonarhDTO getUsuarioSonarh() {
        return usuarioSonarh;
    }

    public void setUsuarioSonarh(UserSonarhDTO usuarioSonarh) {
        this.usuarioSonarh = usuarioSonarh;
    }

    public List<UserSonarhDTO> getListUserSonarh() {
        return listUserSonarh;
    }

    public void setListUserSonarh(List<UserSonarhDTO> listUserSonarh) {
        this.listUserSonarh = listUserSonarh;
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
        return listPerfiles;
    }

    public void setListPerfiles(List<CatalogosDTO> listPerfiles) {
        this.listPerfiles = listPerfiles;
    }
}
