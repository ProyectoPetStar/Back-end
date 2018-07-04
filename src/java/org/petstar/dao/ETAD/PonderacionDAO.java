package org.petstar.dao.ETAD;

import java.sql.Date;
import java.util.List;
import javax.sql.DataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.petstar.configurations.PoolDataSource;
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
}
