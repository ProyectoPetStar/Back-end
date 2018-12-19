/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.petstar.dao;

import java.util.List;
import javax.sql.DataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.petstar.configurations.PoolDataSource;
import org.petstar.dto.CatalogosDTO;
import org.petstar.dto.ResultInteger;
import org.petstar.dto.RolesDTO;

/**
 * Clase DAO de Catalogos
 * @author TechPro
 */
public class CatalogosDAO {
    
    /**
     * Consulta General
     * Metodo generico que devuelve la lista de los catalogos
     * @param tablename
     * @return
     * @throws Exception 
     */
    public List<CatalogosDTO> getCatalogos(String tablename) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_selectPetCatalogos ?");
        Object[] params = { tablename };
        
        ResultSetHandler rsh = new BeanListHandler(CatalogosDTO.class);
        List<CatalogosDTO> data_catalogos = (List<CatalogosDTO>) qr.query(sql.toString(), rsh, params);
        return data_catalogos;
    }
    
    /**
     * Consulta de Catalogos Activos
     * Metodo que devuelve la lista de datos que se encuntran activos
     * @param tablename
     * @return
     * @throws Exception 
     */
    public List<CatalogosDTO> getCatalogosActiveConArea(String tablename) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_selectPetCatalogosActivos ?");
        Object[] params = { tablename };
        
        ResultSetHandler rsh = new BeanListHandler(CatalogosDTO.class);
        List<CatalogosDTO> data_catalogos = (List<CatalogosDTO>) qr.query(sql.toString(), rsh, params);
        return data_catalogos;
    }
    
    public List<CatalogosDTO> getCatalogosActive(String tablename) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT * FROM ").append(tablename).
                append(" WHERE activo = 1 AND valor != 'SIN AREA' AND valor != 'SIN LINEA' ").
                append(" AND valor != 'SIN LINEA-LOGISTICA' AND valor != 'SIN LINEA-MANTENIMIENTO' ").
                append(" AND valor != 'SIN LINEA-REFACCIONES' AND valor != 'SIN LINEA-CONTROL INTERNO' ").
                append(" AND valor != 'SIN GRUPO' AND valor !=  'MIXTO'");
        
        ResultSetHandler rsh = new BeanListHandler(CatalogosDTO.class);
        List<CatalogosDTO> data_catalogos = (List<CatalogosDTO>) qr.query(sql.toString(), rsh);
        return data_catalogos;
    }
    
    public List<CatalogosDTO> getCatalogosFromPerfil(String tablename) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT * FROM ").append(tablename).
                append(" WHERE activo = 1");
                
        
        ResultSetHandler rsh = new BeanListHandler(CatalogosDTO.class);
        List<CatalogosDTO> data_catalogos = (List<CatalogosDTO>) qr.query(sql.toString(), rsh);
        return data_catalogos;
    }
    
    /**
     * Registro de Catalogos
     * Metodo generico para dar de alta nuevos registros de catalogos
     * @param tableName
     * @param valor
     * @param descripcion
     * @throws Exception 
     */
    public void insertCatalogos(String tableName, String valor, String descripcion)throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_insertPetCatalogos ?, ?, ?");
        Object[] params = { tableName, valor, descripcion };
        
        qr.update(sql.toString(), params);
    }
    
    /**
     * Modificación de Catalogos
     * Metodo generico para actualizar registros de catalogos
     * @param id
     * @param valor
     * @param descripcion
     * @param activo
     * @param tableName
     * @throws Exception 
     */
    public void updateCatalogos(int id, String valor, String descripcion, 
            int activo, String tableName) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_updatePetCatalogos ?, ?, ?, ?, ?");
        Object[] params = {
            tableName, id, valor, descripcion, activo
        };
        
        qr.update(sql.toString(), params);
    }
    
    /**
     * Bloqueo de Catalogos
     * Metodo generico que habilita y deshabilita registros de catalogos de acuerdo al ID
     * @param id
     * @param tableName
     * @param activo
     * @throws Exception 
     */
    public void blockCatalogo(int id, String tableName, int activo) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_updateActivoPetCatalogo ?, ?, ?");
        Object[] params = {
            tableName, id, activo
        };
        
        qr.update(sql.toString(), params);
    }
    
    /**
     * Validación para Insert
     * Metodo generico que valida las desscripciones de los catalogos para
     * evitar datos duplicados
     * @param tableName
     * @param valor
     * @param descripcion
     * @return
     * @throws Exception 
     */
    public ResultInteger validateDescripcionInsert(String tableName, String valor, String descripcion) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_insertValidaDescripcion ?, ?, ?");
        Object[] params = {tableName, valor, descripcion };
        
        ResultSetHandler rsh = new BeanHandler(ResultInteger.class);
        ResultInteger count = (ResultInteger)  qr.query(sql.toString(), rsh, params);
        return count;
    }
    
    /**
     * Validación para Update
     * Metodo generico que valida las descripciones de los catalogos para 
     * evitar duplicar datos
     * @param tableName
     * @param id
     * @param valor
     * @param descripcion
     * @return
     * @throws Exception 
     */
    public ResultInteger validateDescripcionUpdate(String tableName, int id, 
            String valor, String descripcion) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("EXEC sp_updateValidaDescripcion ?, ?, ?, ?");
        Object[] params = {
            tableName, id, valor, descripcion
        };
        
        ResultSetHandler rsh = new BeanHandler(ResultInteger.class);
        ResultInteger count = (ResultInteger)  qr.query(sql.toString(), rsh, params);
        return count;
    }
    
    /**
     * Consulta Especifica
     * Metodo Generico que devuelve la descripcion del catalogo
     * @param tableName
     * @param id
     * @return
     * @throws Exception 
     */
    public CatalogosDTO getDescripcionById(String tableName, int id) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
      
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        sql.append("EXEC sp_selectPetCatalogosById ?, ?");
        Object[] params = { tableName, id };
        
        ResultSetHandler rsh = new BeanHandler(CatalogosDTO.class);
        CatalogosDTO catalogosDTO = (CatalogosDTO)  qr.query(sql.toString(), rsh, params);
        return catalogosDTO;
    }
    
    /**
     * Validación de Id
     * Metodo que verifica que el Id enviado exista en la tabla correspondiente
     * @param tableName
     * @param columName
     * @param id
     * @return
     * @throws Exception 
     */
    public ResultInteger validaExistID(String tableName, String columName, int id) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT COUNT(1) AS result FROM ").append(tableName)
                .append(" WHERE ").append(columName).append(" = ?");
        Object[] params = { id };
        
        ResultSetHandler rsh = new BeanHandler(ResultInteger.class);
        ResultInteger result =  (ResultInteger) qr.query(sql.toString(), rsh, params);
        return result;
    }
    
    public List<RolesDTO> getAllRoles() throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT r.*, gpoRol.id AS id_gpo_rol, gpoRol.valor AS valor_gpo_rol ")
                .append("FROM pet_cat_rol r INNER JOIN pet_grupo_rol gr ON r.id = gr.id_rol ")
                .append("INNER JOIN pet_cat_gpo_rol gpoRol ON gr.id_gpo_rol = gpoRol.id ")
                .append("ORDER BY gpoRol.id ");
        
        ResultSetHandler rsh = new BeanListHandler(RolesDTO.class);
        List<RolesDTO> data_catalogos = (List<RolesDTO>) qr.query(sql.toString(), rsh);
        return data_catalogos;
    }
    
    public List<CatalogosDTO> getRolesByPerfil(int idPerfil) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT pcr.* FROM pet_cat_rol AS pcr ")
                .append("INNER JOIN pet_perfil_rol AS ppr ON pcr.id = ppr.id_rol ")
                .append("WHERE ppr.id_perfil = ").append(idPerfil).append(" AND pcr.activo=1 ");
        
        ResultSetHandler rsh = new BeanListHandler(CatalogosDTO.class);
        List<CatalogosDTO> data_catalogos = (List<CatalogosDTO>) qr.query(sql.toString(), rsh);
        return data_catalogos;
    }
    
    public void asignaRolesToPerfil(int idPerfil, List<CatalogosDTO> roles) throws Exception{
        DataSource ds = PoolDataSource.getDataSource();
        QueryRunner qr = new QueryRunner(ds);
        StringBuilder sql = new StringBuilder();
        StringBuilder sqlAsignacion = new StringBuilder();
        
        sql.append("DELETE FROM pet_perfil_rol WHERE id_perfil = ?");
        sqlAsignacion.append("INSERT INTO pet_perfil_rol (id_perfil,id_rol) VALUES (?,?)");
        Object[] params = { idPerfil };
        
        qr.update(sql.toString(), params);
        
        for(CatalogosDTO rol:roles){
            Object[] paramAsig = { idPerfil, rol.getId()};
            qr.update(sqlAsignacion.toString(), paramAsig);
        }
    }
}
