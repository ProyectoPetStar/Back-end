package org.petstar.dto.ishikawa;

import java.sql.Date;

/**
 *
 * @author Tech-Pro
 */
public class PetPlanAccion {
    private int id_plan;
    private String accion;
    private String responsable;
    private Date fecha;
    private String fecha_string;
    private int id_porque;
    private String efectiva;
    private String porque;

    public int getId_plan() {
        return id_plan;
    }

    public void setId_plan(int id_plan) {
        this.id_plan = id_plan;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getFecha_string() {
        return fecha_string;
    }

    public void setFecha_string(String fecha_string) {
        this.fecha_string = fecha_string;
    }

    public int getId_porque() {
        return id_porque;
    }

    public void setId_porque(int id_porque) {
        this.id_porque = id_porque;
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
