package org.petstar.dao;

import java.math.BigDecimal;
import java.util.List;
import javax.sql.DataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.petstar.configurations.PoolDataSource;
import org.petstar.dto.ProduccionDTO;

/**
 *
 * @author Tech-Pro
 */
public class ProduccionDAO {
    
    public List<ProduccionDTO> getProduccionByPeriodo(int mes, int anio) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_selectPetProduccionByAnioMes ?, ?");
        Object[] params = { mes, anio };
        
        ResultSetHandler rsh = new BeanListHandler(ProduccionDTO.class);
        List<ProduccionDTO> list = (List<ProduccionDTO>) qr.query(sql.toString(), rsh, params);
        return list;
    }
    
    public List<ProduccionDTO> getProduccionByPeriodoAndLinea(int mes, int anio, int idLinea) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_selectProduccionByPeriodo ?, ?, ?");
        Object[] params = { mes, anio, idLinea };
        
        ResultSetHandler rsh = new BeanListHandler(ProduccionDTO.class);
        List<ProduccionDTO> list = (List<ProduccionDTO>) qr.query(sql.toString(), rsh, params);
        return list;
    }
    
    public void insertProduccion(int idMeta, int idProducto, BigDecimal valor, int idUsuario) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_insertPetProduccion ?, ?, ?, ?");
        Object[] params = { valor, idMeta, idProducto, idUsuario};
        
        qr.update(sql.toString(), params);
    }
}
