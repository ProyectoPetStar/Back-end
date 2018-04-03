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
 * DAO de Metas
 * @author Tech-Pro
 */
public class MetasDAO {
    
    /**
     * Lista de Metas Catálogo
     * Metodo que devuelve la lista del catalogo de Metas
     * @return
     * @throws Exception 
     */
    public List<MetasDTO> getMetasCarga() throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
      
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        sql.append("EXEC sp_selectPetCarMetas");
        
        ResultSetHandler rsh = new BeanListHandler(MetasDTO.class);
        List<MetasDTO> dataMetas = (List<MetasDTO>) qr.query(sql.toString(), rsh); 
        
        return dataMetas;
    }
    
    /**
     * Selecciona una Meta
     * Metodo que devuelve la información una Meta en especifico 
     * @param idMeta
     * @return
     * @throws Exception 
     */
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
    
    /**
     * Registra Metas
     * Metodo para registrar una nueva Meta en el catalogo
     * @param idLinea
     * @param meta
     * @param tipoMedida
     * @throws Exception 
     */
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
    
    /**
     * Modificación de Metas
     * Metodo para actualizar los datos de una Meta en especifico
     * @param idMeta
     * @param idLinea
     * @param meta
     * @param tipoMedida
     * @param posicion
     * @param activo
     * @throws Exception 
     */
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
       
    /**
     * Validación para Registrar
     * Metodo que valida que los datos para registrar de la Meta no esten repetidos
     * @param idLinea
     * @param meta
     * @return
     * @throws Exception 
     */
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
    
    /**
     * Validación para Modificar
     * Metodo que valida que los datos para modificar una Meta no se repitan
     * @param idMeta
     * @param idLinea
     * @param meta
     * @return
     * @throws Exception 
     */
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
    
    /**
     * Validación que exista la Meta
     * Metodo para validar que el id que recibe sea correcto y corresponda a una meta.
     * @param idMeta
     * @return
     * @throws Exception 
     */
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
     * Asignación de Metas
     * Metodo para realizar la asignación de metas.
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
     * Lista de Asignaciones
     * Metodo que devuelve todas las asignaciones de metas registradas en un año en especifico
     * @param year
     * @return
     * @throws Exception 
     */
    public List<MetasAsignacionDTO> getAllAsignacionesByYear(int year) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_selectPetProMetas ?");
        Object[] params = {
            year
        };
        
        ResultSetHandler rsh = new BeanListHandler(MetasAsignacionDTO.class);
        List<MetasAsignacionDTO> data = (List<MetasAsignacionDTO>) qr.query(sql.toString(), rsh, params);
        return data;
    }
    
    /**
     * Asignación en Especifico
     * Metodo que devuelve la información de una asignacion en especifico de acuerdo al id
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
     * Validación que exista la Asignación de Meta
     * Metodo para validar que el id que recibe sea correcto y corresponda a una Asignación de Meta.
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
    
    /**
     * Eliminación de Asignaciones
     * Metodo que permite la eliminación de una Asignación de Meta de acuerdo al id
     * @param idAsignacion
     * @throws Exception 
     */
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
    
    /**
     * Validación para Registrar Asignación
     * Metodo que valida que los datos para registrar la Asignación de Meta no esten repetidos
     * @param idMeta
     * @param idTurno
     * @param diaMeta
     * @return
     * @throws Exception 
     */
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
    
    /**
     * Validación para Modificar Asignación de Meta
     * Metodo que valida que los datos para modificar una Asignación de Meta no se repitan
     * @param idAsignacion
     * @param idTurno
     * @param idGrupo
     * @param idMeta
     * @param diaMeta
     * @param valorMeta
     * @param borrar
     * @throws Exception 
     */
    public void updateAsignacionMeta(int idAsignacion, int idTurno, int idGrupo, int idMeta, String diaMeta, BigDecimal valorMeta, int borrar) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_updatePetProMetas ?, ?, ?, ?, ?, ?, ?");
        Object[] params = {
            idAsignacion, idTurno, idGrupo, idMeta, diaMeta, valorMeta, borrar
        };
        
         qr.update(sql.toString(), params);
    }
}
