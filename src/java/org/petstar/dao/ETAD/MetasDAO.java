package org.petstar.dao.ETAD;

import java.sql.Date;
import java.util.List;
import javax.sql.DataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.petstar.configurations.PoolDataSource;
import org.petstar.dto.ETAD.PetMetaAnualKpi;
import org.petstar.dto.ETAD.PetMetaAnualEstrategica;
import org.petstar.dto.ETAD.PetMetaAnualObjetivoOperativo;
import org.petstar.dto.ResultInteger;
import org.petstar.model.ETAD.MetasModel;

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
    
    public ResultInteger validateForInsertMetaEstrategicaAnual(MetasModel meta) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT COUNT(1) AS result FROM pet_meta_anual_estrategica ")
                .append("WHERE anio=? AND id_linea=? AND id_meta_estrategica=?");
        Object[] params = { meta.getAnio(), meta.getId_etad(), 
            meta.getMetaEstrategica().getId_meta_estrategica() };
        
        ResultSetHandler rsh = new BeanHandler(ResultInteger.class);
        ResultInteger result = (ResultInteger) qr.query(sql.toString(), rsh, params);
        return result;
    }
    
    public ResultInteger validateForInsertObjetivoOperativoAnual(MetasModel meta) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT COUNT(1) AS result FROM pet_meta_anual_objetivo_operativo ")
                .append("WHERE anio=? AND id_linea=? AND id_objetivo_operativo=?");
        Object[] params = { meta.getAnio(), meta.getId_etad(), 
            meta.getObjetivoOperativo().getId_objetivo_operativo() };
        
        ResultSetHandler rsh = new BeanHandler(ResultInteger.class);
        ResultInteger result = (ResultInteger) qr.query(sql.toString(), rsh, params);
        return result;
    }
    
    public ResultInteger validateForInsertKPIOperativoAnual(MetasModel meta) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT COUNT(1) AS result FROM pet_meta_anual_kpi ")
                .append("WHERE anio=? AND id_linea=? AND id_kpi_operativo=?");
        Object[] params = { meta.getAnio(), meta.getId_etad(), 
            meta.getkPIOperativo().getId_kpi_operativo() };
        
        ResultSetHandler rsh = new BeanHandler(ResultInteger.class);
        ResultInteger result = (ResultInteger) qr.query(sql.toString(), rsh, params);
        return result;
    }
    
    public void insertMetaEstrategicaAnual(MetasModel meta, int usuario, Date fecha)throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_insertManualPetMetaAnualEstrategica ?, ?, ?, ?, ?, ?");
        Object[] params = { meta.getId_etad(), 
            meta.getMetaEstrategica().getId_meta_estrategica(), meta.getAnio(), 
            meta.getMetaEstrategica().getValor(), usuario, fecha };
        
        qr.update(sql.toString(),params);
    }
    
    public void insertObjetivosOperativosAnual(MetasModel meta, int usuario, Date fecha)throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_insertManualPetMetaAnualObjOperativo ?, ?, ?, ?, ?, ?");
        Object[] params = { meta.getId_etad(), 
            meta.getObjetivoOperativo().getId_objetivo_operativo(), meta.getAnio(), 
            meta.getObjetivoOperativo().getValor(), usuario, fecha };
        
        qr.update(sql.toString(),params);
    }
    
    public void insertKPIOperativosAnual(MetasModel meta, int usuario, Date fecha)throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_insertManualPetMetaAnualKpi ?, ?, ?, ?, ?, ?");
        Object[] params = { meta.getId_etad(), 
            meta.getkPIOperativo().getId_kpi_operativo(), meta.getAnio(), 
            meta.getkPIOperativo().getValor(), usuario, fecha };
        
        qr.update(sql.toString(),params);
    }
    
    public PetMetaAnualEstrategica getMetaAnualEstrategicaById(int idMeta) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT * FROM pet_meta_anual_estrategica WHERE id_meta_anual_estrategica = ?");
        Object[] params = { idMeta };
        
        ResultSetHandler rsh = new BeanHandler(PetMetaAnualEstrategica.class);
        PetMetaAnualEstrategica data = (PetMetaAnualEstrategica) qr.query(sql.toString(), rsh, params);
        return data;
    }
    
    public PetMetaAnualObjetivoOperativo getMetaAnualObjetivoOperativoById(int idMeta) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT * FROM pet_meta_anual_objetivo_operativo WHERE id_meta_anual_objetivo_operativo = ?");
        Object[] params = { idMeta };
        
        ResultSetHandler rsh = new BeanHandler(PetMetaAnualObjetivoOperativo.class);
        PetMetaAnualObjetivoOperativo data = (PetMetaAnualObjetivoOperativo) qr.query(sql.toString(), rsh, params);
        return data;
    }
    
    public PetMetaAnualKpi getMetaAnualKPIOperativoById(int idMeta) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT * FROM pet_meta_anual_kpi WHERE id_meta_anual_kpi = ?");
        Object[] params = { idMeta };
        
        ResultSetHandler rsh = new BeanHandler(PetMetaAnualKpi.class);
        PetMetaAnualKpi data = (PetMetaAnualKpi) qr.query(sql.toString(), rsh, params);
        return data;
    }
    
    public void deleteMetaEstrategicaAnual(MetasModel meta)throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("DELETE FROM pet_meta_anual_estrategica")
                .append(" WITH (TABLOCK) WHERE id_meta_anual_estrategica=?");
        Object[] params = { meta.getMetaEstrategica().getId_meta_anual_estrategica() };
        
        qr.update(sql.toString(),params);
    }
    
    public void deleteObjetivosOperativosAnual(MetasModel meta)throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("DELETE FROM pet_meta_anual_objetivo_operativo ")
                .append("WITH (TABLOCK) WHERE id_meta_anual_objetivo_operativo=? ");
        Object[] params = { meta.getObjetivoOperativo().getId_meta_anual_objetivo_operativo() };
        
        qr.update(sql.toString(),params);
    }
    
    public void deleteKPIOperativosAnual(MetasModel meta)throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("DELETE FROM pet_meta_anual_kpi WITH (TABLOCK) WHERE id_meta_anual_kpi=?");
        Object[] params = { meta.getkPIOperativo().getId_meta_anual_kpi() };
        
        qr.update(sql.toString(),params);
    }
    
    public void updateMetaEstrategicaAnual(MetasModel meta, int usuario, Date fecha)throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("UPDATE pet_meta_anual_estrategica SET valor=?, ")
                .append("id_usuario_modificacion=?, fecha_modificacion=? ")
                .append("WHERE id_meta_anual_estrategica = ?");
        Object[] params = { meta.getMetaEstrategica().getValor(), usuario, fecha, 
            meta.getMetaEstrategica().getId_meta_anual_estrategica()};
        
        qr.update(sql.toString(),params);
    }
    
    public void updateObjetivosOperativosAnual(MetasModel meta, int usuario, Date fecha)throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("UPDATE pet_meta_anual_objetivo_operativo SET valor=?, ")
                .append("id_usuario_modifica_registro=?, fecha_modificacion_registro=? ")
                .append("WHERE id_meta_anual_objetivo_operativo = ?");
        Object[] params = { meta.getObjetivoOperativo().getValor(), usuario, fecha,
            meta.getObjetivoOperativo().getId_meta_anual_objetivo_operativo()};
        
        qr.update(sql.toString(),params);
    }
    
    public void updateKPIOperativosAnual(MetasModel meta, int usuario, Date fecha)throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("UPDATE pet_meta_anual_kpi SET valor=?, ")
                .append("id_usuario_modifica_registro=?, fecha_modificacion=? ")
                .append("WHERE id_meta_anual_kpi = ?");
        Object[] params = { meta.getkPIOperativo().getValor(), usuario, fecha,
            meta.getkPIOperativo().getId_meta_anual_kpi() };
        
        qr.update(sql.toString(),params);
    }
}
