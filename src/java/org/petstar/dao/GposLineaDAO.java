package org.petstar.dao;

import java.util.List;
import javax.sql.DataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.petstar.configurations.PoolDataSource;
import org.petstar.dto.GposLineaDTO;

/**
 *
 * @author Tech-Pro
 */
public class GposLineaDAO {
    public List<GposLineaDTO> getGposLineaActiveForOEE() throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_selectPetCatGpoLineaOee");
        
        ResultSetHandler rsh = new BeanListHandler(GposLineaDTO.class);
        List<GposLineaDTO> list = (List<GposLineaDTO>) qr.query(sql.toString(), rsh);
        return list;
    }
}
