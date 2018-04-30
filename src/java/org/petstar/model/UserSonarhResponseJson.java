/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.petstar.model;
import java.util.List;
import org.petstar.dto.UserSonarhDTO;
/**
 * Modelado de JSON para usuarios Sonarh
 * @author Tech-Pro
 */
public class UserSonarhResponseJson {
    private UserSonarhDTO usuarioSonarh;
    private List<UserSonarhDTO> listUserSonarh;

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
}
