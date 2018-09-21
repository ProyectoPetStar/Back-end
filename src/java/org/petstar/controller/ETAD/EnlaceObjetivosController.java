package org.petstar.controller.ETAD;

import com.google.gson.Gson;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONObject;
import static org.petstar.configurations.utils.getPorcentajeParo;
import static org.petstar.configurations.utils.getTotalHoras;
import static org.petstar.configurations.utils.sumarFechasDias;
import org.petstar.controller.ControllerAutenticacion;
import org.petstar.dao.ETAD.EnlaceObjetivosDAO;
import org.petstar.dao.LineasDAO;
import org.petstar.dao.PeriodosDAO;
import org.petstar.dao.ReportesDAO;
import org.petstar.dto.ETAD.ObjetivosREOEKO;
import org.petstar.dto.ETAD.PetReporteEnlace;
import org.petstar.dto.ETAD.ReporteEnlaceDetail;
import org.petstar.dto.Fuentes;
import org.petstar.dto.LineasDTO;
import org.petstar.dto.PeriodosDTO;
import org.petstar.dto.ResultBigDecimal;
import org.petstar.dto.ResultInteger;
import org.petstar.dto.ResultSQLDate;
import org.petstar.dto.UserDTO;
import org.petstar.model.ETAD.EnlaceObjetivosResponse;
import org.petstar.model.OutputJson;
import org.petstar.model.ResponseJson;

/**
 *
 * @author Tech-Pro
 */
public class EnlaceObjetivosController {
    private static final String MSG_SUCESS = "OK";
    private static final String MSG_LOGOUT = "Inicie sesión nuevamente";
    private static final String MSG_ERROR  = "Descripción de error: ";
    private static final String MSG_EXIST  = "Ya existen datos para este periodo.";
    private static final String MSG_NOEXIST= "No existen datos para este periodo.";
    
    /**
     * Carga de Combos
     * Servicio que se encarga del llenado de las listas que se utilizaran
     * para los combos que se utilicen en el registro de catalogos
     * @param request
     * @return 
     */
    public OutputJson loadCombobox(HttpServletRequest request){
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
            
        try{
            UserDTO session = autenticacion.isValidToken(request);
            if(session != null){
                EnlaceObjetivosResponse data = new EnlaceObjetivosResponse();
                PeriodosDAO periodosDAO = new PeriodosDAO();
                data.setListPeriodos(periodosDAO.getAllPeriodos());
                output.setData(data);
                response.setMessage(MSG_SUCESS);
                response.setSucessfull(true);
            }else{
                response.setMessage(MSG_LOGOUT);
                response.setSucessfull(false);
            }
        }catch(Exception ex){
            response.setMessage(MSG_ERROR + ex.getMessage());
            response.setSucessfull(false);
        }
        
        output.setResponse(response);
        return output;
    }
    
    public OutputJson getConfiguracion(HttpServletRequest request){
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
            
        try{
            int idPeriodo = Integer.valueOf(request.getParameter("id_periodo"));
            UserDTO session = autenticacion.isValidToken(request);
            if(session != null){
                EnlaceObjetivosResponse data = new EnlaceObjetivosResponse();
                EnlaceObjetivosDAO eObjetivosDAO = new EnlaceObjetivosDAO();
                
                data.setReporteEnlace(eObjetivosDAO.getConfiguracionByPeriodo(idPeriodo));
                if(data.getReporteEnlace() == null){
                    PetReporteEnlace reporteEnlace = new PetReporteEnlace(idPeriodo);
                    data.setReporteEnlace(reporteEnlace);
                }
                output.setData(data);
                response.setMessage(MSG_SUCESS);
                response.setSucessfull(true);
            }else{
                response.setMessage(MSG_LOGOUT);
                response.setSucessfull(false);
            }
        }catch(Exception ex){
            response.setMessage(MSG_ERROR + ex.getMessage());
            response.setSucessfull(false);
        }
        
        output.setResponse(response);
        return output;
    }
    
