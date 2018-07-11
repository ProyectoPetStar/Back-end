package org.petstar.dao.ETAD;

import java.sql.Date;
import java.util.List;
import javax.sql.DataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.petstar.configurations.PoolDataSource;
import org.petstar.dao.CatalogosDAO;
import org.petstar.dto.ETAD.PetIndicadorDiario;
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
        
        sql.append("SELECT indicador.dia, indicador.id_grupo, gru.valor AS valor_grupo ")
                .append("FROM pet_indicador_diario indicador INNER JOIN pet_meta_kpi meta ")
                .append("ON indicador.id_meta_kpi = meta.id_meta_kpi INNER JOIN ")
                .append("pet_etad_kpi kpi ON meta.id_kpi_etad = kpi.id_kpi_etad ")
                .append("INNER JOIN pet_cat_grupo gru ON indicador.id_grupo = gru.id ")
                .append("WHERE kpi.id_etad = ").append(idEtad)
                .append(" AND MONTH(indicador.dia) = ").append(periodo.getMes())
                .append(" AND YEAR(indicador.dia) = ").append(periodo.getAnio())
                .append(" GROUP BY indicador.dia, indicador.id_grupo, gru.valor");
        
        ResultSetHandler rsh = new BeanListHandler(PetIndicadorDiario.class);
        List<PetIndicadorDiario> lisData = (List<PetIndicadorDiario>) qr.query(sql.toString(), rsh);
        
        CatalogosDAO catalogosDAO = new CatalogosDAO();
        for(PetIndicadorDiario row:lisData){
            row.setGrupo(catalogosDAO.getDescripcionById("pet_cat_grupo", row.getId_grupo()));
        }
        return lisData;
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
        System.out.println(sql.toString());
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
                row.getId_grupo(), 1, usuario};
            qr.update(sql.toString(), param);
        }
    }
}
