package org.petstar.controller.ETAD;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONException;
import org.json.JSONObject;
import org.petstar.controller.ControllerAutenticacion;
import org.petstar.dao.ETAD.KPIOperativosDAO;
import org.petstar.dao.LineasDAO;
import org.petstar.dao.ETAD.MetasDAO;
import org.petstar.dao.ETAD.MetasEstrategicasDAO;
import org.petstar.dao.ETAD.ObjetivosOperativosDAO;
import org.petstar.dao.PeriodosDAO;
import org.petstar.dto.ETAD.PetMetaAnualEstrategica;
import org.petstar.dto.ETAD.PetMetaAnualKpi;
import org.petstar.dto.ETAD.PetMetaAnualObjetivoOperativo;
import org.petstar.dto.UserDTO;
import org.petstar.model.ETAD.MetasModel;
import org.petstar.model.ETAD.MetasResponse;
import org.petstar.model.OutputJson;
import org.petstar.model.ResponseJson;
import static org.petstar.configurations.utils.getCurrentDate;
import org.petstar.dto.ResultInteger;

/**
 *
 * @author Tech-Pro
 */
public class MetasController {
    private static final String MSG_SUCESS = "OK";
    private static final String MSG_LOGOUT = "Inicie sesión nuevamente";
    private static final String MSG_ERROR  = "Descripción de error: ";
    private static final String MSG_EXIST  = "Ya existe una meta con estos valores.";
    private static final String MSG_NOFOUND= "Registro no encontrado.";
    
