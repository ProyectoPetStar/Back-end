package org.petstar.dto.ETAD;

import org.petstar.dto.CatalogosDTO;

/**
 *
 * @author Tech-Pro
 */
public class MetasEstrategicasDTO extends CatalogosDTO{
    private String unidad_medida_me;
    private int anual;
    private int mensual;

    public String getUnidad_medida_me() {
        return unidad_medida_me;
    }

    public void setUnidad_medida_me(String unidad_medida_me) {
        this.unidad_medida_me = unidad_medida_me;
    }

    public int getAnual() {
        return anual;
    }

    public void setAnual(int anual) {
        this.anual = anual;
    }

    public int getMensual() {
        return mensual;
    }

    public void setMensual(int mensual) {
        this.mensual = mensual;
    }
}
