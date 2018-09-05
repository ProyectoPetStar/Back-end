package org.petstar.model;

import java.util.HashMap;
import java.util.List;
import org.petstar.dto.ETAD.Posiciones;

/**
 *
 * @author Tech-Pro
 */
public class VideoWallResponse {
    private List<List<HashMap>> OEE;
    private List<List<HashMap>> ETAD;
    private List<Posiciones> posicionAnual;
    private List<Posiciones> posicionTrimestral;

    public List<List<HashMap>> getOEE() {
        return OEE;
    }

    public void setOEE(List<List<HashMap>> OEE) {
        this.OEE = OEE;
    }

    public List<List<HashMap>> getETAD() {
        return ETAD;
    }

    public void setETAD(List<List<HashMap>> ETAD) {
        this.ETAD = ETAD;
    }

    public List<Posiciones> getPosicionAnual() {
        return posicionAnual;
    }

    public void setPosicionAnual(List<Posiciones> posicionAnual) {
        this.posicionAnual = posicionAnual;
    }

    public List<Posiciones> getPosicionTrimestral() {
        return posicionTrimestral;
    }

    public void setPosicionTrimestral(List<Posiciones> posicionTrimestral) {
        this.posicionTrimestral = posicionTrimestral;
    }
}
