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
    private int id_pet_cat_objetivo_operativo;
    private String lineas;
    private PetCatObjetivoOperativo objetivoOperativo;

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

    public int getId_pet_cat_objetivo_operativo() {
        return id_pet_cat_objetivo_operativo;
    }

    public void setId_pet_cat_objetivo_operativo(int id_pet_cat_objetivo_operativo) {
        this.id_pet_cat_objetivo_operativo = id_pet_cat_objetivo_operativo;
    }

    public String getLineas() {
        return lineas;
    }

    public void setLineas(String lineas) {
        this.lineas = lineas;
    }

    public PetCatObjetivoOperativo getObjetivoOperativo() {
        return objetivoOperativo;
    }

    public void setObjetivoOperativo(PetCatObjetivoOperativo objetivoOperativo) {
        this.objetivoOperativo = objetivoOperativo;
    }
}
