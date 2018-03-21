/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.petstar.dao;

import java.util.List;
import javax.sql.DataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.petstar.configurations.PoolDataSource;
import org.petstar.dto.MetasDTO;
import org.petstar.dto.ResultInteger;

/**
 *
 * @author Tech-Pro
 */
public class MetasDAO {
    public List<MetasDTO> getMetasCarga() throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
      
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        sql.append("EXEC sp_selectPetCarMetas");
        
        ResultSetHandler rsh = new BeanListHandler(MetasDTO.class);
        List<MetasDTO> dataMetas = (List<MetasDTO>) qr.query(sql.toString(), rsh); 
        
        return dataMetas;
    }
    
    public MetasDTO getMetasCargaById(int idMeta) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_selectPetCarMetasById ?");
        Object[] params ={
            idMeta
        };
        ResultSetHandler rsh = new BeanHandler(MetasDTO.class);
        MetasDTO dataMetas = (MetasDTO) qr.query(sql.toString(), rsh, params); 
        
        return dataMetas;
    }
    
    public void insertMetaCarga(int idLinea, String meta, String tipoMedida) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
      
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        sql.append("EXEC sp_insertPetCarMetas ?, ?, ?");
        Object[] params = {
            idLinea, meta, tipoMedida
        };
        
        qr.update(sql.toString(), params);
    }
    
    public void updateMetaCarga(int idMeta, int idLinea, String meta, String tipoMedida, int posicion, int activo) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        sql.append("EXEC sp_updatePetCarMetas ?, ?, ?, ?, ?, ?");
        Object[] params = {
            idMeta, idLinea, meta, tipoMedida, posicion, activo
        };
        
        qr.update(sql.toString(), params);
    }
       
    public ResultInteger validaDataForInsertCarga(int idLinea, String meta) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_insertValidaDesPetCarMetas ?, ?");
        Object[] params = {
            idLinea, meta
        };
        
        ResultSetHandler rsh = new BeanHandler(ResultInteger.class);
        ResultInteger count = (ResultInteger)  qr.query(sql.toString(), rsh, params);

        return count;
    }
    
    public ResultInteger validaDataForUpdateCarga(int idMeta, int idLinea, String meta) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_updateValidaDesPetCarMetas ?, ?, ?");
        Object[] params = {
            idMeta, idLinea, meta
        };
        
        ResultSetHandler rsh = new BeanHandler(ResultInteger.class);
        ResultInteger count = (ResultInteger)  qr.query(sql.toString(), rsh, params);

        return count;
    }
    
    public ResultInteger validaIfExistMetaCarga(int idMeta) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_validaIdPetCarMetas ?");
        Object[] params = {
            idMeta
        };
        
        ResultSetHandler rsh = new BeanHandler(ResultInteger.class);
        ResultInteger count = (ResultInteger)  qr.query(sql.toString(), rsh, params);

        return count;
    }
    
    /**
     * Metodo para hacer la asignación de valores a meta
     * @throws Exception 
     */
    public void asignaValorMeta() throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("");
        Object[] params = {
            
        };
        
        qr.update(sql.toString(), params);
    }
}
