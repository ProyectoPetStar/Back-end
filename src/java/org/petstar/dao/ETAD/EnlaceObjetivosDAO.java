package org.petstar.dao.ETAD;

import java.util.List;
import javax.sql.DataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.petstar.configurations.PoolDataSource;
import org.petstar.dao.PeriodosDAO;
import org.petstar.dto.ETAD.PetReporteEnlace;
import org.petstar.dto.ETAD.ReporteEnlaceDetail;
import org.petstar.dto.ResultBigDecimal;
import org.petstar.dto.ResultInteger;

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
    
    public ResultInteger validateExistConfiguracionEnlace(int idPeriodo) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT COUNT(1) AS result FROM pet_reporte_enlace WHERE id_periodo = ?");
        Object[] params = { idPeriodo };
        
        ResultSetHandler rsh = new BeanHandler(ResultInteger.class);
        ResultInteger result = (ResultInteger) qr.query(sql.toString(), rsh, params);
        return result;
    }
    
    public ResultInteger insertConfiguracionEnlace(PetReporteEnlace pre) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("INSERT INTO pet_reporte_enlace (id_periodo,")
                .append("objetivo_estrategico_uno,objetivo_estrategico_dos,")
                .append("objetivo_estrategico_tres,meta_estrategica_uno,")
                .append("meta_estrategica_dos,meta_estrategica_tres,")
                .append("meta_estrategica_cuatro,merma_mensual,merma_real,")
                .append("subproducto_mensual,subproducto_real,costo_unitario_real,")
                .append("mdp_mensual,mdp_real,eficiencia_entregas_compra_mensual,")
                .append("eficiencia_entregas_compra_real,no_fugas_pet_real,")
                .append("costo_unitario,ajuste_error_inventario,eficiencia_carga_real,")
                .append("descarga_mp_real,liberacion_embarques,")
                .append("efectividad_entrega_cliente_real,")
                .append("control_entradas_salidas_contratistas,")
                .append("control_entradas_salidas_transportistas,")
                .append("control_entradas_salidas_proveedores,")
                .append("control_entradas_salidas_visitantes,ot_alimentadas_mp9) ")
                .append("OUTPUT INSERTED.id_reporte_enlace AS result VALUES ")
                .append("(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
        Object[] params = { pre.getId_periodo(), pre.getObjetivo_estrategico_uno(), 
            pre.getObjetivo_estrategico_dos(), pre.getObjetivo_estrategico_tres(),
            pre.getMeta_estrategica_uno(), pre.getMeta_estrategica_dos(), 
            pre.getMeta_estrategica_tres(), pre.getMeta_estrategica_cuatro(), 
            pre.getMerma_mensual(), pre.getMerma_real(), pre.getSubproducto_mensual(),
            pre.getSubproducto_real(), pre.getCosto_unitario_real(), pre.getMdp_mensual(), 
            pre.getMdp_real(), pre.getEficiencia_entregas_compra_mensual(), 
            pre.getEficiencia_entregas_compra_real(), pre.getNo_fugas_pet_real(), 
            pre.getCosto_unitario(), pre.getAjuste_error_inventario(), pre.getEficiencia_carga_real(), 
            pre.getDescarga_mp_real(), pre.getLiberacion_embarques(), pre.getEfectividad_entrega_cliente_real(), 
            pre.getControl_entradas_salidas_contratistas(), pre.getControl_entradas_salidas_transportistas(),
            pre.getControl_entradas_salidas_proveedores(), pre.getControl_entradas_salidas_visitantes(),
            pre.getOt_alimentadas_mp9() };
        
        ResultSetHandler rsh = new BeanHandler(ResultInteger.class);
        ResultInteger result = (ResultInteger) qr.query(sql.toString(), rsh, params);
        return result;
    }
    
    public void updateConfiguracionEnlace(PetReporteEnlace pre) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("UPDATE pet_reporte_enlace SET objetivo_estrategico_uno = ?")
                .append(", objetivo_estrategico_dos = ?, objetivo_estrategico_tres = ?")
                .append(", meta_estrategica_uno = ?, meta_estrategica_dos = ?")
                .append(", meta_estrategica_tres = ?, meta_estrategica_cuatro = ?")
                .append(", merma_mensual = ?, merma_real = ?, subproducto_mensual = ?")
                .append(", subproducto_real = ?, costo_unitario_real = ?, mdp_mensual = ?")
                .append(", mdp_real = ?, eficiencia_entregas_compra_mensual = ?")
                .append(", eficiencia_entregas_compra_real = ?, no_fugas_pet_real = ?")
                .append(", costo_unitario = ?, ajuste_error_inventario = ?")
                .append(", eficiencia_carga_real = ?, descarga_mp_real = ?, liberacion_embarques = ?")
                .append(", efectividad_entrega_cliente_real = ?, control_entradas_salidas_contratistas = ?")
                .append(", control_entradas_salidas_transportistas = ?")
                .append(", control_entradas_salidas_proveedores = ?")
                .append(", control_entradas_salidas_visitantes = ?, ot_alimentadas_mp9 = ?")
                .append(" WHERE id_reporte_enlace = ?");
        Object[] params = { pre.getObjetivo_estrategico_uno(), 
            pre.getObjetivo_estrategico_dos(), pre.getObjetivo_estrategico_tres(),
            pre.getMeta_estrategica_uno(), pre.getMeta_estrategica_dos(), 
            pre.getMeta_estrategica_tres(), pre.getMeta_estrategica_cuatro(), 
            pre.getMerma_mensual(), pre.getMerma_real(), pre.getSubproducto_mensual(),
            pre.getSubproducto_real(), pre.getCosto_unitario_real(), pre.getMdp_mensual(), 
            pre.getMdp_real(), pre.getEficiencia_entregas_compra_mensual(), 
            pre.getEficiencia_entregas_compra_real(), pre.getNo_fugas_pet_real(), 
            pre.getCosto_unitario(), pre.getAjuste_error_inventario(), pre.getEficiencia_carga_real(), 
            pre.getDescarga_mp_real(), pre.getLiberacion_embarques(), pre.getEfectividad_entrega_cliente_real(), 
            pre.getControl_entradas_salidas_contratistas(), pre.getControl_entradas_salidas_transportistas(),
            pre.getControl_entradas_salidas_proveedores(), pre.getControl_entradas_salidas_visitantes(),
            pre.getOt_alimentadas_mp9(), pre.getId_reporte_enlace() };
        qr.update(sql.toString(), params);
    }
    
    public List<ReporteEnlaceDetail> getDetailsReporte(int idPeriodo, int idLinea) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_reporte_evaluacion_concentrada ?, ?");
        Object[] params = { idPeriodo, idLinea };
        
        ResultSetHandler rsh = new BeanListHandler(ReporteEnlaceDetail.class);
        List<ReporteEnlaceDetail> data = (List<ReporteEnlaceDetail>) qr.query(sql.toString(), rsh, params);
        return data;
    }
    
    public List<ResultBigDecimal> getEficienciaProcesos(int idPeriodo) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_reporteMetaKpiDos ?");
        Object[] params = { idPeriodo };
        
        ResultSetHandler rsh = new BeanListHandler(ResultBigDecimal.class);
        List<ResultBigDecimal> data = (List<ResultBigDecimal>) qr.query(sql.toString(), rsh, params);
        return data;
    }
}
