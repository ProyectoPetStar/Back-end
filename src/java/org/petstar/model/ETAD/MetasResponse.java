package org.petstar.model.ETAD;

import java.util.List;
import org.petstar.dto.ETAD.PetCatKpiOperativo;
import org.petstar.dto.ETAD.PetCatMetaEstrategica;
import org.petstar.dto.ETAD.PetMetaAnualKpi;
import org.petstar.dto.ETAD.PetMetaAnualEstrategica;
import org.petstar.dto.ETAD.PetMetaAnualObjetivoOperativo;
import org.petstar.dto.ETAD.PetCatObjetivoOperativo;
import org.petstar.dto.LineasDTO;
import org.petstar.dto.PeriodosDTO;

/**
 *
 * @author Tech-Pro
 */
public class MetasResponse {
    
    private List<PetMetaAnualObjetivoOperativo> listMetasObjetivosOperativos;
    private List<PetMetaAnualEstrategica> listMetasMetasEstrategicas;
    private List<PetMetaAnualKpi> listMetasKPIOperativos;
    private List<PetCatObjetivoOperativo> listObjetivosOperativos;
    private List<PetCatMetaEstrategica> listMetasEstrategicas;
    private List<PetCatKpiOperativo> listKPIOperativos;
    private List<PeriodosDTO> listPeriodos;
    private List<LineasDTO> listLineas;

    public List<PetMetaAnualObjetivoOperativo> getListMetasObjetivosOperativos() {
        return listMetasObjetivosOperativos;
    }

    public void setListMetasObjetivosOperativos(List<PetMetaAnualObjetivoOperativo> listMetasObjetivosOperativos) {
        this.listMetasObjetivosOperativos = listMetasObjetivosOperativos;
    }

    public List<PetMetaAnualEstrategica> getListMetasMetasEstrategicas() {
        return listMetasMetasEstrategicas;
    }

    public void setListMetasMetasEstrategicas(List<PetMetaAnualEstrategica> listMetasMetasEstrategicas) {
        this.listMetasMetasEstrategicas = listMetasMetasEstrategicas;
    }

    public List<PetMetaAnualKpi> getListMetasKPIOperativos() {
        return listMetasKPIOperativos;
    }

    public void setListMetasKPIOperativos(List<PetMetaAnualKpi> listMetasKPIOperativos) {
        this.listMetasKPIOperativos = listMetasKPIOperativos;
    }

    public List<PetCatObjetivoOperativo> getListObjetivosOperativos() {
        return listObjetivosOperativos;
    }

    public void setListObjetivosOperativos(List<PetCatObjetivoOperativo> listObjetivosOperativos) {
        this.listObjetivosOperativos = listObjetivosOperativos;
    }

    public List<PetCatMetaEstrategica> getListMetasEstrategicas() {
        return listMetasEstrategicas;
    }

    public void setListMetasEstrategicas(List<PetCatMetaEstrategica> listMetasEstrategicas) {
        this.listMetasEstrategicas = listMetasEstrategicas;
    }

    public List<PetCatKpiOperativo> getListKPIOperativos() {
        return listKPIOperativos;
    }

    public void setListKPIOperativos(List<PetCatKpiOperativo> listKPIOperativos) {
        this.listKPIOperativos = listKPIOperativos;
    }

    public List<PeriodosDTO> getListPeriodos() {
        return listPeriodos;
    }

    public void setListPeriodos(List<PeriodosDTO> listPeriodos) {
        this.listPeriodos = listPeriodos;
    }

    public List<LineasDTO> getListLineas() {
        return listLineas;
    }

    public void setListLineas(List<LineasDTO> listLineas) {
        this.listLineas = listLineas;
    }
}
