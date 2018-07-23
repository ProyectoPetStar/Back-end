package org.petstar.dto.ETAD;

import java.math.BigDecimal;

/**
 * Clase Auxiliar
 * Clase Auxiliar para la generación del reporte Enlace Objetivos Estrategicos y KIP´s Operativos
 * @author Tech-Pro
 */
public class ObjetivosREOEKO {
    private String objetivo;
    private BigDecimal meta;
    private BigDecimal real;

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
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
