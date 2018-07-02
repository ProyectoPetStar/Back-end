/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
public class MetasMasivasDAO {
    public void insertMetasEstrategicasAnuales(List<HashMap> data, int archivo) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_insertpetTmpMetaAnualEstrategica ?, ?, ?, ?, ?, ?, ?");
        for(int i=0; i<data.size(); i++){
            Object[] params = { data.get(i).get("idEtad"), data.get(i).get("meta"), 
                    data.get(i).get("year"), data.get(i).get("valor"), archivo,
                    data.get(i).get("usuario"), data.get(i).get("fecha") };
            
            qr.update(sql.toString(),params);
        }
    }
    
    public void insertMetasEstrategicasMensual(List<HashMap> data, int archivo) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("");
        for(int i=0; i<data.size(); i++){
            Object[] params = {  };
            
            qr.update(sql.toString(),params);
        }
    }
    
    public void insertObjetivosOperativosAnual(List<HashMap> data, int archivo) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_InsertPetTmpMetaAnualObjetivoEstrategico ?, ?, ?, ?, ?, ?, ?");
        for(int i=0; i<data.size(); i++){
            Object[] params = { data.get(i).get("idEtad"), data.get(i).get("objetivo"), 
                    data.get(i).get("year"), data.get(i).get("meta"), archivo,
                    data.get(i).get("usuario"), data.get(i).get("fecha") };
            
            qr.update(sql.toString(),params);
        }
    }
    
    public void insertObjetivosOperativosMensual(List<HashMap> data, int archivo) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("");
        for(int i=0; i<data.size(); i++){
            Object[] params = {  };
            
            qr.update(sql.toString(),params);
        }
    }
    
    public ResultInteger insertFileKPI(String nameFile, int usuario, Date fecha) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_insertPetArchivoKpi ?, ?, ?");
        Object[] params = { nameFile, usuario, fecha};
        
        ResultSetHandler rsh = new BeanHandler(ResultInteger.class);
        ResultInteger result = (ResultInteger) qr.query(sql.toString(), rsh, params);
        return result;
    }
    
    public void insertKPIOperativosAnual(List<HashMap> data, int archivo) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_insertpetTmpMetaAnualKpiOperativo ?, ?, ?, ?, ?, ?, ?");
        for(int i=0; i<data.size(); i++){
            Object[] params = { data.get(i).get("idEtad"), data.get(i).get("kpi"), 
                    data.get(i).get("year"), data.get(i).get("meta"), archivo,
                    data.get(i).get("usuario"), data.get(i).get("fecha") };
            
            qr.update(sql.toString(),params);
        }
    }
    
    public void insertKPIOperativosMensual(List<HashMap> data, int archivo) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("");
        for(int i=0; i<data.size(); i++){
            Object[] params = {  };
            
            qr.update(sql.toString(),params);
        }
    }
    
    public void loadDataAnualMetasEstrategicas(int idEtad, int year) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_insertPetMetaAnualEstrategica ?, ?");
        Object[] params = { idEtad, year };
        
        qr.update(sql.toString(), params);
    }
    
    public void loadDataMensualMetasEstrategicas(int idEtad, int idPeriodo) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("");
        Object[] params = { idEtad, idPeriodo };
        
        qr.update(sql.toString(), params);
    }
    
    public void loadDataAnualObjetivosOperativos(int idEtad, int year) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_insertPetMetaAnualObjetivoOperativo ?, ?");
        Object[] params = { idEtad, year };
        
        qr.update(sql.toString(), params);
    }
    
    public void loadDataMensualObjetivosOperativos(int idEtad, int IdPeriodo) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("");
        Object[] params = { idEtad, IdPeriodo };
        
        qr.update(sql.toString(), params);
    }
    
    public void loadDataAnualKPIOperativos(int idEtad, int year) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_insertPetMetaAnualKpi ?, ?");
        Object[] params = { idEtad, year };
        
        qr.update(sql.toString(), params);
    }
    
    public void rewriteDataAnualMetasEstrategicas(int idEtad, int year) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC  ?, ?");
        Object[] params = { idEtad, year };
        
        qr.update(sql.toString(), params);
    }
    
    public void rewriteDataAnualObjetivosOperativos(int idEtad, int year) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC  ?, ?");
        Object[] params = { idEtad, year };
        
        qr.update(sql.toString(), params);
    }
    
    public void rewriteDataAnualKPIOperativos(int idEtad, int year) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC  ?, ?");
        Object[] params = { idEtad, year };
        
        qr.update(sql.toString(), params);
    }
    
    public ResultInteger validateExistDataMetasEstrategiasAnuales(int idEtad, int year) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT COUNT(1) AS result FROM pet_meta_anual_estrategica WHERE id_linea = ? AND anio = ?");
        Object[] params = { idEtad, year };
        
        ResultSetHandler rsh = new BeanHandler(ResultInteger.class);
        ResultInteger result = (ResultInteger) qr.query(sql.toString(), rsh, params);
        return result;
    }
    
    public ResultInteger validateExistDataMetasEstrategiasMensual(int idEtad, int idPeriodo) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("");
        Object[] params = { idEtad, idPeriodo };
        
        ResultSetHandler rsh = new BeanHandler(ResultInteger.class);
        ResultInteger result = (ResultInteger) qr.query(sql.toString(), rsh, params);
        return result;
    }
    
    public ResultInteger validateExistDataMetasOperativasAnuales(int idEtad, int year) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT COUNT(1) AS result FROM pet_meta_anual_objetivo_operativo WHERE id_linea = ? AND anio = ?");
        Object[] params = { idEtad, year };
        
        ResultSetHandler rsh = new BeanHandler(ResultInteger.class);
        ResultInteger result = (ResultInteger) qr.query(sql.toString(), rsh, params);
        return result;
    }
    
    public ResultInteger validateExistDataMetasOperativasMensual(int idEtad, int idPeriodo) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("");
        Object[] params = { idEtad, idPeriodo };
        
        ResultSetHandler rsh = new BeanHandler(ResultInteger.class);
        ResultInteger result = (ResultInteger) qr.query(sql.toString(), rsh, params);
        return result;
    }
    
    public ResultInteger validateExistDataMetasKPIOperativoAnuales(int idEtad, int year) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT COUNT(1) AS result FROM pet_meta_anual_kpi WHERE id_linea = ? AND anio = ?");
        Object[] params = { idEtad, year };
        
        ResultSetHandler rsh = new BeanHandler(ResultInteger.class);
        ResultInteger result = (ResultInteger) qr.query(sql.toString(), rsh, params);
        return result;
    }
}
