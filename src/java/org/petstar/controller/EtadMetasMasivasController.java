package org.petstar.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.petstar.dao.LineasDAO;
import org.petstar.dao.PeriodosDAO;
import org.petstar.dto.UserDTO;
import org.petstar.model.EtadMetasMasivasModel;
import org.petstar.model.OutputJson;
import org.petstar.model.ResponseJson;
import static org.petstar.configurations.Tools.saveFIle;
import static org.petstar.configurations.Tools.listRows;

/**
 *
 * @author Tech-Pro
 */
public class EtadMetasMasivasController {
    private static final String MSG_SUCESS = "OK";
    private static final String MSG_LOGOUT = "Inicie sesión nuevamente";
    private static final String MSG_ERROR  = "Descripción de error: ";
    private static final String MSG_FAILED = "Ha ocurrido un error al cargar el archivo";
    
    public OutputJson loadCombobox(HttpServletRequest request){
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
            
        try{
            UserDTO session = autenticacion.isValidToken(request);
            if(session != null){
                EtadMetasMasivasModel data = new EtadMetasMasivasModel();
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
    
    public OutputJson preview(HttpServletRequest request){
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
            
        try{
            UserDTO session = autenticacion.isValidToken(request);
            if(session != null){
                StringBuilder stringFile = new StringBuilder();
                stringFile.append(request.getParameter("file"));
                String tipoMeta = request.getParameter("tipo_meta");
                String frecuencia = request.getParameter("frecuencia");
                
                SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
                Date date = new Date();
                String nameFile = tipoMeta+"_"+frecuencia+"_"+formato.format(date) + ".csv";
                
                boolean save = saveFIle(stringFile, nameFile);
                if(save){
                    EtadMetasMasivasModel data = new EtadMetasMasivasModel();
                    //listRows(nameFile);
                    data.setListData(listRows(nameFile));
                    output.setData(data);
                    response.setMessage(MSG_SUCESS);
                    response.setSucessfull(true);
                }else{
                    response.setMessage(MSG_FAILED);
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
}
