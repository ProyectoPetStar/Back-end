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
