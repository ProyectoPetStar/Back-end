package org.petstar.controller.ETAD;

import javax.servlet.http.HttpServletRequest;
import org.petstar.controller.ControllerAutenticacion;
import org.petstar.dao.LineasDAO;
import org.petstar.dao.ETAD.MetasDAO;
import org.petstar.dao.PeriodosDAO;
import org.petstar.dto.UserDTO;
import org.petstar.model.ETAD.MetasMasivasModel;
import org.petstar.model.ETAD.MetasModel;
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
                MetasMasivasModel data = new MetasMasivasModel();
                PeriodosDAO periodosDAO = new PeriodosDAO();
                LineasDAO lineasDAO = new LineasDAO();
                
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
            String tipoMeta = request.getParameter("tipo_meta");
            UserDTO session = autenticacion.isValidToken(request);
            if(session != null){
                MetasModel metasModel= new MetasModel();
                MetasDAO metasDAO = new MetasDAO();
                /**
                * Tipos de Metas
                * 1.- Metas Estrategicas
                * 2.- Metas Operativas
                * 3.- KPI Operativo
                */
                switch(tipoMeta){
                    case"1":
                        if(frecuencia.equals("anual")){
                            metasModel.setListMetasEstrategicas(
                                    metasDAO.getAllMetasMetasEstrategicasAnuales(idEtad, year));
                        }
                    break;
                    case"2":
                        if(frecuencia.equals("anual")){
                            metasModel.setListObjetivosOperativos(
                                    metasDAO.getAllMetasObjetivosOperativosAnuales(idEtad, year));
                        }
                    break;
                    case"3":
                        if(frecuencia.equals("anual")){
                            metasModel.setListKPIOperativos(
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
