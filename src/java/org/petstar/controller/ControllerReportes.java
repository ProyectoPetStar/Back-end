package org.petstar.controller;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.petstar.dao.CatalogosDAO;
import org.petstar.dao.LineasDAO;
import org.petstar.dao.RazonParoDAO;
import org.petstar.dto.CatalogosDTO;
import org.petstar.dto.RazonParoDTO;
import org.petstar.model.OutputJson;
import org.petstar.model.ReportesResponseJson;
import org.petstar.model.ResponseJson;
import static org.petstar.configurations.utils.convertStringToSql;
import static org.petstar.configurations.utils.getTotalHoras;
import static org.petstar.configurations.utils.getPorcentajeParo;
import org.petstar.dao.ReportesDAO;
import org.petstar.dto.Fuentes;
import org.petstar.dto.ResultBigDecimal;
import org.petstar.dto.UserDTO;

/**
 * @author Tech-Pro
 */
public class ControllerReportes {
    private static final String TABLE_FUENTES = "pet_cat_fuentes_paro";
    private static final String MSG_SUCESS = "OK";
    private static final String MSG_LOGOUT = "Inicie sesión nuevamente";
    private static final String MSG_ERROR  = "Descripción de error: ";
    
    public OutputJson getOEEFallasByLinea(HttpServletRequest request) throws Exception{
        String fechaIn = request.getParameter("fecha_inicio");
        String fechaTe = request.getParameter("fecha_termino");
        int idLInea = Integer.parseInt(request.getParameter("id_linea"));
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        
        try{
            UserDTO sesion = autenticacion.isValidToken(request);
            if(sesion != null){
                CatalogosDAO catalogosDAO = new CatalogosDAO();
                RazonParoDAO razonParoDAO = new RazonParoDAO();
                ReportesResponseJson data = new ReportesResponseJson();

                List<CatalogosDTO> listFuentes = new ArrayList<>();
                listFuentes = catalogosDAO.getCatalogosActive(TABLE_FUENTES);
                List<HashMap> listOEEFallas = new ArrayList<>();
                BigDecimal tiempoDisponible = getTotalHoras(
                        convertStringToSql(fechaIn), convertStringToSql(fechaTe));
                BigDecimal totalGeneral = new BigDecimal(BigInteger.ZERO);

                for(CatalogosDTO fuente:listFuentes){
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("padre", 1);
                    map.put("fuente", fuente.getValor());
                    listOEEFallas.add(map);

                    List<RazonParoDTO> listRazones = new ArrayList<>();
                    listRazones = razonParoDAO.getFallasByOEE(
                            convertStringToSql(fechaIn), 
                            convertStringToSql(fechaTe), idLInea, fuente.getId());
                    BigDecimal totalParcial = new BigDecimal(BigInteger.ZERO);

                    for(RazonParoDTO razon:listRazones){
                        HashMap<String, Object> raz = new HashMap<>();
                        raz.put("padre", 0);
                        raz.put("fuente", razon.getValor());
                        raz.put("hrs", razon.getSuma_tiempo_paro());
                        raz.put("porcentaje", getPorcentajeParo(
                                razon.getSuma_tiempo_paro(), tiempoDisponible));
                        listOEEFallas.add(raz);

                        totalParcial = totalParcial.add(razon.getSuma_tiempo_paro());
                        totalGeneral = totalGeneral.add(razon.getSuma_tiempo_paro());
                        map.put("hrs", totalParcial);
                        map.put("porcentaje", getPorcentajeParo(totalParcial, tiempoDisponible));
                    }
                }
            
                HashMap<String, Object> mapa = new HashMap<>();
                mapa.put("padre", 2);
                mapa.put("fuente", "Total");
                mapa.put("hrs", totalGeneral);
                mapa.put("porcentaje", getPorcentajeParo(totalGeneral, tiempoDisponible));
                listOEEFallas.add(mapa);
                data.setListaOEEFallas(listOEEFallas);

                output.setData(data);
                response.setMessage(MSG_SUCESS);
                response.setSucessfull(true);
            }else{
                response.setMessage(MSG_LOGOUT);
                response.setSucessfull(false);
            }
        }catch (Exception ex){
            response.setSucessfull(false);
            response.setMessage(MSG_ERROR + ex.getMessage());
        }
        output.setResponse(response);
        return output;
    }
    
    public OutputJson loadCombobox(HttpServletRequest request){
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        
        try{
            UserDTO sesion = autenticacion.isValidToken(request);
            if(sesion != null){
                LineasDAO lineasDAO = new LineasDAO();
                ReportesResponseJson data = new ReportesResponseJson();

                data.setListLineas(lineasDAO.getLineasActive());
                output.setData(data);

                response.setSucessfull(true);
                response.setMessage(MSG_SUCESS);
            }else{
                response.setSucessfull(false);
                response.setMessage(MSG_LOGOUT);
            }
        }catch(Exception ex){
            response.setSucessfull(false);
            response.setMessage(MSG_ERROR + ex.getMessage());
        }
        output.setResponse(response);
        return output;
    }
    
