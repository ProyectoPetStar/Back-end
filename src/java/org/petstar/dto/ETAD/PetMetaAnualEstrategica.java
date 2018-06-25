package org.petstar.dto.ETAD;

import java.math.BigDecimal;
import org.petstar.dto.LineasDTO;

/**
 *
 * @author TEch-Pro
 */
public class PetMetaAnualEstrategica {
    private int id_meta_anual_estrategica;
    private int id_linea;
    private int id_meta_estrategica;
    private int anio;
    private BigDecimal valor;
    private LineasDTO linea;
    private PetCatMetaEstrategica metaEstrategica;

    public int getId_meta_anual_estrategica() {
        return id_meta_anual_estrategica;
    }

    public void setId_meta_anual_estrategica(int id_meta_anual_estrategica) {
        this.id_meta_anual_estrategica = id_meta_anual_estrategica;
    }

    public int getId_linea() {
        return id_linea;
    }

    public void setId_linea(int id_linea) {
        this.id_linea = id_linea;
    }

    public int getId_meta_estrategica() {
        return id_meta_estrategica;
    }

    public void setId_meta_estrategica(int id_meta_estrategica) {
        this.id_meta_estrategica = id_meta_estrategica;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public LineasDTO getLinea() {
        return linea;
    }

    public void setLinea(LineasDTO linea) {
        this.linea = linea;
    }

    public PetCatMetaEstrategica getMetaEstrategica() {
        return metaEstrategica;
    }

    public void setMetaEstrategica(PetCatMetaEstrategica metaEstrategica) {
        this.metaEstrategica = metaEstrategica;
    }
}
