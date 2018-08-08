package org.petstar.model.ishikawa;

import java.util.List;
import org.petstar.dto.CatalogosDTO;
import org.petstar.dto.ishikawa.PetIshikawa;

/**
 *
 * @author Tech-Pro
 */
public class IshikawaResponse {
    private List<CatalogosDTO> listPreguntas;
    private List<PetIshikawa> listIshikawas;
    private List<CatalogosDTO> listGrupos;
    private List<CatalogosDTO> listEtads;
    private List<CatalogosDTO> listMs;
    private PetIshikawa ishikawa;
    private String dia_actual;

    public List<CatalogosDTO> getListPreguntas() {
        return listPreguntas;
    }

    public void setListPreguntas(List<CatalogosDTO> listPreguntas) {
        this.listPreguntas = listPreguntas;
    }

    public List<PetIshikawa> getListIshikawas() {
        return listIshikawas;
    }

    public void setListIshikawas(List<PetIshikawa> listIshikawas) {
        this.listIshikawas = listIshikawas;
    }

    public List<CatalogosDTO> getListGrupos() {
        return listGrupos;
    }

    public void setListGrupos(List<CatalogosDTO> listGrupos) {
        this.listGrupos = listGrupos;
    }

    public List<CatalogosDTO> getListEtads() {
        return listEtads;
    }

    public void setListEtads(List<CatalogosDTO> listEtads) {
        this.listEtads = listEtads;
    }

    public List<CatalogosDTO> getListMs() {
        return listMs;
    }

    public void setListMs(List<CatalogosDTO> listMs) {
        this.listMs = listMs;
    }

    public PetIshikawa getIshikawa() {
        return ishikawa;
    }

    public void setIshikawa(PetIshikawa ishikawa) {
        this.ishikawa = ishikawa;
    }

    public String getDia_actual() {
        return dia_actual;
    }

    public void setDia_actual(String dia_actual) {
        this.dia_actual = dia_actual;
    }
}
