package org.petstar.dao.ETAD;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.petstar.configurations.PoolDataSource;
import org.petstar.dao.CatalogosDAO;
import org.petstar.dao.PeriodosDAO;
import org.petstar.dto.ETAD.PetIndicadorMensual;
import org.petstar.dto.ETAD.PetMetaKpi;
import org.petstar.dto.ResultInteger;

/**
 *
 * @author Tech-Pro
 */
public class IndicadoresMensualesDAO {
    public List<PetIndicadorMensual> getKPIforIndicadores(int idPeriodo, int idEtad)throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT metaKpi.id_meta_kpi, metaKpi.id_kpi_etad, cat.valor AS c FROM pet_meta_kpi metaKpi ")
                .append("INNER JOIN pet_etad_kpi etadKpi ON metaKpi.id_kpi_etad = etadKpi.id_kpi_etad ")
                .append("INNER JOIN pet_cat_kpi_operativo cat ON etadKpi.id_kpi_operativo = cat.id ")
                .append("INNER JOIN pet_periodo per ON metaKpi.id_periodo = per.id_periodo ")
                .append("WHERE etadKpi.id_etad = ").append(idEtad)
                .append(" AND per.id_periodo= ").append(idPeriodo)
                .append(" AND cat.id_frecuencia = 2 ");
        
        ResultSetHandler rsh = new BeanListHandler(PetMetaKpi.class);
        List<PetMetaKpi> lisData = (List<PetMetaKpi>) qr.query(sql.toString(), rsh);
        
        List<PetIndicadorMensual> listIndicadores = new ArrayList<>();
        PetEtadKpiDao etadKpiDao = new PetEtadKpiDao();
        for(PetMetaKpi row:lisData){
            PetIndicadorMensual mensual = new PetIndicadorMensual();
            row.setEtadKpi(etadKpiDao.getEtadKpiById(row.getId_kpi_etad()));
            mensual.setMetaKpi(row);
            mensual.setId_meta_kpi(row.getId_meta_kpi());
            listIndicadores.add(mensual);
        }
        return listIndicadores;
    }
    
    public ResultInteger validaRecordsForPeriodoAndEtad(int idPeriodo, int idEtad, int idGrupo)throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT COUNT(1) AS result FROM pet_indicador_mensual pim ")
                .append("INNER JOIN pet_meta_kpi pmk ON pim.id_meta_kpi = pmk.id_meta_kpi ")
                .append("INNER JOIN pet_etad_kpi pek ON pmk.id_kpi_etad = pek.id_kpi_etad ")
                .append("WHERE pek.id_etad = ").append(idEtad)
                .append(" AND pim.id_grupo = ").append(idGrupo)
                .append(" AND pim.id_periodo = ").append(idPeriodo);
        
        ResultSetHandler rsh = new BeanHandler(ResultInteger.class);
        ResultInteger result = (ResultInteger) qr.query(sql.toString(), rsh);
        return result;
    }
     
    public void insertIndicadoresDiarios(List<PetIndicadorMensual> listIndicador, Date fecha, int usuario)throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("INSERT INTO pet_indicador_mensual (valor, id_meta_kpi, id_periodo,")
                .append("id_grupo, estatus, id_usuario_registro, fecha_registro) ")
                .append("VALUES (?, ?, ?, ?, ?, ?, ?) ");
        
        for(PetIndicadorMensual row:listIndicador){
            Object[] param = { row.getValor(), row.getId_meta_kpi(), row.getId_periodo(),
                row.getId_grupo(), 1, usuario, fecha};
            qr.update(sql.toString(), param);
        }
    }
    
    public List<PetIndicadorMensual> getIndicadoresExtract(int idPeriodo, int idEtad)throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT indicador.id_periodo, indicador.id_grupo, ")
                .append("gru.valor AS valor_grupo FROM pet_indicador_mensual indicador ")
                .append("INNER JOIN pet_meta_kpi meta ON indicador.id_meta_kpi = meta.id_meta_kpi ")
                .append("INNER JOIN pet_etad_kpi kpi ON meta.id_kpi_etad = kpi.id_kpi_etad ")
                .append("INNER JOIN pet_cat_grupo gru ON indicador.id_grupo = gru.id ")
                .append("INNER JOIN pet_periodo per ON indicador.id_periodo = per.id_periodo ")
                .append("WHERE kpi.id_etad = ").append(idEtad)
                .append(" AND indicador.id_periodo = ").append(idPeriodo)
                .append(" GROUP BY indicador.id_periodo, indicador.id_grupo, gru.valor");
        
        ResultSetHandler rsh = new BeanListHandler(PetIndicadorMensual.class);
        List<PetIndicadorMensual> lisData = (List<PetIndicadorMensual>) qr.query(sql.toString(), rsh);
        
        CatalogosDAO catalogosDAO = new CatalogosDAO();
        PeriodosDAO periodosDAO = new PeriodosDAO();
        for(PetIndicadorMensual row:lisData){
            row.setGrupo(catalogosDAO.getDescripcionById("pet_cat_grupo", row.getId_grupo()));
            row.setPeriodo(periodosDAO.getPeriodoById(idPeriodo));
        }
        return lisData;
    }
    
    public List<PetIndicadorMensual> getIndicadoresByPeriodoAndEtadAndGrupo(int idPeriodo, int idEtad, int idGrupo)throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT ind.* FROM pet_indicador_mensual ind ")
                .append("INNER JOIN pet_meta_kpi met ON ind.id_meta_kpi = met.id_meta_kpi ")
                .append("INNER JOIN pet_etad_kpi etkpi ON met.id_kpi_etad = etkpi.id_kpi_etad ")
                .append("INNER JOIN pet_cat_kpi_operativo cat ON etkpi.id_kpi_operativo = cat.id ")
                .append("INNER JOIN pet_cat_grupo gru ON ind.id_grupo = gru.id ")
                .append("WHERE ind.id_periodo =").append(idPeriodo)
                .append(" AND ind.id_grupo = ").append(idGrupo)
                .append(" AND etkpi.id_etad = ").append(idEtad);
        
        ResultSetHandler rsh = new BeanListHandler(PetIndicadorMensual.class);
        List<PetIndicadorMensual> lisData = (List<PetIndicadorMensual>) qr.query(sql.toString(), rsh);
        
        MetasDAO metasDAO = new MetasDAO();
        PeriodosDAO periodosDAO = new PeriodosDAO();
        for(PetIndicadorMensual row:lisData){
            row.setMetaKpi(metasDAO.getMetaKPIById(row.getId_meta_kpi()));
            row.setPeriodo(periodosDAO.getPeriodoById(row.getId_periodo()));
        }
        return lisData;
    }
    
     public void updateIndicadoresMensuales(List<PetIndicadorMensual> listIndicador, Date fecha, int usuario)throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("UPDATE pet_indicador_mensual SET valor = ?, id_usuario_modifica_registro = ?, ")
                .append("fecha_modicacion = ? WHERE id_indicador_mensual = ?");
        
        for(PetIndicadorMensual row:listIndicador){
            Object[] param = { row.getValor(), usuario, fecha, row.getId_indicador_mensual() };
            qr.update(sql.toString(), param);
        }
    }
}
