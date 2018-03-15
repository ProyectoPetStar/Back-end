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
public class EquipoAmutDTO {
    private int id_equipo_amut;
    private String clave_equipo;
    private String nombre_equipo;
    private int activo;

    public int getId_equipo_amut() {
        return id_equipo_amut;
    }

    public void setId_equipo_amut(int id_equipo_amut) {
        this.id_equipo_amut = id_equipo_amut;
    }

    public String getClave_equipo() {
        return clave_equipo;
    }

    public void setClave_equipo(String clave_equipo) {
        this.clave_equipo = clave_equipo;
    }

    public String getNombre_equipo() {
        return nombre_equipo;
    }

    public void setNombre_equipo(String nombre_equipo) {
        this.nombre_equipo = nombre_equipo;
    }

    public int getActivo() {
        return activo;
    }

    public void setActivo(int activo) {
        this.activo = activo;
    }
}
