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
import org.petstar.dto.UserDTO;

/**
 *
 * @author Tech-Pro
 */

public class LoginDAO {

    /**
     * Logueo
     * Metodo que se encarga de validar que los datos sean correctos y devuelve 
     * la información del usuario.
     * @param usuario_acceso
     * @param clave_acceso
     * @param id_sistemas
     * @return
     * @throws Exception 
     */
    public UserDTO Login(int usuario_acceso, String clave_acceso, int id_sistemas) throws Exception {
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_petLogin ?, ?, ?");
        Object[] params = {
            usuario_acceso, clave_acceso, id_sistemas
        };
        
        ResultSetHandler rsh = new BeanHandler(UserDTO.class);
        UserDTO datos_usuario = (UserDTO) qr.query(sql.toString(), rsh, params);
        return datos_usuario;
    }
    
}
