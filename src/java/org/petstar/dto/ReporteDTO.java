package org.petstar.dto;

import java.math.BigDecimal;
import java.sql.Date;

/**
 *
 * @author Tech-Pro
 */
public class ReporteDTO {
    private Date dia;
    private int id_turno;
    private String valor_grupo;
    private String valor_linea;
    private BigDecimal velocidad_promedio;
    private BigDecimal subproductos;

    public Date getDia() {
        return dia;
    }

    public void setDia(Date dia) {
        this.dia = dia;
    }

    public int getId_turno() {
        return id_turno;
    }

    public void setId_turno(int id_turno) {
        this.id_turno = id_turno;
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

    public BigDecimal getVelocidad_promedio() {
        return velocidad_promedio;
    }

    public void setVelocidad_promedio(BigDecimal velocidad_promedio) {
        this.velocidad_promedio = velocidad_promedio;
    }

    public BigDecimal getSubproductos() {
        return subproductos;
    }

    public void setSubproductos(BigDecimal subproductos) {
        this.subproductos = subproductos;
    }
}
