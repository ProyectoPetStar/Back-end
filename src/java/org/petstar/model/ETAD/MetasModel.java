package org.petstar.model.ETAD;

import java.util.List;
import org.petstar.dto.ETAD.MetasKPIOperativosDTO;
import org.petstar.dto.ETAD.MetasMetasEstrategicasDTO;
import org.petstar.dto.ETAD.MetasObjetivosOperativosDTO;
import org.petstar.dto.LineasDTO;
import org.petstar.dto.PeriodosDTO;

/**
 *
 * @author Tech-Pro
 */
public class MetasModel {
    private List<MetasMetasEstrategicasDTO> listMetasEstrategicas;
    private List<MetasObjetivosOperativosDTO> listObjetivosOperativos;
    private List<MetasKPIOperativosDTO> listKPIOperativos;
    private List<PeriodosDTO> listPeriodos;
    private List<LineasDTO> listLineas;

    public List<MetasMetasEstrategicasDTO> getListMetasEstrategicas() {
        return listMetasEstrategicas;
    }

    public void setListMetasEstrategicas(List<MetasMetasEstrategicasDTO> listMetasEstrategicas) {
        this.listMetasEstrategicas = listMetasEstrategicas;
    }

    public List<MetasObjetivosOperativosDTO> getListObjetivosOperativos() {
        return listObjetivosOperativos;
    }

    public void setListObjetivosOperativos(List<MetasObjetivosOperativosDTO> listObjetivosOperativos) {
        this.listObjetivosOperativos = listObjetivosOperativos;
    }

    public List<MetasKPIOperativosDTO> getListKPIOperativos() {
        return listKPIOperativos;
    }

    public void setListKPIOperativos(List<MetasKPIOperativosDTO> listKPIOperativos) {
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
