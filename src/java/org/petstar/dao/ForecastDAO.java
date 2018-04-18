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
    
    public void loadForecast(List<ForecastDTO> listRows) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("INSERT INTO pet_tmp_meta (dia,meta,tmp,velocidad,id_turno,id_grupo,id_linea,id_archivo) VALUES (?,?,?,?,?,?,?,?)");
        
        for(ForecastDTO row : listRows){
            Object[] params = {
                row.getDia(), row.getMeta(), row.getTmp(), row.getVelocidad(), 
                row.getTurno(), row.getGrupo(), 1, 1, 
                
            };

            qr.update(sql.toString(), params);
	}
    }
}
