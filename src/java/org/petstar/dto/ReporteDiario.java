package org.petstar.dto;

import java.math.BigDecimal;
import java.sql.Date;

/**
 *
 * @author Tech-Pro
 */
public class ReporteDiario {
    private Date dia;
    private String periodo;
    private BigDecimal plan_molido;
    private BigDecimal a;
    private BigDecimal b;
    private BigDecimal c;
    private BigDecimal d;
    private BigDecimal meta_dia;
    private BigDecimal meta_dos;
    private BigDecimal meta_uno;
    private String descripcion;
    private BigDecimal meta;
    private BigDecimal produccion;
    private BigDecimal tmp_real;
    private BigDecimal tmp_meta;

    public Date getDia() {
        return dia;
    }

    public void setDia(Date dia) {
        this.dia = dia;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public BigDecimal getPlan_molido() {
        return plan_molido;
    }

    public void setPlan_molido(BigDecimal plan_molido) {
        this.plan_molido = plan_molido;
    }

    public BigDecimal getA() {
        return a;
    }

    public void setA(BigDecimal a) {
        this.a = a;
    }

    public BigDecimal getB() {
        return b;
    }

    public void setB(BigDecimal b) {
        this.b = b;
    }

    public BigDecimal getC() {
        return c;
    }

    public void setC(BigDecimal c) {
        this.c = c;
    }

    public BigDecimal getD() {
        return d;
    }

    public void setD(BigDecimal d) {
        this.d = d;
    }

    public BigDecimal getMeta_dia() {
        return meta_dia;
    }

    public void setMeta_dia(BigDecimal meta_dia) {
        this.meta_dia = meta_dia;
    }

    public BigDecimal getMeta_dos() {
        return meta_dos;
    }

    public void setMeta_dos(BigDecimal meta_dos) {
        this.meta_dos = meta_dos;
    }

    public BigDecimal getMeta_uno() {
        return meta_uno;
    }

    public void setMeta_uno(BigDecimal meta_uno) {
        this.meta_uno = meta_uno;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getMeta() {
        return meta;
    }

    public void setMeta(BigDecimal meta) {
        this.meta = meta;
    }

    public BigDecimal getProduccion() {
        return produccion;
    }

    public void setProduccion(BigDecimal produccion) {
        this.produccion = produccion;
    }

    public BigDecimal getTmp_meta() {
        return tmp_meta;
    }

    public void setTmp_meta(BigDecimal tmp_meta) {
        this.tmp_meta = tmp_meta;
    }

    public BigDecimal getTmp_real() {
        return tmp_real;
    }

    public void setTmp_real(BigDecimal tmp_real) {
        this.tmp_real = tmp_real;
    }
}
