package org.petstar.dao.ETAD;

import java.util.List;
import javax.sql.DataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.petstar.configurations.PoolDataSource;
import org.petstar.dto.ETAD.PetCatMetaEstrategica;

/**
 *
 * @author Tech-Pro
 */
public class MetasEstrategicasDAO {
    public List<PetCatMetaEstrategica> getListMetasEstrategicasAnuales() throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT * FROM pet_cat_meta_estrategica WHERE activo = 1 AND anual =1");
        
        ResultSetHandler rsh = new BeanListHandler(PetCatMetaEstrategica.class);
        List<PetCatMetaEstrategica> listData = (List<PetCatMetaEstrategica>) qr.query(sql.toString(), rsh);
        return listData;
    }
    
    public PetCatMetaEstrategica getMetaEstrategicaAnualById(int id) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT * FROM pet_cat_meta_estrategica WHERE id=?");
        Object[] params = { id };
        
        ResultSetHandler rsh = new BeanHandler(PetCatMetaEstrategica.class);
        PetCatMetaEstrategica data = (PetCatMetaEstrategica) qr.query(sql.toString(), rsh, params);
        return data;
    }
}
