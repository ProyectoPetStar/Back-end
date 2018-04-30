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
    private String valor_linea;
    private int activo;

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

    public String getValor_linea() {
        return valor_linea;
    }

    public void setValor_linea(String valor_linea) {
        this.valor_linea = valor_linea;
    }

    public int getActivo() {
        return activo;
    }

    public void setActivo(int activo) {
        this.activo = activo;
    }
}
