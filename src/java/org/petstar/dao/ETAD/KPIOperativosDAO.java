package org.petstar.dao.ETAD;

import java.util.List;
import javax.sql.DataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.petstar.configurations.PoolDataSource;
import org.petstar.dto.ETAD.KPIOperativosDTO;

/**
 *
 * @author Tech-Pro
 */
public class KPIOperativosDAO {
     public List<KPIOperativosDTO> getListKPIOperativos() throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT * FROM pet_cat_kpi_operativo WHERE activo = 1");
        
        ResultSetHandler rsh = new BeanListHandler(KPIOperativosDTO.class);
        List<KPIOperativosDTO> listData = (List<KPIOperativosDTO>) qr.query(sql.toString(), rsh);
        return listData;
    }
}
