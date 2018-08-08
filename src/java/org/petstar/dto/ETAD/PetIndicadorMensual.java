package org.petstar.dto.ETAD;

import java.math.BigDecimal;
import org.petstar.dto.CatalogosDTO;
import org.petstar.dto.PeriodosDTO;

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
    private int estatus;
    private PeriodosDTO periodo;
    private CatalogosDTO grupo;
    private PetMetaKpi metaKpi;

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

    public int getEstatus() {
        return estatus;
    }

    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }

    public PeriodosDTO getPeriodo() {
        return periodo;
    }

    public void setPeriodo(PeriodosDTO periodo) {
        this.periodo = periodo;
    }

    public CatalogosDTO getGrupo() {
        return grupo;
    }

    public void setGrupo(CatalogosDTO grupo) {
        this.grupo = grupo;
    }

    public PetMetaKpi getMetaKpi() {
        return metaKpi;
    }

    public void setMetaKpi(PetMetaKpi metaKpi) {
        this.metaKpi = metaKpi;
    }
}
