/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.petstar.dao;

import java.util.List;
import javax.sql.DataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.petstar.configurations.PoolDataSource;
import org.petstar.dto.EquiposDTO;

/**
 *
 * @author Tech-PRo
 */
public class EquiposDAO {
     public List<EquiposDTO> getAllEquiposByIdLinea(int idLinea) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT equ.id_equipos, equ.valor ")
                .append("FROM pet_cat_equipos equ ")
                .append("INNER JOIN pet_equipo_linea el ")
                .append("ON equ.id_equipos = el.id_equipo ")
                .append("INNER JOIN pet_cat_linea li ")
                .append("ON el.id_linea = li.id_linea ")
                .append("WHERE li.id_linea = ? AND equ.activo = 1");
        Object[] params = {
             idLinea
        };
        ResultSetHandler rsh = new BeanListHandler(EquiposDTO.class);
        List<EquiposDTO> data = (List<EquiposDTO>) qr.query(sql.toString(), rsh, params);
        
        return data;
    }
}
