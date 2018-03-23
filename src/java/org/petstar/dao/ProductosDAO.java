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
import org.petstar.dto.ProductosDTO;
import org.petstar.dto.ResultInteger;

/**
 *
 * @author Tech-Pro
 */
public class ProductosDAO {
    
    /**
     * Metodo que devuelve la lista de productos
     * @return
     * @throws Exception 
     */
    public List<ProductosDTO> getDataCarProductos() throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("");
        
        ResultSetHandler rsh = new BeanListHandler(ProductosDTO.class);
        List<ProductosDTO> data = (List<ProductosDTO>) qr.query(sql.toString(), rsh);
        
        return data;
    }
    
    /**
     * Metodo que devuelve un producto en especifico 
     * @param idProducto
     * @return
     * @throws Exception 
     */
    public ProductosDTO getDataCarProductosById(int idProducto) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("");
        Object[] params = {
            idProducto
        };
        
        ResultSetHandler rsh = new BeanHandler(ProductosDTO.class);
        ProductosDTO data = (ProductosDTO) qr.query(sql.toString(), rsh, params);
        
        return data;
    }
    
    /**
     * Metodo para registrar un nuevo producto
     * @throws Exception 
     */
    public void insertNewCarProducto() throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("");
        Object[] params = {
            
        };
        
        qr.update(sql.toString(), params);
    }
    
    /**
     * Metodo para modificar un producto
     * @throws Exception 
     */
    public void updateCarProducto() throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("");
        Object[] params = {
            
        };
        
        qr.update(sql.toString(), params);
    }
    
    /**
     * Metodo para eliminar un metodo
     * @param idProducto
     * @throws Exception 
     */
    public void deleteCarProducto(int idProducto) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("");
        Object[] params = {
            idProducto
        };
        
        qr.update(sql.toString(), params);
    }
    
    /**
     * Metodo que valida los datos para registrar un producto nuevo
     * @return
     * @throws Exception 
     */
    public ResultInteger validaForInsertCarProducto() throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("");
        Object[] params = {
            
        };
        
        ResultSetHandler rsh = new BeanHandler(ResultInteger.class);
        ResultInteger result = (ResultInteger) qr.query(sql.toString(), rsh, params);
        
        return result;
    }
    
    /**
     * Metodo que valida los datos para modificar un producto
     * @return
     * @throws Exception 
     */
    public ResultInteger validaForUpdateCarProducto() throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("");
        Object[] params = {
            
        };
        
        ResultSetHandler rsh = new BeanHandler(ResultInteger.class);
        ResultInteger result = (ResultInteger) qr.query(sql.toString(), rsh, params);
        
        return result;
    }
    
    /**
     * Metodo para validar que exista el producto.
     * @param idProducto
     * @return
     * @throws Exception 
     */
    public ResultInteger validaIfExistCarProducto(int idProducto) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("");
        Object[] params = {
            idProducto
        };
        
        ResultSetHandler rsh = new BeanHandler(ResultInteger.class);
        ResultInteger result = (ResultInteger) qr.query(sql.toString(), rsh, params);
        
        return result;
    }
    
    /**
     * Metodo para validar que el grupo y turno seleccionado sean validos
     * @param idGrupo
     * @param turno
     * @param DiaMeta
     * @return
     * @throws Exception 
     */
    public ResultInteger validaGrupoTurno(int idGrupo, int turno, String DiaMeta)throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("");
        Object[] params = {
            idGrupo, turno, DiaMeta
        };
        
        ResultSetHandler rsh = new BeanHandler(ResultInteger.class);
        ResultInteger result = (ResultInteger) qr.query(sql.toString(), rsh, params);
        
        return result;
    }
}
