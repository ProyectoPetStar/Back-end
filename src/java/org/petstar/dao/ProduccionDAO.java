package org.petstar.dao;

import java.math.BigDecimal;
import java.sql.Date;
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
    
    /**
     * Consulta Produccion por periodo
     * Metodo que devulve un listado de los dias con produccion registrada
     * @param mes
     * @param anio
     * @return
     * @throws Exception 
     */
    public List<ProduccionDTO> getProduccionByPeriodo(int mes, int anio, int idLinea) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_selectPetProduccionByAnioMes ?, ?, ?");
        Object[] params = { mes, anio, idLinea };
        
        ResultSetHandler rsh = new BeanListHandler(ProduccionDTO.class);
        List<ProduccionDTO> list = (List<ProduccionDTO>) qr.query(sql.toString(), rsh, params);
        return list;
    }
    
    public List<ProduccionDTO> getProduccionByPeriodoAndLinea(int mes, int anio, int idLinea, int idGrupo) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_selectProduccionByPeriodo ?, ?, ?, ?");
        Object[] params = { mes, anio, idLinea, idGrupo };
        
        ResultSetHandler rsh = new BeanListHandler(ProduccionDTO.class);
        List<ProduccionDTO> list = (List<ProduccionDTO>) qr.query(sql.toString(), rsh, params);
        return list;
    }
    
    public List<ProduccionDTO> getProduccionByPeriodoAndGpoln(int mes, int anio, int idGpolinea, int idGrupo) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_selectProduccionByPeriodoAndGrupoLinea ?, ?, ?, ?");
        Object[] params = { mes, anio, idGpolinea, idGrupo };
        
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
    
    public List<ProduccionDTO> getProduccionByIdMeta(int idMeta) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_detalleProduccion ?");
        Object[] params = { idMeta };
        
        ResultSetHandler rsh = new BeanListHandler(ProduccionDTO.class);
        List<ProduccionDTO> list = (List<ProduccionDTO>) qr.query(sql.toString(), rsh, params);
        return list;
    }
    
    public void updateProduccion(int idProduccion, BigDecimal valor, int idUsuario, Date fecha) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_updatePetProduccion ?, ?, ?, ?");
        Object[] params = { idProduccion, valor, fecha, idUsuario };
        
        qr.update(sql.toString(), params);
    }
    
    public List<ProduccionDTO> getProduccionForLiberar(int idGpoLinea, int idGrupo) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_selectValidaProduccion ?, ?");
        Object[] params = { idGrupo, idGpoLinea };
        
        ResultSetHandler rsh = new BeanListHandler(ProduccionDTO.class);
        List<ProduccionDTO> data = (List<ProduccionDTO>) qr.query(sql.toString(), rsh, params);
        return data;
    }
    
    public void liberarDatos(int idMeta, int estatus) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("UPDATE pet_meta SET estatus = ? WHERE id_meta = ?");
        Object[] params = { estatus, idMeta };
        
        qr.update(sql.toString(), params);
    }
}
