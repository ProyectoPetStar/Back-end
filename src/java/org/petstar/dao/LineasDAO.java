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
 * Clase de Accesos a Datos de Lineas
 * @author Tech-Pro
 */
public class LineasDAO {
    
    /**
     * Consulta de Lineas
     * Metodo que devuelve lista de Lineas.
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
     * Consulta de Lineas Activas
     * Metodo que devuelve lista de Lineas que se encuentran activas.
     * @return
     * @throws Exception 
     */
    public List<LineasDTO> getLineasActive() throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_selectPetCatLineaActivos");
        
        ResultSetHandler rsh = new BeanListHandler(LineasDTO.class);
        List<LineasDTO> lineasData = (List<LineasDTO>) qr.query(sql.toString(), rsh);
        return lineasData;
    }
    
    /**
     * Registro de Lineas
     * Metodo que registra una nueva linea en DB
     * @param linea
     * @throws Exception 
     */
    public void insertNewLinea(LineasDTO linea) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_insertPetCatLineas ?, ?, ?");
        Object[] params = {
            linea.getValor(), linea.getDescripcion(), linea.getId_gpo_linea()
        };
        
        qr.update(sql.toString(), params);
    }
    
    /**
     * Modificación de Linea
     * Metodo que actualiza los datos de una linea
     * @param linea
     * @throws Exception 
     */
    public void updateLinea(LineasDTO linea) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_updatePetCatLinea ?, ?, ?, ?");
        Object[] params = {
            linea.getId_linea(), linea.getValor(), linea.getDescripcion(), linea.getId_gpo_linea()
        };
        
        qr.update(sql.toString(), params);
    }
    
    /**
     * Bloqueo de Linea
     * Metodo que habilita o deshabilita una Linea de acuerdo al id
     * @param idLinea
     * @param activo
     * @throws Exception 
     */
    public void blockLinea(int idLinea, int activo) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("UPDATE pet_cat_linea SET activo = ? WHERE id_linea = ?");
        Object[] params = { activo, idLinea };
        
        qr.update(sql.toString(), params);
    }
    
    /**
     * Consulta de linea por ID
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
        Object[] params = { idLinea };
        
        ResultSetHandler rsh = new BeanHandler(LineasDTO.class);
        LineasDTO lineaData = (LineasDTO) qr.query(sql.toString(), rsh, params);
        return lineaData;
    }
    
    /**
     * Validación para Modificar
     * Metodo que valida los datos antes de hacer el update de la linea
     * @param linea
     * @return
     * @throws Exception 
     */
    public ResultInteger validaForUpdate(LineasDTO linea) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_updateValidaPetCatLineas ?, ?, ?, ?");
        Object[] params = {
            linea.getId_linea(), linea.getValor(), linea.getDescripcion(), linea.getId_gpo_linea()
        };
        
        ResultSetHandler rsh = new BeanHandler(ResultInteger.class);
        ResultInteger count = (ResultInteger)  qr.query(sql.toString(), rsh, params);
        return count;
    }
    
    /**
     * Validación para Registrar
     * Metodo que valida los datos antes de hacer el insert de la linea
     * @param linea
     * @return
     * @throws Exception 
     */
    public ResultInteger validaForInsert(LineasDTO linea) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_insertValidaPetCatLineas ?, ?, ?");
        Object[] params = {
            linea.getValor(), linea.getDescripcion(), linea.getId_gpo_linea()
        };
        
        ResultSetHandler rsh = new BeanHandler(ResultInteger.class);
        ResultInteger count = (ResultInteger)  qr.query(sql.toString(), rsh, params);
        return count;
    }
    
    /**
     * Consulta de lineas por Grupo de linea
     * Metodo que devuelve lista de Lineas segun el grupo de linea
     * @param idGpoLinea
     * @return
     * @throws Exception 
     */
    public List<LineasDTO> getLineasByGpoLinea(int idGpoLinea) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT * FROM pet_cat_linea WHERE id_gpo_linea = ?");
        Object[] params = { idGpoLinea };
        
        ResultSetHandler rsh = new BeanListHandler(LineasDTO.class);
        List<LineasDTO> lineasData = (List<LineasDTO>) qr.query(sql.toString(), rsh, params);
        return lineasData;
    }
}
