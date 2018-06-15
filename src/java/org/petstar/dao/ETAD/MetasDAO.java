package org.petstar.dao.ETAD;

import java.util.List;
import javax.sql.DataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.petstar.configurations.PoolDataSource;
import org.petstar.dto.ETAD.MetasKPIOperativosDTO;
import org.petstar.dto.ETAD.MetasMetasEstrategicasDTO;
import org.petstar.dto.ETAD.MetasObjetivosOperativosDTO;

/**
 *
 * @author Tech-Pro
 */
public class MetasDAO {
    public List<MetasMetasEstrategicasDTO> getAllMetasMetasEstrategicasAnuales(
            int idEtad, int year) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC  ?, ?");
        Object[] params = { year, idEtad };
        
        ResultSetHandler rsh = new BeanListHandler(MetasMetasEstrategicasDTO.class);
        List<MetasMetasEstrategicasDTO> listData = 
                (List<MetasMetasEstrategicasDTO>) qr.query(sql.toString(), rsh, params);
        return listData;
    }
    
    public List<MetasObjetivosOperativosDTO> getAllMetasObjetivosOperativosAnuales(
            int idEtad, int year) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_selectPetMetaAnualObjOpe ?, ?");
        Object[] params = { year, idEtad };
        
        ResultSetHandler rsh = new BeanListHandler(MetasObjetivosOperativosDTO.class);
        List<MetasObjetivosOperativosDTO> listData = 
                (List<MetasObjetivosOperativosDTO>) qr.query(sql.toString(), rsh, params);
        return listData;
    }
    
    public List<MetasKPIOperativosDTO> getAllMetasKPIOperativosAnuales(
            int idEtad, int year) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_selectPetMetaAnualKpi ?, ?");
        Object[] params = { year, idEtad };
        
        ResultSetHandler rsh = new BeanListHandler(MetasKPIOperativosDTO.class);
        List<MetasKPIOperativosDTO> listData = 
                (List<MetasKPIOperativosDTO>) qr.query(sql.toString(), rsh, params);
        return listData;
    }
}
