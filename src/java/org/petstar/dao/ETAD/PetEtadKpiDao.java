package org.petstar.dao.ETAD;

import javax.sql.DataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.petstar.configurations.PoolDataSource;
import org.petstar.dto.ETAD.PetEtadKpi;

/**
 *
 * @author Tech-Pro
 */
public class PetEtadKpiDao {
    public PetEtadKpi getEtadKpiById(int idEtadKpi) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT * FROM pet_etad_kpi WHERE id_kpi_etad = ?");
        Object[] params = { idEtadKpi };
        
        ResultSetHandler rsh = new BeanHandler(PetEtadKpi.class);
        PetEtadKpi etadKpi = (PetEtadKpi) qr.query(sql.toString(), rsh, params);
        
        KPIOperativosDAO kpioDAO = new KPIOperativosDAO();
        etadKpi.setKpiOperativo(kpioDAO.getKPIOperativoById(etadKpi.getId_kpi_operativo()));
        return etadKpi;
    }
    
    public PetEtadKpi getEtadKpiByKpiAndEtad(int idKPI, int idEtad) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT * FROM pet_etad_kpi WHERE id_etad=? AND id_kpi_operativo =?");
        Object[] params = { idEtad, idKPI };
        
        ResultSetHandler rsh = new BeanHandler(PetEtadKpi.class);
        PetEtadKpi petEtadKpi = (PetEtadKpi) qr.query(sql.toString(), rsh, params);
        
        KPIOperativosDAO kpioDAO = new KPIOperativosDAO();
        petEtadKpi.setKpiOperativo(kpioDAO.getKPIOperativoById(petEtadKpi.getId_kpi_operativo()));
        
        return petEtadKpi;
    }
}
