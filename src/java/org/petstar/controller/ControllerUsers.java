/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.petstar.controller;

import javax.servlet.http.HttpServletRequest;
import org.petstar.dao.UsersDAO;
import org.petstar.model.OutputJson;
import org.petstar.model.UserResponseJson;
import org.petstar.model.UserSonarthResponseJson;

/**
 *
 * @author Tech-Pro
 */
public class ControllerUsers {

    public OutputJson getUsersSonarh(HttpServletRequest request) {
        
        UserResponseJson response = new UserResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion auth = new ControllerAutenticacion();
        
        try {
             
            if (auth.isValidToken(request)) {
                UsersDAO dao = new UsersDAO();
                UserSonarthResponseJson list = new UserSonarthResponseJson();
                list.setList(dao.getUsersSonarh());
                output.setData(list);
                response.setSucessfull(true);
                response.setMessage("OK");
            } else {
                response.setSucessfull(false);
                response.setMessage("Inicie sesión nuevamente");
            }
        } catch (Exception ex) {
            response.setSucessfull(false);
            response.setMessage("Descripcion de error: " + ex.getMessage());
        }
        output.setResponse(response);
        return output;

    }

    public OutputJson getPerfilUserSonarh(HttpServletRequest request){
        int idUsuario = Integer.parseInt(request.getParameter("id_usuario"));
        UserResponseJson response = new UserResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion auth = new ControllerAutenticacion();
        
        try {
             
            if (auth.isValidToken(request)) {
                if(auth.id_usuario_valido(request) != "-1"){
                    UsersDAO userDAO = new UsersDAO();
                    UserResponseJson userResponseJson = new UserResponseJson();
                    //userSonarthResponseJson.setUsuarioSonarth(userDAO.getPerfilUserSonarh(idUsuario));
                    userResponseJson.setUsuario(userDAO.getPerfilUserSonarh(idUsuario));
                    output.setData(userResponseJson);
                    response.setMessage("OK");
                    response.setSucessfull(true);
                    
                }else{
                    response.setSucessfull(false);
                    response.setMessage("Usuario Incorrecto");
                }
            } else {
                response.setSucessfull(false);
                response.setMessage("Inicie sesión nuevamente");
            }
        } catch (Exception ex) {
            response.setSucessfull(false);
            response.setMessage("Descripcion de error: " + ex.getMessage());
        }
        output.setResponse(response);
        return output;
    }

}
