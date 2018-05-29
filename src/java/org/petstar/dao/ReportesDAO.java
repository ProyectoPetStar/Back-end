package org.petstar.dao;

import java.sql.Date;
import java.util.List;
import javax.sql.DataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.petstar.configurations.PoolDataSource;
import org.petstar.dto.FallasDTO;
import org.petstar.dto.Fuentes;
import org.petstar.dto.ReporteDiario;
import org.petstar.dto.ReporteDTO;
import org.petstar.dto.ResultBigDecimal;
import org.petstar.dto.ResultSQLDate;

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

	public List<ReporteDiario> getJUCODIparos(Date dia, int idGpoLinea)throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();

        sql.append("EXEC sp_selectReporteJucodiParo ?, ?");
        Object[] parmas = {dia, idGpoLinea};

        ResultSetHandler rsh = new BeanListHandler(ReporteDiario.class);
        List<ReporteDiario> data = (List<ReporteDiario>) qr.query(sql.toString(), rsh, parmas);
        return data;
    }

    public List<ReporteDTO> getReporteSubproducto(Date fechaI, Date fechaT, int idLinea)throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();

        sql.append("EXEC sp_selectReporteSubproducto ?, ?, ?");
        Object[] parmas = {fechaI, fechaT, idLinea};

        ResultSetHandler rsh = new BeanListHandler(ReporteDTO.class);
        List<ReporteDTO> data = (List<ReporteDTO>) qr.query(sql.toString(), rsh, parmas);
        return data;
    }

    public ResultBigDecimal getTotalSubProducto(Date fechaI, Date fechaT, int idLinea) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_selectReporteSubProductoTotal ?, ?, ?");
        Object[] params = { fechaI, fechaT, idLinea };
        
        ResultSetHandler rsh = new BeanHandler(ResultBigDecimal.class);
        ResultBigDecimal result = (ResultBigDecimal) qr.query(sql.toString(), rsh, params);
        return result;
    }
    
    public List<ReporteDTO> getReporteVelPromedio(Date fechaI, Date fechaT, int idLinea)throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();

        sql.append("EXEC sp_selectReporteVelocidad ?, ?, ?");
        Object[] parmas = {fechaI, fechaT, idLinea};

        ResultSetHandler rsh = new BeanListHandler(ReporteDTO.class);
        List<ReporteDTO> data = (List<ReporteDTO>) qr.query(sql.toString(), rsh, parmas);
        return data;
    }

    public List<ReporteDiario> getReportePerformanceByWeek(int mes, int anio, int idLinea)throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();

        sql.append("EXEC sp_selectReporteJucodiSemanal ?, ?, ?");
        Object[] parmas = {anio, mes, idLinea};

        ResultSetHandler rsh = new BeanListHandler(ReporteDiario.class);
        List<ReporteDiario> data = (List<ReporteDiario>) qr.query(sql.toString(), rsh, parmas);
        return data;
    }
    
    public ReporteDiario getGraficaPerformanceByWeek(int anio, int mes, int idLinea) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_selectGraficaMes ?, ?, ?");
        Object[] params = { anio, mes, idLinea };
        
        ResultSetHandler rsh = new BeanHandler(ReporteDiario.class);
        ReporteDiario data = (ReporteDiario) qr.query(sql.toString(), rsh, params);
        return data;
    }
    
    public List<ReporteDiario> getReportePerformanceByMonth(int anio, int idLinea, int lastDayFeb)throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();

        sql.append("EXEC sp_selectReporteJucodiAnual ?, ?, ?");
        Object[] parmas = {anio, idLinea, lastDayFeb};

        ResultSetHandler rsh = new BeanListHandler(ReporteDiario.class);
        List<ReporteDiario> data = (List<ReporteDiario>) qr.query(sql.toString(), rsh, parmas);
        return data;
    }
    
    public ReporteDiario getGraficaPerformanceByMonth(int anio, int idLinea) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_selectGraficaRadarAnual ?, ?");
        Object[] params = {anio, idLinea};
        
        ResultSetHandler rsh = new BeanHandler(ReporteDiario.class);
        ReporteDiario data = (ReporteDiario) qr.query(sql.toString(), rsh, params);
        return data;
    }
        
    public ResultSQLDate getFirstDateofPeriodo(int mes, int anio, int idLinea)throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT TOP 1 me.dia AS result FROM dbo.pet_meta AS me ")
                .append("INNER JOIN dbo.pet_produccion AS pr ON pr.id_meta = me.id_meta ")
                .append("WHERE MONTH(me.dia) = ").append(mes).
                append(" AND YEAR(me.dia) = ").append(anio).append(" AND me.id_linea = ")
                .append(idLinea).append(" ORDER BY me.dia ASC");
        //Object[] params = { idLinea, mes, anio };
        
        ResultSetHandler rsh = new BeanHandler(ResultSQLDate.class);
        ResultSQLDate result = (ResultSQLDate) qr.query(sql.toString(), rsh);
        return result;
    }
    
    public ResultSQLDate getLastDateofPeriodo(int mes, int anio, int idLinea)throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
         sql.append("SELECT TOP 1 me.dia AS result FROM dbo.pet_meta AS me ")
                .append("INNER JOIN dbo.pet_produccion AS pr ON pr.id_meta = me.id_meta ")
                .append("WHERE MONTH(me.dia) = ").append(mes).
                append(" AND YEAR(me.dia) = ").append(anio).append(" AND me.id_linea = ")
                .append(idLinea).append(" ORDER BY me.dia DESC");
        //Object[] params = { idLinea, mes, anio };
        
        ResultSetHandler rsh = new BeanHandler(ResultSQLDate.class);
        ResultSQLDate result = (ResultSQLDate) qr.query(sql.toString(), rsh);
        return result;
    }
    
    public List<FallasDTO> getFallasByPeriodo(Date fechaI, Date fechaT, int idLinea) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_selectAllFallas ?, ?, ?");
        Object[] params = { fechaI, fechaT, idLinea };
        
        ResultSetHandler rsh = new BeanListHandler(FallasDTO.class);
        List<FallasDTO> listFallas = (List<FallasDTO>) qr.query(sql.toString(), rsh, params);
        return listFallas;
    }
}
