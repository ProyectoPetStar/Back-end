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
import org.petstar.dto.LineasDTO;
import org.petstar.dto.ResultInteger;

/**
 *
 * @author Tech-Pro
 */
public class LineasDAO {
    
    /**
     * Metodo que devuelve lista de Lines en DB
     * @return
     * @throws Exception 
     */
    public List<LineasDTO> getLineasData() throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_selectPetCatLineas");
        
        ResultSetHandler rsh = new BeanListHandler(LineasDTO.class);
        List<LineasDTO> lineasData = (List<LineasDTO>) qr.query(sql.toString(), rsh);
                
        return lineasData;
    }
    
    /**
     * Metodo que registra una nueva linea en DB
     * @param descripcion
     * @param idGpoLinea
     * @throws Exception 
     */
    public void insertNewLinea(String descripcion, int idGpoLinea) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_insertPetCatLineas ?, ?");
        Object[] params = {
            descripcion, idGpoLinea
        };
        
        qr.update(sql.toString(), params);
    }
    
    /**
     * Metodo que actualiza un linea
     * @param idLinea
     * @param descripcion
     * @param activo
     * @param idGpoLinea
     * @throws Exception 
     */
    public void updateLinea(int idLinea, String descripcion, int activo, int idGpoLinea) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_updatePetCatLinea ?, ?, ?, ?");
        Object[] params = {
            idLinea, descripcion, activo, idGpoLinea
        };
        
        qr.update(sql.toString(), params);
    }
    
    /**
     * Metodo que elimina una Linea
     * @param idLinea
     * @throws Exception 
     */
    public void deleteLinea(int idLinea) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_deletePetCatLinea ?");
        Object[] params = {
            idLinea
        };
        
        qr.update(sql.toString(), params);
    }
    
    /**
     * Metodo que devuelve los datos de la linea de acuerdo al id
     * @param idLinea
     * @return
     * @throws Exception 
     */
    public LineasDTO getLineasDataById(int idLinea) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_selectPetCatLineasById ?");
        Object[] params = {
            idLinea
        };
        
        ResultSetHandler rsh = new BeanHandler(LineasDTO.class);
        LineasDTO lineaData = (LineasDTO) qr.query(sql.toString(), rsh, params);
                
        return lineaData;
    }
    
    /**
     * Metodo que valida la descripcion antes de hacer el update de la linea
     * @param id
     * @param descripcion
     * @return
     * @throws Exception 
     */
    public ResultInteger validaDescripcionUpdate(int id, String descripcion) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
      
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        sql.append("EXEC sp_updateValidaDescripcionLinea ?, ?");
        Object[] params = {
            id, descripcion
        };
        
        ResultSetHandler rsh = new BeanHandler(ResultInteger.class);
        ResultInteger count = (ResultInteger)  qr.query(sql.toString(), rsh, params);

        return count;
    }
}
