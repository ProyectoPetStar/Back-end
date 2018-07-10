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
    
    public void cleanTmpKPI(int idPeriodo) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("DELETE FROM pet_tmp_meta_kpi WITH(TABLOCK) WHERE id_periodo = ?");
        Object[] params = { idPeriodo };
        
        qr.update(sql.toString(), params);
    }
    
    public void insertMetasKPIOperativos(List<HashMap> data, int archivo) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_insertPetTmpMetaKpi ?, ?, ?, ?, ?, ?");
        for(int i=0; i<data.size(); i++){
            Object[] params = {  data.get(i).get("meta"), data.get(i).get("periodo"), 
                    data.get(i).get("kpi"), 1, archivo, data.get(i).get("idEtad") };
            
            qr.update(sql.toString(),params);
        }
    }
    
    public void loadMetasKPIOperativos(int idEtad, int idPeriodo) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_insertPetMetaKpi ?, ?");
        Object[] params = { idPeriodo, idEtad };
        
        qr.update(sql.toString(), params);
    }
    
    public ResultInteger validateExistMetasKPIOperativo(int idEtad, int idPeriodo) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT COUNT(1) AS result FROM pet_meta_kpi pmk INNER JOIN ")
                .append("pet_etad_kpi pek ON pmk.id_kpi_etad = pek.id_kpi_etad ")
                .append("WHERE pmk.id_periodo = ").append(idPeriodo)
                .append(" AND pek.id_etad = ").append(idEtad);
        
        ResultSetHandler rsh = new BeanHandler(ResultInteger.class);
        ResultInteger result = (ResultInteger) qr.query(sql.toString(), rsh);
        return result;
    }
    
    public void rewriteDataKPIOperativos(int idEtad, int idPeriodo, int usuario, Date fecha) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_UpdatePetMetaKpi ?, ?, ?, ?");
        Object[] params = { idPeriodo, idEtad, usuario, fecha };
        
        qr.update(sql.toString(), params);
    }
    
}
