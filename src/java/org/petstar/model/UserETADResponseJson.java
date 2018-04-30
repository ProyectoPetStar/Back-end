/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.petstar.model;

import java.util.List;
import org.petstar.dto.UserDTO;

/**
 * Modelado de JSON para usuarios
 * @author Tech-Pro
 */
public class UserETADResponseJson {
    private UserDTO userETAD;
    private List<UserDTO> listUserETAD;

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
}
