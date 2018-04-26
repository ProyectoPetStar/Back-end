package org.petstar.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.petstar.dao.CatalogosDAO;
import org.petstar.dao.LineasDAO;
import org.petstar.dao.MetasDAO;
import org.petstar.dao.PeriodosDAO;
import org.petstar.dao.RazonParoDAO;
import org.petstar.dto.PeriodosDTO;
import org.petstar.model.OutputJson;
import org.petstar.model.ReportesResponseJson;
import org.petstar.model.ResponseJson;

/**
 * @author Tech-Pro
 */
public class ControllerReportes {
    private static final String TABLE_FUENTES = "pet_cat_fuentes_paro";
    private static final String MSG_SUCESS = "OK";
    private static final String MSG_LOGOUT = "Inicie sesión nuevamente";
    private static final String MSG_ERROR  = "Descripción de error: ";
    private static final String MSG_INVALID= "Ya existe una meta con esos valores.";
    private static final String MSG_NO_EXIST= "La meta no existe.";
    
    public OutputJson getAllMetas(HttpServletRequest request) throws Exception{
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        
        try{
            CatalogosDAO catalogosDAO = new CatalogosDAO();
            RazonParoDAO razonParoDAO = new RazonParoDAO();
            ReportesResponseJson data = new ReportesResponseJson();
            
            List<HashMap> lista = new ArrayList<>();
            
            for(int i=0; i<5; i++){
                HashMap<String, Object> params = new HashMap<>();
                
                params.put("Campo1", 1);
                params.put("campo2", i);
                
                lista.add(params);
            }
            //data.setLista(lista);
            
            output.setData(data);
            response.setMessage(MSG_SUCESS);
            response.setSucessfull(true);
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
        
        try{
            LineasDAO lineasDAO = new LineasDAO();
            ReportesResponseJson data = new ReportesResponseJson();
            
            data.setListLineas(lineasDAO.getLineasData());
            output.setData(data);
            
            response.setSucessfull(true);
            response.setMessage(MSG_SUCESS);
        }catch(Exception ex){
            response.setSucessfull(false);
            response.setMessage(MSG_ERROR + ex.getMessage());
        }
        output.setResponse(response);
        return output;
    }
}