/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.petstar.dto;

/**
 *
 * @author Tech-Pro
 */
public class MetasDTO {
    
    private int id_meta;
    private int id_linea;
    private String descripcion_linea;
    private String meta;
    private String tipo_medida;
    private int posicion;
    private int activo;

    public int getId_meta() {
        return id_meta;
    }

    public void setId_meta(int id_meta) {
        this.id_meta = id_meta;
    }

    public int getId_linea() {
        return id_linea;
    }

    public void setId_linea(int id_linea) {
        this.id_linea = id_linea;
    }

    public String getDescripcion_linea() {
        return descripcion_linea;
    }

    public void setDescripcion_linea(String descripcion_linea) {
        this.descripcion_linea = descripcion_linea;
    }

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    public String getTipo_medida() {
        return tipo_medida;
    }

    public void setTipo_medida(String tipo_medida) {
        this.tipo_medida = tipo_medida;
    }

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    public int getActivo() {
        return activo;
    }

    public void setActivo(int activo) {
        this.activo = activo;
    }
    
}
