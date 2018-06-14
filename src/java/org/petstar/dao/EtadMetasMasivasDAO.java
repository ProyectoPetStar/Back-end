/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.petstar.dao;

import java.util.HashMap;
import java.util.List;
import javax.sql.DataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.petstar.configurations.PoolDataSource;
import org.petstar.dto.CatalogosDTO;

/**
 *
 * @author Tech-Pro
 */
public class EtadMetasMasivasDAO {
    public void insertTMPObjetivosOperativos(List<HashMap> data) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_InsertPetTmpMetaAnualObjetivoEstrategico ?, ?, ?, ?, ?, ?");
        for(int i=0; i<data.size(); i++){
            Object[] params = { data.get(i).get("idEtad"), data.get(i).get("objetivo"), 
                    data.get(i).get("year"), data.get(i).get("meta"),
                    data.get(i).get("usuario"), data.get(i).get("fecha") };
            
            qr.update(sql.toString(),params);
        }
    }
}
