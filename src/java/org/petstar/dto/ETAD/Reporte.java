package org.petstar.dto.ETAD;

import java.math.BigDecimal;

/**
 *
 * @author Tech-Pro
 */
public class Reporte {
    private String objetivo_operativo;
    private String kpi_operativo;
    private BigDecimal meta;
    private String unidad_medida;
    private int ponderacion;
    private int tipo_kpi;
    private int id_frecuencia;
    private BigDecimal total_mes;
    private int resultado;
    private BigDecimal grupoa;
    private BigDecimal grupob;
    private BigDecimal grupoc;
    private BigDecimal grupod;

    public String getObjetivo_operativo() {
        return objetivo_operativo;
    }

    public void setObjetivo_operativo(String objetivo_operativo) {
        this.objetivo_operativo = objetivo_operativo;
    }

    public String getKpi_operativo() {
        return kpi_operativo;
    }

    public void setKpi_operativo(String kpi_operativo) {
        this.kpi_operativo = kpi_operativo;
    }

    public BigDecimal getMeta() {
        return meta;
    }

    public void setMeta(BigDecimal meta) {
        this.meta = meta;
    }

    public String getUnidad_medida() {
        return unidad_medida;
    }

    public void setUnidad_medida(String unidad_medida) {
        this.unidad_medida = unidad_medida;
    }

    public int getPonderacion() {
        return ponderacion;
    }

    public void setPonderacion(int ponderacion) {
        this.ponderacion = ponderacion;
    }

    public int getTipo_kpi() {
        return tipo_kpi;
    }

    public void setTipo_kpi(int tipo_kpi) {
        this.tipo_kpi = tipo_kpi;
    }

    public int getId_frecuencia() {
        return id_frecuencia;
    }

    public void setId_frecuencia(int id_frecuencia) {
        this.id_frecuencia = id_frecuencia;
    }

    public BigDecimal getTotal_mes() {
        return total_mes;
    }

    public void setTotal_mes(BigDecimal total_mes) {
        this.total_mes = total_mes;
    }

    public int getResultado() {
        return resultado;
    }

    public void setResultado(int resultado) {
        this.resultado = resultado;
    }

    public BigDecimal getGrupoa() {
        return grupoa;
    }

    public void setGrupoa(BigDecimal grupoa) {
        this.grupoa = grupoa;
    }

    public BigDecimal getGrupob() {
        return grupob;
    }

    public void setGrupob(BigDecimal grupob) {
        this.grupob = grupob;
    }

    public BigDecimal getGrupoc() {
        return grupoc;
    }

    public void setGrupoc(BigDecimal grupoc) {
        this.grupoc = grupoc;
    }

    public BigDecimal getGrupod() {
        return grupod;
    }

    public void setGrupod(BigDecimal grupod) {
        this.grupod = grupod;
    }
}
