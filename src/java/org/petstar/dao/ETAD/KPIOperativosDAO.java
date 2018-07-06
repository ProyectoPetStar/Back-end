package org.petstar.dao.ETAD;

import java.util.List;
import javax.sql.DataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.petstar.configurations.PoolDataSource;
import org.petstar.dto.ETAD.PetCatKpiOperativo;
import org.petstar.dto.ResultInteger;

/**
 *
 * @author Tech-Pro
 */
public class KPIOperativosDAO {
    public List<PetCatKpiOperativo> getAllKPIOperativos() throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT * FROM pet_cat_kpi_operativo");
        
        ResultSetHandler rsh = new BeanListHandler(PetCatKpiOperativo.class);
        List<PetCatKpiOperativo> listData = (List<PetCatKpiOperativo>) qr.query(sql.toString(), rsh);
        return listData;
    }
    
    public List<PetCatKpiOperativo> getAllKPIOperativosActive() throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_selectCatKpiOperativo");
        
        ResultSetHandler rsh = new BeanListHandler(PetCatKpiOperativo.class);
        List<PetCatKpiOperativo> listData = (List<PetCatKpiOperativo>) qr.query(sql.toString(), rsh);
        return listData;
    }
    
    public List<PetCatKpiOperativo> getKPIOperativosByObjetivoAndEtad(int idObjetivo, int idEtad) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT cko.* FROM pet_cat_kpi_operativo cko ")
                .append("INNER JOIN pet_etad_kpi pek ON cko.id = pek.id_kpi_operativo ")
                .append("WHERE cko.id_cat_objetivo_operativo = ").append(idObjetivo)
                .append(" AND pek.id_etad = ").append(idEtad);
        //Object[] params = { idObjetivo, idEtad };
        ResultSetHandler rsh = new BeanListHandler(PetCatKpiOperativo.class);
        List<PetCatKpiOperativo> listData = (List<PetCatKpiOperativo>) qr.query(sql.toString(), rsh);
        return listData;
    }
    
    public List<PetCatKpiOperativo> getListKPIOperativosAnuales() throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT * FROM pet_cat_kpi_operativo WHERE activo = 1 AND anual=1");
        
        ResultSetHandler rsh = new BeanListHandler(PetCatKpiOperativo.class);
        List<PetCatKpiOperativo> listData = (List<PetCatKpiOperativo>) qr.query(sql.toString(), rsh);
        return listData;
    }
    
    public PetCatKpiOperativo getKPIOperativoById(int id) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_selectCatKpiOperativoById ?");
        Object[] params = { id };
        
        ResultSetHandler rsh = new BeanHandler(PetCatKpiOperativo.class);
        PetCatKpiOperativo data = (PetCatKpiOperativo) qr.query(sql.toString(), rsh, params);
        ObjetivosOperativosDAO ooDAO = new ObjetivosOperativosDAO();
        data.setObjetivoOperativo(ooDAO.getObjetivoOperativoById(data.getId_cat_objetivo_operativo()));
        return data;
    }
    
    public void changeEstatus(int id, int estatus) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("UPDATE pet_cat_kpi_operativo SET activo = ? WHERE id = ?");
        Object[] params = { estatus, id };
        
        qr.update(sql.toString(), params);
    }
    
    public ResultInteger validateUpdate(PetCatKpiOperativo pcko) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT COUNT(1) AS result FROM pet_cat_kpi_operativo ")
                .append("WHERE id <> ? AND ( valor = ? OR descripcion = ?)");
        Object[] params = { pcko.getId(), pcko.getValor(), pcko.getDescripcion() };
        
        ResultSetHandler rsh = new BeanHandler(ResultInteger.class);
        ResultInteger result = (ResultInteger) qr.query(sql.toString(), rsh, params);
        return result;
    }
    
    public ResultInteger validateInsert(PetCatKpiOperativo pcko) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT COUNT(1) AS result FROM pet_cat_kpi_operativo ")
                .append("WHERE valor = ? OR descripcion = ?");
        Object[] params = { pcko.getValor(), pcko.getDescripcion() };
        
        ResultSetHandler rsh = new BeanHandler(ResultInteger.class);
        ResultInteger result = (ResultInteger) qr.query(sql.toString(), rsh, params);
        return result;
    }
    
    public void updateKPIOperativo(PetCatKpiOperativo pcko) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql1 = new StringBuilder();
        StringBuilder sql2 = new StringBuilder();
        
        sql1.append("UPDATE pet_cat_kpi_operativo SET valor = ?, descripcion = ?, ")
                .append("unidad_medida = ?, tipo_kpi = ?, id_frecuencia = ?, ")
                .append("tipo_operacion = ? WHERE id = ?");
        Object[] params1 = { pcko.getValor(), pcko.getDescripcion(), pcko.getUnidad_medida(),
            pcko.getTipo_kpi(), pcko.getId_frecuencia(), pcko.getTipo_operacion(), pcko.getId() };
        
        sql2.append("DELETE FROM pet_etad_kpi ")
                .append("WITH (TABLOCK) WHERE id_kpi_operativo = ?");
        Object[] params2 = { pcko.getId() };
        qr.update(sql1.toString(), params1);
        qr.update(sql2.toString(), params2);
    }
    
    public void asignaLineasToKPIOperativos(PetCatKpiOperativo pcko) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        String[] lineas = pcko.getLineas().split(",");
        
        sql.append("INSERT INTO pet_etad_kpi ")
                .append("(id_kpi_operativo, id_etad) VALUES (?, ?)");
        
        for(String linea:lineas){
            Object[] params = { pcko.getId(), Integer.valueOf(linea) };
            qr.update(sql.toString(), params);
        }
    }
    
    public ResultInteger insertKPIOperativo(PetCatKpiOperativo pcko) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("INSERT INTO pet_cat_kpi_operativo (valor,descripcion,")
                .append("id_cat_objetivo_operativo,id_frecuencia,unidad_medida,")
                .append("tipo_kpi,tipo_operacion,activo) ")
                .append("OUTPUT INSERTED.ID AS result VALUES(?,?,?,?,?,?,?,?)");
        Object[] params = { pcko.getValor(), pcko.getDescripcion(), pcko.getId_cat_objetivo_operativo(),
            pcko.getId_frecuencia(), pcko.getUnidad_medida(), pcko.getTipo_kpi(), pcko.getTipo_kpi(), 1 };
        
        ResultSetHandler rsh = new BeanHandler(ResultInteger.class);
        ResultInteger result = (ResultInteger) qr.query(sql.toString(), rsh, params);
        return result;
    }
}
