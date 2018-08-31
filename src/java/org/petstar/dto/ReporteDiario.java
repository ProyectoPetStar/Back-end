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
    private BigDecimal meta_a;
    private BigDecimal meta_b;
    private BigDecimal meta_c;
    private BigDecimal meta_d;
    private BigDecimal hojuela;
    private BigDecimal plastas;
    private BigDecimal pellet;

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

    public BigDecimal getMeta_a() {
        return meta_a;
    }

    public void setMeta_a(BigDecimal meta_a) {
        this.meta_a = meta_a;
    }

    public BigDecimal getMeta_b() {
        return meta_b;
    }

    public void setMeta_b(BigDecimal meta_b) {
        this.meta_b = meta_b;
    }

    public BigDecimal getMeta_c() {
        return meta_c;
    }

    public void setMeta_c(BigDecimal meta_c) {
        this.meta_c = meta_c;
    }

    public BigDecimal getMeta_d() {
        return meta_d;
    }

    public void setMeta_d(BigDecimal meta_d) {
        this.meta_d = meta_d;
    }

    public BigDecimal getHojuela() {
        return hojuela;
    }

    public void setHojuela(BigDecimal hojuela) {
        this.hojuela = hojuela;
    }

    public BigDecimal getPlastas() {
        return plastas;
    }

    public void setPlastas(BigDecimal plastas) {
        this.plastas = plastas;
    }

    public BigDecimal getPellet() {
        return pellet;
    }

    public void setPellet(BigDecimal pellet) {
        this.pellet = pellet;
    }
}
