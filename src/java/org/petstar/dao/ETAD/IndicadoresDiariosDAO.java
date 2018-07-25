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
import org.petstar.dto.ETAD.PetIndicadorDiario;
import org.petstar.dto.ETAD.PetMetaKpi;
import org.petstar.dto.PeriodosDTO;
import org.petstar.dto.ResultInteger;

/**
 *
 * @author Tech-Pro
 */
public class IndicadoresDiariosDAO {
    public List<PetIndicadorDiario> getIndicadoresExtract(PeriodosDTO periodo, int idEtad)throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT indicador.dia, indicador.estatus, indicador.id_grupo, gru.valor AS valor_grupo ")
                .append("FROM pet_indicador_diario indicador INNER JOIN pet_meta_kpi meta ")
                .append("ON indicador.id_meta_kpi = meta.id_meta_kpi INNER JOIN ")
                .append("pet_etad_kpi kpi ON meta.id_kpi_etad = kpi.id_kpi_etad ")
                .append("INNER JOIN pet_cat_grupo gru ON indicador.id_grupo = gru.id ")
                .append("WHERE kpi.id_etad = ").append(idEtad)
                .append(" AND MONTH(indicador.dia) = ").append(periodo.getMes())
                .append(" AND YEAR(indicador.dia) = ").append(periodo.getAnio())
                .append(" GROUP BY indicador.dia, indicador.estatus, indicador.id_grupo, gru.valor");
        
        ResultSetHandler rsh = new BeanListHandler(PetIndicadorDiario.class);
        List<PetIndicadorDiario> lisData = (List<PetIndicadorDiario>) qr.query(sql.toString(), rsh);
        
