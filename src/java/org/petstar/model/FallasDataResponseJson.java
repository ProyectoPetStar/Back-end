/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.petstar.model;

import java.util.List;
import org.petstar.dto.FallasDTO;

/**
 *
 * @author Tech-Pro
 */
public class FallasDataResponseJson {
    private FallasDTO fallasDTO;
    private List<FallasDTO> listFallas;

    public FallasDTO getFallasDTO() {
        return fallasDTO;
    }

    public void setFallasDTO(FallasDTO fallasDTO) {
        this.fallasDTO = fallasDTO;
    }

    public List<FallasDTO> getListFallas() {
        return listFallas;
    }

    public void setListFallas(List<FallasDTO> listFallas) {
        this.listFallas = listFallas;
    }
    
}
