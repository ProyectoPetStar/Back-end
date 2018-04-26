package org.petstar.dao;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import javax.sql.DataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.petstar.configurations.PoolDataSource;
import org.petstar.dto.RazonParoDTO;

/**
 *
 * @author Tech-Pro
 */
public class RazonParoDAO {
     public List<RazonParoDTO> getAllRazones() throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT id_razon_paro, valor, id_fuente_paro ")
                .append("FROM pet_cat_razon_paro ")
                .append("WHERE activo = 1");
       
        ResultSetHandler rsh = new BeanListHandler(RazonParoDTO.class);
        List<RazonParoDTO> data = (List<RazonParoDTO>) qr.query(sql.toString(), rsh);
        
        return data;
    }
     
     public List<RazonParoDTO> getFallasByOEE(Date fechaIn, Date fechaTe, int idLinea, int idFuente) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_selectReporte_petFalla ?, ?, ?, ?");
        Object[] params = {
            idFuente, fechaIn, fechaTe, idLinea
        };
       
        ResultSetHandler rsh = new BeanListHandler(RazonParoDTO.class);
        List<RazonParoDTO> data = (List<RazonParoDTO>) qr.query(sql.toString(), rsh, params);
        
        return data;
     }
}
