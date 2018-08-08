package org.petstar.dto.ishikawa;

/**
 *
 * @author Tech-Pro
 */
public class PetPorques {
    private int id_porque;
    private int id_idea;
    private String porque_uno;
    private String porque_dos;
    private String porque_tres;
    private String porque_cuatro;
    private String porque_cinco;
    private PetPlanAccion planAccion;

    public int getId_porque() {
        return id_porque;
    }

    public void setId_porque(int id_porque) {
        this.id_porque = id_porque;
    }

    public int getId_idea() {
        return id_idea;
    }

    public void setId_idea(int id_idea) {
        this.id_idea = id_idea;
    }

    public String getPorque_uno() {
        return porque_uno;
    }

    public void setPorque_uno(String porque_uno) {
        this.porque_uno = porque_uno;
    }

    public String getPorque_dos() {
        return porque_dos;
    }

    public void setPorque_dos(String porque_dos) {
        this.porque_dos = porque_dos;
    }

    public String getPorque_tres() {
        return porque_tres;
    }

    public void setPorque_tres(String porque_tres) {
        this.porque_tres = porque_tres;
    }

    public String getPorque_cuatro() {
        return porque_cuatro;
    }

    public void setPorque_cuatro(String porque_cuatro) {
        this.porque_cuatro = porque_cuatro;
    }

    public String getPorque_cinco() {
        return porque_cinco;
    }

    public void setPorque_cinco(String porque_cinco) {
        this.porque_cinco = porque_cinco;
    }

    public PetPlanAccion getPlanAccion() {
        return planAccion;
    }

    public void setPlanAccion(PetPlanAccion planAccion) {
        this.planAccion = planAccion;
    }
}
