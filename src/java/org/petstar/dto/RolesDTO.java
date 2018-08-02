
package org.petstar.dto;

/**
 *
 * @author Tech-Pro
 */
public class RolesDTO extends CatalogosDTO{
    private int id_gpo_rol;
    private String valor_gpo_rol;

    public int getId_gpo_rol() {
        return id_gpo_rol;
    }

    public void setId_gpo_rol(int id_gpo_rol) {
        this.id_gpo_rol = id_gpo_rol;
    }

    public String getValor_gpo_rol() {
        return valor_gpo_rol;
    }

    public void setValor_gpo_rol(String valor_gpo_rol) {
        this.valor_gpo_rol = valor_gpo_rol;
    }
}
