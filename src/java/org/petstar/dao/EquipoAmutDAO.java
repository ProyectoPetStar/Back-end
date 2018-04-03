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
import org.petstar.dto.EquipoAmutDTO;
import org.petstar.dto.ResultInteger;

/**
 * Clase DAO de Equipos Amut
 * @author Tech-Pro
 */
public class EquipoAmutDAO {
    
    /**
     * Consulta General
     * Metodo que devuelve la lista de los Equipos Amut
     * @return
     * @throws Exception 
     */
    public List<EquipoAmutDTO> getEquipoAmutData() throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_selectPetCatNomEquipoAmut");
        
        ResultSetHandler rsh = new BeanListHandler(EquipoAmutDTO.class);
        List<EquipoAmutDTO> data = (List<EquipoAmutDTO>) qr.query(sql.toString(), rsh);
        return data;
    }
    
    /**
     * Registro de Equipo Amut
     * Metodo que permite el regitro de nuevos Equipos Amut
     * @param claveEquipo
     * @param nombreEquipo
     * @throws Exception 
     */
    public void insertNewEquipoAmut(String claveEquipo, String nombreEquipo) throws  Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_insertPetCatNomEquipoAmut ?, ?");
        Object[] params = {
            claveEquipo, nombreEquipo
        };
        
        qr.update(sql.toString(), params);
    }
    
    /**
     * Modificación Equipo Amut
     * Metodo que permite la modificación de datos del equipo amut
     * @param idEquipo
     * @param claveEquipo
     * @param nombreEquipo
     * @param activo
     * @throws Exception 
     */
    public void updateEquipoAmut(int idEquipo, String claveEquipo, String nombreEquipo, int activo) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_updatePetCatNomEquipoAmut ?, ?, ?, ?");
        Object[] params = {
          idEquipo, claveEquipo, nombreEquipo, activo  
        };
        
        qr.update(sql.toString(), params);
    }
    
    /**
     * Eliminación de Equipo Amut
     * Metodo que realiza la eliminación de equipo amut de acuerdo al ID
     * @param idEquipo
     * @throws Exception 
     */
    public void deledeEquipoAmut(int idEquipo) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_deletePetCatNomEquipoAmut ?");
        Object[] params = {
            idEquipo
        };
        
        qr.update(sql.toString(), params);
    }
    
    /**
     * Validación para Modificación
     * Metodo que valida los datos antes de hacer el update del equipo amut
     * @param idEquipo
     * @param claveEquipo
     * @param nombreEquipo
     * @return
     * @throws Exception 
     */
    public ResultInteger validaDataForUpdate(int idEquipo, String claveEquipo, String nombreEquipo) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_updateValidaPetCatNomEquipoAmut ?, ?, ?");
        Object[] params = {
            idEquipo, claveEquipo, nombreEquipo
        };
        
        ResultSetHandler rsh = new BeanHandler(ResultInteger.class);
        ResultInteger result = (ResultInteger) qr.query(sql.toString(), rsh, params);
        return result;
    }
    
    /**
     * Validación para Registrar
     * Metodo que valida los datos antes de hacer el registro del equipo amut
     * @param claveEquipo
     * @param nombreEquipo
     * @return
     * @throws Exception 
     */
    public ResultInteger validaDataForInsert(String claveEquipo, String nombreEquipo) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_insertValidaPetCatNomEquipoAmut ?, ?");
        Object[] params = {
            claveEquipo, nombreEquipo
        };
        
        ResultSetHandler rsh = new BeanHandler(ResultInteger.class);
        ResultInteger result = (ResultInteger) qr.query(sql.toString(), rsh, params);
        return result;
    }
    
    /**
     * Consulta Especifica
     * Metodo que devuelve los datos del Equipo Amut de acuerdo a un id
     * @param idEquipo
     * @return
     * @throws Exception 
     */
    public EquipoAmutDTO getEquipoAmutById(int idEquipo)throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_selectPetCatNomEquipoAmutById ?");
        Object[] params = {
            idEquipo
        };
        
        ResultSetHandler rsh = new BeanHandler(EquipoAmutDTO.class);
        EquipoAmutDTO equipoAmutDTO = (EquipoAmutDTO) qr.query(sql.toString(), rsh, params);
        return equipoAmutDTO;
    }
}
