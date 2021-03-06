/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.petstar.dto;

/**
 * DTO de Usuarios
 * @author Tech-Pro
 */
public class UserDTO {
    
    private int id_acceso;
    private int usuario_sonarh;
    private String nombre;
    private String valor_grupo;
    private int id_grupo;
    private String valor_linea;
    private int id_linea;
    private int id_etad;
    private int id_grupo_linea;
    private int activo;
    private String perfiles;
    private String roles_oee;
    private String roles_etad;
    private String roles_ishikawa;
    private String roles_generales;
    private String roles_videowall;
    private String token;

    public int getId_acceso() {
        return id_acceso;
    }

    public void setId_acceso(int id_acceso) {
        this.id_acceso = id_acceso;
    }

    public int getUsuario_sonarh() {
        return usuario_sonarh;
    }

    public void setUsuario_sonarh(int usuario_sonarh) {
        this.usuario_sonarh = usuario_sonarh;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getValor_grupo() {
        return valor_grupo;
    }

    public void setValor_grupo(String valor_grupo) {
        this.valor_grupo = valor_grupo;
    }

    public int getId_grupo() {
        return id_grupo;
    }

    public void setId_grupo(int id_grupo) {
        this.id_grupo = id_grupo;
    }

    public String getValor_linea() {
        return valor_linea;
    }

    public void setValor_linea(String valor_linea) {
        this.valor_linea = valor_linea;
    }

    public int getId_linea() {
        return id_linea;
    }

    public void setId_linea(int id_linea) {
        this.id_linea = id_linea;
    }

    public int getId_etad() {
        return id_etad;
    }

    public void setId_etad(int id_etad) {
        this.id_etad = id_etad;
    }

    public int getId_grupo_linea() {
        return id_grupo_linea;
    }

    public void setId_grupo_linea(int id_grupo_linea) {
        this.id_grupo_linea = id_grupo_linea;
    }

    public int getActivo() {
        return activo;
    }

    public void setActivo(int activo) {
        this.activo = activo;
    }

    public String getPerfiles() {
        return perfiles;
    }

    public void setPerfiles(String perfiles) {
        this.perfiles = perfiles;
    }

    public String getRoles_oee() {
        return roles_oee;
    }

    public void setRoles_oee(String roles_oee) {
        this.roles_oee = roles_oee;
    }

    public String getRoles_etad() {
        return roles_etad;
    }

    public void setRoles_etad(String roles_etad) {
        this.roles_etad = roles_etad;
    }

    public String getRoles_ishikawa() {
        return roles_ishikawa;
    }

    public void setRoles_ishikawa(String roles_ishikawa) {
        this.roles_ishikawa = roles_ishikawa;
    }

    public String getRoles_generales() {
        return roles_generales;
    }

    public void setRoles_generales(String roles_generales) {
        this.roles_generales = roles_generales;
    }

    public String getRoles_videowall() {
        return roles_videowall;
    }

    public void setRoles_videowall(String roles_videowall) {
        this.roles_videowall = roles_videowall;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
