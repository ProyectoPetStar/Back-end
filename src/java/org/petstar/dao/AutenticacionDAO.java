/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.petstar.dao;

import org.petstar.configurations.PoolDataSource;
import javax.sql.DataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.petstar.dto.ResultString;
import org.petstar.dto.UserDTO;

/**
 *
 * @author Tech-Pro
 */

public class AutenticacionDAO {

    public String getToken_Key(int id_usuario) throws Exception {
        DataSource ds = PoolDataSource.getDataSource();
      
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        sql.append("EXEC sp_SelectTokenPetUsuario ?");
        Object[] params = {
            id_usuario
        };
        ResultSetHandler rsh = new BeanHandler(ResultString.class);
        ResultString result = (ResultString) qr.query(sql.toString(), rsh, params);
        
        return result.getResult();
    }
    
    public void updateToken_Key(int id_usuario, String token_key) throws Exception {
        DataSource ds = PoolDataSource.getDataSource();
      
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        sql.append("EXEC sp_updateTokenPetUsuario ?, ?");
        Object[] params = {
            id_usuario, token_key
        };
        
        qr.update(sql.toString(), params);
    }

}
