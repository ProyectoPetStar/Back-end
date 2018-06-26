package org.petstar.dao.ETAD;

import java.util.List;
import javax.sql.DataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.petstar.configurations.PoolDataSource;
import org.petstar.dto.ETAD.PetCatKpiOperativo;

/**
 *
 * @author Tech-Pro
 */
public class KPIOperativosDAO {
    public List<PetCatKpiOperativo> getListKPIOperativos() throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT * FROM pet_cat_kpi_operativo WHERE activo = 1");
        
        ResultSetHandler rsh = new BeanListHandler(PetCatKpiOperativo.class);
        List<PetCatKpiOperativo> listData = (List<PetCatKpiOperativo>) qr.query(sql.toString(), rsh);
        return listData;
    }
    
    public PetCatKpiOperativo getKPIOperativoById(int id) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT * FROM pet_cat_kpi_operativo WHERE id = ?");
        Object[] params = { id };
        
        ResultSetHandler rsh = new BeanHandler(PetCatKpiOperativo.class);
        PetCatKpiOperativo data = (PetCatKpiOperativo) qr.query(sql.toString(), rsh, params);
        return data;
    }
}
