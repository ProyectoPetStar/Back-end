package org.petstar.model;

import java.util.List;
import org.petstar.dto.CatalogosDTO;
import org.petstar.dto.FallasDTO;
import org.petstar.dto.LineasDTO;
import org.petstar.dto.MetasDTO;
import org.petstar.dto.PeriodosDTO;
import org.petstar.dto.ProduccionDTO;
import org.petstar.dto.ProductosDTO;

/**
 *
 * @author Tech-Pro
 */
public class ProduccionResponseJson {
    private List<ProduccionDTO> listProduccion;
    private List<ProduccionDTO> listDetalle;
    private List<PeriodosDTO> listPeriodos;
    private List<ProductosDTO> listProductos;
    private List<LineasDTO> listLineas;
    private List<CatalogosDTO> listGrupos;
    private List<CatalogosDTO> listTurnos;
    private List<FallasDTO> listFallas;
    private boolean estatusPeriodo;
    private MetasDTO meta;
    

    public List<ProduccionDTO> getListProduccion() {
        return listProduccion;
    }

    public void setListProduccion(List<ProduccionDTO> listProduccion) {
        this.listProduccion = listProduccion;
    }

    public List<ProduccionDTO> getListDetalle() {
        return listDetalle;
    }

    public void setListDetalle(List<ProduccionDTO> listDetalle) {
        this.listDetalle = listDetalle;
    }

    public List<PeriodosDTO> getListPeriodos() {
        return listPeriodos;
    }

    public void setListPeriodos(List<PeriodosDTO> listPeriodos) {
        this.listPeriodos = listPeriodos;
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

    public MetasDTO getMeta() {
        return meta;
    }

    public void setMeta(MetasDTO meta) {
        this.meta = meta;
    }

    public List<FallasDTO> getListFallas() {
        return listFallas;
    }

    public void setListFallas(List<FallasDTO> listFallas) {
        this.listFallas = listFallas;
    }

    public boolean isEstatusPeriodo() {
        return estatusPeriodo;
    }

    public void setEstatusPeriodo(boolean estatusPeriodo) {
        this.estatusPeriodo = estatusPeriodo;
    }
}
