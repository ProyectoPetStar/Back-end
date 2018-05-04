package org.petstar.dao;

import java.sql.Date;
import java.util.List;
import javax.sql.DataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.petstar.configurations.PoolDataSource;
import org.petstar.dto.RazonParoDTO;
import org.petstar.dto.ResultInteger;

/**
 *
 * @author Tech-Pro
 */
public class RazonParoDAO {
    
    /**
     * Consulta General
     * Metodo que devulve una lista con todas la razones que existen en la tabla
     * @return
     * @throws Exception 
     */
    public List<RazonParoDTO> getAllRazones() throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_selectPetCatRazonParo");
       
        ResultSetHandler rsh = new BeanListHandler(RazonParoDTO.class);
        List<RazonParoDTO> data = (List<RazonParoDTO>) qr.query(sql.toString(), rsh);
        
        return data;
    }
    
    /**
     * Consulta registros activos
     * Metodo que devuelve una lista con los registros activo
     * @return
     * @throws Exception 
     */
    public List<RazonParoDTO> getAllRazonesActive() throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_selectPetCatRazonParoActivos");
       
        ResultSetHandler rsh = new BeanListHandler(RazonParoDTO.class);
        List<RazonParoDTO> data = (List<RazonParoDTO>) qr.query(sql.toString(), rsh);
        
        return data;
    }
    
    /**
     * Consulta especifica
     * Metodo que devulve una razon de acuerdo al id
     * @param idRazon
     * @return
     * @throws Exception 
     */
    public RazonParoDTO getRazonById(int idRazon) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_selectPetCatRazonParoById ?");
        Object[] params = { idRazon };
       
        ResultSetHandler rsh = new BeanHandler(RazonParoDTO.class);
        RazonParoDTO data = (RazonParoDTO) qr.query(sql.toString(), rsh, params);
        
        return data;
    }
    
    /**
     * Registro de una Razon de Paro
     * Metodo que guarda en la DB los datos
     * @param razon
     * @throws Exception 
     */
    public void insertRazones(RazonParoDTO razon) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_insertPetCatRazonParo ?, ?, ?");
        Object[] params = { 
            razon.getValor(), razon.getDescripcion(), razon.getId_fuente_paro() 
        };
        
        qr.update(sql.toString(), params);
    }
    
    /**
     * Modificación de una Razon de Paro
     * Metodo que actualiza los datos de un registro en especifico
     * @param razon
     * @throws Exception 
     */
    public void updateRazones(RazonParoDTO razon) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_updatePetCatRazonParo ?, ?, ?, ?");
        Object[] params = { 
            razon.getId_razon_paro(),razon.getValor(),
            razon.getDescripcion(), razon.getId_fuente_paro() 
        };
        
        qr.update(sql.toString(), params);
    }
    
    /** Bloqueo de Registros
     * Metodo que habilita o deshabilita un registro
     * @param idRazon
     * @param activo
     * @throws Exception 
     */
    public void blockRazones(int idRazon, int activo) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("UPDATE pet_cat_razon_paro SET activo = ? WHERE id_razon_paro = ?");
        Object[] params = { activo, idRazon };
        
        qr.update(sql.toString(), params);
    }
    
    /**
     * Validacion para registrar
     * Metodo que valida los datos, para evitar datos duplicados
     * @param razon
     * @return
     * @throws Exception 
     */
    public ResultInteger validaForInsert(RazonParoDTO razon) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_insertValidaPetCatRazonParo ?, ?, ?");
        Object[] params = { 
            razon.getValor(), razon.getDescripcion(), razon.getId_fuente_paro() 
        };
        
        ResultSetHandler rsh = new BeanHandler(ResultInteger.class);
        ResultInteger result = (ResultInteger) qr.query(sql.toString(), rsh, params);
        return result;
    }
    
    /**
     * Validacion para modificar
     * Metodo que valida los datos, para evitar datos duplicados
     * @param razon
     * @return
     * @throws Exception 
     */
    public ResultInteger validaForUpdate(RazonParoDTO razon) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_updateValidaPetCatRazonParo ?, ?, ?, ?");
        Object[] params = { 
            razon.getId_razon_paro(), razon.getValor(), 
            razon.getDescripcion(), razon.getId_fuente_paro() 
        };
        
        ResultSetHandler rsh = new BeanHandler(ResultInteger.class);
        ResultInteger result = (ResultInteger) qr.query(sql.toString(), rsh, params);
        return result;
    }
    
    /**
     * Razones de Paro para OEE
     * Metodo que devulve la lista de razones de paro para el reporte de Fallas OEE
     * @param fechaIn
     * @param fechaTe
     * @param idLinea
     * @param idFuente
     * @return
     * @throws Exception 
     */
    public List<RazonParoDTO> getFallasByOEE(Date fechaIn, Date fechaTe, int idLinea, int idFuente) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_selectReporte_petFalla ?, ?, ?, ?");
        Object[] params = {
            idFuente, fechaIn, fechaTe, idLinea
        };
       
        ResultSetHandler rsh = new BeanListHandler(RazonParoDTO.class);
        List<RazonParoDTO> data = (List<RazonParoDTO>) qr.query(sql.toString(), rsh, params);
        
        return data;
     }
}
