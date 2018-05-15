package org.petstar.dao;

import java.util.List;
import javax.sql.DataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
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
        sql.append("SELECT id_periodo, anio, mes, descripcion_mes, estatus FROM DBO.pet_periodo WHERE estatus=0");
                
        ResultSetHandler rsh = new BeanListHandler(PeriodosDTO.class);
        List<PeriodosDTO> data = (List<PeriodosDTO>) qr.query(sql.toString(), rsh); 
        
        return data;
    }
    
    public PeriodosDTO getPeriodoById(int idPeriodo) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
      
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        sql.append("EXEC sp_detallePeriodoMeta ?");
        Object[] params ={
            idPeriodo
        };
        
        ResultSetHandler rsh = new BeanHandler(PeriodosDTO.class);
        PeriodosDTO data = (PeriodosDTO) qr.query(sql.toString(), rsh, params); 
        
        return data;
    }
    
    public List<PeriodosDTO> getAllPeriodos() throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
      
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id_periodo, anio, mes, descripcion_mes, estatus FROM pet_periodo ORDER BY anio, mes");
                
        ResultSetHandler rsh = new BeanListHandler(PeriodosDTO.class);
        List<PeriodosDTO> data = (List<PeriodosDTO>) qr.query(sql.toString(), rsh); 
        
        return data;
    }
}
