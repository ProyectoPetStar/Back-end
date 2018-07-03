package org.petstar.controller.ETAD;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import static org.petstar.configurations.Tools.listRows;
import static org.petstar.configurations.Tools.saveFIle;
import static org.petstar.configurations.Tools.validateFilePonderacionKPIOperativo;
import static org.petstar.configurations.Tools.validateFilePonderacionObjetivoOperativo;
import static org.petstar.configurations.utils.getCurrentDate;
import org.petstar.controller.ControllerAutenticacion;
import org.petstar.dao.ETAD.PonderacionMasivaDAO;
import org.petstar.dao.LineasDAO;
import org.petstar.dao.PeriodosDAO;
import org.petstar.dto.ResultInteger;
import org.petstar.dto.UserDTO;
import org.petstar.model.ETAD.MetasMasivasModel;
import org.petstar.model.ETAD.PonderacionResponse;
import org.petstar.model.OutputJson;
import org.petstar.model.ResponseJson;

/**
 *
 * @author Tech-Pro
 */
public class PonderacionMasivaController {
    private static final String MSG_SUCESS = "OK";
    private static final String MSG_LOGOUT = "Inicie sesión nuevamente";
    private static final String MSG_ERROR  = "Descripción de error: ";
    private static final String MSG_FAILED = "Ha ocurrido un error al cargar el archivo";
    private static final String MSG_INVALID= "El archivo contiene errores";
    
    public OutputJson loadCombobox(HttpServletRequest request){
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
            
        try{
            UserDTO session = autenticacion.isValidToken(request);
            if(session != null){
                PonderacionResponse data = new PonderacionResponse();
                PeriodosDAO periodosDAO = new PeriodosDAO();
                LineasDAO lineasDAO = new LineasDAO();
                
                data.setListPeriodos(periodosDAO.getPeriodos());
                data.setListEtads(lineasDAO.getLineasActiveByETAD());
                
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
                int anio = Integer.valueOf(request.getParameter("anio"));
                String tipoPond = request.getParameter("tipo_ponderacion");
                
                SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
                Date date = getCurrentDate();
                String nameFile = "weighing_"+anio+"_"+formato.format(date) + ".csv";
                
                boolean save = saveFIle(stringFile, nameFile);
                if(save){
                    PonderacionMasivaDAO masivaDAO = new PonderacionMasivaDAO();
                    OutputJson valid = new OutputJson();
                    ResponseJson rj = new ResponseJson();
                    /**
                     * Tipos de Ponderacion
                     * 1.- Ponderacion Anual de Objetivos Operativos
                     * 2.- Ponderacion Anual de KPI operativos
                     */
                    switch(tipoPond){
                        case"1":
                            valid = validateFilePonderacionObjetivoOperativo(request, nameFile, session.getId_acceso());
                            rj = (ResponseJson) valid.getResponse();
                        break;
                        case"2":
                            valid = validateFilePonderacionKPIOperativo(request, nameFile, session.getId_acceso());
                            rj = (ResponseJson) valid.getResponse();
                        break;
                    }
                    
                    if(rj.isSucessfull()){
                        PonderacionResponse data = new PonderacionResponse();
                        Date fecha = getCurrentDate();
                        ResultInteger idFile = masivaDAO.saveFile(nameFile, session.getId_acceso(), fecha);
                        switch(tipoPond){
                            case"1":
                                masivaDAO.insertPonderacionObjetivosOperativos((List<HashMap>) valid.getData(),idFile.getResult());
                            break;
                            case"2":
                                masivaDAO.insertPonderacionKPIOperativos((List<HashMap>) valid.getData(),idFile.getResult());
                            break;
                        }
                        data.setList((List<HashMap>) valid.getData());
                        //MetasMasivasModel data = new MetasMasivasModel();
                        //data.setListData(listRows(nameFile));
                        output.setData(data);
                        response.setMessage(MSG_SUCESS);
                        response.setSucessfull(true);
                    }else{
                        response.setMessage(MSG_INVALID);
                        response.setSucessfull(false);
                    }
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
