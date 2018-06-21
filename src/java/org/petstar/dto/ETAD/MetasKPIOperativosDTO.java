package org.petstar.dto.ETAD;

import java.math.BigDecimal;

/**
 *
 * @author Tech-Pro
 */
public class MetasKPIOperativosDTO {
    private int id_meta_anual_kpi;
    private String valor_linea;
    private String valor_kpi;
    private int anio;
    private BigDecimal valor;
    private int estatus;

    public int getId_meta_anual_kpi() {
        return id_meta_anual_kpi;
    }

    public void setId_meta_anual_kpi(int id_meta_anual_kpi) {
        this.id_meta_anual_kpi = id_meta_anual_kpi;
    }

    public String getValor_linea() {
        return valor_linea;
    }

    public void setValor_linea(String valor_linea) {
        this.valor_linea = valor_linea;
    }

    public String getValor_kpi() {
        return valor_kpi;
    }

    public void setValor_kpi(String valor_kpi) {
        this.valor_kpi = valor_kpi;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public int getEstatus() {
        return estatus;
    }

    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }
}
