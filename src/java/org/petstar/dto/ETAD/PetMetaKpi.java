package org.petstar.dto.ETAD;

import java.math.BigDecimal;
import org.petstar.dto.PeriodosDTO;

/**
 *
 * @author Tech-Pro
 */
public class PetMetaKpi {
    private int id_meta_kpi;
    private BigDecimal valor;
    private int id_periodo;
    private int id_kpi_etad;
    private PetEtadKpi etadKpi;
    private PeriodosDTO periodo;

    public int getId_meta_kpi() {
        return id_meta_kpi;
    }

    public void setId_meta_kpi(int id_meta_kpi) {
        this.id_meta_kpi = id_meta_kpi;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public int getId_periodo() {
        return id_periodo;
    }

    public void setId_periodo(int id_periodo) {
        this.id_periodo = id_periodo;
    }

    public int getId_kpi_etad() {
        return id_kpi_etad;
    }

    public void setId_kpi_etad(int id_kpi_etad) {
        this.id_kpi_etad = id_kpi_etad;
    }

    public PetEtadKpi getEtadKpi() {
        return etadKpi;
    }

    public void setEtadKpi(PetEtadKpi etadKpi) {
        this.etadKpi = etadKpi;
    }

    public PeriodosDTO getPeriodo() {
        return periodo;
    }

    public void setPeriodo(PeriodosDTO periodo) {
        this.periodo = periodo;
    }
}
