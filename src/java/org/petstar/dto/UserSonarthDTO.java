/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.petstar.dto;

/**
 * DTO de Usuarios Sonarh
 * @author Tech-Pro
 */
public class UserSonarthDTO {

    private int id_sonarh;
    private int id_grupo;
    private String grupo;
    private int id_linea;
    private String linea;
    private String usuario_acceso;
    private String nombre_completo;
    
    public int getId_sonarh() {
        return id_sonarh;
    }

    public void setId_sonarh(int id_sonarh) {
        this.id_sonarh = id_sonarh;
    }

    public int getId_grupo() {
        return id_grupo;
    }

    public void setId_grupo(int id_grupo) {
        this.id_grupo = id_grupo;
    }

    public int getId_linea() {
        return id_linea;
    }

    public void setId_linea(int id_linea) {
        this.id_linea = id_linea;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getLinea() {
        return linea;
    }

    public void setLinea(String linea) {
        this.linea = linea;
    }

    public String getUsuario_acceso() {
        return usuario_acceso;
    }

    public void setUsuario_acceso(String usuario_acceso) {
        this.usuario_acceso = usuario_acceso;
    }

    public String getNombre_completo() {
        return nombre_completo;
    }

    public void setNombre_completo(String nombre_completo) {
        this.nombre_completo = nombre_completo;
    }
    
}