    public OutputJson insertConfiguracion(HttpServletRequest request){
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        Gson gson = new Gson();
            
        try{
            String jsonString = request.getParameter("record");
            JSONObject jsonResponse = new JSONObject(jsonString);
            UserDTO session = autenticacion.isValidToken(request);
            if(session != null){
                EnlaceObjetivosDAO objetivosDAO = new EnlaceObjetivosDAO();
                PetReporteEnlace reporteEnlace = gson.fromJson(jsonResponse.
                                getJSONObject("record").toString(), PetReporteEnlace.class);
                
                ResultInteger result = objetivosDAO.validateExistConfiguracionEnlace(reporteEnlace.getId_periodo());
                if(result.getResult().equals(0)){
                    ResultInteger id = objetivosDAO.insertConfiguracionEnlace(reporteEnlace);
                    response.setMessage(id.getResult().toString());
                    response.setSucessfull(true);
                }else{
                    response.setMessage(MSG_EXIST);
                    response.setSucessfull(true);
                }
            }else{
                response.setMessage(MSG_LOGOUT);
                response.setSucessfull(false);
            }
        }catch(Exception ex){
            response.setMessage(MSG_ERROR + ex.getMessage());
            response.setSucessfull(false);
        }
        
        output.setResponse(response);
        return output;
    }
    
    public OutputJson updateConfiguracion(HttpServletRequest request){
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        Gson gson = new Gson();
            
        try{
            String jsonString = request.getParameter("record");
            JSONObject jsonResponse = new JSONObject(jsonString);
            UserDTO session = autenticacion.isValidToken(request);
            if(session != null){
                EnlaceObjetivosDAO objetivosDAO = new EnlaceObjetivosDAO();
                PetReporteEnlace reporteEnlace = gson.fromJson(jsonResponse.
                                getJSONObject("record").toString(), PetReporteEnlace.class);
                
                objetivosDAO.updateConfiguracionEnlace(reporteEnlace);
                response.setMessage(MSG_SUCESS);
                response.setSucessfull(true);                
            }else{
                response.setMessage(MSG_LOGOUT);
                response.setSucessfull(false);
            }
        }catch(Exception ex){
            response.setMessage(MSG_ERROR + ex.getMessage());
            response.setSucessfull(false);
        }
        
        output.setResponse(response);
        return output;
    }
    
    public OutputJson getReporteEnlaceObjetivos(HttpServletRequest request){
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        
        try{
            int idPeriodo = Integer.valueOf(request.getParameter("id_periodo"));
            UserDTO session = autenticacion.isValidToken(request);
            if(session != null){
                EnlaceObjetivosResponse data = new EnlaceObjetivosResponse();
                EnlaceObjetivosDAO eObjetivosDAO = new EnlaceObjetivosDAO();
                
                ResultInteger result = eObjetivosDAO.validateExistConfiguracionEnlace(idPeriodo);
                if(!result.getResult().equals(0)){
                    data.setReporteEnlace(this.getBuilReporteEnlaceObjetivos(idPeriodo));
                    output.setData(data);
                    response.setMessage(MSG_SUCESS);
                    response.setSucessfull(true);
                }else{
                    response.setMessage(MSG_NOEXIST);
                    response.setSucessfull(false);
                }
            }else{
                response.setMessage(MSG_LOGOUT);
                response.setSucessfull(false);
            }
        }catch(Exception ex){
            response.setMessage(MSG_ERROR + ex.getMessage());
            response.setSucessfull(false);
        }
        
        output.setResponse(response);
        return output;
    }
    
