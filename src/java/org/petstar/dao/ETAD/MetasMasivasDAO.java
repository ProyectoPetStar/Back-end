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
    public void insertTMPObjetivosOperativos(List<HashMap> data, int archivo) throws Exception{
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
    
    public void insertKPIOperativos(List<HashMap> data, int archivo) throws Exception{
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
}
