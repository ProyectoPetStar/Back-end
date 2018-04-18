package org.petstar.dao;

import java.util.List;
import javax.sql.DataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.petstar.configurations.PoolDataSource;
import org.petstar.dto.PeriodosDTO;

/**
 * @author Tech-Pro
 */
public class PeriodosDAO {
    
    public List<PeriodosDTO> getPeriodos() throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
      
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id_periodo, anio, mes, estatus FROM DBO.pet_periodo WHERE estatus=0");
                
        ResultSetHandler rsh = new BeanListHandler(PeriodosDTO.class);
        List<PeriodosDTO> data = (List<PeriodosDTO>) qr.query(sql.toString(), rsh); 
        
        return data;
    }
}
