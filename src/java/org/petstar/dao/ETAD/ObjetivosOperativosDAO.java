package org.petstar.dao.ETAD;

import java.util.List;
import javax.sql.DataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.petstar.configurations.PoolDataSource;
import org.petstar.dto.ETAD.ObjetivosOperativosDTO;

/**
 *
 * @author Tech-Pro
 */
public class ObjetivosOperativosDAO {
    public List<ObjetivosOperativosDTO> getListObjetivosOperativos() throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT * FROM pet_cat_objetivo_operativo WHERE activo=1");
        
        ResultSetHandler rsh = new BeanListHandler(ObjetivosOperativosDTO.class);
        List<ObjetivosOperativosDTO> listData = (List<ObjetivosOperativosDTO>) qr.query(sql.toString(), rsh);
        return listData;
    }
}
