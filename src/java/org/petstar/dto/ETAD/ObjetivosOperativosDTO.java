package org.petstar.dto.ETAD;

import org.petstar.dto.CatalogosDTO;

/**
 *
 * @author Tech-Pro
 */
public class ObjetivosOperativosDTO extends CatalogosDTO{
    private String unidad_medida_objetivo_operativo;

    public String getUnidad_medida_objetivo_operativo() {
        return unidad_medida_objetivo_operativo;
    }

    public void setUnidad_medida_objetivo_operativo(String unidad_medida_objetivo_operativo) {
        this.unidad_medida_objetivo_operativo = unidad_medida_objetivo_operativo;
    }
}
