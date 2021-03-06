/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.petstar.dto;

import java.sql.Date;

/**
 * DTO de Metas
 * @author Tech-Pro
 */
public class MetasDTO {
    
    private int id_meta;
    private Date dia;
    private Float meta;
    private Float tmp;
    private Float velocidad;
    private int id_turno;
    private int id_grupo;
    private String nombre_grupo;
    private int id_linea;
    private String valor;
    private int id_archivo;
    private int id_usuario_modifica;
    private Date fecha_modificacion;
    private int estatus;
    private String dia_string;

    public int getId_meta() {
        return id_meta;
    }

    public void setId_meta(int id_meta) {
        this.id_meta = id_meta;
    }

    public Date getDia() {
        return dia;
    }

    public void setDia(Date dia) {
        this.dia = dia;
    }

    public Float getMeta() {
        return meta;
    }

    public void setMeta(Float meta) {
        this.meta = meta;
    }

    public Float getTmp() {
        return tmp;
    }

    public void setTmp(Float tmp) {
        this.tmp = tmp;
    }

    public Float getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(Float velocidad) {
        this.velocidad = velocidad;
    }

    public int getId_turno() {
        return id_turno;
    }

    public void setId_turno(int id_turno) {
        this.id_turno = id_turno;
    }

    public int getId_grupo() {
        return id_grupo;
    }

    public void setId_grupo(int id_grupo) {
        this.id_grupo = id_grupo;
    }

    public String getNombre_grupo() {
        return nombre_grupo;
    }

    public void setNombre_grupo(String nombre_grupo) {
        this.nombre_grupo = nombre_grupo;
    }

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
    
    public int getId_archivo() {
        return id_archivo;
    }

    public void setId_archivo(int id_archivo) {
        this.id_archivo = id_archivo;
    }

    public int getId_usuario_modifica() {
        return id_usuario_modifica;
    }

    public void setId_usuario_modifica(int id_usuario_modifica) {
        this.id_usuario_modifica = id_usuario_modifica;
    }

    public Date getFecha_modificacion() {
        return fecha_modificacion;
    }

    public void setFecha_modificacion(Date fecha_modificacion) {
        this.fecha_modificacion = fecha_modificacion;
    }

    public int getEstatus() {
        return estatus;
    }

    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }

    public String getDia_string() {
        return dia_string;
    }

    public void setDia_string(String dia_string) {
        this.dia_string = dia_string;
    }
}
