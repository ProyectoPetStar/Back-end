package org.petstar.dto.ETAD;

/**
 *
 * @author Tech-Pro
 */
public class PetEtadKpi {
    private int id_kpi_etad;
    private int id_kpi_operativo;
    private int id_etad;
    private PetCatKpiOperativo kpiOperativo;

    public int getId_kpi_etad() {
        return id_kpi_etad;
    }

    public void setId_kpi_etad(int id_kpi_etad) {
        this.id_kpi_etad = id_kpi_etad;
    }

    public int getId_kpi_operativo() {
        return id_kpi_operativo;
    }

    public void setId_kpi_operativo(int id_kpi_operativo) {
        this.id_kpi_operativo = id_kpi_operativo;
    }

    public int getId_etad() {
        return id_etad;
    }

    public void setId_etad(int id_etad) {
        this.id_etad = id_etad;
    }

    public PetCatKpiOperativo getKpiOperativo() {
        return kpiOperativo;
    }

    public void setKpiOperativo(PetCatKpiOperativo kpiOperativo) {
        this.kpiOperativo = kpiOperativo;
    }
}
