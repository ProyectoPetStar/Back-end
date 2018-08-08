package org.petstar.dto.ishikawa;

/**
 *
 * @author Tech-Pro
 */
public class PetVerificacion {
    private int id_verificacion;
    private int id_plan;
    private String efectiva;
    private String porque;

    public int getId_verificacion() {
        return id_verificacion;
    }

    public void setId_verificacion(int id_verificacion) {
        this.id_verificacion = id_verificacion;
    }

    public int getId_plan() {
        return id_plan;
    }

    public void setId_plan(int id_plan) {
        this.id_plan = id_plan;
    }

    public String getEfectiva() {
        return efectiva;
    }

    public void setEfectiva(String efectiva) {
        this.efectiva = efectiva;
    }

    public String getPorque() {
        return porque;
    }

    public void setPorque(String porque) {
        this.porque = porque;
    }
}
