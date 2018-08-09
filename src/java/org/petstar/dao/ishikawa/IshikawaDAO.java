package org.petstar.dao.ishikawa;

import java.util.List;
import javax.sql.DataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.petstar.configurations.PoolDataSource;
import org.petstar.configurations.utils;
import org.petstar.dto.ResultInteger;
import org.petstar.dto.ishikawa.PetConsenso;
import org.petstar.dto.ishikawa.PetIdeas;
import org.petstar.dto.ishikawa.PetIshikawa;
import org.petstar.dto.ishikawa.PetPlanAccion;
import org.petstar.dto.ishikawa.PetPorques;

/**
 *
 * @author Tech-Pro
 */
public class IshikawaDAO {
    public void saveIshikawa(PetIshikawa is) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("INSERT INTO pet_ishikawa (que,donde,cuando,como,problema,fecha,nombre_etad,")
                .append("id_grupo,id_etad,causa_raiz,estatus) OUTPUT INSERTED.ID AS result ")
                .append(" VALUES (?,?,?,?,?,?,?,?,?,?,0)");
        Object[] params = { is.getQue(), is.getDonde(), is.getCuando(), is.getComo(),
                is.getProblema(), is.getFecha(), is.getNombre_etad(), is.getId_grupo(),
                is.getId_etad(), is.getCausa_raiz() };
        
        ResultSetHandler rsh = new BeanHandler(ResultInteger.class);
        ResultInteger result = (ResultInteger) qr.query(sql.toString(), rsh, params);
        this.saveIdeasIshikawa(is.getListIdeas(), result.getResult());
        this.saveConsenso(is.getListConsenso(), result.getResult());
    }
    
    private void saveIdeasIshikawa(List<PetIdeas> ideas, int idIshikawa) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("INSERT INTO pet_ideas(id_ishikawa,id_eme,idea) OUTPUT INSERTED.ID_IDEA AS result VALUES (?,?,?)");
        ResultSetHandler rsh = new BeanHandler(ResultInteger.class);
        
        for(PetIdeas pi :ideas){
            Object[] params = { idIshikawa, pi.getId_eme(), pi.getIdea() };
            ResultInteger result = (ResultInteger) qr.query(sql.toString(), rsh, params);
            if(pi.getPorques() != null){
                this.save5porques(pi.getPorques(), result.getResult());
            }
        }
    }
    
    private void save5porques(PetPorques pp, int idIdea) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("INSERT INTO pet_porques (id_idea,porque_uno,porque_dos,porque_tres,porque_cuatro,")
                .append("porque_cinco) OUTPUT INSERTED.ID_PORQUE AS result VALUES (?,?,?,?,?,?)");
        
        Object[] params = { idIdea, pp.getPorque_uno(), pp.getPorque_dos(), pp.getPorque_tres(),
                pp.getPorque_cuatro(), pp.getPorque_cinco()};
        
        ResultSetHandler rsh = new BeanHandler(ResultInteger.class);
        ResultInteger result = (ResultInteger) qr.query(sql.toString(), rsh, params);
        this.savePlanAccion(pp.getPlanAccion(), result.getResult());
    }
    
    private void saveConsenso(List<PetConsenso> consensos, int idIshikawa) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("INSERT INTO pet_consenso (id_pregunta,id_ishikawa,respuesta) VALUES (?,?,?)");
        
        for(PetConsenso pc : consensos){
            Object[] params = { pc.getId_pregunta(), idIshikawa, pc.getRespuesta()};
            qr.update(sql.toString(), params);
        }
    }
    
    private void savePlanAccion(PetPlanAccion plan, int idPorque) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        plan.setFecha(utils.convertStringToSql(plan.getFecha_string()));
        sql.append("INSERT INTO pet_plan_accion(accion,responsable,fecha,id_porque) VALUES (?,?,?,?)");
        Object[] params = { plan.getAccion(), plan.getResponsable(), plan.getFecha(), idPorque};
        
        qr.update(sql.toString(), params);
    }
}
