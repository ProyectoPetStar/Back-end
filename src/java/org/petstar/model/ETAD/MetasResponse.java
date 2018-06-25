package org.petstar.model.ETAD;

import java.util.List;
import org.petstar.dto.ETAD.KPIOperativosDTO;
import org.petstar.dto.ETAD.MetasEstrategicasDTO;
import org.petstar.dto.ETAD.MetasKPIOperativosDTO;
import org.petstar.dto.ETAD.MetasMetasEstrategicasDTO;
import org.petstar.dto.ETAD.MetasObjetivosOperativosDTO;
import org.petstar.dto.ETAD.ObjetivosOperativosDTO;
import org.petstar.dto.LineasDTO;
import org.petstar.dto.PeriodosDTO;

/**
 *
 * @author Tech-Pro
 */
public class MetasResponse {
    
    private List<MetasObjetivosOperativosDTO> listMetasObjetivosOperativos;
    private List<MetasMetasEstrategicasDTO> listMetasMetasEstrategicas;
    private List<MetasKPIOperativosDTO> listMetasKPIOperativos;
    private List<ObjetivosOperativosDTO> listObjetivosOperativos;
    private List<MetasEstrategicasDTO> listMetasEstrategicas;
    private List<KPIOperativosDTO> listKPIOperativos;
    private List<PeriodosDTO> listPeriodos;
    private List<LineasDTO> listLineas;

    public List<MetasObjetivosOperativosDTO> getListMetasObjetivosOperativos() {
        return listMetasObjetivosOperativos;
    }

    public void setListMetasObjetivosOperativos(List<MetasObjetivosOperativosDTO> listMetasObjetivosOperativos) {
        this.listMetasObjetivosOperativos = listMetasObjetivosOperativos;
    }

    public List<MetasMetasEstrategicasDTO> getListMetasMetasEstrategicas() {
        return listMetasMetasEstrategicas;
    }

    public void setListMetasMetasEstrategicas(List<MetasMetasEstrategicasDTO> listMetasMetasEstrategicas) {
        this.listMetasMetasEstrategicas = listMetasMetasEstrategicas;
    }

    public List<MetasKPIOperativosDTO> getListMetasKPIOperativos() {
        return listMetasKPIOperativos;
    }

    public void setListMetasKPIOperativos(List<MetasKPIOperativosDTO> listMetasKPIOperativos) {
        this.listMetasKPIOperativos = listMetasKPIOperativos;
    }

    public List<ObjetivosOperativosDTO> getListObjetivosOperativos() {
        return listObjetivosOperativos;
    }

    public void setListObjetivosOperativos(List<ObjetivosOperativosDTO> listObjetivosOperativos) {
        this.listObjetivosOperativos = listObjetivosOperativos;
    }

    public List<MetasEstrategicasDTO> getListMetasEstrategicas() {
        return listMetasEstrategicas;
    }

    public void setListMetasEstrategicas(List<MetasEstrategicasDTO> listMetasEstrategicas) {
        this.listMetasEstrategicas = listMetasEstrategicas;
    }

    public List<KPIOperativosDTO> getListKPIOperativos() {
        return listKPIOperativos;
    }

    public void setListKPIOperativos(List<KPIOperativosDTO> listKPIOperativos) {
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
