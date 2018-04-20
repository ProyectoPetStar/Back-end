/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.petstar.model;
import org.petstar.dto.UserSonarthDTO;
import java.util.List;
/**
 * Modelado de JSON para usuarios Sonarh
 * @author Tech-Pro
 */
public class UserSonarthResponseJson {
    private UserSonarthDTO usuarioSonarth;
    
    private List<UserSonarthDTO> list;

    public UserSonarthDTO getUsuarioSonarth() {
        return usuarioSonarth;
    }

    public void setUsuarioSonarth(UserSonarthDTO usuarioSonarth) {
        this.usuarioSonarth = usuarioSonarth;
    }

    public List<UserSonarthDTO> getList() {
        return list;
    }

    public void setList(List<UserSonarthDTO> list) {
        this.list = list;
    }

 
    
    
    
    
}
