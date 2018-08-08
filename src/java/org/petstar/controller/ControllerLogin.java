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

    /**
     * Logueo
     * Metodo que se encarga de validar los datos de usuario y brindar accesos 
     * al sistema.
     * @param request
     * @return 
     */
    public OutputJson Login(HttpServletRequest request) {
        String usuario_acceso = request.getParameter("usuario_acceso");
        String clave_acceso = request.getParameter("clave_acceso");
        int id_sistemas = Integer.parseInt(request.getParameter("id_sistemas"));
        int numEmpleado = Integer.parseInt(usuario_acceso);
        UserResponseJson response = new UserResponseJson();
        OutputJson output = new OutputJson();

        LoginDAO dao = new LoginDAO();
        ControllerAutenticacion auth = new ControllerAutenticacion();
        try {
            UserDTO datos_usuario = dao.Login(numEmpleado, clave_acceso, id_sistemas);
            
            if (datos_usuario != null) {
                String token = auth.createJWT(datos_usuario);
                datos_usuario.setToken(token);
                response.setSucessfull(true);
                response.setUsuario(datos_usuario);
            } else {
                response.setSucessfull(false);
                response.setMessage("Usuario o contraseña incorrectos");
            }
        } catch (Exception ex) {
            response.setSucessfull(false);
            response.setMessage("" + ex.getMessage());
        }
        output.setResponse(response);
        return output;

    }

}
