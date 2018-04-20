/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.petstar.dto;

/**
 * DTO de Lineas
 * @author Tech-Pro
 */
public class LineasDTO {
    private int id_linea;
    private String valor;
    private String descripcion;
    private int activo;
    private int id_gpo_linea;
    private String descripcion_gpo_linea;

    public int getId_linea() {
        return id_linea;
    }

    public void setId_linea(int id_linea) {
        this.id_linea = id_linea;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getActivo() {
        return activo;
    }

    public void setActivo(int activo) {
        this.activo = activo;
    }

    public int getId_gpo_linea() {
        return id_gpo_linea;
    }

    public void setId_gpo_linea(int id_gpo_linea) {
        this.id_gpo_linea = id_gpo_linea;
    }

    public String getDescripcion_gpo_linea() {
        return descripcion_gpo_linea;
    }

    public void setDescripcion_gpo_linea(String descripcion_gpo_linea) {
        this.descripcion_gpo_linea = descripcion_gpo_linea;
    }
    
}
