package org.petstar.controller.ishikawa;

import javax.servlet.http.HttpServletRequest;
import org.petstar.controller.ControllerAutenticacion;
import org.petstar.dao.CatalogosDAO;
import org.petstar.dto.UserDTO;
import org.petstar.model.OutputJson;
import org.petstar.model.ResponseJson;
import org.petstar.model.ishikawa.IshikawaResponse;

/**
 *
 * @author Tech-Pro
 */
public class IshikawaController {
    private static final String MSG_SUCESS = "OK";
    private static final String MSG_LOGOUT = "Inicie sesión nuevamente";
    private static final String MSG_ERROR  = "Descripción de error: ";
    private static final String MSG_EXIST  = "Ya existe una meta con estos valores.";
    
    public OutputJson loadCombobox(HttpServletRequest request){
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson responseJson = new ResponseJson();
        OutputJson outputJson = new OutputJson();
        
        try {
            UserDTO sesion = autenticacion.isValidToken(request);
            if(sesion != null){
                IshikawaResponse data = new IshikawaResponse();
                CatalogosDAO catalogosDAO = new CatalogosDAO();
                
                data.setListPreguntas(catalogosDAO.getCatalogosActive("pet_cat_preguntas"));
                data.setListGrupos(catalogosDAO.getCatalogosActive("pet_cat_grupo"));
                data.setListEtads(catalogosDAO.getCatalogosActive("pet_cat_etad"));
                data.setListMs(catalogosDAO.getCatalogosActive("pet_cat_emes"));
                
                responseJson.setMessage(MSG_SUCESS);
                responseJson.setSucessfull(true);
                outputJson.setData(data);
           }else{
                responseJson.setMessage(MSG_LOGOUT);
                responseJson.setSucessfull(false);
            }
        } catch (Exception e) {
            responseJson.setMessage(MSG_ERROR + e.getMessage());
            responseJson.setSucessfull(false);
        }
        outputJson.setResponse(responseJson);
        return outputJson;
    }
}
