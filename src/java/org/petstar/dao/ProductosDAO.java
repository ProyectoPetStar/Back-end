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
        
        sql.append("EXEC sp_selectPetCarProductos");
        
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
        
        sql.append("EXEC sp_selectPetCarProductosById ?");
        Object[] params = {
            idProducto
        };
        
        ResultSetHandler rsh = new BeanHandler(ProductosDTO.class);
        ProductosDTO data = (ProductosDTO) qr.query(sql.toString(), rsh, params);
        
        return data;
    }
    
    /**
     * Metodo para registrar un nuevo producto
     * @param idLinea
     * @param producto
     * @param medida
     * @throws Exception 
     */
    public void insertNewCarProducto(int idLinea, String producto, String medida) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_insertPetCarProductos ?, ?, ?");
        Object[] params = {
            idLinea, producto, medida
        };
        
        qr.update(sql.toString(), params);
    }
    
    /**
     * Metodo para modificar un producto
     * @param idProducto
     * @param idLinea
     * @param producto
     * @param medida
     * @param posicion
     * @param activo
     * @throws Exception 
     */
    public void updateCarProducto(int idProducto, int idLinea, String producto, String medida, int posicion, int activo) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_updatePetCarProductos ?, ?, ?, ?, ?, ?");
        Object[] params = {
            idProducto, idLinea, producto, medida, posicion, activo
        };
        
        qr.update(sql.toString(), params);
    }
       
    /**
     * Metodo que valida los datos para registrar un producto nuevo
     * @param idLinea
     * @param producto
     * @return
     * @throws Exception 
     */
    public ResultInteger validaForInsertCarProducto(int idLinea, String producto) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_insertValidaDesPetCarProductos ?, ?");
        Object[] params = {
            idLinea, producto
        };
        
        ResultSetHandler rsh = new BeanHandler(ResultInteger.class);
        ResultInteger result = (ResultInteger) qr.query(sql.toString(), rsh, params);
        
        return result;
    }
    
    /**
     * Metodo que valida los datos para modificar un producto
     * @param idProducto
     * @param idLinea
     * @param Producto
     * @return
     * @throws Exception 
     */
    public ResultInteger validaForUpdateCarProducto(int idProducto, int idLinea, String Producto) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_updateValidaDesPetCarProductos ?, ?, ?");
        Object[] params = {
            idProducto, idLinea, Producto
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
        
        sql.append("EXEC  sp_validaIdPetCarProductos ?");
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