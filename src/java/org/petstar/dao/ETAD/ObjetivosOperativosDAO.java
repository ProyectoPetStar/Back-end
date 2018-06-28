package org.petstar.dao.ETAD;

import java.util.List;
import javax.sql.DataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.petstar.configurations.PoolDataSource;
import org.petstar.dto.ETAD.PetCatObjetivoOperativo;
import org.petstar.dto.ResultInteger;

/**
 *
 * @author Tech-Pro
 */
public class ObjetivosOperativosDAO {
    public List<PetCatObjetivoOperativo> getAllObjetivosOperativos() throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT * FROM pet_cat_objetivo_operativo");
        
        ResultSetHandler rsh = new BeanListHandler(PetCatObjetivoOperativo.class);
        List<PetCatObjetivoOperativo> listData = (List<PetCatObjetivoOperativo>) qr.query(sql.toString(), rsh);
        return listData;
    }
    
    public List<PetCatObjetivoOperativo> getAllObjetivosOperativosActive() throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_selectCatObjetivoOperativo");
        
        ResultSetHandler rsh = new BeanListHandler(PetCatObjetivoOperativo.class);
        List<PetCatObjetivoOperativo> listData = (List<PetCatObjetivoOperativo>) qr.query(sql.toString(), rsh);
        return listData;
    }
    
    public List<PetCatObjetivoOperativo> getListObjetivosOperativosAnuales() throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT * FROM pet_cat_objetivo_operativo WHERE activo=1 AND anual=1");
        
        ResultSetHandler rsh = new BeanListHandler(PetCatObjetivoOperativo.class);
        List<PetCatObjetivoOperativo> listData = (List<PetCatObjetivoOperativo>) qr.query(sql.toString(), rsh);
        return listData;
    }
    
    public PetCatObjetivoOperativo getObjetivoOperativoById(int id) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT * FROM pet_cat_objetivo_operativo WHERE id=?");
        Object[] params = { id };
        
        ResultSetHandler rsh = new BeanHandler(PetCatObjetivoOperativo.class);
        PetCatObjetivoOperativo data = (PetCatObjetivoOperativo) qr.query(sql.toString(), rsh, params);
        return data;
    }
    
    public void changeEstatus(int id, int estatus) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("UPDATE pet_cat_objetivo_operativo SET activo = ? WHERE id = ?");
        Object[] params = { estatus, id };
        
        qr.update(sql.toString(), params);
    }
    
    public ResultInteger validateUpdate(PetCatObjetivoOperativo pcoo) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT COUNT(1) AS result FROM pet_cat_objetivo_operativo ")
                .append("WHERE id <> ? AND ( valor = ? OR descripcion = ?)");
        Object[] params = { pcoo.getId(), pcoo.getValor(), pcoo.getDescripcion() };
        
        ResultSetHandler rsh = new BeanHandler(ResultInteger.class);
        ResultInteger result = (ResultInteger) qr.query(sql.toString(), rsh, params);
        return result;
    }
    
    public void updateMetaEstrategica(PetCatObjetivoOperativo pcoo) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql1 = new StringBuilder();
        StringBuilder sql2 = new StringBuilder();
        
        sql1.append("UPDATE pet_cat_objetivo_operativo SET valor = ?, descripcion = ?, ")
                .append("unidad_medida = ?, anual = ?, mensual = ? WHERE id = ?");
        Object[] params1 = { pcoo.getValor(), pcoo.getDescripcion(), 
            pcoo.getUnidad_medida(), pcoo.getAnual(), pcoo.getMensual(), pcoo.getId() };
        
        sql2.append("DELETE FROM pet_linea_objetivo_operativo ")
                .append("WITH (TABLOCK) WHERE id_objetivo_operativo = ?");
        Object[] params2 = { pcoo.getId() };
        qr.update(sql1.toString(), params1);
        qr.update(sql2.toString(), params2);
    }
    
    public void asignaLineasToMetaEstrategica(PetCatObjetivoOperativo pcoo) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        String[] lineas = pcoo.getLineas().split(",");
        
        sql.append("INSERT INTO pet_linea_objetivo_operativo ")
                .append("(id_objetivo_operativo, id_linea) VALUES (?, ?)");
        
        for(String linea:lineas){
            Object[] params = { pcoo.getId(), Integer.valueOf(linea) };
            qr.update(sql.toString(), params);
        }
    }
}