        CatalogosDAO catalogosDAO = new CatalogosDAO();
        PeriodosDAO periodosDAO = new PeriodosDAO();
        for(PetIndicadorDiario row:lisData){
            row.setGrupo(catalogosDAO.getDescripcionById("pet_cat_grupo", row.getId_grupo()));
            row.setPeriodo(periodosDAO.getPeriodoById(periodo.getId_periodo()));
        }
        return lisData;
    }
    
    public List<PetIndicadorDiario> getKPIforIndicadores(Date dia, int idEtad)throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT metaKpi.id_meta_kpi, metaKpi.id_kpi_etad, cat.valor AS c FROM pet_meta_kpi metaKpi ")
                .append("INNER JOIN pet_etad_kpi etadKpi ON metaKpi.id_kpi_etad = etadKpi.id_kpi_etad ")
                .append("INNER JOIN pet_cat_kpi_operativo cat ON etadKpi.id_kpi_operativo = cat.id ")
                .append("INNER JOIN pet_periodo per ON metaKpi.id_periodo = per.id_periodo ")
                .append("WHERE etadKpi.id_etad = ").append(idEtad)
                .append(" AND per.mes = MONTH('").append(dia).append("')")
                .append(" AND per.anio = YEAR('").append(dia).append("')")
                .append(" AND cat.id_frecuencia = 1 ");
        
        ResultSetHandler rsh = new BeanListHandler(PetMetaKpi.class);
        List<PetMetaKpi> lisData = (List<PetMetaKpi>) qr.query(sql.toString(), rsh);
        
        List<PetIndicadorDiario> listIndicadores = new ArrayList<>();
        PetEtadKpiDao etadKpiDao = new PetEtadKpiDao();
        for(PetMetaKpi row:lisData){
            PetIndicadorDiario diario = new PetIndicadorDiario();
            row.setEtadKpi(etadKpiDao.getEtadKpiById(row.getId_kpi_etad()));
            diario.setMetaKpi(row);
            diario.setId_meta_kpi(row.getId_meta_kpi());
            listIndicadores.add(diario);
        }
        return listIndicadores;
    }
    
    public ResultInteger validaRecordsForDayAndEtad(Date dia, int idEtad, int idGrupo)throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT COUNT(1) AS result FROM pet_indicador_diario pid ")
                .append("INNER JOIN pet_meta_kpi pmk ON pid.id_meta_kpi = pmk.id_meta_kpi ")
                .append("INNER JOIN pet_etad_kpi pek ON pmk.id_kpi_etad = pek.id_kpi_etad ")
                .append("WHERE pek.id_etad = ").append(idEtad)
                .append(" AND pid.id_grupo = ").append(idGrupo)
                .append(" AND pid.dia = '").append(dia).append("'");
        
        ResultSetHandler rsh = new BeanHandler(ResultInteger.class);
        ResultInteger result = (ResultInteger) qr.query(sql.toString(), rsh);
        return result;
    }
    
    public void insertIndicadoresDiarios(List<PetIndicadorDiario> listIndicador, Date dia, int usuario)throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("INSERT INTO pet_indicador_diario (dia, valor, id_meta_kpi, ")
                .append("id_grupo, estatus, id_usuario_registro) ")
                .append("VALUES (?, ?, ?, ?, ?, ?)");
        
        for(PetIndicadorDiario row:listIndicador){
            Object[] param = { dia, row.getValor(), row.getId_meta_kpi(),
                row.getId_grupo(), 0, usuario};
            qr.update(sql.toString(), param);
        }
    }
    
    public List<PetIndicadorDiario> getIndicadoresByDiaAndEtadAndGrupo(Date dia, int idEtad, int idGrupo)throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT ind.* FROM pet_indicador_diario ind ")
                .append("INNER JOIN pet_meta_kpi met ON ind.id_meta_kpi = met.id_meta_kpi ")
                .append("INNER JOIN pet_etad_kpi etkpi ON met.id_kpi_etad = etkpi.id_kpi_etad ")
                .append("INNER JOIN pet_cat_kpi_operativo cat ON etkpi.id_kpi_operativo = cat.id ")
                .append("INNER JOIN pet_cat_grupo gru ON ind.id_grupo = gru.id ")
                .append("WHERE ind.dia = '").append(dia)
                .append("' AND ind.id_grupo = ").append(idGrupo)
                .append(" AND etkpi.id_etad = ").append(idEtad);
        
        ResultSetHandler rsh = new BeanListHandler(PetIndicadorDiario.class);
        List<PetIndicadorDiario> lisData = (List<PetIndicadorDiario>) qr.query(sql.toString(), rsh);
        
        MetasDAO metasDAO = new MetasDAO();
        for(PetIndicadorDiario row:lisData){
            row.setMetaKpi(metasDAO.getMetaKPIById(row.getId_meta_kpi()));
        }
        return lisData;
    }
    
    public void updateIndicadoresDiarios(List<PetIndicadorDiario> listIndicador, Date fecha, int usuario)throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("UPDATE pet_indicador_diario SET valor = ?, fecha_modificacion = ?, ")
                .append("id_usuario_modifica_registro = ? WHERE id_indicador_diario = ?");
        
        for(PetIndicadorDiario row:listIndicador){
            Object[] param = { row.getValor(), fecha, usuario, row.getId_indicador_diario() };
            qr.update(sql.toString(), param);
        }
    }
    
    public void changeEstatusIndicadoresDiarios(Date fecha, int idGrupo, int idEtad, int estatus)throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("UPDATE ind SET estatus = ")
                .append(estatus).append(" FROM pet_indicador_diario ind ")
                .append("INNER JOIN pet_meta_kpi met ON ind.id_meta_kpi = met.id_meta_kpi ")
                .append("INNER JOIN pet_etad_kpi etkpi ON met.id_kpi_etad = etkpi.id_kpi_etad ")
                .append("INNER JOIN pet_cat_kpi_operativo cat ON etkpi.id_kpi_operativo = cat.id ")
                .append("INNER JOIN pet_cat_grupo gru ON ind.id_grupo = gru.id ")
                .append("WHERE ind.dia = '").append(fecha)
                .append("' AND ind.id_grupo = ").append(idGrupo)
                .append(" AND etkpi.id_etad = ").append(idEtad);
        qr.update(sql.toString());
    }
}
