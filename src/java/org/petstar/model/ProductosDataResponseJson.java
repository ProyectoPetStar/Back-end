/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.petstar.model;

import java.util.List;
import org.petstar.dto.CatalogosDTO;
import org.petstar.dto.LineasDTO;
import org.petstar.dto.ProductosDTO;

/**
 *
 * @author Tech-Pro
 */
public class ProductosDataResponseJson {
    
    private ProductosDTO producto;
    private List<ProductosDTO> listProductos;
    private List<LineasDTO> listLineas;
    private List<CatalogosDTO> listTurnos;
    private List<CatalogosDTO> listGrupos;

    public ProductosDTO getProducto() {
        return producto;
    }

    public void setProducto(ProductosDTO producto) {
        this.producto = producto;
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

    public List<CatalogosDTO> getListTurnos() {
        return listTurnos;
    }

    public void setListTurnos(List<CatalogosDTO> listTurnos) {
        this.listTurnos = listTurnos;
    }

    public List<CatalogosDTO> getListGrupos() {
        return listGrupos;
    }

    public void setListGrupos(List<CatalogosDTO> listGrupos) {
        this.listGrupos = listGrupos;
    }
    
}