    public PetReporteEnlace getBuilReporteEnlaceObjetivos(int idPeriodo){
        PetReporteEnlace reporte = null;
        
        EnlaceObjetivosDAO eObjetivosDAO = new EnlaceObjetivosDAO();
        PeriodosDAO periodosDAO = new PeriodosDAO();
        LineasDAO lineasDAO = new LineasDAO();
        
        try{
            reporte = new PetReporteEnlace();
            reporte = eObjetivosDAO.getConfiguracionByPeriodo(idPeriodo);
            reporte.setEficienciaProcesos(eObjetivosDAO.getEficienciaProcesos(idPeriodo));
            PeriodosDTO periodo = periodosDAO.getPeriodoById(idPeriodo);
            List<LineasDTO> listLineas = lineasDAO.getLineasActive();

            for (LineasDTO linea : listLineas) {
                switch (linea.getId_linea()) {
                    case 1:
                        reporte.setObjetivosAmut1(this.getReporteEficiencia(idPeriodo, linea.getId_linea()));
                        List<ReporteEnlaceDetail> listValA1 = eObjetivosDAO.getDetailsReporte(idPeriodo, linea.getId_linea());
                        List<ResultBigDecimal> listRealA1 = eObjetivosDAO.getRealKpi(periodo, linea.getId_linea());
                        reporte.setKpisAmut1(this.fusionDeListas(listValA1, listRealA1));
                        break;
                    case 2:
                        reporte.setObjetivosAmut2(this.getReporteEficiencia(idPeriodo, linea.getId_linea()));
                        List<ReporteEnlaceDetail> listValA2 = eObjetivosDAO.getDetailsReporte(idPeriodo, linea.getId_linea());
                        List<ResultBigDecimal> listRealA2 = eObjetivosDAO.getRealKpi(periodo, linea.getId_linea());
                        reporte.setKpisAmut2(this.fusionDeListas(listValA2, listRealA2));
                        break;
                    case 3:
                        reporte.setObjetivosEx1(this.getReporteEficiencia(idPeriodo, linea.getId_linea()));
                        List<ReporteEnlaceDetail> listValE1 = eObjetivosDAO.getDetailsReporte(idPeriodo, linea.getId_linea());
                        List<ResultBigDecimal> listRealE1 = eObjetivosDAO.getRealKpi(periodo, linea.getId_linea());
                        reporte.setKpisEx1(this.fusionDeListas(listValE1, listRealE1));
                        break;
                    case 4:
                        reporte.setObjetivosEx2(this.getReporteEficiencia(idPeriodo, linea.getId_linea()));
                        List<ReporteEnlaceDetail> listValE2 = eObjetivosDAO.getDetailsReporte(idPeriodo, linea.getId_linea());
                        List<ResultBigDecimal> listRealE2 = eObjetivosDAO.getRealKpi(periodo, linea.getId_linea());
                        reporte.setKpisEx2(this.fusionDeListas(listValE2, listRealE2));
                        break;
                    case 5:
                        reporte.setObjetivosSSP(this.getReporteEficiencia(idPeriodo, linea.getId_linea()));
                        List<ReporteEnlaceDetail> listValSSP = eObjetivosDAO.getDetailsReporte(idPeriodo, linea.getId_linea());
                        List<ResultBigDecimal> listRealSSP = eObjetivosDAO.getRealKpi(periodo, linea.getId_linea());
                        reporte.setKpisSSP(this.fusionDeListas(listValSSP, listRealSSP));
                        break;
                }
            }
        }catch(Exception e) {
            e.getMessage();
        }
        
        return reporte;
    }
    
