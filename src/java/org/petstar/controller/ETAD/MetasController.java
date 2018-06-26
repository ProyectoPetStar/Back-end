package org.petstar.controller.ETAD;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.util.ArrayList;
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
import org.petstar.dto.UserDTO;
import org.petstar.model.ETAD.MetasModel;
import org.petstar.model.ETAD.MetasResponse;
import org.petstar.model.OutputJson;
import org.petstar.model.ResponseJson;

/**
 *
 * @author Tech-Pro
 */
public class MetasController {
    private static final String MSG_SUCESS = "OK";
    private static final String MSG_LOGOUT = "Inicie sesión nuevamente";
    private static final String MSG_ERROR  = "Descripción de error: ";
    
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
                
                data.setListObjetivosOperativos(objetivosOperativosDAO.getListObjetivosOperativos());
                data.setListMetasEstrategicas(metasEstrategicasDAO.getListMetasEstrategicasAnuales());
                data.setListKPIOperativos(kPIOperativosDAO.getListKPIOperativos());
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
                MetasResponse metasModel= new MetasResponse();
                MetasDAO metasDAO = new MetasDAO();
                LineasDAO lineasDAO = new LineasDAO();
                MetasEstrategicasDAO meDAO = new MetasEstrategicasDAO();
                
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
                                data.setListMetaEstrategica(list);
                            }
                            data.setAnio(year);
                            data.setFrecuencia(frecuencia);
                            data.setId_etad(idEtad);
                            data.setTipo_meta(tipoMeta);
                            data.setListMetaEstrategica(list);
                            metasModel.setMetasEstrategicas(data);
                        }
                    break;
                    case 2:
                        if(frecuencia.equals("anual")){
                            metasModel.setListMetasObjetivosOperativos(
                                    metasDAO.getAllMetasObjetivosOperativosAnuales(idEtad, year));
                        }
                    break;
                    case 3:
                        if(frecuencia.equals("anual")){
                            metasModel.setListMetasKPIOperativos(
                                    metasDAO.getAllMetasKPIOperativosAnuales(idEtad, year));
                        }
                    break;
                }
                output.setData(metasModel);
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
}
