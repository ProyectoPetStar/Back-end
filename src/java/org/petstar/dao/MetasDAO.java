package org.petstar.dao;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import javax.sql.DataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.petstar.configurations.PoolDataSource;
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
     * @param mes
     * @param anio
     * @param idLinea
     * @return
     * @throws Exception 
     */
    public List<MetasDTO> getAllMetas(int mes, int anio, int idLinea) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_select_petMetasByPeriodoLinea ?, ?, ?");
        Object[] params = {
            mes, anio, idLinea
        };
        
        ResultSetHandler rsh = new BeanListHandler(MetasDTO.class);
        List<MetasDTO> dataMetas = (List<MetasDTO>) qr.query(sql.toString(), rsh, params); 
        
        return dataMetas;
    }
    
    /**
     * Selecciona una Meta
     * Metodo que devuelve la información una Meta en especifico 
     * @param idMeta
     * @return
     * @throws Exception 
     */
    public MetasDTO getMetaById(int idMeta) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_select_petMetasById ?");
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
    * @param dia
    * @param meta
    * @param tmp
    * @param velocidad
    * @param idTurno
    * @param idGrupo
    * @param idLinea
    * @throws Exception 
    */
    public void insertNewMeta(Date dia, BigDecimal meta, BigDecimal tmp, BigDecimal velocidad, 
            int idTurno, int idGrupo, int idLinea) throws Exception{
        
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_insertManual_petMetas ?, ?, ?, ?, ?, ?, ?");
        Object[] params = {
            dia, meta, tmp, velocidad, idTurno, idGrupo, idLinea
        };
        
        qr.update(sql.toString(), params);
    }
    
    /**
     * Modificación de Metas
     * Metodo para actualizar los datos de una Meta en especifico
     * @param idMeta
     * @param dia
     * @param meta
     * @param tmp
     * @param vel
     * @param idTurno
     * @param idGrupo
     * @param idLinea
     * @param idFile
     * @param idUsuarioMod
     * @param fechaMod
     * @param activo
     * @throws Exception 
     */
    public void updateMeta(int idMeta, Date dia, BigDecimal meta, BigDecimal tmp, BigDecimal vel, 
            int idTurno, int idGrupo, int idLinea, int idFile, int idUsuarioMod, Date fechaMod, int estatus) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_update_petMeta ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?");
        Object[] params = {
            idMeta, dia, meta, tmp, vel, idTurno, idGrupo, idLinea, idFile, estatus, idUsuarioMod, fechaMod
        };
        
        qr.update(sql.toString(), params);
    }
    
    public void deleteMeta(int idMeta) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_tmpDelete_petMeta ?");
        Object[] params = {
            idMeta
        };
        
        qr.update(sql.toString(), params);
    }
    
    /**
     * Validación para Registrar
     * Metodo que valida que los datos para registrar de la Meta no esten repetidos
     * @param dia
     * @param idTurno
     * @param idGrupo
     * @param idLinea
     * @return
     * @throws Exception 
     */
    public ResultInteger validaDataForInsertMeta(Date dia, int idTurno, int idGrupo, int idLinea) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_valida_petMeta ?, ?, ?, ?");
        Object[] params = {
            dia, idTurno, idGrupo, idLinea
        };
        
        ResultSetHandler rsh = new BeanHandler(ResultInteger.class);
        ResultInteger count = (ResultInteger)  qr.query(sql.toString(), rsh, params);

        return count;
    }
    
   /**
    * Validación para Modificar
    * Metodo que valida que los datos para modificar una Meta no se repitan
    * @param idMeta
    * @param dia
    * @param idTurno
    * @param idGrupo
    * @param idLinea
    * @return
    * @throws Exception 
    */
    public ResultInteger validaDataForUpdateMeta(int idMeta, Date dia, int idTurno, int idGrupo, int idLinea) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_validaUpdate_petMetas ?, ?, ?, ?, ?");
        Object[] params = {
            idMeta, dia, idTurno, idGrupo, idLinea
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
    public ResultInteger validaIfExistMeta(int idMeta) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_validaRegistro_petMeta ?");
        Object[] params = {
            idMeta
        };
        
        ResultSetHandler rsh = new BeanHandler(ResultInteger.class);
        ResultInteger count = (ResultInteger)  qr.query(sql.toString(), rsh, params);

        return count;
    }
}
