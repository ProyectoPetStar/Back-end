package org.petstar.dto.ETAD;

import org.petstar.dto.CatalogosDTO;

/**
 *
 * @author Tech-Pro
 */
public class PetCatKpiOperativo extends CatalogosDTO{
    private int id_cat_objetivo_operativo;
    private int id_frecuencia;
    private int tipo_kpi;
    private int tipo_operacion;
    private String unidad_medida;
    private String lineas;
    private PetCatObjetivoOperativo objetivoOperativo;
    private PetCatFrecuencia frecuencia;

    public int getId_cat_objetivo_operativo() {
        return id_cat_objetivo_operativo;
    }

    public void setId_cat_objetivo_operativo(int id_cat_objetivo_operativo) {
        this.id_cat_objetivo_operativo = id_cat_objetivo_operativo;
    }

    public int getId_frecuencia() {
        return id_frecuencia;
    }

    public void setId_frecuencia(int id_frecuencia) {
        this.id_frecuencia = id_frecuencia;
    }

    public int getTipo_kpi() {
        return tipo_kpi;
    }

    public void setTipo_kpi(int tipo_kpi) {
        this.tipo_kpi = tipo_kpi;
    }

    public int getTipo_operacion() {
        return tipo_operacion;
    }

    public void setTipo_operacion(int tipo_operacion) {
        this.tipo_operacion = tipo_operacion;
    }

    public String getUnidad_medida() {
        return unidad_medida;
    }

    public void setUnidad_medida(String unidad_medida) {
        this.unidad_medida = unidad_medida;
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

    public PetCatFrecuencia getFrecuencia() {
        return frecuencia;
    }

    public void setFrecuencia(PetCatFrecuencia frecuencia) {
        this.frecuencia = frecuencia;
    }
}
