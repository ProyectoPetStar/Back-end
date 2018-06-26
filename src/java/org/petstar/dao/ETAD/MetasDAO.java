package org.petstar.dao.ETAD;

import java.util.List;
import javax.sql.DataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.petstar.configurations.PoolDataSource;
import org.petstar.dto.ETAD.PetMetaAnualKpi;
import org.petstar.dto.ETAD.PetMetaAnualEstrategica;
import org.petstar.dto.ETAD.PetMetaAnualObjetivoOperativo;

/**
 *
 * @author Tech-Pro
 */
public class MetasDAO {
    public List<PetMetaAnualEstrategica> getAllMetasMetasEstrategicasAnuales(
            int idEtad, int year) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT * FROM pet_meta_anual_estrategica WHERE anio=? and id_linea= ?");
        Object[] params = { year, idEtad };
        
        ResultSetHandler rsh = new BeanListHandler(PetMetaAnualEstrategica.class);
        List<PetMetaAnualEstrategica> listData = 
                (List<PetMetaAnualEstrategica>) qr.query(sql.toString(), rsh, params);
        return listData;
    }
    
    public List<PetMetaAnualObjetivoOperativo> getAllMetasObjetivosOperativosAnuales(
            int idEtad, int year) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT * FROM pet_meta_anual_objetivo_operativo WHERE anio=? AND id_linea=?");
        Object[] params = { year, idEtad };
        
        ResultSetHandler rsh = new BeanListHandler(PetMetaAnualObjetivoOperativo.class);
        List<PetMetaAnualObjetivoOperativo> listData = 
                (List<PetMetaAnualObjetivoOperativo>) qr.query(sql.toString(), rsh, params);
        return listData;
    }
    
    public List<PetMetaAnualKpi> getAllMetasKPIOperativosAnuales(int idEtad, int year) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT * FROM pet_meta_anual_kpi WHERE anio=? AND id_linea=?");
        Object[] params = { year, idEtad };
        
        ResultSetHandler rsh = new BeanListHandler(PetMetaAnualKpi.class);
        List<PetMetaAnualKpi> listData = 
                (List<PetMetaAnualKpi>) qr.query(sql.toString(), rsh, params);
        return listData;
    }
}
