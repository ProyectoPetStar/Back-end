package org.petstar.dto.ETAD;

import org.petstar.dto.CatalogosDTO;

/**
 *
 * @author Tech-Pro
 */
public class PetCatKpiOperativo extends CatalogosDTO{
    private String tipo_kpi;
    private String unidad_medida_kpi_operativo;

    public String getTipo_kpi() {
        return tipo_kpi;
    }

    public void setTipo_kpi(String tipo_kpi) {
        this.tipo_kpi = tipo_kpi;
    }

    public String getUnidad_medida_kpi_operativo() {
        return unidad_medida_kpi_operativo;
    }

    public void setUnidad_medida_kpi_operativo(String unidad_medida_kpi_operativo) {
        this.unidad_medida_kpi_operativo = unidad_medida_kpi_operativo;
    }
}
