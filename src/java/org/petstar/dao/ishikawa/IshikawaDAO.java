package org.petstar.dao.ishikawa;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.sql.DataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.petstar.configurations.PoolDataSource;
import org.petstar.configurations.utils;
import org.petstar.dao.CatalogosDAO;
import org.petstar.dto.ResultInteger;
import org.petstar.dto.ResultSQLDate;
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
    public ResultInteger saveIshikawa(PetIshikawa is) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("INSERT INTO pet_ishikawa (que,donde,cuando,como,problema,fecha,nombre_etad,")
                .append("id_grupo,id_etad,causa_raiz,elaborado,descripcion_corta,estatus) ")
                .append("OUTPUT INSERTED.ID AS result VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)");
        Object[] params = { is.getQue(), is.getDonde(), is.getCuando(), is.getComo(),
                is.getProblema(), is.getFecha(), is.getNombre_etad(), is.getId_grupo(),
                is.getId_etad(), is.getCausa_raiz(), is.getElaborado(), is.getDescripcion_corta(), 0 };
        
        ResultSetHandler rsh = new BeanHandler(ResultInteger.class);
        ResultInteger result = (ResultInteger) qr.query(sql.toString(), rsh, params);
        this.saveIdeasIshikawa(is.getListIdeas(), result.getResult());
        this.saveConsenso(is.getListConsenso(), result.getResult());
        
        return result;
    }
    
    private void saveIdeasIshikawa(List<PetIdeas> ideas, int idIshikawa) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("INSERT INTO pet_ideas (id_ishikawa,id_eme,idea) OUTPUT INSERTED.ID_IDEA AS result VALUES (?,?,?)");
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
        sql.append("INSERT INTO pet_plan_accion (accion,responsable,fecha,id_porque) VALUES (?,?,?,?)");
        Object[] params = { plan.getAccion(), plan.getResponsable(), plan.getFecha(), idPorque};
        
        qr.update(sql.toString(), params);
    }
    
    public List<PetIshikawa> getAllIshikawas(int anio) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT *, ISNULL(revisado,'') AS revisado, ISNULL(autorizado,'') ")
                .append("AS autorizado FROM pet_ishikawa WHERE YEAR(fecha) = ? ORDER BY fecha DESC, id_etad, id_grupo ");
        Object[] params = { anio };
        
        ResultSetHandler rsh = new BeanListHandler(PetIshikawa.class);
        List<PetIshikawa> list = (List<PetIshikawa>) qr.query(sql.toString(), rsh, params);
        CatalogosDAO catalogosDAO = new CatalogosDAO();
        Date actual = utils.getCurrentDate(new SimpleDateFormat("dd/MM/yyyy"));
        
        for(PetIshikawa pi : list){
            pi.setFecha(utils.sumarFechasDias(pi.getFecha(), 2));
            pi.setFecha_string(utils.convertSqlToDay(pi.getFecha(), new SimpleDateFormat("dd/MM/yyyy")));
            pi.setGrupo(catalogosDAO.getDescripcionById("pet_cat_grupo", pi.getId_grupo()));
            pi.setEtad(catalogosDAO.getDescripcionById("pet_cat_etad", pi.getId_etad()));
            pi.setListConsenso(this.getConsensoOfIshikawa(pi.getId()));
            pi.setListIdeas(this.getIdeasOfIshikawa(pi.getId()));
            Date maximo = this.lastDatePlan(pi.getId());
            if(pi.getEstatus() == 1){
                if(actual.after(maximo)){
                    pi.setVerificar(true);
                }
            }
        }
        return list;
    }
    
    public PetIshikawa getIshikawaById(int idIshikawa) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT *, ISNULL(revisado,'') AS revisado, ")
                .append("ISNULL(autorizado,'') AS autorizado FROM pet_ishikawa WHERE id = ?");
        Object[] params = { idIshikawa };
        
        ResultSetHandler rsh = new BeanHandler(PetIshikawa.class);
        PetIshikawa ishikawa = (PetIshikawa) qr.query(sql.toString(), rsh, params);
        CatalogosDAO catalogosDAO = new CatalogosDAO();
        
        if(ishikawa != null){
            ishikawa.setFecha(utils.sumarFechasDias(ishikawa.getFecha(), 2));
            ishikawa.setFecha_string(utils.convertSqlToDay(ishikawa.getFecha(), new SimpleDateFormat("dd/MM/yyyy")));
            ishikawa.setGrupo(catalogosDAO.getDescripcionById("pet_cat_grupo", ishikawa.getId_grupo()));
            ishikawa.setEtad(catalogosDAO.getDescripcionById("pet_cat_etad", ishikawa.getId_etad()));
            ishikawa.setListConsenso(this.getConsensoOfIshikawa(idIshikawa));
            ishikawa.setListIdeas(this.getIdeasOfIshikawa(idIshikawa));
        }
        
        Date actual = utils.getCurrentDate(new SimpleDateFormat("dd/MM/yyyy"));
        Date maximo = this.lastDatePlan(idIshikawa);
        if(ishikawa.getEstatus() == 1){
            if(actual.after(maximo)){
                ishikawa.setVerificar(true);
            }
        }
        return ishikawa;
    }
    
    private List<PetIdeas> getIdeasOfIshikawa(int idIshikawa) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT * FROM pet_ideas WHERE id_ishikawa = ?");
        Object[] params = { idIshikawa };
        
        ResultSetHandler rsh = new BeanListHandler(PetIdeas.class);
        List<PetIdeas> list = (List<PetIdeas>) qr.query(sql.toString(), rsh, params);
        CatalogosDAO catalogosDAO = new CatalogosDAO();
        for(PetIdeas pi : list){
            pi.setPorques(this.getPorquesOfIdea(pi.getId_idea()));
            pi.setEme(catalogosDAO.getDescripcionById("pet_cat_emes", pi.getId_eme()));
        }
        return list;
    }
    
    private PetPorques getPorquesOfIdea(int idIdea) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT id_porque, id_idea, ISNULL(porque_uno,'') AS porque_uno, ")
                .append("ISNULL(porque_dos,'') AS porque_dos, ISNULL(porque_tres,'') ")
                .append("AS porque_tres, ISNULL(porque_cuatro,'') AS porque_cuatro, ")
                .append("ISNULL(porque_cinco,'') AS porque_cinco FROM pet_porques WHERE id_idea = ?");
        Object[] params = { idIdea };
        
        ResultSetHandler rsh = new BeanHandler(PetPorques.class);
        PetPorques porques = (PetPorques) qr.query(sql.toString(), rsh, params);
        if(porques != null){
            porques.setPlanAccion(this.getPlanAccionByPorque(porques.getId_porque()));
        }
        return porques;
    }
    
    private PetPlanAccion getPlanAccionByPorque(int idPorque) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT *, ISNULL(porque,'') AS porque FROM pet_plan_accion WHERE id_porque = ?");
        Object[] params = { idPorque };
        
        ResultSetHandler rsh = new BeanHandler(PetPlanAccion.class);
        PetPlanAccion planAccion = (PetPlanAccion) qr.query(sql.toString(), rsh, params);
        
        planAccion.setFecha(utils.sumarFechasDias(planAccion.getFecha(), 2));
        planAccion.setFecha_string(utils.convertSqlToDay(planAccion.getFecha(), new SimpleDateFormat("dd/MM/yyyy")));
        return planAccion;
    }
    
    private List<PetConsenso> getConsensoOfIshikawa(int idIshikawa) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT * FROM pet_consenso WHERE id_ishikawa = ?");
        Object[] params = { idIshikawa };
        
        ResultSetHandler rsh = new BeanListHandler(PetConsenso.class);
        List<PetConsenso> list = (List<PetConsenso>) qr.query(sql.toString(), rsh, params);
        return list;
    }
    
    public void deleteIshikawa(int idIshikawa) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("DELETE FROM pet_ishikawa WHERE id = ?");
        Object[] params = { idIshikawa };
        
        qr.update(sql.toString(), params);
    }
    
    public void revisarIshikawa(int idIshikawa, String reviso) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("UPDATE pet_ishikawa SET revisado = ? , estatus = 1 WHERE id = ?");
        Object[] params = { reviso, idIshikawa };
        
        qr.update(sql.toString(), params);
    }
    
    public void checkIshikawa(PetPlanAccion plan) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("UPDATE pet_plan_accion SET efectiva = ? , porque = ? WHERE id_plan = ?");
        Object[] params = { plan.getEfectiva(), plan.getPorque(), plan.getId_plan() };
        
        qr.update(sql.toString(), params);
    }
    
    public void traicingIshikawa(PetIshikawa ishikawa) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("UPDATE pet_ishikawa SET solucionado=? , recurrente=? , ")
                .append("analisis=? , autorizado=? , estatus=2 WHERE id = ?");
        Object[] params = { ishikawa.getSolucionado(), ishikawa.getRecurrente(), 
            ishikawa.getAnalisis(), ishikawa.getAutorizado(), ishikawa.getId() };
        
        qr.update(sql.toString(), params);
    }
    
    private Date lastDatePlan(int idIshikawa) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT MAX(ppa.fecha) AS result FROM pet_plan_accion AS ppa ")
                .append("INNER JOIN pet_porques AS pps ON ppa.id_porque = pps.id_porque ")
                .append("INNER JOIN pet_ideas AS pis ON pps.id_idea = pis.id_idea ")
                .append("INNER JOIN pet_ishikawa AS pia ON pis.id_ishikawa = pia.id ")
                .append("WHERE pia.id=").append(idIshikawa);
        
        ResultSetHandler rsh = new BeanHandler(ResultSQLDate.class);
        ResultSQLDate date = (ResultSQLDate) qr.query(sql.toString(), rsh);
        
        return utils.sumarFechasDias(date.getResult(), 2);
    }
}
