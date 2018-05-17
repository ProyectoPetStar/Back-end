package org.petstar.dao;

import java.sql.Date;
import java.util.List;
import javax.sql.DataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.petstar.configurations.PoolDataSource;
import org.petstar.dto.Fuentes;
import org.petstar.dto.ReporteDiario;
import org.petstar.dto.ResultBigDecimal;

/**
 *
 * @author Tech-Pro
 */
public class ReportesDAO {
    public List<Fuentes> getFuentes(int idLinea, Date fechaI, Date fechaT) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_selectReporteEficiencia ?, ?, ?");
        Object[] params = { fechaI, fechaT, idLinea };
        
        ResultSetHandler rsh = new BeanListHandler(Fuentes.class);
        List<Fuentes> data = (List<Fuentes>) qr.query(sql.toString(), rsh, params);
        return data;
    }
    
    public ResultBigDecimal getProduccionBuena(int idLinea, Date fechaI, Date fechaT) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_selectReporteProductoTotal ?, ?, ?");
        Object[] params = { fechaI, fechaT, idLinea };
        
        ResultSetHandler rsh = new BeanHandler(ResultBigDecimal.class);
        ResultBigDecimal result = (ResultBigDecimal) qr.query(sql.toString(), rsh, params);
        return result;
    }
    
    public ResultBigDecimal getSumaSubProductos(int idLinea, Date fechaI, Date fechaT) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_selectReporteSubProductoTotal ?, ?, ?");
        Object[] params = { fechaI, fechaT, idLinea };
        
        ResultSetHandler rsh = new BeanHandler(ResultBigDecimal.class);
        ResultBigDecimal result = (ResultBigDecimal) qr.query(sql.toString(), rsh, params);
        return result;
    }
    
    public List<ResultBigDecimal> getMolidoByLinea(Date fechaI, Date fechaT, int idLinea)throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_selectReporteDiarioMolido ?, ?, ?");
        Object[] params = { fechaI, fechaT, idLinea };
        
        ResultSetHandler rsh = new BeanListHandler(ResultBigDecimal.class);
        List<ResultBigDecimal> data = (List<ResultBigDecimal>) qr.query(sql.toString(), rsh, params);
        return data;
    }
    
    public ResultBigDecimal getTotalMolidoByLinea(Date fechaI, Date fechaT, int idLinea)throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_selectReporteDiarioTotalMolidos ?, ?, ?");
        Object[] params = { fechaI, fechaT, idLinea };
        
        ResultSetHandler rsh = new BeanHandler(ResultBigDecimal.class);
        ResultBigDecimal result = (ResultBigDecimal) qr.query(sql.toString(), rsh, params);
        return result;
    }
    
    public List<ReporteDiario> getReporteDiario(Date fechaI, Date fechaT, int idGpoLinea)throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_selectReporteDiarioDiaPlanMolido ?, ?, ?");
        Object[] parmas = {fechaI, fechaT, idGpoLinea};
        
        ResultSetHandler rsh = new BeanListHandler(ReporteDiario.class);
        List<ReporteDiario> data = (List<ReporteDiario>) qr.query(sql.toString(), rsh, parmas);
        return data;
    }
    
    public List<ReporteDiario> getDailyPerformance(Date fechaI, Date fechaT, int idLinea)throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_selectReporteProduccionReal ?, ?, ?");
        Object[] parmas = {fechaI, fechaT, idLinea};
        
        ResultSetHandler rsh = new BeanListHandler(ReporteDiario.class);
        List<ReporteDiario> data = (List<ReporteDiario>) qr.query(sql.toString(), rsh, parmas);
        return data;
    }
    
    public List<ReporteDiario> getJUCODIproduccion(Date dia, int idGpoLinea)throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_selectReporteJucodiProduccion ?, ?");
        Object[] parmas = {dia, idGpoLinea};
        
        ResultSetHandler rsh = new BeanListHandler(ReporteDiario.class);
        List<ReporteDiario> data = (List<ReporteDiario>) qr.query(sql.toString(), rsh, parmas);
        return data;
    }
}
