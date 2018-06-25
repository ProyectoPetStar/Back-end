package org.petstar.dto.ETAD;

import java.math.BigDecimal;
import org.petstar.dto.LineasDTO;

/**
 *
 * @author Tech-Pro
 */
public class PetMetaAnualKpi {
    private int id_meta_anual_kpi;
    private int id_linea;
    private int id_kpi_operativo;
    private int anio;
    private BigDecimal valor;
    private LineasDTO linea;
    private PetCatKpiOperativo kPIOperativo;

    public int getId_meta_anual_kpi() {
        return id_meta_anual_kpi;
    }

    public void setId_meta_anual_kpi(int id_meta_anual_kpi) {
        this.id_meta_anual_kpi = id_meta_anual_kpi;
    }

    public int getId_linea() {
        return id_linea;
    }

    public void setId_linea(int id_linea) {
        this.id_linea = id_linea;
    }

    public int getId_kpi_operativo() {
        return id_kpi_operativo;
    }

    public void setId_kpi_operativo(int id_kpi_operativo) {
        this.id_kpi_operativo = id_kpi_operativo;
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

    public LineasDTO getLinea() {
        return linea;
    }

    public void setLinea(LineasDTO linea) {
        this.linea = linea;
    }

    public PetCatKpiOperativo getkPIOperativo() {
        return kPIOperativo;
    }

    public void setkPIOperativo(PetCatKpiOperativo kPIOperativo) {
        this.kPIOperativo = kPIOperativo;
    }
}
