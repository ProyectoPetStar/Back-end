package org.petstar.model.ETAD;

import java.util.List;
import org.petstar.dto.CatalogosDTO;
import org.petstar.dto.ETAD.PetCatFrecuencia;
import org.petstar.dto.ETAD.PetCatKpiOperativo;
import org.petstar.dto.ETAD.PetCatMetaEstrategica;
import org.petstar.dto.ETAD.PetCatObjetivoOperativo;

/**
 *
 * @author Tech-Pro
 */
public class CatalogosResponse {
    private List<PetCatObjetivoOperativo> listObjetivoOperativos;
    private List<PetCatMetaEstrategica> listMetasEstrategicas; 
    private List<PetCatKpiOperativo> listKpiOperativos;
    private PetCatObjetivoOperativo objetivoOperativo;
    private List<PetCatFrecuencia> listFrecuencias;
    private PetCatMetaEstrategica metaEstrategica;
    private PetCatKpiOperativo kpiOperativo;
    private List<CatalogosDTO> listEtads;

    public List<PetCatObjetivoOperativo> getListObjetivoOperativos() {
        return listObjetivoOperativos;
    }

    public void setListObjetivoOperativos(List<PetCatObjetivoOperativo> listObjetivoOperativos) {
        this.listObjetivoOperativos = listObjetivoOperativos;
    }

    public List<PetCatMetaEstrategica> getListMetasEstrategicas() {
        return listMetasEstrategicas;
    }

    public void setListMetasEstrategicas(List<PetCatMetaEstrategica> listMetasEstrategicas) {
        this.listMetasEstrategicas = listMetasEstrategicas;
    }

    public List<PetCatKpiOperativo> getListKpiOperativos() {
        return listKpiOperativos;
    }

    public void setListKpiOperativos(List<PetCatKpiOperativo> listKpiOperativos) {
        this.listKpiOperativos = listKpiOperativos;
    }

    public PetCatObjetivoOperativo getObjetivoOperativo() {
        return objetivoOperativo;
    }

    public void setObjetivoOperativo(PetCatObjetivoOperativo objetivoOperativo) {
        this.objetivoOperativo = objetivoOperativo;
    }

    public List<PetCatFrecuencia> getListFrecuencias() {
        return listFrecuencias;
    }

    public void setListFrecuencias(List<PetCatFrecuencia> listFrecuencias) {
        this.listFrecuencias = listFrecuencias;
    }

    public PetCatMetaEstrategica getMetaEstrategica() {
        return metaEstrategica;
    }

    public void setMetaEstrategica(PetCatMetaEstrategica metaEstrategica) {
        this.metaEstrategica = metaEstrategica;
    }

    public PetCatKpiOperativo getKpiOperativo() {
        return kpiOperativo;
    }

    public void setKpiOperativo(PetCatKpiOperativo kpiOperativo) {
        this.kpiOperativo = kpiOperativo;
    }

    public List<CatalogosDTO> getListEtads() {
        return listEtads;
    }

    public void setListEtads(List<CatalogosDTO> listEtads) {
        this.listEtads = listEtads;
    }
}
