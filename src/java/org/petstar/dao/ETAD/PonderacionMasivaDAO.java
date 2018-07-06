package org.petstar.dao.ETAD;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import javax.sql.DataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.petstar.configurations.PoolDataSource;
import org.petstar.dto.ETAD.ValidacionKPI;
import org.petstar.dto.ResultInteger;

/**
 *
 * @author Tech-Pro
 */
public class PonderacionMasivaDAO {
    public ResultInteger saveFile(String nameFile, int usuario, Date fecha) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_insertPetArchivoKpi ?, ?, ?");
        Object[] params = { nameFile, usuario, fecha};
        
        ResultSetHandler rsh = new BeanHandler(ResultInteger.class);
        ResultInteger result = (ResultInteger) qr.query(sql.toString(), rsh, params);
        return result;
    }
    
    public void insertPonderacionObjetivosOperativos(List<HashMap> data, int archivo) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        StringBuilder sql1 = new StringBuilder();
        sql1.append("DELETE FROM pet_tmp_ponderacion_objetivo_operativo WITH (TABLOCK) WHERE anio=?");
        Object[] params1 = {data.get(0).get("year")};
        qr.update(sql1.toString(),params1);
        
        sql.append("EXEC sp_insertTmpPonderacionObjOperativo ?, ?, ?, ?");
        for(int i=0; i<data.size(); i++){
            Object[] params = { data.get(i).get("year"), data.get(i).get("ponderacion"),
                    data.get(i).get("objetivo"), archivo };
            
            qr.update(sql.toString(),params);
        }
    }
    
    public void insertPonderacionKPIOperativos(List<HashMap> data, int archivo) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_insertTmpPonderacionKpi ?,?,?,?,?");
        for(int i=0; i<data.size(); i++){
            Object[] params = { data.get(i).get("year"), data.get(i).get("ponderacion"),
                data.get(i).get("kpi"), archivo, data.get(i).get("idEtad") };
            
            qr.update(sql.toString(),params);
        }
    }
    
    public ResultInteger validateExistDataObjetivosOperativos(int anio) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT COUNT(1) AS result ")
                .append("FROM pet_ponderacion_objetivo_operativo WHERE anio = ?");
        Object[] params = { anio };
        
        ResultSetHandler rsh = new BeanHandler(ResultInteger.class);
        ResultInteger result = (ResultInteger) qr.query(sql.toString(), rsh, params);
        return result;
    }
    
    public ResultInteger validateExistDataKPIOperativos(int anio, int idEtad) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT COUNT(1) result FROM pet_ponderacion_kpi_operativo pki ")
                .append("INNER JOIN pet_etad_kpi pek ON pek.id_kpi_etad = pki.id_kpi_etad ")
                .append("WHERE pki.anio = ").append(anio)
                .append(" AND pek.id_etad = ").append(idEtad);
        
        ResultSetHandler rsh = new BeanHandler(ResultInteger.class);
        ResultInteger result = (ResultInteger) qr.query(sql.toString(), rsh);
        return result;
    }
    
    public void loadDataObjetivosOperativos(int anio) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_insertPetPonderacionObjOperativo ?");
        Object[] params = { anio };
        
        qr.update(sql.toString(), params);
    }
    
    public void loadDataKPIOperativos(int anio, int idEtad) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_insertPetPonderacionKpiOperativo ?,?");
        Object[] params = { anio, idEtad };
        
        qr.update(sql.toString(), params);
    }
    
    public void rewriteDataAnualObjetivosOperativos(int year) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_UpdatePonderacionObjetivoOperativo ?");
        Object[] params = { year };
        
        qr.update(sql.toString(), params);
    }
    
    public List<ValidacionKPI> comparacionPonderacionKPI(int anio, int idEtad) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT valor, ponderacion, suma FROM( ")
                .append("SELECT coo.valor, poo.ponderacion, SUM(pki.ponderacion) AS suma ")
                .append("FROM pet_tmp_ponderacion_kpi_operativo pki ")
                .append("INNER JOIN pet_etad_kpi pek ON pki.id_kpi_etad = pek.id_kpi_etad ")
                .append("INNER JOIN pet_cat_kpi_operativo cko ON pek.id_kpi_operativo = cko.id ")
                .append("INNER JOIN pet_cat_objetivo_operativo coo ON cko.id_cat_objetivo_operativo = coo.id ")
                .append("INNER JOIN pet_ponderacion_objetivo_operativo poo ON coo.id = poo.id_objetivo_operativo ")
                .append("AND poo.anio=pki.anio WHERE pki.anio = ").append(anio).append(" AND pek.id_etad = ")
                .append(idEtad).append("GROUP BY coo.valor, poo.ponderacion ) t1 ");
        
        ResultSetHandler rsh = new BeanListHandler(ValidacionKPI.class);
        List<ValidacionKPI> listResult = (List<ValidacionKPI>) qr.query(sql.toString(), rsh);
        return listResult;
    }
    
    public void cleanTmpKpiOperativos(int anio, int idEtad) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("DELETE pet_tmp_ponderacion_kpi_operativo ")
                .append("FROM pet_tmp_ponderacion_kpi_operativo pki ")
                .append("INNER JOIN pet_etad_kpi pek ON pek.id_kpi_etad = ")
                .append("pki.id_kpi_etad WHERE pki.anio = ").append(anio)
                .append(" AND pek.id_etad = ").append(idEtad);
        
        qr.update(sql.toString());
    }
}
