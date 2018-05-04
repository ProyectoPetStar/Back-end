/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.petstar.controller;

import java.sql.Date;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import org.petstar.dao.CatalogosDAO;
import org.petstar.dao.LineasDAO;
import org.petstar.dao.UsersDAO;
import org.petstar.dto.ResultInteger;
import org.petstar.model.OutputJson;
import org.petstar.model.ResponseJson;
import org.petstar.model.UserETADResponseJson;
import org.petstar.model.UserResponseJson;
import org.petstar.model.UserSonarhResponseJson;
import static org.petstar.configurations.utils.getCurrentDate;
import org.petstar.dto.UserDTO;

/**
 * 
 * @author Tech-Pro
 */
public class ControllerUsers {
    private static final String TABLE_GRUPOS = "pet_cat_grupo";
    private static final String TABLE_PERFIL = "pet_cat_perfil";
    
    /**
     * Lista de usuarios Sonarh
     * Metodo que devuelve la lista de usuarios de sonarh conforme al modelo.
     * @param request
     * @return 
     */
    public OutputJson getUsersSonarh(HttpServletRequest request) {
        
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion auth = new ControllerAutenticacion();
        
        try {
            UserDTO usuario = auth.isValidToken(request);
            if (usuario != null) {
                UsersDAO dao = new UsersDAO();
                UserSonarhResponseJson json = new UserSonarhResponseJson();
                json.setListUserSonarh(dao.getUsersSonarh());
                output.setData(json);
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
     * Consulta de usuario por id
     * Metodo que devuelve la información del usuario logueado
     * @param request
     * @return 
     */
    public OutputJson getUserETADById(HttpServletRequest request){
        int idAcceso = Integer.parseInt(request.getParameter("id_acceso"));
        UserResponseJson response = new UserResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion auth = new ControllerAutenticacion();
        
        try {
            UserDTO usuario = auth.isValidToken(request);
            if (usuario != null) {
                UsersDAO userDAO = new UsersDAO();
                CatalogosDAO catalogosDAO = new CatalogosDAO();
                LineasDAO lineasDAO = new LineasDAO();
                UserETADResponseJson data = new UserETADResponseJson();
                
                data.setUserETAD(userDAO.getUserEtadByID(idAcceso));
                data.setListLineas(lineasDAO.getLineasActive());
                data.setListGrupos(catalogosDAO.getCatalogosActive(TABLE_GRUPOS));
                data.setListPerfiles(catalogosDAO.getCatalogosActive(TABLE_PERFIL));
                
                output.setData(data);
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
     * Perfil Sonarh
     * Metodo para obtener datos de los diferentes usuarios de Sonarh
     * @param request
     * @return 
     */
    public OutputJson getPerfilUserSonarhById(HttpServletRequest request){
        int numeroEmpleado = Integer.parseInt(request.getParameter("numero_empleado"));
        UserResponseJson response = new UserResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        
        try{
            UserDTO usuario = autenticacion.isValidToken(request);
            if(usuario != null){
                UsersDAO userDAO = new UsersDAO();
                CatalogosDAO catalogosDAO = new CatalogosDAO();
                LineasDAO lineasDAO = new LineasDAO();
                ResultInteger result = userDAO.validaExistUsers(numeroEmpleado);
                if(result.getResult().equals(1)){
                    UserSonarhResponseJson userData = new UserSonarhResponseJson();
                    userData.setUsuarioSonarh(userDAO.getUserSonarhById(numeroEmpleado));
                    userData.setListGrupos(catalogosDAO.getCatalogosActive(TABLE_GRUPOS));
                    userData.setListPerfiles(catalogosDAO.getCatalogosActive(TABLE_PERFIL));
                    userData.setListLineas(lineasDAO.getLineasActive());

                    output.setData(userData);
                    response.setSucessfull(true);
                    response.setMessage("OK");
                }else{
                    response.setSucessfull(false);
                    response.setMessage("Usuario incorrecto");
                }
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
     * Cambio de contraseña
     * Metodo que permite el cambio del password del usuario, validando su contraseña anterior
     * @param request
     * @return 
     */
    public OutputJson changePasswordUser(HttpServletRequest request){
        int idAcceso = Integer.parseInt(request.getParameter("id_acceso"));
        String newPassword = request.getParameter("new_password");
        
        UserResponseJson response = new UserResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion auth = new ControllerAutenticacion();
        
        try {
            UserDTO usuario = auth.isValidToken(request);
            if (usuario != null) {
                UsersDAO userDAO = new UsersDAO();
                
                userDAO.changePassword(newPassword, idAcceso);
                    
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
     * Alta de usuarios
     * Metodo que registra un nuevo usuario para el sistema.
     * @param request
     * @return 
     */
    public OutputJson insertUsersETAD(HttpServletRequest request){
        int numeroEmpleado = Integer.parseInt(request.getParameter("numero_empleado"));
        int idLinea = Integer.parseInt(request.getParameter("id_linea"));
        int idGrupo = Integer.parseInt(request.getParameter("id_grupo"));
        String perfiles = request.getParameter("perfiles");
        String[] listPerfiles = perfiles.split(",");
        
        UserResponseJson response = new UserResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion auth = new ControllerAutenticacion();
        
        try {
            UserDTO usuario = auth.isValidToken(request);
            if (usuario != null) {
                UsersDAO userDAO = new UsersDAO();
                ResultInteger existeUSuario = userDAO.validaExistUsers(numeroEmpleado);
                if(existeUSuario.getResult().equals(1)){
                    Date fecha = getCurrentDate();
                    userDAO.insertNewUser(numeroEmpleado, idLinea, idGrupo, fecha, 1, usuario.getId_acceso());

                    ResultInteger result = userDAO.getIdUserByNumeroEmpleado(numeroEmpleado);
                    if(null != result){
                        for(String perfil:listPerfiles){
                            userDAO.registraPerfilByUser(result.getResult(), Integer.parseInt(perfil));

                            response.setMessage("OK");
                            response.setSucessfull(true);
                        }
                    }else{
                        response.setMessage("Error");
                        response.setSucessfull(false);
                    }
                }else{
                    response.setMessage("No existe el usuario de Sonarh");
                    response.setSucessfull(false);
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
     * Modificación de Usuario
     * Metodo que permite la actualización de datos de un usuario.
     * @param request
     * @return 
     */
    public OutputJson updateUserETAD(HttpServletRequest request){
        int idAcceso = Integer.parseInt(request.getParameter("id_acceso"));
        int idGrupo = Integer.parseInt(request.getParameter("id_grupo"));
        int idLinea = Integer.parseInt(request.getParameter("id_linea"));
        String perfiles = request.getParameter("perfiles");
        
        UserResponseJson response = new UserResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion auth = new ControllerAutenticacion();
        
        try {
            UserDTO usuario = auth.isValidToken(request);
            if (usuario != null) {
                UsersDAO userDAO = new UsersDAO();
                
                Date fecha = getCurrentDate();
                userDAO.updateUserETAD(idAcceso, idLinea, idGrupo, usuario.getId_acceso(), fecha);
                String[] listPerfiles = perfiles.split(",");
                for(String perfil:listPerfiles){
                    userDAO.registraPerfilByUser(idAcceso, Integer.parseInt(perfil));

                    response.setMessage("OK");
                    response.setSucessfull(true);
                }
                
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
     * Lista de usuarios ETAD
     * Metodo que devuelve la lista de usuarios validos para firmarse en el sistema.
     * @param request
     * @return 
     */
    public OutputJson getUsersETAD(HttpServletRequest request){
        UserResponseJson response = new UserResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion auth = new ControllerAutenticacion();
        
        try {
            UserDTO usuario = auth.isValidToken(request);
            if (usuario != null) {
                UsersDAO dao = new UsersDAO();
                UserETADResponseJson list = new UserETADResponseJson();
                list.setListUserETAD(dao.getUsersETAD());
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
     * Eliminación de usuarios.
     * Metodo para eliminar un usuario en especifico
     * @param request
     * @return 
     */
    public OutputJson deleteUsersETAD(HttpServletRequest request){
        int idAcceso = Integer.parseInt(request.getParameter("id_acceso"));
        int activo = Integer.parseInt(request.getParameter("activo"));
        UserResponseJson response = new UserResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        
        try{
            UserDTO usuario = autenticacion.isValidToken(request);
            if(usuario != null){
                UsersDAO usersDAO = new UsersDAO();
                usersDAO.deleteUsersETAD(idAcceso, activo);
                
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
    
    public OutputJson getMiPerfil(HttpServletRequest request) throws Exception{
        UserResponseJson response = new UserResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        
        try{
            UserDTO sesion = autenticacion.isValidToken(request);
            if(sesion != null){
                UsersDAO usersDAO = new UsersDAO();
                CatalogosDAO catalogosDAO = new CatalogosDAO();
                LineasDAO lineasDAO = new LineasDAO();
                UserETADResponseJson data = new UserETADResponseJson();
                
                data.setUserETAD(usersDAO.getUserEtadByID(sesion.getId_acceso()));
                data.setListGrupos(catalogosDAO.getCatalogosActive(TABLE_GRUPOS));
                data.setListPerfiles(catalogosDAO.getCatalogosActive(TABLE_PERFIL));
                data.setListLineas(lineasDAO.getLineasActive());
                output.setData(data);
                
                response.setMessage("OK");
                response.setSucessfull(true);
            }else{
                response.setMessage("Inicie sesión nuevamente");
                response.setSucessfull(false);
            }
        }catch(SQLException ex){
            response.setMessage("Descripción Error: " + ex.getMessage());
            response.setSucessfull(false);
        }
        output.setResponse(response);
        return output;
    }
}
