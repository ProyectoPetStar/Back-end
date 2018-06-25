package org.petstar.dto.ETAD;

import java.math.BigDecimal;

/**
 *
 * @author Tech-Pro
 */
public class MetasObjetivosOperativosDTO {
    private int id_meta_anual_estrategica;
    private int anio;
    private int estatus;
    private String valor_linea;
    private String valor_cat_obj_anual;
    private String um_obj_ope;
    private BigDecimal valor;

    public int getId_meta_anual_estrategica() {
        return id_meta_anual_estrategica;
    }

    public void setId_meta_anual_estrategica(int id_meta_anual_estrategica) {
        this.id_meta_anual_estrategica = id_meta_anual_estrategica;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public int getEstatus() {
        return estatus;
    }

    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }

    public String getValor_linea() {
        return valor_linea;
    }

    public void setValor_linea(String valor_linea) {
        this.valor_linea = valor_linea;
    }

    public String getValor_cat_obj_anual() {
        return valor_cat_obj_anual;
    }

    public void setValor_cat_obj_anual(String valor_cat_obj_anual) {
        this.valor_cat_obj_anual = valor_cat_obj_anual;
    }

    public String getUm_obj_ope() {
        return um_obj_ope;
    }

    public void setUm_obj_ope(String um_obj_ope) {
        this.um_obj_ope = um_obj_ope;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}
