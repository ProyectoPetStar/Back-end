/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.petstar.model;

import java.util.List;
import org.petstar.dto.MetasAsignacionDTO;
import org.petstar.dto.ProductosAsignacionDTO;

/**
 *
 * @author Tech-Pro
 */
public class ProductosAsignacionesResponseJson {
    private ProductosAsignacionDTO produtoAsignacion;
    private List<ProductosAsignacionDTO> listProductosAsignacion;
    private List<MetasAsignacionDTO> listMetasAsignacion;

    public ProductosAsignacionDTO getProdutoAsignacion() {
        return produtoAsignacion;
    }

    public void setProdutoAsignacion(ProductosAsignacionDTO produtoAsignacion) {
        this.produtoAsignacion = produtoAsignacion;
    }

    public List<ProductosAsignacionDTO> getListProductosAsignacion() {
        return listProductosAsignacion;
    }

    public void setListProductosAsignacion(List<ProductosAsignacionDTO> listProductosAsignacion) {
        this.listProductosAsignacion = listProductosAsignacion;
    }

    public List<MetasAsignacionDTO> getListMetasAsignacion() {
        return listMetasAsignacion;
    }

    public void setListMetasAsignacion(List<MetasAsignacionDTO> listMetasAsignacion) {
        this.listMetasAsignacion = listMetasAsignacion;
    }
    
}
