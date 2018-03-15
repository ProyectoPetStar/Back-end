/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.petstar.dao;

import java.util.List;
import org.petstar.configurations.PoolDataSource;
import javax.sql.DataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.petstar.dto.ResultInteger;
import org.petstar.dto.UserDTO;
import org.petstar.dto.UserSonarthDTO;

/**
 *
 * @author Tech-Pro
 */

public class UsersDAO {

    /**
     * Metodo que devuelve lista de usuuarios Sonarh
     * @return
     * @throws Exception 
     */
    public List<UserSonarthDTO> getUsersSonarh() throws Exception {
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        sql.append(" EXEC sp_SelectSonarh ");
         ResultSetHandler rsh = new BeanListHandler(UserSonarthDTO.class);
         List<UserSonarthDTO> usuarios_sonarh = (List<UserSonarthDTO>) qr.query(sql.toString(), rsh); 
        return usuarios_sonarh;
    }
    
    /**
     * Metodo que devuelve los datos del usuario
     * @param idUsuario
     * @return
     * @throws Exception 
     */
    public UserDTO getPerfilUserSonarh(int idUsuario) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
      
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        sql.append("EXEC sp_selectPetUsuarioById ?");
        Object[] params = {
            idUsuario
        };
        ResultSetHandler rsh = new BeanHandler(UserDTO.class);
        UserDTO datosUsuario = (UserDTO) qr.query(sql.toString(), rsh, params);

        return datosUsuario;
    }
    
    /**
     * Metodo que devuelve los datos de usuarios sonarh
     * @param idUsuario
     * @return
     * @throws Exception 
     */
    public UserSonarthDTO getUserSonarhById(int idUsuarioSonarh) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
      
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        sql.append("EXEC sp_selectSonarhById ?");
        Object[] params = {
            idUsuarioSonarh
        };
        ResultSetHandler rsh = new BeanHandler(UserSonarthDTO.class);
        UserSonarthDTO datosUsuario = (UserSonarthDTO) qr.query(sql.toString(), rsh, params);

        return datosUsuario;
    }
    /**
     * Metodo que realiza la validacion del password del usuario
     * @param contraseniaAnterior
     * @param idUsuario
     * @return
     * @throws Exception 
     */
    public ResultInteger validaPassword(String contraseniaAnterior, int idUsuario) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
      
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        sql.append("EXEC sp_validaPetUsuarioByPass ?, ?");
        Object[] params = {
            idUsuario, contraseniaAnterior 
        };
        ResultSetHandler rsh = new BeanHandler(ResultInteger.class);
        ResultInteger resultInteger = (ResultInteger)  qr.query(sql.toString(), rsh, params);

        return resultInteger;
        
    }
    
    /**
     * Metodo que actualiza el password del usuario
     * @param contraseniaNueva
     * @param idUsuario
     * @throws Exception 
     */
    public void changePassword(String contraseniaNueva, int idUsuario) throws Exception{
        
       DataSource ds = PoolDataSource.getDataSource();
      
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        sql.append("EXEC sp_UpdatePassPetUsuarios ?, ?");
        Object[] params = {
            idUsuario, contraseniaNueva 
        };
        
        qr.update(sql.toString(), params);
       
    }

    /**
     * Metodo que actiualiza un usuario
     * @param idUsuario
     * @param idTurno
     * @param idPerfil
     * @param activo
     * @throws Exception 
     */
    public void updatePerfilUser(int idUsuario, int idTurno, int idPerfil, int activo) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
      
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        sql.append("EXEC sp_updatePetUsuarios ?, ?, ?, ?");
        Object[] params = {
            idUsuario, idTurno, idPerfil, activo
        };
        
        qr.update(sql.toString(), params);
    }
    
    /**
     * Metodo que permite registrar usuarios nuevos
     * @param nombre
     * @param idSonarh
     * @param idLinea
     * @param idGrupo
     * @param idTurno
     * @param usuarioAcceso
     * @param idPerfil
     * @throws Exception 
     */
    public void insertNewUser(String nombre, int idSonarh, int idLinea, int idGrupo, int idTurno, String usuarioAcceso, int idPerfil) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
      
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        sql.append("EXEC sp_insertPetUsuario ?, ?, ?, ?, ?, ?, ?");
        Object[] params = {
            nombre, idSonarh, idLinea, idGrupo, idTurno, usuarioAcceso, idPerfil
        };
        
        qr.update(sql.toString(), params);
    }
    
    /**
     * Metodo que devuelve la lista de los usuarios ETAD
     * @return
     * @throws Exception 
     */
    public List<UserDTO> getUsersETAD() throws Exception {
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        sql.append(" EXEC sp_selectPetUsuarios ");
        ResultSetHandler rsh = new BeanListHandler(UserDTO.class);
        List<UserDTO> usuariosETAD = (List<UserDTO>) qr.query(sql.toString(), rsh); 
        return usuariosETAD;
    }
    
    /**
     * Metodo para eliminar usuarios ETAD
     * @param idUsers
     * @throws Exception 
     */
    public void deleteUsersETAD(int idUsers) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_deletePetUsuarios ?");
        Object[] params = {
            idUsers
        };
        
        qr.update(sql.toString(), params);
    }
    
    public ResultInteger validaExistUsers(int idUserSonarh) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_insertPetValidaUsuario ?");
        Object[] params = {
            idUserSonarh
        };
        
        ResultSetHandler rsh = new BeanHandler(ResultInteger.class);
        ResultInteger result = (ResultInteger) qr.query(sql.toString(), rsh, params);
        return result;
    }
}
