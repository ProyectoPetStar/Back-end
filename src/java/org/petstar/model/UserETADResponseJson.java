/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.petstar.model;

import java.util.List;
import org.petstar.dto.UserDTO;

/**
 *
 * @author Tech-Pro
 */
public class UserETADResponseJson {
    private UserDTO userDTO;
    private List<UserDTO> listUserDTO;

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public List<UserDTO> getListUserDTO() {
        return listUserDTO;
    }

    public void setListUserDTO(List<UserDTO> listUserDTO) {
        this.listUserDTO = listUserDTO;
    }
    
}
