/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.petstar.dao;

import java.sql.Date;
import java.util.List;
import javax.sql.DataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.petstar.configurations.PoolDataSource;
import org.petstar.dto.FallasDTO;

/**
 *
 * @author Tech-Pro
 */
public class FallasDAO {
    
    public List<FallasDTO> getAllFallasByDays(Date fechaIn, Date fechaTe) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("");
        Object[] params = {
            fechaIn, fechaTe
        };
        
        ResultSetHandler rsh = new BeanListHandler(FallasDTO.class);
        List<FallasDTO> data = (List<FallasDTO>) qr.query(sql.toString(), rsh);
        
        return data;
    }
    
    public FallasDTO getFallaById() throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("");
        
        ResultSetHandler rsh = new BeanHandler(FallasDTO.class);
        FallasDTO data = (FallasDTO) qr.query(sql.toString(), rsh);
        
        return data;
    }
    
    public void insertNewFalla(FallasDTO fallas) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_insert_petFallas ?, ?, ?, ?, ?, ?, ?, ?");
        Object[] paramas = {
            fallas.getDescripcion(), fallas.getHora_inicio(), fallas.getHora_final(),
            fallas.getTiempo_paro(), fallas.getId_meta(), fallas.getId_razon(), 
            fallas.getId_equipo(), fallas.getId_usuario_registro()
        };
        
        qr.update(sql.toString(), paramas);
    }
    
    public void deleteFalla() throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("");
        Object[] paramas = {
            
        };
        
        qr.update(sql.toString(), paramas);
    }
    
    public void updateFalla() throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("");
        Object[] paramas = {
            
        };
        
        qr.update(sql.toString(), paramas);
    }
}
