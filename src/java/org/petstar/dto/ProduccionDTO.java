package org.petstar.dto;

import java.math.BigDecimal;
import java.sql.Date;

/**
 *
 * @author Tech-Pro
 */
public class ProduccionDTO {
    private int id_produccion;
    private BigDecimal valor;
    private int id_meta;
    private Date dia;
    private String diaString;
    private int id_grupo;
    private String valor_grupo;
    private int id_turno;
    private String valor_turno;
    private int id_linea;
    private String valor_linea;
    private int id_producto;
    private String valor_producto;
    private String descripcion_producto;
    private String descripcion_tipo_producto;
    private int estatus;

    public int getId_produccion() {
        return id_produccion;
    }

    public void setId_produccion(int id_produccion) {
        this.id_produccion = id_produccion;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

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

    public String getDiaString() {
        return diaString;
    }

    public void setDiaString(String diaString) {
        this.diaString = diaString;
    }

    public int getId_grupo() {
        return id_grupo;
    }

    public void setId_grupo(int id_grupo) {
        this.id_grupo = id_grupo;
    }

    public String getValor_grupo() {
        return valor_grupo;
    }

    public void setValor_grupo(String valor_grupo) {
        this.valor_grupo = valor_grupo;
    }

    public int getId_turno() {
        return id_turno;
    }

    public void setId_turno(int id_turno) {
        this.id_turno = id_turno;
    }

    public String getValor_turno() {
        return valor_turno;
    }

    public void setValor_turno(String valor_turno) {
        this.valor_turno = valor_turno;
    }

    public int getId_linea() {
        return id_linea;
    }

    public void setId_linea(int id_linea) {
        this.id_linea = id_linea;
    }

    public String getValor_linea() {
        return valor_linea;
    }

    public void setValor_linea(String valor_linea) {
        this.valor_linea = valor_linea;
    }

    public int getId_producto() {
        return id_producto;
    }

    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
    }

    public String getValor_producto() {
        return valor_producto;
    }

    public void setValor_producto(String valor_producto) {
        this.valor_producto = valor_producto;
    }

    public String getDescripcion_producto() {
        return descripcion_producto;
    }

    public void setDescripcion_producto(String descripcion_producto) {
        this.descripcion_producto = descripcion_producto;
    }

    public String getDescripcion_tipo_producto() {
        return descripcion_tipo_producto;
    }

    public void setDescripcion_tipo_producto(String descripcion_tipo_producto) {
        this.descripcion_tipo_producto = descripcion_tipo_producto;
    }

    public int getEstatus() {
        return estatus;
    }

    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }
}
