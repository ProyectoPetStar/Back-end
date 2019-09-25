package org.petstar.dao.ETAD;

import java.sql.Date;
import java.util.List;
import javax.sql.DataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.petstar.configurations.PoolDataSource;
import org.petstar.dto.ETAD.PetEtadKpi;
import org.petstar.dto.ETAD.PetPonderacionKpiOperativo;
import org.petstar.dto.ETAD.PetPonderacionObjetivoOperativo;
import org.petstar.dto.ResultInteger;

/**
 *
 * @author Tech-Pro
 */
public class PonderacionDAO {
    public ResultInteger validateExistRecords(int anio) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT COUNT(1) AS result ")
                .append("FROM pet_ponderacion_objetivo_operativo WHERE anio=?");
        Object[] params = { anio };
        
        ResultSetHandler rsh = new BeanHandler(ResultInteger.class);
        ResultInteger result = (ResultInteger) qr.query(sql.toString(), rsh, params);
        return result;
    }
    
    public ResultInteger validateExistRecordsKPI(int anio, int idEtad) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_validaRecordKpi ?, ?");
        Object[] params = { anio, idEtad };
        
        ResultSetHandler rsh = new BeanHandler(ResultInteger.class);
        ResultInteger result = (ResultInteger) qr.query(sql.toString(), rsh, params);
        return result;
    }
    
    public void insertPonderacionObjetivos
        (List<PetPonderacionObjetivoOperativo> data, int usuario, Date fecha) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("INSERT INTO pet_ponderacion_objetivo_operativo ")
                .append("(anio,ponderacion,id_objetivo_operativo,id_usuario_registro,")
                .append("fecha_registro) VALUES (?,?,?,?,?)");
        
        for (PetPonderacionObjetivoOperativo row : data) {
            Object[] params = { row.getAnio(), row.getPonderacion(),
                row.getId_objetivo_operativo(), usuario, fecha};
            qr.update(sql.toString(), params);
        }
    }
        
    public void insertPonderacionKPI
        (List<PetPonderacionKpiOperativo> data, int anio, int usuario, Date fecha) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("INSERT INTO pet_ponderacion_kpi_operativo (anio,")
                .append("ponderacion,id_kpi_etad,id_usuario_registro,fecha_registro) ")
                .append("VALUES (?,?,?,?,?)");
        
        for (PetPonderacionKpiOperativo row : data) {
            Object[] params = { anio, row.getPonderacion(),
                row.getId_kpi_etad(), usuario, fecha};
            qr.update(sql.toString(), params);
        }
    }
    
    public void updatePonderacionObjetivos
        (List<PetPonderacionObjetivoOperativo> data, int usuario, Date fecha) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("UPDATE pet_ponderacion_objetivo_operativo SET ponderacion = ?, ")
                .append("id_usuario_modifica_registro = ?, fecha_modificacion = ? ")
                .append("WHERE id_ponderacion_obj_operativo = ?");
        
        for (PetPonderacionObjetivoOperativo row : data) {
            Object[] params = { row.getPonderacion(), usuario, fecha, 
                row.getId_ponderacion_obj_operativo()};
            qr.update(sql.toString(), params);
        }
    }
        
    public void updatePonderacionKPI
        (List<PetPonderacionKpiOperativo> data, int usuario, Date fecha) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("UPDATE pet_ponderacion_kpi_operativo SET ponderacion = ?, ")
                .append("id_usuario_modifica_registro = ?, fecha_modificacion = ? ")
                .append("WHERE id_ponderacion_kpi_operativo = ?");
        
        for (PetPonderacionKpiOperativo row : data) {
            Object[] params = { row.getPonderacion(), usuario, fecha, 
                row.getId_ponderacion_kpi_operativo()};
            qr.update(sql.toString(), params);
        }
    }
        
    public List<PetPonderacionObjetivoOperativo> getPonderacionObejtivos(int anio) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT * FROM pet_ponderacion_objetivo_operativo WHERE anio=?");
        Object[] params = { anio };
        
        ResultSetHandler rsh = new BeanListHandler(PetPonderacionObjetivoOperativo.class);
        List<PetPonderacionObjetivoOperativo> data = (List<PetPonderacionObjetivoOperativo>) 
                qr.query(sql.toString(), rsh, params);
        
        ObjetivosOperativosDAO oodao = new ObjetivosOperativosDAO();
        for(PetPonderacionObjetivoOperativo row : data){
            row.setObjetivoOperativo(oodao.getObjetivoOperativoById(row.getId_objetivo_operativo()));
        }
        return data;
    }
    
    public PetPonderacionObjetivoOperativo getPonderacionObejtivoById(int anio, int idObjetivo) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT * FROM pet_ponderacion_objetivo_operativo ")
                .append("WHERE anio = ? AND id_objetivo_operativo = ?");
        Object[] params = { anio, idObjetivo };
        
        ResultSetHandler rsh = new BeanHandler(PetPonderacionObjetivoOperativo.class);
        PetPonderacionObjetivoOperativo data = (PetPonderacionObjetivoOperativo) 
                qr.query(sql.toString(), rsh, params);
        
        ObjetivosOperativosDAO oodao = new ObjetivosOperativosDAO();
        data.setObjetivoOperativo(oodao.getObjetivoOperativoById(data.getId_objetivo_operativo()));
        return data;
    }
    
    public List<PetPonderacionKpiOperativo> getPonderacionKPI(int anio, int idEtad) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT pko.* FROM pet_ponderacion_kpi_operativo pko ")
                .append("INNER JOIN pet_etad_kpi pek ON pko.id_kpi_etad = pek.id_kpi_etad ")
                .append("INNER JOIN pet_cat_kpi_operativo cko ON pek.id_kpi_operativo = cko.id ")
                .append("WHERE pko.anio = ").append(anio).append(" AND pek.id_etad = ").append(idEtad)
                .append("AND pek.estatus = 1");
        
        ResultSetHandler rsh = new BeanListHandler(PetPonderacionKpiOperativo.class);
        List<PetPonderacionKpiOperativo> data = (List<PetPonderacionKpiOperativo>) 
                qr.query(sql.toString(), rsh);
        
        for(PetPonderacionKpiOperativo row : data){
            row.setPetEtadKpi(this.getEtadKpiById(row.getId_kpi_etad()));
        }
        return data;
    }
    
    public PetEtadKpi getEtadKpiById(int idEtadKpi) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT * FROM pet_etad_kpi WHERE id_kpi_etad = ?");
        Object[] params = { idEtadKpi };
        
        ResultSetHandler rsh = new BeanHandler(PetEtadKpi.class);
        PetEtadKpi petEtadKpi = (PetEtadKpi) qr.query(sql.toString(), rsh, params);
        
        KPIOperativosDAO kpioDAO = new KPIOperativosDAO();
        petEtadKpi.setKpiOperativo(kpioDAO.getKPIOperativoById(petEtadKpi.getId_kpi_operativo()));
        
        return petEtadKpi;
    }
    
    public PetEtadKpi getEtadKpi(int idKPI, int idEtad) throws Exception{
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
