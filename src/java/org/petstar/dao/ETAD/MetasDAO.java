package org.petstar.dao.ETAD;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.petstar.configurations.PoolDataSource;
import org.petstar.dto.ETAD.PetCatKpiOperativo;
import org.petstar.dto.ETAD.PetMetaKpi;
import org.petstar.dto.ResultInteger;

/**
 *
 * @author Tech-Pro
 */
public class MetasDAO {
    public List<PetMetaKpi> getMetasKPIByEtadAndPeriodo(int idEtad, int idPeriodo) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT pmk.* FROM pet_meta_kpi AS pmk ")
                .append("INNER JOIN pet_etad_kpi AS pek ON pmk.id_kpi_etad = pek.id_kpi_etad ")
                .append("INNER JOIN pet_cat_kpi_operativo AS cko ON pek.id_kpi_operativo = cko.id ")
                .append("WHERE pmk.id_periodo=").append(idPeriodo).append(" AND pek.id_etad=").append(idEtad);
        
        ResultSetHandler rsh = new BeanListHandler(PetMetaKpi.class);
        List<PetMetaKpi> listData = (List<PetMetaKpi>) qr.query(sql.toString(), rsh);
        
        PetEtadKpiDao etadKpiDao = new PetEtadKpiDao();
        for(PetMetaKpi row:listData){
            row.setEtadKpi(etadKpiDao.getEtadKpiById(row.getId_kpi_etad()));
        }
        return listData;
    }
    
    public PetMetaKpi getMetaKPIById(int idMeta) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT * FROM pet_meta_kpi WHERE id_meta_kpi = ?");
        Object[] params = { idMeta };
        
        ResultSetHandler rsh = new BeanHandler(PetMetaKpi.class);
        PetMetaKpi data = (PetMetaKpi) qr.query(sql.toString(), rsh, params);
        
        PetEtadKpiDao etadKpiDao = new PetEtadKpiDao();
        data.setEtadKpi(etadKpiDao.getEtadKpiById(data.getId_kpi_etad()));
        return data;
    }
    
    public List<PetMetaKpi> getKpisWithoutMeta(int idEtad) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT cko.* FROM pet_cat_kpi_operativo AS cko INNER JOIN pet_etad_kpi AS pek ")
                .append(" ON cko.id = pek.id_kpi_operativo AND pek.id_etad=").append(idEtad);
        
        ResultSetHandler rsh = new BeanListHandler(PetCatKpiOperativo.class);
        List<PetCatKpiOperativo> listData = (List<PetCatKpiOperativo>) qr.query(sql.toString(), rsh);
        
        List<PetMetaKpi> listMetas = new ArrayList<>();
        PetEtadKpiDao etadKpiDao = new PetEtadKpiDao();
        for(PetCatKpiOperativo row:listData){
            PetMetaKpi meta = new PetMetaKpi();
            meta.setEtadKpi(etadKpiDao.getEtadKpiByKpiAndEtad(row.getId(), idEtad));
            meta.setId_kpi_etad(meta.getEtadKpi().getId_kpi_etad());
            listMetas.add(meta);
        }
        return listMetas;
    }
    
    public ResultInteger validateForInsertKPIOperativo(int idEtad, int idPeriodo) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT COUNT(1) AS result FROM pet_meta_kpi pmk INNER JOIN pet_etad_kpi ")
                .append("pek ON pmk.id_kpi_etad = pek.id_kpi_etad WHERE pek.id_etad=")
                .append(idEtad).append(" AND pmk.id_periodo=").append(idPeriodo);
        
        ResultSetHandler rsh = new BeanHandler(ResultInteger.class);
        ResultInteger result = (ResultInteger) qr.query(sql.toString(), rsh);
        return result;
    }
   
    public void insertKPIOperativosAnual(List<PetMetaKpi> metas, int idPeriodo, int usuario, Date fecha)throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("INSERT INTO pet_meta_kpi (valor,id_periodo,id_kpi_etad,estatus,")
                .append("id_usuario_registro,fecha_registro) VALUES (?,?,?,?,?,?)");
        for(PetMetaKpi row: metas){
            Object[] params = { row.getValor(), idPeriodo, 
                row.getId_kpi_etad(), 1, usuario, fecha};
            
            qr.update(sql.toString(),params);
        }
    }
    
    public void updateKPIOperativos(List<PetMetaKpi> metas, int usuario, Date fecha)throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("UPDATE pet_meta_kpi SET valor = ?, ")
                .append("id_usuario_modifica_registro = ?, fecha_modificacion = ? ")
                .append("WHERE id_meta_kpi = ?");
        for(PetMetaKpi row:metas){
            Object[] params = { row.getValor(), usuario, fecha, row.getId_meta_kpi()};
            qr.update(sql.toString(),params);
        }
    }
}
