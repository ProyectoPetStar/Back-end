/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.petstar.model;
import org.petstar.dto.UserDTO;

/**
 * Modelado de JSON Usuarios
 * @author Tech-Pro
 */
public class UserResponseJson extends ResponseJson{
    private UserDTO usuario;
   
    public UserDTO getUsuario() {
        return usuario;
    }

    public void setUsuario(UserDTO usuario) {
        this.usuario = usuario;
    }
       
    
}
