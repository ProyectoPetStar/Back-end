/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.petstar.dto;

/**
 * DTO de Productos
 * @author Tech-Pro
 */
public class ProductosDTO {
    private int id_producto;
    private String valor;
    private String descripcion;
    private int activo;
    private int id_linea;
    private int id_tipo_producto;
    private String valor_linea;
    private String valor_tipo_producto;

    public int getId_producto() {
        return id_producto;
    }

    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getActivo() {
        return activo;
    }

    public void setActivo(int activo) {
        this.activo = activo;
    }

    public int getId_linea() {
        return id_linea;
    }

    public void setId_linea(int id_linea) {
        this.id_linea = id_linea;
    }

    public int getId_tipo_producto() {
        return id_tipo_producto;
    }

    public void setId_tipo_producto(int id_tipo_producto) {
        this.id_tipo_producto = id_tipo_producto;
    }

    public String getValor_linea() {
        return valor_linea;
    }

    public void setValor_linea(String valor_linea) {
        this.valor_linea = valor_linea;
    }

    public String getValor_tipo_producto() {
        return valor_tipo_producto;
    }

    public void setValor_tipo_producto(String valor_tipo_producto) {
        this.valor_tipo_producto = valor_tipo_producto;
    }
}
