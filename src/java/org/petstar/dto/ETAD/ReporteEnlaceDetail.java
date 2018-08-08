package org.petstar.dto.ETAD;

import java.math.BigDecimal;

/**
 *
 * @author Tech-Pro
 */
public class ReporteEnlaceDetail {
    private int linea;
    private BigDecimal meta;
    private BigDecimal real;

    public int getLinea() {
        return linea;
    }

    public void setLinea(int linea) {
        this.linea = linea;
    }

    public BigDecimal getMeta() {
        return meta;
    }

    public void setMeta(BigDecimal meta) {
        this.meta = meta;
    }

    public BigDecimal getReal() {
        return real;
    }

    public void setReal(BigDecimal real) {
        this.real = real;
    }
}
