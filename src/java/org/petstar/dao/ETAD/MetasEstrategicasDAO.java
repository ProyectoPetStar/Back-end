package org.petstar.dao.ETAD;

import java.util.List;
import javax.sql.DataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.petstar.configurations.PoolDataSource;
import org.petstar.dto.ETAD.PetCatMetaEstrategica;
import org.petstar.dto.ResultInteger;

/**
 *
 * @author Tech-Pro
 */
public class MetasEstrategicasDAO {
    public List<PetCatMetaEstrategica> getAllMetasEstrategicas() throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT * FROM pet_cat_meta_estrategica ORDER BY anual");
        
        ResultSetHandler rsh = new BeanListHandler(PetCatMetaEstrategica.class);
        List<PetCatMetaEstrategica> listData = (List<PetCatMetaEstrategica>) qr.query(sql.toString(), rsh);
        return listData;
    }
    
    public List<PetCatMetaEstrategica> getAllMetasEstrategicasActive() throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT * FROM pet_cat_meta_estrategica WHERE activo = 1");
        
        ResultSetHandler rsh = new BeanListHandler(PetCatMetaEstrategica.class);
        List<PetCatMetaEstrategica> listData = (List<PetCatMetaEstrategica>) qr.query(sql.toString(), rsh);
        return listData;
    }
    
    public List<PetCatMetaEstrategica> getListMetasEstrategicasAnuales() throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT * FROM pet_cat_meta_estrategica WHERE activo = 1 AND anual =1");
        
        ResultSetHandler rsh = new BeanListHandler(PetCatMetaEstrategica.class);
        List<PetCatMetaEstrategica> listData = (List<PetCatMetaEstrategica>) qr.query(sql.toString(), rsh);
        return listData;
    }
    
    public PetCatMetaEstrategica getMetaEstrategicaAnualById(int id) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT * FROM pet_cat_meta_estrategica WHERE id=?");
        Object[] params = { id };
        
        ResultSetHandler rsh = new BeanHandler(PetCatMetaEstrategica.class);
        PetCatMetaEstrategica data = (PetCatMetaEstrategica) qr.query(sql.toString(), rsh, params);
        return data;
    }
    
    public void changeEstatus(int id, int estatus) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("UPDATE pet_cat_meta_estrategica SET activo = ? WHERE id = ?");
        Object[] params = { estatus, id };
        
        qr.update(sql.toString(), params);
    }
    
    public ResultInteger validateUpdate(PetCatMetaEstrategica pcme) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT COUNT(1) AS result FROM pet_cat_meta_estrategica ")
                .append("WHERE id <> ? AND ( valor = ? OR descripcion = ?)");
        Object[] params = { pcme.getId(), pcme.getValor(), pcme.getDescripcion() };
        
        ResultSetHandler rsh = new BeanHandler(ResultInteger.class);
        ResultInteger result = (ResultInteger) qr.query(sql.toString(), rsh, params);
        return result;
    }
    
    public ResultInteger validateInsert(PetCatMetaEstrategica pcme) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT COUNT(1) AS result FROM pet_cat_meta_estrategica ")
                .append("WHERE valor = ? OR descripcion = ?");
        Object[] params = { pcme.getValor(), pcme.getDescripcion() };
        
        ResultSetHandler rsh = new BeanHandler(ResultInteger.class);
        ResultInteger result = (ResultInteger) qr.query(sql.toString(), rsh, params);
        return result;
    }
    
    public void updateMetaEstrategica(PetCatMetaEstrategica pcme) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("UPDATE pet_cat_meta_estrategica SET valor = ?, descripcion = ?, ")
                .append("unidad_medida = ?, anual = ?, mensual = ? WHERE id = ?");
        Object[] params = { pcme.getValor(), pcme.getDescripcion(), 
            pcme.getUnidad_medida(), pcme.getAnual(), pcme.getMensual(), pcme.getId() };
        
        qr.update(sql.toString(), params);
    }
    
    public void insertMetaEstrategica(PetCatMetaEstrategica pcme) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("INSERT INTO pet_cat_meta_estrategica ")
                .append("(valor,descripcion,unidad_medida,anual,mensual,activo) ")
                .append("VALUES (?,?,?,?,?,?)");
        Object[] params = { pcme.getValor(), pcme.getDescripcion(), 
            pcme.getUnidad_medida(), pcme.getAnual(), pcme.getMensual(), 1 };
        
        qr.update(sql.toString(), params);
    }
}
