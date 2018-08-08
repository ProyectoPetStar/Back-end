package org.petstar.dto.ETAD;

/**
 *
 * @author Tech-Pro
 */
public class PetPonderacionKpiOperativo {
    private int id_ponderacion_kpi_operativo;
    private int anio;
    private int ponderacion;
    private int id_kpi_etad;
    private PetEtadKpi petEtadKpi;

    public int getId_ponderacion_kpi_operativo() {
        return id_ponderacion_kpi_operativo;
    }

    public void setId_ponderacion_kpi_operativo(int id_ponderacion_kpi_operativo) {
        this.id_ponderacion_kpi_operativo = id_ponderacion_kpi_operativo;
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

    public int getId_kpi_etad() {
        return id_kpi_etad;
    }

    public void setId_kpi_etad(int id_kpi_etad) {
        this.id_kpi_etad = id_kpi_etad;
    }

    public PetEtadKpi getPetEtadKpi() {
        return petEtadKpi;
    }

    public void setPetEtadKpi(PetEtadKpi petEtadKpi) {
        this.petEtadKpi = petEtadKpi;
    }
}
