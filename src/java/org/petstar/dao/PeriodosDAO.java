package org.petstar.dao;

import java.sql.Date;
import java.util.List;
import javax.sql.DataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.petstar.configurations.PoolDataSource;
import org.petstar.dto.PeriodosDTO;
import org.petstar.dto.ResultInteger;

/**
 * @author Tech-Pro
 */
public class PeriodosDAO {
    
    public List<PeriodosDTO> getPeriodos() throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
      
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id_periodo, anio, mes, descripcion_mes, estatus FROM DBO.pet_periodo WHERE estatus=0");
                
        ResultSetHandler rsh = new BeanListHandler(PeriodosDTO.class);
        List<PeriodosDTO> data = (List<PeriodosDTO>) qr.query(sql.toString(), rsh); 
        
        return data;
    }
    
    public PeriodosDTO getPeriodoById(int idPeriodo, int idLinea) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_detallePeriodoMeta ?, ?");
        Object[] params ={ idPeriodo, idLinea };
        
        ResultSetHandler rsh = new BeanHandler(PeriodosDTO.class);
        PeriodosDTO data = (PeriodosDTO) qr.query(sql.toString(), rsh, params);
        return data;
    }
    
    public PeriodosDTO getPeriodoById(int idPeriodo) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT * from dbo.pet_periodo where id_periodo =  ?");
        Object[] params ={ idPeriodo };
        
        ResultSetHandler rsh = new BeanHandler(PeriodosDTO.class);
        PeriodosDTO data = (PeriodosDTO) qr.query(sql.toString(), rsh, params);
        return data;
    }
    
    public PeriodosDTO getPeriodoByMesAndAnio(int mes, int anio) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT * FROM pet_periodo WHERE mes=? AND anio=?");
        Object[] params = { mes, anio };
        
        ResultSetHandler rsh = new BeanHandler(PeriodosDTO.class);
        PeriodosDTO periodo = (PeriodosDTO) qr.query(sql.toString(), rsh, params);
        return periodo;
    }
    
    public List<PeriodosDTO> getAllPeriodos() throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
      
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id_periodo, anio, mes, descripcion_mes, estatus FROM pet_periodo ORDER BY anio, mes");
                
        ResultSetHandler rsh = new BeanListHandler(PeriodosDTO.class);
        List<PeriodosDTO> data = (List<PeriodosDTO>) qr.query(sql.toString(), rsh); 
        
        return data;
    }
    
    public PeriodosDTO getLastPeriodo() throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
      
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT TOP 1 id_periodo, anio, mes, descripcion_mes, estatus ")
                .append("FROM dbo.pet_periodo ")
                .append("ORDER BY id_periodo DESC");
               
        ResultSetHandler rsh = new BeanHandler(PeriodosDTO.class);
        PeriodosDTO data = (PeriodosDTO) qr.query(sql.toString(), rsh); 
        
        return data;
    }
    
    public void insertPeriodo(PeriodosDTO periodo, int idUser, Date fecha) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
      
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO pet_periodo ")
                .append("(anio, mes, descripcion_mes ,estatus ,id_usuario_registro,fecha_registro) ")
                .append("VALUES ")
                .append("(?, ?, ?, ?, ?, ?)");
        Object[] params = { periodo.getAnio(), periodo.getMes(), 
                periodo.getDescripcion_mes(), periodo.getEstatus(), idUser, fecha};
               
        qr.update(sql.toString(), params);
    }
    
    public ResultInteger validateForInsert(int year, int month) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT COUNT(1) AS result FROM pet_periodo WHERE anio= ? AND mes = ?");
        Object[] params = { year, month };
        
        ResultSetHandler rsh = new BeanHandler(ResultInteger.class);
        ResultInteger result = (ResultInteger) qr.query(sql.toString(), rsh, params);
        return result;
    }
    
    public void saveMetasPeriodo(PeriodosDTO periodo) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
      
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        sql.append("EXEC sp_insertPetMetasPeriodo ?, ?, ?, ?, ?, ?, ?, ?, ?, ?");
        Object[] params = { periodo.getDisponibilidad(), periodo.getCalidad(),
                    periodo.getUtilizacion(), periodo.getOee(),
                    periodo.getEficiencia_teorica(), periodo.getNo_ventas(),
                    periodo.getVelocidad_ideal(), periodo.getVelocidad_po(),
                    periodo.getId_periodo(), periodo.getId_linea()};
               
        qr.update(sql.toString(), params);
    }
    
    public void changeEstatus(int idPeriodo, int estatus) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("UPDATE pet_periodo SET estatus = ? WHERE id_periodo = ?");
        Object[] params = { estatus, idPeriodo };
               
        qr.update(sql.toString(), params);
    }
    
    public List<PeriodosDTO> getDetailsPeriodo(int idPeriodo) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT pe.estatus, mp.*, li.id_gpo_linea, li.valor AS valor_linea ")
                .append("FROM pet_periodo AS pe ")
                .append("INNER JOIN pet_metas_periodo AS mp ON pe.id_periodo = mp.id_periodo ")
                .append("INNER JOIN pet_cat_linea li ON mp.id_linea = li.id_linea ")
                .append("WHERE mp.id_periodo = ").append(idPeriodo);
        
        ResultSetHandler rsh = new BeanListHandler(PeriodosDTO.class);
        List<PeriodosDTO> data = (List<PeriodosDTO>) qr.query(sql.toString(), rsh);
        return data;
    }
    
    public void updateMetasPeriodo(PeriodosDTO periodo) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
      
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE pet_metas_periodo SET disponibilidad = ? ")
                .append(", calidad = ? , utilizacion = ? , oee = ? ")
                .append(", no_ventas = ? , velocidad_ideal = ? ")
                .append(", velocidad_po = ? , eficiencia_teorica = ? ")
                .append("WHERE id_metas_periodo = ?");
        Object[] params = { periodo.getDisponibilidad(), periodo.getCalidad(),
                    periodo.getUtilizacion(), periodo.getOee(),
                    periodo.getNo_ventas(), periodo.getVelocidad_ideal(),
                    periodo.getVelocidad_po(), periodo.getEficiencia_teorica(), 
                    periodo.getId_metas_periodo()};
               
        qr.update(sql.toString(), params);
    }
    
    public ResultInteger validateForClose(int mes, int anio) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT COUNT(1) AS result FROM pet_meta WHERE MONTH(dia) = ? AND YEAR(dia) = ? AND estatus = 0");
        Object[] params = { mes, anio };
        
        ResultSetHandler rsh = new BeanHandler(ResultInteger.class);
        ResultInteger result = (ResultInteger) qr.query(sql.toString(), rsh, params);
        return result;
    }
    
    public List<ResultInteger> yearsWithoutPonderacionObjetivos() throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT PE.anio AS result FROM pet_periodo pe LEFT JOIN ")
                .append("pet_ponderacion_objetivo_operativo po ON ")
                .append("po.anio = pe.anio GROUP BY pe.anio");
        
        ResultSetHandler rsh = new BeanListHandler(ResultInteger.class);
        List<ResultInteger> listresult = (List<ResultInteger>) qr.query(sql.toString(), rsh);
        return listresult;
    }
}
