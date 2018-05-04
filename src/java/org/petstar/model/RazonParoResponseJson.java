package org.petstar.model;

import java.util.List;
import org.petstar.dto.CatalogosDTO;
import org.petstar.dto.RazonParoDTO;

/**
 *
 * @author Tech-Pro
 */
public class RazonParoResponseJson {
    private RazonParoDTO razonParo;
    private List<RazonParoDTO> listRazonParo;
    private List<CatalogosDTO> listFuentesParo;

    public RazonParoDTO getRazonParo() {
        return razonParo;
    }

    public void setRazonParo(RazonParoDTO razonParo) {
        this.razonParo = razonParo;
    }

    public List<RazonParoDTO> getListRazonParo() {
        return listRazonParo;
    }

    public void setListRazonParo(List<RazonParoDTO> listRazonParo) {
        this.listRazonParo = listRazonParo;
    }

    public List<CatalogosDTO> getListFuentesParo() {
        return listFuentesParo;
    }

    public void setListFuentesParo(List<CatalogosDTO> listFuentesParo) {
        this.listFuentesParo = listFuentesParo;
    }
}
