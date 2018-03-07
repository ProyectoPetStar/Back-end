/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.petstar.controller;


import javax.servlet.http.HttpServletRequest;
import org.petstar.dao.LoginDAO;
import org.petstar.dto.UserDTO;
import org.petstar.model.OutputJson;
import org.petstar.model.UserResponseJson;

/**
 *
 * @author Tech-Pro
 */
public class ControllerLogin {

    public OutputJson Login(HttpServletRequest request) {
        String usuario_acceso = request.getParameter("usuario_acceso");
        String clave_acceso = request.getParameter("clave_acceso");
        int id_sistemas = Integer.parseInt(request.getParameter("id_sistemas"));
        UserResponseJson response = new UserResponseJson();
        OutputJson output = new OutputJson();

        LoginDAO dao = new LoginDAO();
        ControllerAutenticacion auth = new ControllerAutenticacion();
        try {
            UserDTO datos_usuario = dao.Login(usuario_acceso, clave_acceso, id_sistemas);
            if (datos_usuario.getUsuario_acceso() != null) {
                String token = auth.createJWT(datos_usuario.getId_usuario(), datos_usuario.getUsuario_acceso(), datos_usuario.getPerfil());
                datos_usuario.setToken(token);
                response.setSucessfull(true);
                response.setUsuario(datos_usuario);
            } else {
                response.setSucessfull(false);
                response.setMessage("Nombre de usuario o contraseña incorrectos");
            }
        } catch (Exception ex) {
            response.setSucessfull(false);
            response.setMessage("Descripcion de error: " + ex.getMessage());
        }
        output.setResponse(response);
        return output;

    }

}
