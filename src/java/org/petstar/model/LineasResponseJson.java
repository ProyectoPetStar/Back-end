/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.petstar.model;

import java.util.List;
import org.petstar.dto.LineasDTO;

/**
 *
 * @author Tech-Pro
 */
public class LineasResponseJson extends ResponseJson{
    private LineasDTO lineasDTO;
    private List<LineasDTO> listLineasDTO;

    public LineasDTO getLineasDTO() {
        return lineasDTO;
    }

    public void setLineasDTO(LineasDTO lineasDTO) {
        this.lineasDTO = lineasDTO;
    }

    public List<LineasDTO> getListLineasDTO() {
        return listLineasDTO;
    }

    public void setListLineasDTO(List<LineasDTO> listLineasDTO) {
        this.listLineasDTO = listLineasDTO;
    }
    
}
