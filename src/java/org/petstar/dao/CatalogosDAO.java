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
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.petstar.configurations.PoolDataSource;
import org.petstar.dto.CatalogosDTO;
import org.petstar.dto.UserSonarthDTO;

/**
 *
 * @author GuillermoB
 */
public class CatalogosDAO {
    public List<CatalogosDTO> getCatalogos(String tablename) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
      
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        sql.append("EXEC sp_selectPetCatalogos ?");
        Object[] params = {
            tablename
        };
        
        ResultSetHandler rsh = new BeanListHandler(CatalogosDTO.class);
        List<CatalogosDTO> data_catalogos = (List<CatalogosDTO>) qr.query(sql.toString(), rsh, params); 
        
        return data_catalogos;
    }
}
