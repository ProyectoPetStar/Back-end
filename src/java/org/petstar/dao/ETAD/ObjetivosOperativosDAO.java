package org.petstar.dao.ETAD;

import java.util.List;
import javax.sql.DataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.petstar.configurations.PoolDataSource;
import org.petstar.dto.ETAD.PetCatObjetivoOperativo;

/**
 *
 * @author Tech-Pro
 */
public class ObjetivosOperativosDAO {
    public List<PetCatObjetivoOperativo> getAllObjetivosOperativosActive() throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_selectCatObjetivoOperativo");
        
        ResultSetHandler rsh = new BeanListHandler(PetCatObjetivoOperativo.class);
        List<PetCatObjetivoOperativo> listData = (List<PetCatObjetivoOperativo>) qr.query(sql.toString(), rsh);
        return listData;
    }
    
    public List<PetCatObjetivoOperativo> getListObjetivosOperativosAnuales() throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT * FROM pet_cat_objetivo_operativo WHERE activo=1 AND anual=1");
        
        ResultSetHandler rsh = new BeanListHandler(PetCatObjetivoOperativo.class);
        List<PetCatObjetivoOperativo> listData = (List<PetCatObjetivoOperativo>) qr.query(sql.toString(), rsh);
        return listData;
    }
    
    public PetCatObjetivoOperativo getObjetivoOperativoById(int id) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT * FROM pet_cat_objetivo_operativo WHERE id=?");
        Object[] params = { id };
        
        ResultSetHandler rsh = new BeanHandler(PetCatObjetivoOperativo.class);
        PetCatObjetivoOperativo data = (PetCatObjetivoOperativo) qr.query(sql.toString(), rsh, params);
        return data;
    }
}
