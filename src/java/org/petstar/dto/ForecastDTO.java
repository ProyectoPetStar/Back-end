/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.petstar.dto;

import java.util.Date;

/**
 *
 * Tech-Pro
 */
public class ForecastDTO {
    private Date dia;
    private String turno;
    private String grupo;
    private int id_linea;
    private Float meta;
    private Float tmp;
    private Float velocidad;

    public ForecastDTO(Date dia, String turno, String grupo, int id_linea, Float meta, Float tmp, Float velocidad) {
        this.dia = dia;
        this.turno = turno;
        this.grupo = grupo;
        this.id_linea = id_linea;
        this.meta = meta;
        this.tmp = tmp;
        this.velocidad = velocidad;
    }

    public Date getDia() {
        return dia;
    }

    public void setDia(Date dia) {
        this.dia = dia;
    }
    
    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public int getId_linea() {
        return id_linea;
    }

    public void setId_linea(int id_linea) {
        this.id_linea = id_linea;
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

}
