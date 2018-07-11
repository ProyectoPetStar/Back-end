package org.petstar.dto.ETAD;

import java.math.BigDecimal;

/**
 *
 * @author Tech-Pro
 */
public class PetIndicadorMensual {
    private int id_indicador_mensual;
    private BigDecimal valor;
    private int id_meta_kpi;
    private int id_periodo;
    private int id_grupo;
    private int id_linea;
    private int estatus;

    public int getId_indicador_mensual() {
        return id_indicador_mensual;
    }

    public void setId_indicador_mensual(int id_indicador_mensual) {
        this.id_indicador_mensual = id_indicador_mensual;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public int getId_meta_kpi() {
        return id_meta_kpi;
    }

    public void setId_meta_kpi(int id_meta_kpi) {
        this.id_meta_kpi = id_meta_kpi;
    }

    public int getId_periodo() {
        return id_periodo;
    }

    public void setId_periodo(int id_periodo) {
        this.id_periodo = id_periodo;
    }

    public int getId_grupo() {
        return id_grupo;
    }

    public void setId_grupo(int id_grupo) {
        this.id_grupo = id_grupo;
    }

    public int getId_linea() {
        return id_linea;
    }

    public void setId_linea(int id_linea) {
        this.id_linea = id_linea;
    }

    public int getEstatus() {
        return estatus;
    }

    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }
}