package org.petstar.model.ETAD;

import java.util.List;
import org.petstar.dto.ETAD.PetCatKpiOperativo;
import org.petstar.dto.ETAD.PetCatMetaEstrategica;
import org.petstar.dto.ETAD.PetCatObjetivoOperativo;
import org.petstar.dto.LineasDTO;
import org.petstar.dto.PeriodosDTO;

/**
 *
 * @author Tech-Pro
 */
public class MetasResponse {
    
    private MetasModel metasObjetivosOperativos;
    private MetasModel metasEstrategicas;
    private MetasModel metasKPIOperativos;
    private List<PetCatObjetivoOperativo> listObjetivosOperativos;
    private List<PetCatMetaEstrategica> listMetasEstrategicas;
    private List<PetCatKpiOperativo> listKPIOperativos;
    private List<PeriodosDTO> listPeriodos;
    private List<LineasDTO> listLineas;

    public MetasModel getMetasObjetivosOperativos() {
        return metasObjetivosOperativos;
    }

    public void setMetasObjetivosOperativos(MetasModel metasObjetivosOperativos) {
        this.metasObjetivosOperativos = metasObjetivosOperativos;
    }

    public MetasModel getMetasEstrategicas() {
        return metasEstrategicas;
    }

    public void setMetasEstrategicas(MetasModel metasEstrategicas) {
        this.metasEstrategicas = metasEstrategicas;
    }

    public MetasModel getMetasKPIOperativos() {
        return metasKPIOperativos;
    }

    public void setMetasKPIOperativos(MetasModel metasKPIOperativos) {
        this.metasKPIOperativos = metasKPIOperativos;
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
