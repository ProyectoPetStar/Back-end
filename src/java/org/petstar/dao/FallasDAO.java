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
import org.petstar.dto.FallasDTO;

/**
 *
 * @author Tech-Pro
 */
public class FallasDAO {
    
    public List<FallasDTO> getAllDataFallas() throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("");
        
        ResultSetHandler rsh = new BeanListHandler(FallasDTO.class);
        List<FallasDTO> data = (List<FallasDTO>) qr.query(sql.toString(), rsh);
        
        return data;
    }
    
    public List<FallasDTO> getDataFallasByDay() throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("");
        
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
    
    public void insertNewFalla() throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("");
        Object[] paramas = {
            
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
