package org.petstar.model.ETAD;

import java.util.HashMap;
import java.util.List;
import org.petstar.dto.ETAD.PetCatObjetivoOperativo;
import org.petstar.dto.LineasDTO;
import org.petstar.dto.PeriodosDTO;

/**
 *
 * @author Tech-Pro
 */
public class PonderacionResponse {
    private List<PetCatObjetivoOperativo> listObjetivosOperativos;
    private List<PeriodosDTO> listPeriodos;
    private List<LineasDTO> listEtads;
    private List<HashMap> listData;

    public List<PetCatObjetivoOperativo> getListObjetivosOperativos() {
        return listObjetivosOperativos;
    }

    public void setListObjetivosOperativos(List<PetCatObjetivoOperativo> listObjetivosOperativos) {
        this.listObjetivosOperativos = listObjetivosOperativos;
    }

    public List<PeriodosDTO> getListPeriodos() {
        return listPeriodos;
    }

    public void setListPeriodos(List<PeriodosDTO> listPeriodos) {
        this.listPeriodos = listPeriodos;
    }

    public List<LineasDTO> getListEtads() {
        return listEtads;
    }

    public void setListEtads(List<LineasDTO> listEtads) {
        this.listEtads = listEtads;
    }

    public List<HashMap> getListData() {
        return listData;
    }

    public void setListData(List<HashMap> listData) {
        this.listData = listData;
    }    
}
