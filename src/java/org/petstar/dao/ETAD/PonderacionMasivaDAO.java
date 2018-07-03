package org.petstar.dao.ETAD;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import javax.sql.DataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.petstar.configurations.PoolDataSource;
import org.petstar.dto.ResultInteger;

/**
 *
 * @author Tech-Pro
 */
public class PonderacionMasivaDAO {
    public ResultInteger saveFile(String nameFile, int usuario, Date fecha) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_insertPetArchivoKpi ?, ?, ?");
        Object[] params = { nameFile, usuario, fecha};
        
        ResultSetHandler rsh = new BeanHandler(ResultInteger.class);
        ResultInteger result = (ResultInteger) qr.query(sql.toString(), rsh, params);
        return result;
    }
    
    public void insertPonderacionObjetivosOperativos(List<HashMap> data, int archivo) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        StringBuilder sql1 = new StringBuilder();
        sql1.append("DELETE FROM pet_tmp_ponderacion_objetivo_operativo WITH (TABLOCK) WHERE anio=?");
        Object[] params1 = {data.get(0).get("year")};
        qr.update(sql1.toString(),params1);
        
        sql.append("EXEC sp_insertTmpPonderacionObjOperativo ?, ?, ?, ?");
        for(int i=0; i<data.size(); i++){
            Object[] params = { data.get(i).get("year"), data.get(i).get("ponderacion"),
                    data.get(i).get("objetivo"), archivo };
            
            qr.update(sql.toString(),params);
        }
    }
    
    public void insertPonderacionKPIOperativos(List<HashMap> data, int archivo) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("INSERT INTO pet_tmp_ponderacion_kpi_operativo ")
                .append("(anio,valor,id_kpi_operativo,id_archivo_kpi) VALUES (?,?,?,?)");
        for(int i=0; i<data.size(); i++){
            Object[] params = { data.get(i).get("year"), data.get(i).get("ponderacion"),
                    data.get(i).get("kpi"), archivo };
            
            qr.update(sql.toString(),params);
        }
    }
    
    public ResultInteger validateExistDataObjetivosOperativos(int anio) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT COUNT(1) AS result ")
                .append("FROM pet_ponderacion_objetivo_operativo WHERE anio = ?");
        Object[] params = { anio };
        
        ResultSetHandler rsh = new BeanHandler(ResultInteger.class);
        ResultInteger result = (ResultInteger) qr.query(sql.toString(), rsh, params);
        return result;
    }
    
    public void loadDataObjetivosOperativos(int anio) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_insertPetPonderacionObjOperativo ?");
        Object[] params = { anio };
        
        qr.update(sql.toString(), params);
    }
    
    public void rewriteDataAnualObjetivosOperativos(int year) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_UpdatePonderacionObjetivoOperativo ?");
        Object[] params = { year };
        
        qr.update(sql.toString(), params);
    }
}
