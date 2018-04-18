package org.petstar.dao;

import java.util.List;
import javax.sql.DataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.petstar.configurations.PoolDataSource;
import org.petstar.dto.ForecastDTO;

/**
 * @author Tech-Pro
 */
public class ForecastDAO {
    
    public void loadForecast(List<ForecastDTO> listRows, int idLinea, int idFile) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_insert_petTmpMetas ?,?,?,?,?,?,?,?");
        
        for(ForecastDTO row : listRows){
            Object[] params = {
                row.getDia(), row.getMeta(), row.getTmp(), row.getVelocidad(), 
                row.getTurno(), row.getGrupo(), idLinea, idFile
            };

            qr.update(sql.toString(), params);
	}
    }
}
