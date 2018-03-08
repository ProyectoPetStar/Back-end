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
import org.petstar.dto.UserDTO;
import org.petstar.dto.UserSonarthDTO;

/**
 *
 * @author Tech-Pro
 */

public class UsersDAO {

    public List<UserSonarthDTO> getUsersSonarh() throws Exception {
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        sql.append(" EXEC sp_SelectSonarh ");
         ResultSetHandler rsh = new BeanListHandler(UserSonarthDTO.class);
         List<UserSonarthDTO> usuarios_sonarh = (List<UserSonarthDTO>) qr.query(sql.toString(), rsh); 
        return usuarios_sonarh;
    }
    
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

}