    public OutputJson getReporteEficiencia(HttpServletRequest request) throws Exception{
        String fechaIn = request.getParameter("fecha_inicio");
        String fechaTe = request.getParameter("fecha_termino");
        int idLInea = Integer.parseInt(request.getParameter("id_linea"));
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ReportesResponseJson data = new ReportesResponseJson();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        
        try{
            UserDTO sesion = autenticacion.isValidToken(request);
            if(sesion != null){
                ReportesDAO reportesDAO = new ReportesDAO();
                Date fechaInicio = convertStringToSql(fechaIn);
                Date fechaTermino = convertStringToSql(fechaTe);
                BigDecimal tiempoDisponibleTotal = getTotalHoras(fechaInicio, fechaTermino);
                
                List<HashMap> reporte = new ArrayList<>();
                HashMap<String, Object> map0 = new HashMap<>();
                map0.put("padre", 1);
                map0.put("titulo", "Titulo");
                map0.put("hrs", "Hrs.");
                map0.put("porcentaje", "%");
                reporte.add(map0);
                HashMap<String, Object> map1 = new HashMap<>();
                map1.put("padre", 0);
                map1.put("titulo", "Tiempo Disponible Total");
                map1.put("hrs", tiempoDisponibleTotal);
                map1.put("porcentaje", 100);
                reporte.add(map1);
                HashMap<String, Object> map2 = new HashMap<>();
                map2.put("padre", 0);
                map2.put("titulo", "No Ventas");
                map2.put("hrs", 0);
                map2.put("porcentaje", 0);
                reporte.add(map2);
                HashMap<String, Object> map3 = new HashMap<>();
                map3.put("padre", 0);
                map3.put("titulo", "Tiempo Disponible");
                map3.put("hrs", tiempoDisponibleTotal);
                map3.put("porcentaje", 100);
                reporte.add(map3);
                BigDecimal totalHoraParo = new BigDecimal(BigInteger.ZERO);
                BigDecimal desempenoEfec = new BigDecimal(BigInteger.ZERO);
                List<Fuentes> listFuentes = reportesDAO.getFuentes(idLInea, fechaInicio, fechaTermino);
                ResultBigDecimal prodBuena = reportesDAO.getProduccionBuena(idLInea, fechaInicio, fechaTermino);
                ResultBigDecimal subProduc = reportesDAO.getSumaSubProductos(idLInea, fechaInicio, fechaTermino);
                for(Fuentes fuente:listFuentes){
                    HashMap<String, Object> map4 = new HashMap<>();
                    map4.put("padre", 0);
                    map4.put("titulo", fuente.getValor());
                    map4.put("hrs", fuente.getHrs());
                    map4.put("porcentaje", getPorcentajeParo(fuente.getHrs(), tiempoDisponibleTotal));
                    reporte.add(map4);
                    desempenoEfec = desempenoEfec.add(fuente.getHrs());
                    if(fuente.getId() == 1 || fuente.getId() == 2){
                        totalHoraParo = totalHoraParo.add(fuente.getHrs());
                    }
                }
                HashMap<String, Object> map5 = new HashMap<>();
                map5.put("padre", 0);
                map5.put("titulo", "Desempeño Efectivo Total de Equipos");
                map5.put("hrs", desempenoEfec);
                map5.put("porcentaje", getPorcentajeParo(desempenoEfec, tiempoDisponibleTotal));
                reporte.add(map5);
                HashMap<String, Object> map6 = new HashMap<>();
                map6.put("padre", 0);
                map6.put("titulo", "Total Hora de Paro");
                map6.put("hrs", totalHoraParo);
                map6.put("porcentaje", getPorcentajeParo(totalHoraParo, tiempoDisponibleTotal));
                reporte.add(map6);
                HashMap<String, Object> map12 = new HashMap<>();
                map12.put("padre", 1);
                map12.put("titulo", "Datos de Producción");
                map12.put("hrs", "");
                map12.put("porcentaje", "");
                reporte.add(map12);
                HashMap<String, Object> map7 = new HashMap<>();
                map7.put("padre", 0);
                map7.put("titulo", "Velocidad Ideal (Hora)");
                map7.put("hrs", 3.5);
                map7.put("porcentaje", "");
                reporte.add(map7);
                HashMap<String, Object> map8 = new HashMap<>();
                map8.put("padre", 0);
                map8.put("titulo", "Capacidad Productiva (Turno)");
                map8.put("hrs", (3.5 * 8));
                map8.put("porcentaje", "");
                reporte.add(map8);
                reporte.add(map3);
                HashMap<String, Object> map9 = new HashMap<>();
                map9.put("padre", 0);
                map9.put("titulo", "Tiempo de Operación");
                map9.put("hrs", (tiempoDisponibleTotal.subtract(totalHoraParo)));
                map9.put("porcentaje", "");
                reporte.add(map9);
                HashMap<String, Object> map10 = new HashMap<>();
                map10.put("padre", 0);
                map10.put("titulo", "Producción Buena");
                map10.put("hrs", prodBuena.getResult());
                map10.put("porcentaje", "");
                reporte.add(map10);
                HashMap<String, Object> map11 = new HashMap<>();
                map11.put("padre", 0);
                map11.put("titulo", "Producción Total");
                map11.put("hrs", prodBuena.getResult().add(subProduc.getResult()));
                map11.put("porcentaje", "");
                reporte.add(map11);
                
                data.setListaOEEFallas(reporte);
                output.setData(data);
                response.setMessage(MSG_SUCESS);
                response.setSucessfull(true);
            }else{
                response.setMessage(MSG_LOGOUT);
                response.setSucessfull(false);
            }
        }catch (Exception ex){
            response.setSucessfull(false);
            response.setMessage(MSG_ERROR + ex.getMessage());
        }
        output.setResponse(response);
        return output;
    }
}