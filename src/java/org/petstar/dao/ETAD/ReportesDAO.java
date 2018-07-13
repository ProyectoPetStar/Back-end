package org.petstar.dao.ETAD;

import java.util.List;
import javax.sql.DataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.petstar.configurations.PoolDataSource;
import org.petstar.dto.ETAD.Reporte;

/**
 *
 * @author Tech-Pro
 */
public class ReportesDAO {
    public List<Reporte> indicadorClaveDesempenoByGrupo(int idPeriodo, int idEtad, int idGrupo, int anio)throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_reporteIndacadorClaveDesempeno ?,?,?,?");
        Object[] params = { idEtad, idPeriodo, idGrupo, anio };
        
        ResultSetHandler rsh = new BeanListHandler(Reporte.class);
        List<Reporte> lisData = (List<Reporte>) qr.query(sql.toString(), rsh, params);
        return lisData;
    }
    
    public List<Reporte> indicadorClaveDesempenoGlobal(int idPeriodo, int idEtad, int mes, int anio)throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_reporteGlobal ?,?,?,?");
        Object[] params = { mes, anio, idEtad, idPeriodo };
        
        ResultSetHandler rsh = new BeanListHandler(Reporte.class);
        List<Reporte> lisData = (List<Reporte>) qr.query(sql.toString(), rsh, params);
        return lisData;
    }
}
