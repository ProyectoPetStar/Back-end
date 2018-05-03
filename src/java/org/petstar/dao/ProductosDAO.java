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
import org.petstar.dto.MetasAsignacionDTO;
import org.petstar.dto.ProductosAsignacionDTO;
import org.petstar.dto.ProductosDTO;
import org.petstar.dto.ResultInteger;

/**
 * DAO de Productos
 * @author Tech-Pro
 */
public class ProductosDAO {
    
    /**
     * Lista de Productos
     * Metodo que devuelve la lista del catalogo de productos
     * @return
     * @throws Exception 
     */
    public List<ProductosDTO> getAllProductos() throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_selectPetCatProducto");
        
        ResultSetHandler rsh = new BeanListHandler(ProductosDTO.class);
        List<ProductosDTO> data = (List<ProductosDTO>) qr.query(sql.toString(), rsh);
        return data;
    }
    
    /**
     * Selecciona un Producto
     * Metodo que devuelve la información un producto en especifico 
     * @param idProducto
     * @return
     * @throws Exception 
     */
    public ProductosDTO getProductoById(int idProducto) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_selectPetCatProductoById ?");
        Object[] params = { idProducto };
        
        ResultSetHandler rsh = new BeanHandler(ProductosDTO.class);
        ProductosDTO data = (ProductosDTO) qr.query(sql.toString(), rsh, params);
        
        return data;
    }
    
    /**
     * Registra Producto
     * Metodo para registrar un nuevo producto en el catalogo
     * @param producto
     * @throws Exception 
     */
    public void insertProducto(ProductosDTO producto) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_insertPetCatProducto ?, ?, ?, ?");
        Object[] params = {
            producto.getValor(), producto.getDescripcion(),
            producto.getId_linea(), producto.getId_tipo_producto()
        };
        
        qr.update(sql.toString(), params);
    }
    
    /**
     * Modificación de Producto
     * Metodo para actualizar los datos de un producto en especifico
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
     * Validación para Registrar
     * Metodo que valida que los datos para registrar un producto no esten repetidos
     * @param producto
     * @return
     * @throws Exception 
     */
    public ResultInteger validaForInsert(ProductosDTO producto) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_insertValidaPetCatProducto ?, ?, ?, ?");
        Object[] params = {
            producto.getValor(), producto.getDescripcion(),
            producto.getId_linea(), producto.getId_tipo_producto()
        };
        
        ResultSetHandler rsh = new BeanHandler(ResultInteger.class);
        ResultInteger result = (ResultInteger) qr.query(sql.toString(), rsh, params);
        return result;
    }
    
    /**
     * Validación para Modificar
     * Metodo que valida que los datos para modificar un producto no se repitan
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
}
