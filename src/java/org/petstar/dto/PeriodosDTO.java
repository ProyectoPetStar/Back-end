/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.petstar.dto;

import java.math.BigDecimal;

/**
 * @author Tech-Pro
 */
public class PeriodosDTO {
    private int id_periodo;
    private int anio;
    private int mes;
    private String descripcion_mes;
    private int estatus;
    private BigDecimal disponibilidad;
    private BigDecimal utilizacion;
    private BigDecimal calidad;
    private BigDecimal oee;
    private BigDecimal eficiencia_teorica;
    private int id_linea;

    public int getId_periodo() {
        return id_periodo;
    }

    public void setId_periodo(int id_periodo) {
        this.id_periodo = id_periodo;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public String getDescripcion_mes() {
        return descripcion_mes;
    }

    public void setDescripcion_mes(String descripcion_mes) {
        this.descripcion_mes = descripcion_mes;
    }

    public int getEstatus() {
        return estatus;
    }

    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }

    public BigDecimal getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(BigDecimal disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public BigDecimal getUtilizacion() {
        return utilizacion;
    }

    public void setUtilizacion(BigDecimal utilizacion) {
        this.utilizacion = utilizacion;
    }

    public BigDecimal getCalidad() {
        return calidad;
    }

    public void setCalidad(BigDecimal calidad) {
        this.calidad = calidad;
    }

    public BigDecimal getOee() {
        return oee;
    }

    public void setOee(BigDecimal oee) {
        this.oee = oee;
    }

    public BigDecimal getEficiencia_teorica() {
        return eficiencia_teorica;
    }

    public void setEficiencia_teorica(BigDecimal eficiencia_teorica) {
        this.eficiencia_teorica = eficiencia_teorica;
    }

    public int getId_linea() {
        return id_linea;
    }

    public void setId_linea(int id_linea) {
        this.id_linea = id_linea;
    }
}