    private List<ObjetivosREOEKO> getReporteEficiencia(int idPeriodo, int idLinea) throws Exception{
        List<ObjetivosREOEKO> reporteOEE = new ArrayList<>();
        PeriodosDAO periodosDAO = new PeriodosDAO();
        PeriodosDTO periodo = periodosDAO.getPeriodoById(idPeriodo, idLinea);
        ReportesDAO reportesDAO = new ReportesDAO();
        ResultSQLDate fecIni = reportesDAO.getFirstDateofPeriodo(periodo.getMes(), periodo.getAnio(), idLinea);
        ResultSQLDate fecTer = reportesDAO.getLastDateofPeriodo(periodo.getMes(), periodo.getAnio(), idLinea);
                    
        if(fecIni != null && fecTer != null){
            Date fechaInicio = sumarFechasDias(fecIni.getResult(), 2);
            Date fechaTermino = sumarFechasDias(fecTer.getResult(), 2);
            BigDecimal tiempoDisponibleTotal = getTotalHoras(fechaInicio, fechaTermino);
            ResultBigDecimal subproductos = reportesDAO.getTotalSubProducto(fechaInicio, fechaTermino, idLinea);
            List<Fuentes> listFuentes = reportesDAO.getFuentes(idLinea, fechaInicio, fechaTermino);
            ResultBigDecimal prodBuena = reportesDAO.getProduccionBuena(idLinea, fechaInicio, fechaTermino);
            ResultBigDecimal subProduc = reportesDAO.getSumaSubProductos(idLinea, fechaInicio, fechaTermino);
            BigDecimal produccionTotal = prodBuena.getResult().add(subProduc.getResult());
            BigDecimal reduccionVelocidad = BigDecimal.ZERO;
            BigDecimal desempenoEfec = BigDecimal.ZERO;
            BigDecimal totalHoraParo = BigDecimal.ZERO;
            BigDecimal porCalidad = BigDecimal.ZERO;
            for(Fuentes fuente:listFuentes){
                if(fuente.getValor().equals("Reducción de velocidad") || fuente.getId() == 3){
                    reduccionVelocidad = fuente.getHrs();
                }
                if(fuente.getId() == 1 || fuente.getId() == 2){
                    totalHoraParo = totalHoraParo.add(fuente.getHrs());
                }
                if(fuente.getValor().equals("Por Calidad") || fuente.getId() == 4){
                    if(subproductos.getResult().compareTo(BigDecimal.ZERO) != 0){
                        BigDecimal subproducto = subproductos.getResult();
                        subproducto = subproducto.divide(new BigDecimal(3.5), RoundingMode.CEILING);
                        porCalidad = subproducto.add(fuente.getHrs());
                    }
                }
                desempenoEfec = desempenoEfec.add(fuente.getHrs()).add(porCalidad);
            }
                        
            BigDecimal tiempoDisponible = tiempoDisponibleTotal.subtract(periodo.getNo_ventas());
            BigDecimal tiempoOperacion = tiempoDisponible.subtract(totalHoraParo);
            ObjetivosREOEKO mapaD = new ObjetivosREOEKO();
            BigDecimal pDisponibilidad = getPorcentajeParo(tiempoOperacion, tiempoDisponible);
            mapaD.setObjetivo("Disponibilidad");
            mapaD.setReal(pDisponibilidad);            
            mapaD.setMeta(periodo.getDisponibilidad());
            reporteOEE.add(mapaD);
            
            ObjetivosREOEKO mapaU = new ObjetivosREOEKO();
            BigDecimal calculo = tiempoOperacion.subtract(reduccionVelocidad);
            BigDecimal utilizacion = prodBuena.getResult().divide(calculo, RoundingMode.CEILING);
            int compare = periodo.getVelocidad_ideal().compareTo(BigDecimal.ZERO);
            BigDecimal pUtilizacion = BigDecimal.ZERO;
            if(compare != 0){
                pUtilizacion = utilizacion.divide(periodo.getVelocidad_ideal(), RoundingMode.CEILING);
            }
            pUtilizacion = pUtilizacion.multiply(new BigDecimal(100));
            mapaU.setObjetivo("Utilizacion");
            mapaU.setReal(pUtilizacion);
            mapaU.setMeta(periodo.getUtilizacion());
            reporteOEE.add(mapaU);
            
            ObjetivosREOEKO mapaC = new ObjetivosREOEKO();
            BigDecimal pCalidad = BigDecimal.ZERO;
            int resultado = BigDecimal.ZERO.compareTo(prodBuena.getResult());
            if(resultado != 0){
                pCalidad = prodBuena.getResult().divide(produccionTotal,RoundingMode.CEILING);
                pCalidad = pCalidad.multiply(new BigDecimal(100));
            }
            mapaC.setObjetivo("Calidad");
            mapaC.setReal(pCalidad);
            mapaC.setMeta(periodo.getCalidad());
            reporteOEE.add(mapaC);
        }else{
            ObjetivosREOEKO mapa1 = new ObjetivosREOEKO();
            ObjetivosREOEKO mapa2 = new ObjetivosREOEKO();
            ObjetivosREOEKO mapa3 = new ObjetivosREOEKO();
            mapa1.setObjetivo("Disponibilidad");
            mapa1.setReal(BigDecimal.ZERO);
            mapa1.setMeta(periodo.getDisponibilidad());
            mapa2.setObjetivo("Utilizacion");
            mapa2.setReal(BigDecimal.ZERO);
            mapa2.setMeta(periodo.getUtilizacion());
            mapa3.setObjetivo("Calidad");
            mapa3.setReal(BigDecimal.ZERO);
            mapa3.setMeta(periodo.getCalidad());
            
            reporteOEE.add(mapa1);
            reporteOEE.add(mapa2);
            reporteOEE.add(mapa3);
        }
        return reporteOEE;
    }
    
    private List<ReporteEnlaceDetail> fusionDeListas(List<ReporteEnlaceDetail> listMetas, List<ResultBigDecimal> listReal){
        for(int y=0; y<listMetas.size(); y++){
            listMetas.get(y).setReal(listReal.get(y).getResult());
        }
        return listMetas;
    }
}
