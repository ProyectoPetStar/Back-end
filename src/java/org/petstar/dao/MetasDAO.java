/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.petstar.dao;

import java.math.BigDecimal;
import java.util.List;
import javax.sql.DataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.petstar.configurations.PoolDataSource;
import org.petstar.dto.MetasAsignacionDTO;
import org.petstar.dto.MetasDTO;
import org.petstar.dto.ResultInteger;

/**
 *
 * @author Tech-Pro
 */
public class MetasDAO {
    public List<MetasDTO> getMetasCarga() throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
      
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        sql.append("EXEC sp_selectPetCarMetas");
        
        ResultSetHandler rsh = new BeanListHandler(MetasDTO.class);
        List<MetasDTO> dataMetas = (List<MetasDTO>) qr.query(sql.toString(), rsh); 
        
        return dataMetas;
    }
    
    public MetasDTO getMetasCargaById(int idMeta) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_selectPetCarMetasById ?");
        Object[] params ={
            idMeta
        };
        ResultSetHandler rsh = new BeanHandler(MetasDTO.class);
        MetasDTO dataMetas = (MetasDTO) qr.query(sql.toString(), rsh, params); 
        
        return dataMetas;
    }
    
    public void insertMetaCarga(int idLinea, String meta, String tipoMedida) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
      
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        sql.append("EXEC sp_insertPetCarMetas ?, ?, ?");
        Object[] params = {
            idLinea, meta, tipoMedida
        };
        
        qr.update(sql.toString(), params);
    }
    
    public void updateMetaCarga(int idMeta, int idLinea, String meta, String tipoMedida, int posicion, int activo) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        sql.append("EXEC sp_updatePetCarMetas ?, ?, ?, ?, ?, ?");
        Object[] params = {
            idMeta, idLinea, meta, tipoMedida, posicion, activo
        };
        
        qr.update(sql.toString(), params);
    }
       
    public ResultInteger validaDataForInsertCarga(int idLinea, String meta) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_insertValidaDesPetCarMetas ?, ?");
        Object[] params = {
            idLinea, meta
        };
        
        ResultSetHandler rsh = new BeanHandler(ResultInteger.class);
        ResultInteger count = (ResultInteger)  qr.query(sql.toString(), rsh, params);

        return count;
    }
    
    public ResultInteger validaDataForUpdateCarga(int idMeta, int idLinea, String meta) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_updateValidaDesPetCarMetas ?, ?, ?");
        Object[] params = {
            idMeta, idLinea, meta
        };
        
        ResultSetHandler rsh = new BeanHandler(ResultInteger.class);
        ResultInteger count = (ResultInteger)  qr.query(sql.toString(), rsh, params);

        return count;
    }
    
    public ResultInteger validaIfExistMetaCarga(int idMeta) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_validaIdPetCarMetas ?");
        Object[] params = {
            idMeta
        };
        
        ResultSetHandler rsh = new BeanHandler(ResultInteger.class);
        ResultInteger count = (ResultInteger)  qr.query(sql.toString(), rsh, params);

        return count;
    }
    
    /**
     * Metodo para hacer la asignación de metas
     * @param idGrupo
     * @param idTurno
     * @param idMeta
     * @param diaMeta
     * @param valorMeta
     * @throws Exception 
     */
    public void registraAsignacion(int idGrupo, int idTurno, int idMeta, String diaMeta, BigDecimal valorMeta) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_insertPetProMetas ?, ?, ?, ?, ?");
        Object[] params = {
            idGrupo, idTurno, idMeta, diaMeta, valorMeta
        };
        
        qr.update(sql.toString(), params);
    }
    
    /**
     * MEtoodo que devuelve toda la informacion sobre las metas del año
     * @return
     * @throws Exception 
     */
    public List<MetasAsignacionDTO> getAllAsignacionesByYear() throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_selectPetProMetas");
        
        ResultSetHandler rsh = new BeanListHandler(MetasAsignacionDTO.class);
        List<MetasAsignacionDTO> data = (List<MetasAsignacionDTO>) qr.query(sql.toString(), rsh);
        return data;
    }
    
    /**
     * MEtodo que devuelve la asignacion de acuerdo al id
     * @param idAsignacion
     * @return
     * @throws Exception 
     */
    public MetasAsignacionDTO getAsignacionById(int idAsignacion) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_selectPetProMetasById ?");
        Object[] params = {
            idAsignacion
        };
        
        ResultSetHandler rsh = new BeanHandler(MetasAsignacionDTO.class);
        MetasAsignacionDTO data = (MetasAsignacionDTO) qr.query(sql.toString(), rsh, params);
        return data;
    }
    
    /**
     * Valida que el ID que se envia exista en la DB
     * @param idAsignacion
     * @return
     * @throws Exception 
     */
    public ResultInteger validaIfExistAsignacion(int idAsignacion) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_validaIdPetProMetas ?");
        Object[] params = {
            idAsignacion
        };
        
        ResultSetHandler rsh = new BeanHandler(ResultInteger.class);
        ResultInteger result = (ResultInteger) qr.query(sql.toString(), rsh, params);
        return result;
    }
    
    public void deleteAsignacionMeta(int idAsignacion) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_deletePetProMetas ?");
        Object[] params = {
            idAsignacion
        };
        
         qr.update(sql.toString(), params);
    }
    
    public ResultInteger validaDataForAsignacion(int idMeta, int idTurno, String diaMeta)throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_insertValidaPetProMetas ?, ?, ?");
        Object[] params = {
            idMeta, idTurno, diaMeta
        };
        
        ResultSetHandler rsh = new BeanHandler(ResultInteger.class);
        ResultInteger result = (ResultInteger) qr.query(sql.toString(), rsh, params);
        return result;
    }
    
    public void updateAsignacionMeta(int idAsignacion, int idTurno, int idGrupo, int idMeta, String diaMeta, BigDecimal valorMeta, int borrar) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_updatePetProMetas ?, ?, ?, ?, ?, ?, ?");
        Object[] params = {
            idAsignacion, idTurno, idGrupo, idMeta, valorMeta, borrar
        };
        
         qr.update(sql.toString(), params);
    }
}
