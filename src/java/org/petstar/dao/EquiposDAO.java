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
import org.petstar.dto.EquiposDTO;
import org.petstar.dto.ResultInteger;

/**
 *
 * @author Tech-PRo
 */
public class EquiposDAO {
    
    /**
     * Consulta de Equipos por Linea
     * Metodo que devuelve una lista de Equipos de acuerdo al id de linea
     * @param idLinea
     * @return
     * @throws Exception 
     */
    public List<EquiposDTO> getAllEquiposByIdLinea(int idLinea) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_select_catEquipos ?");
        Object[] params = {
             idLinea
        };
        ResultSetHandler rsh = new BeanListHandler(EquiposDTO.class);
        List<EquiposDTO> data = (List<EquiposDTO>) qr.query(sql.toString(), rsh, params);
        
        return data;
    }
     
    /**
     * Registra Equipo
     * Metodo que guarda en la base de datos un nuevo equipo
     * @param equipo
     * @throws Exception 
     */
    public void inserEquipo(EquiposDTO equipo) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_insertPetCatEquipos ?, ?");
        Object[] params = { equipo.getValor(), equipo.getDescripcion()};
        
        qr.update(sql.toString(), params);
    }
    
    /**
     * Consulta General
     * Metodo que devulve la lista de todos los equipos que se encuentren en la DB
     * @return
     * @throws Exception 
     */
    public List<EquiposDTO> getAllEquipos() throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_selectPetCatEquipos");
        
        ResultSetHandler rsh = new BeanListHandler(EquiposDTO.class);
        List<EquiposDTO> lista = (List<EquiposDTO>) qr.query(sql.toString(), rsh);
        return lista;
    }
    
    /**
     * Consulta Especifica
     * Metodo que devuelve los datos de un Equipo de acuerdo a su ID
     * @param id
     * @return
     * @throws Exception 
     */
    public EquiposDTO getEquipoById(int id) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_selectPetCatEquiposById ?");
        Object[] params = {id };
        
        ResultSetHandler rsh = new BeanHandler(EquiposDTO.class);
        EquiposDTO equipo = (EquiposDTO) qr.query(sql.toString(), rsh, params);
        return equipo;
    }
    
    /**
     * Modificación de Equipos
     * Metodo que se encarga de la modificacion de un equipo
     * @param equipo
     * @throws Exception 
     */
    public void updateEquipo(EquiposDTO equipo) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_updatePetCatEquipos ?, ?, ?");
        Object[] params = { 
            equipo.getId_equipos(), equipo.getValor(), equipo.getDescripcion()
        };
        
        qr.update(sql.toString(), params);
    }
    
    /**
     * Validacion para modificacion
     * Metodo que valida que no se repitan los datos
     * @param equipo
     * @return
     * @throws Exception 
     */
    public ResultInteger validaForUpdate(EquiposDTO equipo) throws Exception{
        DataSource dataSource = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(dataSource);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_updateValidaPetCatEquipos ?, ?, ?");
        Object[] params = {
            equipo.getId_equipos(), equipo.getValor(), equipo.getDescripcion()
        };
        
        ResultSetHandler rsh = new BeanHandler(ResultInteger.class);
        ResultInteger result = (ResultInteger) qr.query(sql.toString(), rsh, params);
        return result;
    }
    
    /**
     * Bloqueo de Equipos
     * Metodo que cambia el estatus del equipo
     * @param idEquipo
     * @param activo
     * @throws Exception 
     */
    public void blockEquipo(int idEquipo, int activo) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("UPDATE pet_cat_equipos SET activo = ? WHERE id_equipos = ?");
        Object[] params = { activo, idEquipo };
        
        qr.update(sql.toString(), params);
    }
}
