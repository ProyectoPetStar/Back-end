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
import org.petstar.dto.CatalogosDTO;
import org.petstar.dto.ResultInteger;

/**
 *
 * @author TechPro
 */
public class CatalogosDAO {
    
    /**
     * Metodo generico que devuelve la lista de datos en DB 
     * @param tablename
     * @return
     * @throws Exception 
     */
    public List<CatalogosDTO> getCatalogos(String tablename) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
      
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        sql.append("EXEC sp_selectPetCatalogos ?");
        Object[] params = {
            tablename
        };
        
        ResultSetHandler rsh = new BeanListHandler(CatalogosDTO.class);
        List<CatalogosDTO> data_catalogos = (List<CatalogosDTO>) qr.query(sql.toString(), rsh, params); 
        
        return data_catalogos;
    }
    
    /**
     * Metodo generico para dar de alta nuevos registros
     * @param tableName
     * @param descripcion
     * @throws Exception 
     */
    public void insertCatalogos(String tableName, String descripcion)throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_insertPetCatalogos ?, ?");
        Object[] params = {
            tableName, descripcion
        };
        
        qr.update(sql.toString(), params);
    }
    
    /**
     * Metodo generico para actualizar registros de catalogos
     * @param id
     * @param descripcion
     * @param activo
     * @param tableName
     * @throws Exception 
     */
    public void updateCatalogos(int id, String descripcion, int activo, String tableName) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_updatePetCatalogos ?, ?, ?, ?");
        Object[] params = {
            tableName, id, descripcion, activo 
        };
        
        qr.update(sql.toString(), params);
    }
    
    /**
     * Metodo generico que elimina registros de catalogos
     * @param id
     * @param tableName
     * @throws Exception 
     */
    public void deleteCatalogo(int id, String tableName) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_deletePetCatalogos ?, ?");
        Object[] params = {
            tableName, id
        };
        
        qr.update(sql.toString(), params);
    }
    
    /**
     * Metodo generico que valida las desscripciones de los catalogos
     * @param tableName
     * @param descripcion
     * @return
     * @throws Exception 
     */
    public ResultInteger validateDescripcionInsert(String tableName, String descripcion) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
      
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        sql.append("EXEC sp_insertValidaDescripcion ?, ?");
        Object[] params = {
            tableName, descripcion
        };
        ResultSetHandler rsh = new BeanHandler(ResultInteger.class);
        ResultInteger count = (ResultInteger)  qr.query(sql.toString(), rsh, params);

        return count;
    }
    
    /**
     * Metodo Generico que devuelve la descripcion del catalogo
     * @param tableName
     * @param id
     * @return
     * @throws Exception 
     */
    public CatalogosDTO getDescripcionById(String tableName, int id) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
      
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        sql.append("EXEC sp_selectDescripcionCatalogo ?, ?");
        Object[] params = {
            tableName, id
        };
        ResultSetHandler rsh = new BeanHandler(CatalogosDTO.class);
        CatalogosDTO catalogosDTO = (CatalogosDTO)  qr.query(sql.toString(), rsh, params);

        return catalogosDTO;
        
    }
}
