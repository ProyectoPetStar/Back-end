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
    public List<RazonParoDTO> getAllRazones() throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_selectPetCatRazonParo");
       
        ResultSetHandler rsh = new BeanListHandler(RazonParoDTO.class);
        List<RazonParoDTO> data = (List<RazonParoDTO>) qr.query(sql.toString(), rsh);
        
        return data;
    }
    
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
    
    public void blockRazones(int idRazon, int activo) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("UPDATE pet_cat_razon_paro SET activo = ? WHERE id_razon_paro = ?");
        Object[] params = { activo, idRazon };
        
        qr.update(sql.toString(), params);
    }
    
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
    
    public ResultInteger validaForUpdate(RazonParoDTO razon) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_updateValidaPetCatRazonParo ?, ?, ?");
        Object[] params = { 
            razon.getId_razon_paro(), razon.getValor(), 
            razon.getDescripcion(), razon.getId_fuente_paro() 
        };
        
        ResultSetHandler rsh = new BeanHandler(ResultInteger.class);
        ResultInteger result = (ResultInteger) qr.query(sql.toString(), rsh, params);
        return result;
    }
    
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
