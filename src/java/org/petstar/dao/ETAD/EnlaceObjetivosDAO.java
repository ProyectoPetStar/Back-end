package org.petstar.dao.ETAD;

import javax.sql.DataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.petstar.configurations.PoolDataSource;
import org.petstar.dao.PeriodosDAO;
import org.petstar.dto.ETAD.PetReporteEnlace;

/**
 *
 * @author Tech-Pro
 */
public class EnlaceObjetivosDAO {
    public PetReporteEnlace getConfiguracionByPeriodo(int idPeriodo) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT * FROM pet_reporte_enlace WHERE id_periodo = ?");
        Object[] params = { idPeriodo };
        
        ResultSetHandler rsh = new BeanHandler(PetReporteEnlace.class);
        PetReporteEnlace reporteEnlace = (PetReporteEnlace) qr.query(sql.toString(), rsh, params);
        
        if(reporteEnlace != null){
            PeriodosDAO periodosDAO = new PeriodosDAO();
            reporteEnlace.setPeriodo(periodosDAO.getPeriodoById(reporteEnlace.getId_periodo()));
        }
        return reporteEnlace;
    }
}
