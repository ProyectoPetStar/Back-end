package org.petstar.dto;

/**
 *
 * @author Tech-PRo
 */
public class EquiposDTO {
    private int id_equipos;
    private String valor;
    private String descripcion;
    private int activo;

    public int getId_equipos() {
        return id_equipos;
    }

    public void setId_equipos(int id_equipos) {
        this.id_equipos = id_equipos;
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
}
