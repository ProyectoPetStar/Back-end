package org.petstar.controller;

import java.math.BigDecimal;
import java.math.BigInteger;
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
}