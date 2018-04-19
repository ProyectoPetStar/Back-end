package org.petstar.dao;

import java.sql.Date;
import java.util.List;
import javax.sql.DataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.petstar.configurations.PoolDataSource;
import org.petstar.dto.ForecastDTO;
import org.petstar.dto.ResultInteger;

/**
 * @author Tech-Pro
 */
public class ForecastDAO {
    
    public void saveFile(String nameFile, int idPeriodo, int idLInea, 
            int idUSuario, Date fecha) throws Exception{
        
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_insert_petArchivo ?, ?, ?, ?, ?");
        Object[] params = {
            nameFile, idPeriodo, idLInea, idUSuario, fecha
        };
        
        qr.update(sql.toString(), params);
    }
    
    public ResultInteger getIdFile(String nameFile) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_selectId_petArchivo ?");
        Object[] params = {
            nameFile
        };
        
        ResultSetHandler rsh = new BeanHandler(ResultInteger.class);
        ResultInteger result = (ResultInteger) qr.query(sql.toString(), rsh, params);
        return result;
    }
    
    public void loadForecast(List<ForecastDTO> listRows, int idLinea, int idFile, int idPerido) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        this.cleanTable(idLinea, idPerido);
        sql.append("EXEC sp_insert_petTmpMetas ?, ?, ?, ?, ?, ?, ?, ?, ?");
        
        for(ForecastDTO row : listRows){
            Object[] params = {
                row.getDia(), row.getMeta(), row.getTmp(), row.getVelocidad(), 
                row.getTurno(), row.getGrupo(), idLinea, idFile, idPerido
            };

            qr.update(sql.toString(), params);
	}
    }
    
    public void procesarFile(int idLinea, int idPeriodo) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_insert_petMetas ?, ?");
        Object[] params = {
            idLinea, idPeriodo
        };
        
        qr.update(sql.toString(), params);
    }
    
    public void cleanTable(int idLinea, int idPeriodo) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("DELETE FROM pet_tmp_meta WHERE id_periodo = ? AND id_linea = ?");
        Object[] params = {
            idPeriodo, idLinea
        };
        
        qr.update(sql.toString(), params);
    }
}
