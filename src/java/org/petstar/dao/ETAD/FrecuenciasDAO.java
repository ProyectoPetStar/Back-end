package org.petstar.dao.ETAD;

import java.util.List;
import javax.sql.DataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.petstar.configurations.PoolDataSource;
import org.petstar.dto.ETAD.PetCatFrecuencia;

/**
 *
 * @author Tech-Pro
 */
public class FrecuenciasDAO {
    public List<PetCatFrecuencia> getAllFrecuenciasActive() throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT * FROM pet_cat_frecuencia WHERE activo = 1");
        
        ResultSetHandler rsh = new BeanListHandler(PetCatFrecuencia.class);
        List<PetCatFrecuencia> listData = (List<PetCatFrecuencia>) qr.query(sql.toString(), rsh);
        return listData;
    }
    
    public PetCatFrecuencia getFrecuenciaById(int id) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT * FROM pet_cat_frecuencia WHERE id = ?");
        Object[] params = { id };
        
        ResultSetHandler rsh = new BeanHandler(PetCatFrecuencia.class);
        PetCatFrecuencia data = (PetCatFrecuencia) qr.query(sql.toString(), rsh, params);
        return data;
    }
}
