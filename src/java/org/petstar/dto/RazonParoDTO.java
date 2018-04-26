package org.petstar.dto;

import java.math.BigDecimal;

/**
 *
 * @author Tech-Pro
 */
public class RazonParoDTO {
    private int id_razon_paro;
    private String valor;
    private String descripcion;
    private int activo;
    private int id_fuente_paro;
    private BigDecimal suma_tiempo_paro;

    public int getId_razon_paro() {
        return id_razon_paro;
    }

    public void setId_razon_paro(int id_razon_paro) {
        this.id_razon_paro = id_razon_paro;
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

    public int getId_fuente_paro() {
        return id_fuente_paro;
    }

    public void setId_fuente_paro(int id_fuente_paro) {
        this.id_fuente_paro = id_fuente_paro;
    }

    public BigDecimal getSuma_tiempo_paro() {
        return suma_tiempo_paro;
    }

    public void setSuma_tiempo_paro(BigDecimal suma_tiempo_paro) {
        this.suma_tiempo_paro = suma_tiempo_paro;
    }
    
}
