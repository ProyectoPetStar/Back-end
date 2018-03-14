/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.petstar.controller;

import javax.servlet.http.HttpServletRequest;
import org.petstar.dao.UsersDAO;
import org.petstar.dto.ResultInteger;
import org.petstar.model.OutputJson;
import org.petstar.model.UserETADResponseJson;
import org.petstar.model.UserResponseJson;
import org.petstar.model.UserSonarthResponseJson;

/**
 *
 * @author Tech-Pro
 */
public class ControllerUsers {

    /**
     * Metodo que devuelve la lista de usuario de Sonarh
     * @param request
     * @return 
     */
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

    /**
     * Metodo que devuelve la información del usuario logueado
     * @param request
     * @return 
     */
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
    
    /**
     * Metodo para obtener datos de los diferentes usuarios de ETAD
     * @param request
     * @return 
     */
    public OutputJson getPerfilUserEtadById(HttpServletRequest request){
        int idUsuario = Integer.parseInt(request.getParameter("id_usuario_buscar"));
        UserResponseJson response = new UserResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        
        try{
            if(autenticacion.isValidToken(request)){
                UsersDAO userDAO = new UsersDAO();
                UserETADResponseJson userData = new UserETADResponseJson();
                userData.setUserDTO(userDAO.getPerfilUserSonarh(idUsuario));
                
                output.setData(userData);
                response.setSucessfull(true);
                response.setMessage("OK");
            }else{
                response.setSucessfull(false);
                response.setMessage("Inicie sesión nuevamente");
            }
        } catch (Exception ex){
            response.setSucessfull(false);
            response.setMessage("Error: " + ex.getMessage());
        }
        output.setResponse(response);
        return output;
    }

    /**
     * Metodo para obtener datos de los diferentes usuarios de Sonarh
     * @param request
     * @return 
     */
    public OutputJson getPerfilUserSonarhById(HttpServletRequest request){
        int idUsuarioSonarh = Integer.parseInt(request.getParameter("id_usuario_sonarh"));
        UserResponseJson response = new UserResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        
        try{
            if(autenticacion.isValidToken(request)){
                UsersDAO userDAO = new UsersDAO();
                UserSonarthResponseJson userData = new UserSonarthResponseJson();
                userData.setUsuarioSonarth(userDAO.getUserSonarhById(idUsuarioSonarh));
                
                output.setData(userData);
                response.setSucessfull(true);
                response.setMessage("OK");
            }else{
                response.setSucessfull(false);
                response.setMessage("Inicie sesión nuevamente");
            }
        } catch (Exception ex){
            response.setSucessfull(false);
            response.setMessage("Error: " + ex.getMessage());
        }
        output.setResponse(response);
        return output;
    }
    
    /**
     * Metodo que permite el cambio del password del usuario
     * @param request
     * @return 
     */
    public OutputJson changePasswordUser(HttpServletRequest request){
        int idUsuario = Integer.parseInt(request.getParameter("id_usuario"));
        String contraseniaAnterior = request.getParameter("contraseniaAnterior");
        String contraseniaNueva = request.getParameter("contraseniaNueva");
        
        UserResponseJson response = new UserResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion auth = new ControllerAutenticacion();
        
        try {
             
            if (auth.isValidToken(request)) {
                UsersDAO userDAO = new UsersDAO();
                ResultInteger resultado = userDAO.validaPassword(contraseniaAnterior, idUsuario);
                if( resultado.getResult().equals(1)){
                    userDAO.changePassword(contraseniaNueva, idUsuario);
                    
                    response.setMessage("OK");
                    response.setSucessfull(true);
                    
                }else{
                    response.setSucessfull(false);
                    response.setMessage("Contraseña Invalida");
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
    
    /**
     * Metodo que registra un nuevo usuario para el sistema ETAD
     * @param request
     * @return 
     */
    public OutputJson insertNewUsersETAD(HttpServletRequest request){
        String nombre = request.getParameter("nombre");
        int idSonarh = Integer.parseInt(request.getParameter("id_sonarh"));
        int idLinea = Integer.parseInt(request.getParameter("id_linea"));
        int idGrupo = Integer.parseInt(request.getParameter("id_grupo"));
        int idPerfil = Integer.parseInt(request.getParameter("id_perfil"));
        int idTurno = Integer.parseInt(request.getParameter("id_turno"));
        String usuario = request.getParameter("usuario_acceso");
        
        UserResponseJson response = new UserResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion auth = new ControllerAutenticacion();
        
        try {
             
            if (auth.isValidToken(request)) {
                UsersDAO userDAO = new UsersDAO();
                
                userDAO.insertNewUser(nombre, idSonarh, idLinea, idGrupo, idTurno, usuario, idPerfil);
                response.setMessage("OK");
                response.setSucessfull(true);
                
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
    
    /** 
     * Metodo que permite la modificacion de un usuario
     * @param request
     * @return 
     */
    public OutputJson updatePerfilUser(HttpServletRequest request){
        int idUsuario = Integer.parseInt(request.getParameter("id_usuario_modificar"));
        int idPerfil = Integer.parseInt(request.getParameter("id_perfil"));
        int idTurno = Integer.parseInt(request.getParameter("id_turno"));
        int activo = Integer.parseInt(request.getParameter("activo"));
        
        UserResponseJson response = new UserResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion auth = new ControllerAutenticacion();
        
        try {
             
            if (auth.isValidToken(request)) {
                UsersDAO userDAO = new UsersDAO();
                
                userDAO.updatePerfilUser(idUsuario, idTurno, idPerfil, activo);
                response.setMessage("OK");
                response.setSucessfull(true);
                
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
    
    public OutputJson getUsersETAD(HttpServletRequest request){
        UserResponseJson response = new UserResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion auth = new ControllerAutenticacion();
        
        try {
             
            if (auth.isValidToken(request)) {
                UsersDAO dao = new UsersDAO();
                UserETADResponseJson list = new UserETADResponseJson();
                list.setListUserDTO(dao.getUsersETAD());
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
    
    /**
     * Metodo para eliminar usuarios ETAD
     * @param request
     * @return 
     */
    public OutputJson deleteUsersETAD(HttpServletRequest request){
        int idUser = Integer.parseInt(request.getParameter("id_usuario_delete"));
        UserResponseJson response = new UserResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        
        try{
            if(autenticacion.isValidToken(request)){
                UsersDAO usersDAO = new UsersDAO();
                usersDAO.deleteUsersETAD(idUser);
                
                response.setSucessfull(true);
                response.setMessage("OK");
            }else{
                response.setSucessfull(false);
                response.setMessage("Inicie sesión nuevamente");
            }
        } catch (Exception ex){
            response.setSucessfull(false);
            response.setMessage("Descripcion del Error: " + ex.getMessage());
        }
        output.setResponse(response);
        return output;
    }
}
