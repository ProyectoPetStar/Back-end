package org.petstar.dto.ETAD;

import java.math.BigDecimal;

/**
 *
 * @author TEch-Pro
 */
public class MetasMetasEstrategicasDTO {
    private int id_meta_anual_estrategica;
    private String valor_linea;
    private String valor_meta_estrategica;
    private String unidad_medida_me;
    private int anio;
    private BigDecimal valor;
    private int estatus;

    public int getId_meta_anual_estrategica() {
        return id_meta_anual_estrategica;
    }

    public void setId_meta_anual_estrategica(int id_meta_anual_estrategica) {
        this.id_meta_anual_estrategica = id_meta_anual_estrategica;
    }

    public String getValor_linea() {
        return valor_linea;
    }

    public void setValor_linea(String valor_linea) {
        this.valor_linea = valor_linea;
    }

    public String getValor_meta_estrategica() {
        return valor_meta_estrategica;
    }

    public void setValor_meta_estrategica(String valor_meta_estrategica) {
        this.valor_meta_estrategica = valor_meta_estrategica;
    }

    public String getUnidad_medida_me() {
        return unidad_medida_me;
    }

    public void setUnidad_medida_me(String unidad_medida_me) {
        this.unidad_medida_me = unidad_medida_me;
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
