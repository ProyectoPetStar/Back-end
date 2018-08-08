package org.petstar.dto;

import java.math.BigDecimal;

/**
 *
 * @author Tech-Pro
 */
public class Fuentes {
    private int id;
    private String valor;
    private BigDecimal hrs;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
    
    public BigDecimal getHrs() {
        return hrs;
    }

    public void setHrs(BigDecimal hrs) {
        this.hrs = hrs;
    }
}
