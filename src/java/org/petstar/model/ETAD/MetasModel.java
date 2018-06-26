package org.petstar.model.ETAD;

import java.util.List;
import org.petstar.dto.ETAD.PetMetaAnualEstrategica;
import org.petstar.dto.ETAD.PetMetaAnualKpi;
import org.petstar.dto.ETAD.PetMetaAnualObjetivoOperativo;

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
    private PetMetaAnualKpi kPIOperativo;
    private PetMetaAnualEstrategica metaEstrategica;
    private PetMetaAnualObjetivoOperativo objetivoOperativo;
    private List<PetMetaAnualKpi> listKPIOperativo;
    private List<PetMetaAnualEstrategica> ListMetaEstrategica;
    private List<PetMetaAnualObjetivoOperativo> listObjetivoOperativo;

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

    public PetMetaAnualKpi getkPIOperativo() {
        return kPIOperativo;
    }

    public void setkPIOperativo(PetMetaAnualKpi kPIOperativo) {
        this.kPIOperativo = kPIOperativo;
    }

    public PetMetaAnualEstrategica getMetaEstrategica() {
        return metaEstrategica;
    }

    public void setMetaEstrategica(PetMetaAnualEstrategica metaEstrategica) {
        this.metaEstrategica = metaEstrategica;
    }

    public PetMetaAnualObjetivoOperativo getObjetivoOperativo() {
        return objetivoOperativo;
    }

    public void setObjetivoOperativo(PetMetaAnualObjetivoOperativo objetivoOperativo) {
        this.objetivoOperativo = objetivoOperativo;
    }

    public List<PetMetaAnualKpi> getListKPIOperativo() {
        return listKPIOperativo;
    }

    public void setListKPIOperativo(List<PetMetaAnualKpi> listKPIOperativo) {
        this.listKPIOperativo = listKPIOperativo;
    }

    public List<PetMetaAnualEstrategica> getListMetaEstrategica() {
        return ListMetaEstrategica;
    }

    public void setListMetaEstrategica(List<PetMetaAnualEstrategica> ListMetaEstrategica) {
        this.ListMetaEstrategica = ListMetaEstrategica;
    }

    public List<PetMetaAnualObjetivoOperativo> getListObjetivoOperativo() {
        return listObjetivoOperativo;
    }

    public void setListObjetivoOperativo(List<PetMetaAnualObjetivoOperativo> listObjetivoOperativo) {
        this.listObjetivoOperativo = listObjetivoOperativo;
    }
}
