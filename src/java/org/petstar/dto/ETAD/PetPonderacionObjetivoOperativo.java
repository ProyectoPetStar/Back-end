package org.petstar.dto.ETAD;

/**
 *
 * @author Tech-Pro
 */
public class PetPonderacionObjetivoOperativo {
    private int id_ponderacion_obj_operativo;
    private int anio;
    private int ponderacion;
    private int id_objetivo_operativo;

    public int getId_ponderacion_obj_operativo() {
        return id_ponderacion_obj_operativo;
    }

    public void setId_ponderacion_obj_operativo(int id_ponderacion_obj_operativo) {
        this.id_ponderacion_obj_operativo = id_ponderacion_obj_operativo;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public int getPonderacion() {
        return ponderacion;
    }

    public void setPonderacion(int ponderacion) {
        this.ponderacion = ponderacion;
    }

    public int getId_objetivo_operativo() {
        return id_objetivo_operativo;
    }

    public void setId_objetivo_operativo(int id_objetivo_operativo) {
        this.id_objetivo_operativo = id_objetivo_operativo;
    }
}
