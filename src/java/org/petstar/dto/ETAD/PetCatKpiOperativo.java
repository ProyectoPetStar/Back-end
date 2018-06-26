package org.petstar.dto.ETAD;

import org.petstar.dto.CatalogosDTO;

/**
 *
 * @author Tech-Pro
 */
public class PetCatKpiOperativo extends CatalogosDTO{
    private int tipo_kpi;
    private String unidad_medida;
    private int mensual;
    private int anual;
    private String lineas;

    public int getTipo_kpi() {
        return tipo_kpi;
    }

    public void setTipo_kpi(int tipo_kpi) {
        this.tipo_kpi = tipo_kpi;
    }

    public String getUnidad_medida() {
        return unidad_medida;
    }

    public void setUnidad_medida(String unidad_medida) {
        this.unidad_medida = unidad_medida;
    }

    public int getMensual() {
        return mensual;
    }

    public void setMensual(int mensual) {
        this.mensual = mensual;
    }

    public int getAnual() {
        return anual;
    }

    public void setAnual(int anual) {
        this.anual = anual;
    }

    public String getLineas() {
        return lineas;
    }

    public void setLineas(String lineas) {
        this.lineas = lineas;
    }
}
