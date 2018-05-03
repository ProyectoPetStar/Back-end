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
 * Clase DAO de Catalogos
 * @author TechPro
 */
public class CatalogosDAO {
    
    /**
     * Consulta General
     * Metodo generico que devuelve la lista de los catalogos
     * @param tablename
     * @return
     * @throws Exception 
     */
    public List<CatalogosDTO> getCatalogos(String tablename) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_selectPetCatalogos ?");
        Object[] params = { tablename };
        
        ResultSetHandler rsh = new BeanListHandler(CatalogosDTO.class);
        List<CatalogosDTO> data_catalogos = (List<CatalogosDTO>) qr.query(sql.toString(), rsh, params);
        return data_catalogos;
    }
    
    /**
     * Registro de Catalogos
     * Metodo generico para dar de alta nuevos registros de catalogos
     * @param tableName
     * @param valor
     * @param descripcion
     * @throws Exception 
     */
    public void insertCatalogos(String tableName, String valor, String descripcion)throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_insertPetCatalogos ?, ?, ?");
        Object[] params = { tableName, valor, descripcion };
        
        qr.update(sql.toString(), params);
    }
    
    /**
     * Modificación de Catalogos
     * Metodo generico para actualizar registros de catalogos
     * @param id
     * @param valor
     * @param descripcion
     * @param activo
     * @param tableName
     * @throws Exception 
     */
    public void updateCatalogos(int id, String valor, String descripcion, 
            int activo, String tableName) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_updatePetCatalogos ?, ?, ?, ?, ?");
        Object[] params = {
            tableName, id, valor, descripcion, activo
        };
        
        qr.update(sql.toString(), params);
    }
    
    /**
     * Bloqueo de Catalogos
     * Metodo generico que habilita y deshabilita registros de catalogos de acuerdo al ID
     * @param id
     * @param tableName
     * @param activo
     * @throws Exception 
     */
    public void blockCatalogo(int id, String tableName, int activo) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_updateActivoPetCatalogo ?, ?, ?");
        Object[] params = {
            tableName, id, activo
        };
        
        qr.update(sql.toString(), params);
    }
    
    /**
     * Validación para Insert
     * Metodo generico que valida las desscripciones de los catalogos para
     * evitar datos duplicados
     * @param tableName
     * @param valor
     * @param descripcion
     * @return
     * @throws Exception 
     */
    public ResultInteger validateDescripcionInsert(String tableName, String valor, String descripcion) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_insertValidaDescripcion ?, ?, ?");
        Object[] params = {tableName, valor, descripcion };
        
        ResultSetHandler rsh = new BeanHandler(ResultInteger.class);
        ResultInteger count = (ResultInteger)  qr.query(sql.toString(), rsh, params);
        return count;
    }
    
    /**
     * Validación para Update
     * Metodo generico que valida las descripciones de los catalogos para 
     * evitar duplicar datos
     * @param tableName
     * @param id
     * @param valor
     * @param descripcion
     * @return
     * @throws Exception 
     */
    public ResultInteger validateDescripcionUpdate(String tableName, int id, 
            String valor, String descripcion) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_updateValidaDescripcion ?, ?, ?, ?");
        Object[] params = {
            tableName, id, valor, descripcion
        };
        
        ResultSetHandler rsh = new BeanHandler(ResultInteger.class);
        ResultInteger count = (ResultInteger)  qr.query(sql.toString(), rsh, params);
        return count;
    }
    
    /**
     * Consulta Especifica
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
        sql.append("EXEC sp_selectPetCatalogosById ?, ?");
        Object[] params = { tableName, id };
        
        ResultSetHandler rsh = new BeanHandler(CatalogosDTO.class);
        CatalogosDTO catalogosDTO = (CatalogosDTO)  qr.query(sql.toString(), rsh, params);
        return catalogosDTO;
    }
    
    public ResultInteger validaExistID(String tableName, String columName, int id) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT COUNT(1) AS result FROM ").append(tableName)
                .append(" WHERE ").append(columName).append(" = ?");
        Object[] params = { id };
        
        ResultSetHandler rsh = new BeanHandler(ResultInteger.class);
        ResultInteger result =  (ResultInteger) qr.query(sql.toString(), rsh, params);
        return result;
    }
}