    public OutputJson loadCombobox(HttpServletRequest request){
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
            
        try{
            UserDTO session = autenticacion.isValidToken(request);
            if(session != null){
                ObjetivosOperativosDAO objetivosOperativosDAO = new ObjetivosOperativosDAO();
                MetasEstrategicasDAO metasEstrategicasDAO = new MetasEstrategicasDAO();
                KPIOperativosDAO kPIOperativosDAO = new KPIOperativosDAO();
                MetasResponse data = new MetasResponse();
                PeriodosDAO periodosDAO = new PeriodosDAO();
                LineasDAO lineasDAO = new LineasDAO();
                
                data.setListObjetivosOperativos(objetivosOperativosDAO.getAllObjetivosOperativosActive());
                data.setListMetasEstrategicas(metasEstrategicasDAO.getAllMetasEstrategicasActive());
                data.setListKPIOperativos(kPIOperativosDAO.getAllKPIOperativosActive());
                data.setListPeriodos(periodosDAO.getPeriodos());
                data.setListLineas(lineasDAO.getLineasActiveByETAD());
                
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
    
    public OutputJson getAllMetas(HttpServletRequest request){
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
            
        try{
            int idEtad = Integer.valueOf(request.getParameter("id_etad"));
            int year = Integer.valueOf(request.getParameter("anio"));
            String frecuencia = request.getParameter("frecuencia");
            int tipoMeta = Integer.valueOf(request.getParameter("tipo_meta"));
            UserDTO session = autenticacion.isValidToken(request);
            if(session != null){
                MetasResponse metasResponse= new MetasResponse();
                MetasDAO metasDAO = new MetasDAO();
                LineasDAO lineasDAO = new LineasDAO();
                KPIOperativosDAO kpioDAO = new KPIOperativosDAO();
                MetasEstrategicasDAO meDAO = new MetasEstrategicasDAO();
                ObjetivosOperativosDAO ooDAO = new ObjetivosOperativosDAO();
                
                /**
                * Tipos de Metas
                * 1.- Metas Estrategicas
                * 2.- Metas Operativas
                * 3.- KPI Operativo
                */
                switch(tipoMeta){
                    case 1:
                        if(frecuencia.equals("anual")){
                            MetasModel data = new MetasModel();
                            List<PetMetaAnualEstrategica> list = metasDAO.getAllMetasMetasEstrategicasAnuales(idEtad, year);
                            for(PetMetaAnualEstrategica row:list){
                                row.setLinea(lineasDAO.getLineasDataById(row.getId_linea()));
                                row.setMetaEstrategica(meDAO.getMetaEstrategicaAnualById(row.getId_meta_estrategica()));
                            }
                            data.setListMetaEstrategica(list);
                            data.setAnio(year);
                            data.setFrecuencia(frecuencia);
                            data.setId_etad(idEtad);
                            data.setTipo_meta(tipoMeta);
                            data.setListMetaEstrategica(list);
                            metasResponse.setMetasEstrategicas(data);
                        }
                    break;
                    case 2:
                        if(frecuencia.equals("anual")){
                            MetasModel data = new MetasModel();
                            List<PetMetaAnualObjetivoOperativo> list = metasDAO.getAllMetasObjetivosOperativosAnuales(idEtad, year);
                            for(PetMetaAnualObjetivoOperativo row:list){
                                row.setLinea(lineasDAO.getLineasDataById(row.getId_linea()));
                                row.setObjetivoOperativo(ooDAO.getObjetivoOperativoById(row.getId_objetivo_operativo()));
                            }
                            data.setListObjetivoOperativo(list);
                            data.setFrecuencia(frecuencia);
                            data.setTipo_meta(tipoMeta);
                            data.setId_etad(idEtad);
                            data.setAnio(year);
                            metasResponse.setMetasObjetivosOperativos(data);
                        }
                    break;
                    case 3:
                        if(frecuencia.equals("anual")){
                            MetasModel data = new MetasModel();
                            List<PetMetaAnualKpi> list = metasDAO.getAllMetasKPIOperativosAnuales(idEtad, year);
                            for(PetMetaAnualKpi row:list){
                                row.setLinea(lineasDAO.getLineasDataById(row.getId_linea()));
                                row.setkPIOperativo(kpioDAO.getKPIOperativoById(row.getId_kpi_operativo()));
                            }
                            data.setListKPIOperativo(list);
                            data.setFrecuencia(frecuencia);
                            data.setTipo_meta(tipoMeta);
                            data.setId_etad(idEtad);
                            data.setAnio(year);
                            metasResponse.setMetasKPIOperativos(data);
                        }
                    break;
                }
                output.setData(metasResponse);
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
    
    public OutputJson insertMetas(HttpServletRequest request) throws Exception{
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        Gson gson = new Gson();
        
        try{
            String jsonString = request.getParameter("meta");
            
            JSONObject jsonResponse = new JSONObject(jsonString);
            MetasModel meta = gson.fromJson(jsonResponse.getJSONObject("meta").toString(), MetasModel.class);

            UserDTO session = autenticacion.isValidToken(request);
            if(session != null){
                MetasDAO metasDAO = new MetasDAO();
                /**
                * Tipos de Metas
                * 1.- Metas Estrategicas
                * 2.- Metas Operativas
                * 3.- KPI Operativo
                */
                switch(meta.getTipo_meta()){
                    case 1:
                        if(meta.getFrecuencia().equals("anual")){
                            ResultInteger result = metasDAO.validateForInsertMetaEstrategicaAnual(meta);
                            if(result.getResult().equals(0)){
                                metasDAO.insertMetaEstrategicaAnual(meta, session.getId_acceso(), getCurrentDate());
                                response = messageForSave(true, MSG_SUCESS);
                            }else{
                                response = messageForSave(false, MSG_EXIST);
                            }
                        }
                    break;
                    case 2:
                        if(meta.getFrecuencia().equals("anual")){
                            ResultInteger result = metasDAO.validateForInsertObjetivoOperativoAnual(meta);
                            if(result.getResult().equals(0)){
                                metasDAO.insertObjetivosOperativosAnual(meta, session.getId_acceso(), getCurrentDate());
                                response = messageForSave(true, MSG_SUCESS);
                            }else{
                                response = messageForSave(false, MSG_EXIST);
                            }
                        }
                    break;
                    case 3:
                        if(meta.getFrecuencia().equals("anual")){
                            ResultInteger result = metasDAO.validateForInsertKPIOperativoAnual(meta);
                            if(result.getResult().equals(0)){
                                metasDAO.insertKPIOperativosAnual(meta, session.getId_acceso(), getCurrentDate());
                                response = messageForSave(true, MSG_SUCESS);
                            }else{
                                response = messageForSave(false, MSG_EXIST);
                            }
                        }
                    break;
                }
            }else{
                response.setMessage(MSG_LOGOUT);
                response.setSucessfull(false);
            }
        }catch(JsonSyntaxException | JSONException ex){
            response.setMessage(MSG_ERROR + ex.getMessage());
            response.setSucessfull(false);
        }
        
        output.setResponse(response);
        return output;
    }
    
    public OutputJson getMetaById(HttpServletRequest request) throws Exception{
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        Gson gson = new Gson();
        
        try{
            String jsonString = request.getParameter("meta");
            
            JSONObject jsonResponse = new JSONObject(jsonString);
            MetasModel meta =gson.fromJson(jsonResponse.getJSONObject("meta").toString(), MetasModel.class);

            UserDTO session = autenticacion.isValidToken(request);
            if(session != null){
                MetasModel data = new MetasModel();
                ObjetivosOperativosDAO operativosDAO = new ObjetivosOperativosDAO();
                MetasEstrategicasDAO estrategicasDAO = new MetasEstrategicasDAO();
                KPIOperativosDAO kpioDAO = new KPIOperativosDAO();
                LineasDAO lineasDAO = new LineasDAO();
                MetasDAO metasDAO = new MetasDAO();
                /**
                * Tipos de Metas
                * 1.- Metas Estrategicas
                * 2.- Metas Operativas
                * 3.- KPI Operativo
                */
                switch(meta.getTipo_meta()){
                    case 1:
                        if(meta.getFrecuencia().equals("anual")){
                            PetMetaAnualEstrategica currentRow = metasDAO.getMetaAnualEstrategicaById(
                                            meta.getMetaEstrategica().getId_meta_anual_estrategica());
                            if(currentRow != null){
                                currentRow.setMetaEstrategica(estrategicasDAO
                                        .getMetaEstrategicaAnualById(currentRow.getId_meta_estrategica()));
                                currentRow.setLinea(lineasDAO.getLineasDataById(currentRow.getId_linea()));
                                data.setId_etad(currentRow.getId_linea());
                                data.setFrecuencia(meta.getFrecuencia());
                                data.setMetaEstrategica(currentRow);
                                data.setAnio(currentRow.getAnio());
                                data.setTipo_meta(1);

                                output.setData(data);
                                response = messageForSave(true, MSG_SUCESS);
                            }else{
                                response = messageForSave(false, MSG_NOFOUND);
                            }
                        }
                    break;
                    case 2:
                        if(meta.getFrecuencia().equals("anual")){
                            PetMetaAnualObjetivoOperativo currentRow = metasDAO.getMetaAnualObjetivoOperativoById(
                                            meta.getObjetivoOperativo().getId_meta_anual_objetivo_operativo());
                            if(currentRow != null){
                                currentRow.setObjetivoOperativo(operativosDAO.
                                        getObjetivoOperativoById(currentRow.getId_objetivo_operativo()));
                                currentRow.setLinea(lineasDAO.getLineasDataById(currentRow.getId_linea()));
                                data.setId_etad(currentRow.getId_linea());
                                data.setFrecuencia(meta.getFrecuencia());
                                data.setObjetivoOperativo(currentRow);
                                data.setAnio(currentRow.getAnio());
                                data.setTipo_meta(2);

                                output.setData(data);
                                response = messageForSave(true, MSG_SUCESS);
                            }else{
                                response = messageForSave(false, MSG_NOFOUND);
                            }
                        }
                    break;
                    case 3:
                        if(meta.getFrecuencia().equals("anual")){
                            PetMetaAnualKpi currentRow = metasDAO.
                                    getMetaAnualKPIOperativoById(meta.getkPIOperativo().getId_meta_anual_kpi());
                            if(currentRow != null){
                                currentRow.setkPIOperativo(
                                        kpioDAO.getKPIOperativoById(currentRow.getId_kpi_operativo()));
                                currentRow.setLinea(lineasDAO.getLineasDataById(currentRow.getId_linea()));
                                data.setId_etad(currentRow.getId_linea());
                                data.setFrecuencia(meta.getFrecuencia());
                                data.setkPIOperativo(currentRow);
                                data.setAnio(currentRow.getAnio());
                                data.setTipo_meta(3);

                                output.setData(data);
                                response = messageForSave(true, MSG_SUCESS);
                            }else{
                                response = messageForSave(false, MSG_NOFOUND);
                            }
                        }
                    break;
                }
            }else{
                response.setMessage(MSG_LOGOUT);
                response.setSucessfull(false);
            }
        }catch(JsonSyntaxException | JSONException ex){
            response.setMessage(MSG_ERROR + ex.getMessage());
            response.setSucessfull(false);
        }
        
        output.setResponse(response);
        return output;
    }
    
    public ResponseJson messageForSave(boolean result, String reply){
        ResponseJson response = new ResponseJson();
        if(result){
            response.setMessage(reply);
            response.setSucessfull(true);
        }else{
            response.setMessage(reply);
            response.setSucessfull(false);
        }
        return response;
    }
}
