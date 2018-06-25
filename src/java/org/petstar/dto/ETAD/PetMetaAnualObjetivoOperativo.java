package org.petstar.dto.ETAD;

import java.math.BigDecimal;
import org.petstar.dto.LineasDTO;

/**
 *
 * @author Tech-Pro
 */
public class PetMetaAnualObjetivoOperativo {
    private int id_meta_anual_objetivo_operativo;
    private int id_linea;
    private int id_objetivo_operativo;
    private int anio;
    private BigDecimal valor;
    private LineasDTO linea;
    private PetCatObjetivoOperativo objetivoOperativo;

    public int getId_meta_anual_objetivo_operativo() {
        return id_meta_anual_objetivo_operativo;
    }

    public void setId_meta_anual_objetivo_operativo(int id_meta_anual_objetivo_operativo) {
        this.id_meta_anual_objetivo_operativo = id_meta_anual_objetivo_operativo;
    }

    public int getId_linea() {
        return id_linea;
    }

    public void setId_linea(int id_linea) {
        this.id_linea = id_linea;
    }

    public int getId_objetivo_operativo() {
        return id_objetivo_operativo;
    }

    public void setId_objetivo_operativo(int id_objetivo_operativo) {
        this.id_objetivo_operativo = id_objetivo_operativo;
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

    public PetCatObjetivoOperativo getObjetivoOperativo() {
        return objetivoOperativo;
    }

    public void setObjetivoOperativo(PetCatObjetivoOperativo objetivoOperativo) {
        this.objetivoOperativo = objetivoOperativo;
    }
}
