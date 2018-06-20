package org.petstar.model;

import java.util.HashMap;
import java.util.List;
import org.petstar.dto.FallasDTO;
import org.petstar.dto.GposLineaDTO;
import org.petstar.dto.LineasDTO;
import org.petstar.dto.PeriodosDTO;

/**
 * @author Tech-Pro
 */
public class ReportesResponseJson {
    private List<HashMap> listaOEEFallas;
    private List<LineasDTO> listLineas;
    private List<GposLineaDTO> listGposLineas;
    private List<PeriodosDTO> listPeriodos;
    private List<HashMap> reporteDisponibilidad;
    private List<HashMap> datosProduccion;
    private List<HashMap> reporteOEE;
    private List<HashMap> reporteDiario;
    private List<HashMap> reporteMap;
    private List<HashMap> graficaMap;
    private List<HashMap> reporteDesempeno;
    private List<List<HashMap>> reporteDailyPerformance;
    private List<FallasDTO> listFallas;

    public List<HashMap> getListaOEEFallas() {
        return listaOEEFallas;
    }

    public void setListaOEEFallas(List<HashMap> listaOEEFallas) {
        this.listaOEEFallas = listaOEEFallas;
    }

    public List<LineasDTO> getListLineas() {
        return listLineas;
    }

    public void setListLineas(List<LineasDTO> listLineas) {
        this.listLineas = listLineas;
    }

    public List<PeriodosDTO> getListPeriodos() {
        return listPeriodos;
    }

    public List<GposLineaDTO> getListGposLineas() {
        return listGposLineas;
    }

    public void setListGposLineas(List<GposLineaDTO> listGposLineas) {
        this.listGposLineas = listGposLineas;
    }

    public void setListPeriodos(List<PeriodosDTO> listPeriodos) {
        this.listPeriodos = listPeriodos;
    }

    public List<HashMap> getReporteDisponibilidad() {
        return reporteDisponibilidad;
    }

    public void setReporteDisponibilidad(List<HashMap> reporteDisponibilidad) {
        this.reporteDisponibilidad = reporteDisponibilidad;
    }
    
    public List<HashMap> getDatosProduccion() {
        return datosProduccion;
    }

    public void setDatosProduccion(List<HashMap> datosProduccion) {
        this.datosProduccion = datosProduccion;
    }

    public List<HashMap> getReporteOEE() {
        return reporteOEE;
    }

    public void setReporteOEE(List<HashMap> reporteOEE) {
        this.reporteOEE = reporteOEE;
    }

    public List<HashMap> getReporteDiario() {
        return reporteDiario;
    }

    public void setReporteDiario(List<HashMap> reporteDiario) {
        this.reporteDiario = reporteDiario;
    }

    public List<List<HashMap>> getReporteDailyPerformance() {
        return reporteDailyPerformance;
    }

    public void setReporteDailyPerformance(List<List<HashMap>> reporteDailyPerformance) {
        this.reporteDailyPerformance = reporteDailyPerformance;
    }
    
    public List<HashMap> getReporteMap() {
        return reporteMap;
    }

    public void setReporteMap(List<HashMap> reporteMap) {
        this.reporteMap = reporteMap;
    }

    public List<HashMap> getGraficaMap() {
        return graficaMap;
    }

    public void setGraficaMap(List<HashMap> graficaMap) {
        this.graficaMap = graficaMap;
    }

    public List<HashMap> getReporteDesempeno() {
        return reporteDesempeno;
    }

    public void setReporteDesempeno(List<HashMap> reporteDesempeno) {
        this.reporteDesempeno = reporteDesempeno;
    }

    public List<FallasDTO> getListFallas() {
        return listFallas;
    }

    public void setListFallas(List<FallasDTO> listFallas) {
        this.listFallas = listFallas;
    }
}
