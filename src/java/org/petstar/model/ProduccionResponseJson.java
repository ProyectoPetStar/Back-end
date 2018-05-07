package org.petstar.model;

import java.util.List;
import org.petstar.dto.CatalogosDTO;
import org.petstar.dto.LineasDTO;
import org.petstar.dto.ProduccionDTO;
import org.petstar.dto.ProductosDTO;

/**
 *
 * @author Tech-Pro
 */
public class ProduccionResponseJson {
    private List<ProduccionDTO> listProduccion;
    private List<ProductosDTO> listProductos;
    private List<LineasDTO> listLineas;
    private List<CatalogosDTO> listGrupos;
    private List<CatalogosDTO> listTurnos;
    private String dia;

    public List<ProduccionDTO> getListProduccion() {
        return listProduccion;
    }

    public void setListProduccion(List<ProduccionDTO> listProduccion) {
        this.listProduccion = listProduccion;
    }

    public List<ProductosDTO> getListProductos() {
        return listProductos;
    }

    public void setListProductos(List<ProductosDTO> listProductos) {
        this.listProductos = listProductos;
    }

    public List<LineasDTO> getListLineas() {
        return listLineas;
    }

    public void setListLineas(List<LineasDTO> listLineas) {
        this.listLineas = listLineas;
    }

    public List<CatalogosDTO> getListGrupos() {
        return listGrupos;
    }

    public void setListGrupos(List<CatalogosDTO> listGrupos) {
        this.listGrupos = listGrupos;
    }

    public List<CatalogosDTO> getListTurnos() {
        return listTurnos;
    }

    public void setListTurnos(List<CatalogosDTO> listTurnos) {
        this.listTurnos = listTurnos;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }
}
