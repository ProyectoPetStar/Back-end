package org.petstar.dto.ETAD;

import java.math.BigDecimal;
import java.sql.Date;
import org.petstar.dto.CatalogosDTO;

/**
 *
 * @author Tech-Pro
 */
public class PetIndicadorDiario {
    private int id_indicador_diario;
    private Date dia;
    private BigDecimal valor;
    private int id_meta_kpi;
    private int id_grupo;
    private int estatus;
    private CatalogosDTO grupo;
    private PetMetaKpi metaKpi;
    private String dia_string;

    public int getId_indicador_diario() {
        return id_indicador_diario;
    }

    public void setId_indicador_diario(int id_indicador_diario) {
        this.id_indicador_diario = id_indicador_diario;
    }

    public Date getDia() {
        return dia;
    }

    public void setDia(Date dia) {
        this.dia = dia;
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

    public String getDia_string() {
        return dia_string;
    }

    public void setDia_string(String dia_string) {
        this.dia_string = dia_string;
    }
}
