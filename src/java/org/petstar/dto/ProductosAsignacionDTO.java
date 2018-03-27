/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.petstar.dto;

/**
 *
 * @author Tech-Pro
 */
public class ProductosAsignacionDTO {
    private int id_pro_productos;
    private String dia;
    private String linea;
    private int id_turno;
    private String turno;
    private int id_grupo;
    private String grupo;
    private int id_producto;
    private String producto;
    private Float valor;
    private int borrar;

    public int getId_pro_productos() {
        return id_pro_productos;
    }

    public void setId_pro_productos(int id_pro_productos) {
        this.id_pro_productos = id_pro_productos;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getLinea() {
        return linea;
    }

    public void setLinea(String linea) {
        this.linea = linea;
    }

    public int getId_turno() {
        return id_turno;
    }

    public void setId_turno(int id_turno) {
        this.id_turno = id_turno;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public int getId_grupo() {
        return id_grupo;
    }

    public void setId_grupo(int id_grupo) {
        this.id_grupo = id_grupo;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public int getId_producto() {
        return id_producto;
    }

    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public Float getValor() {
        return valor;
    }

    public void setValor(Float valor) {
        this.valor = valor;
    }

    public int getBorrar() {
        return borrar;
    }

    public void setBorrar(int borrar) {
        this.borrar = borrar;
    }
}
