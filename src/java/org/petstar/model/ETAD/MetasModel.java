package org.petstar.model.ETAD;

import org.petstar.dto.ETAD.KPIOperativosDTO;
import org.petstar.dto.ETAD.MetasEstrategicasDTO;
import org.petstar.dto.ETAD.ObjetivosOperativosDTO;

/**
 *
 * @author Tech-Pro
 */
public class MetasModel {
    private int id_etad;
    private int tipo_meta;
    private String frecuencia;
    private int anio;
    private int id_periodo;
    private KPIOperativosDTO kPIOperativo;
    private MetasEstrategicasDTO metaEstrategica;
    private ObjetivosOperativosDTO objetivoOperativo;

    public int getId_etad() {
        return id_etad;
    }

    public void setId_etad(int id_etad) {
        this.id_etad = id_etad;
    }

    public int getTipo_meta() {
        return tipo_meta;
    }

    public void setTipo_meta(int tipo_meta) {
        this.tipo_meta = tipo_meta;
    }

    public String getFrecuencia() {
        return frecuencia;
    }

    public void setFrecuencia(String frecuencia) {
        this.frecuencia = frecuencia;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public int getId_periodo() {
        return id_periodo;
    }

    public void setId_periodo(int id_periodo) {
        this.id_periodo = id_periodo;
    }

    public KPIOperativosDTO getkPIOperativo() {
        return kPIOperativo;
    }

    public void setkPIOperativo(KPIOperativosDTO kPIOperativo) {
        this.kPIOperativo = kPIOperativo;
    }

    public MetasEstrategicasDTO getMetaEstrategica() {
        return metaEstrategica;
    }

    public void setMetaEstrategica(MetasEstrategicasDTO metaEstrategica) {
        this.metaEstrategica = metaEstrategica;
    }

    public ObjetivosOperativosDTO getObjetivoOperativo() {
        return objetivoOperativo;
    }

    public void setObjetivoOperativo(ObjetivosOperativosDTO objetivoOperativo) {
        this.objetivoOperativo = objetivoOperativo;
    }
}
