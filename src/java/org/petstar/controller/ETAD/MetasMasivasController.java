package org.petstar.controller.ETAD;

import com.csvreader.CsvWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.axis.encoding.Base64;
import org.petstar.configurations.Configuration;
import org.petstar.controller.ControllerAutenticacion;
import org.petstar.dao.PeriodosDAO;
import org.petstar.dto.UserDTO;
import org.petstar.model.ETAD.MetasMasivasModel;
import org.petstar.model.OutputJson;
import org.petstar.model.ResponseJson;
import static org.petstar.configurations.Tools.saveFIle;
import static org.petstar.configurations.Tools.validateFileKPIOperativo;
import static org.petstar.configurations.utils.getCurrentDate;
import org.petstar.dao.CatalogosDAO;
import org.petstar.dao.ETAD.KPIOperativosDAO;
import org.petstar.dao.ETAD.MetasMasivasDAO;
import org.petstar.dto.ETAD.PetCatKpiOperativo;
import org.petstar.dto.ResultInteger;

/**
 *
 * @author Tech-Pro
 */
public class MetasMasivasController {
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
                MetasMasivasModel data = new MetasMasivasModel();
                CatalogosDAO catalogosDAO = new CatalogosDAO();
                PeriodosDAO periodosDAO = new PeriodosDAO();
                
                data.setListPeriodos(periodosDAO.getPeriodos());
                data.setListEtad(catalogosDAO.getCatalogosActive("pet_cat_etad"));
                
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
            int idPeriodo = Integer.valueOf(request.getParameter("id_periodo"));
            UserDTO session = autenticacion.isValidToken(request);
            if(session != null){
                StringBuilder stringFile = new StringBuilder();
                stringFile.append(request.getParameter("file"));
                
                SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
                Date date = getCurrentDate();
                String nameFile = "MetasKPI_"+formato.format(date) + ".csv";
                
                boolean save = saveFIle(stringFile, nameFile);
                if(save){
                    MetasMasivasDAO masivasDAO = new MetasMasivasDAO();
                    OutputJson valid = new OutputJson();
                    ResponseJson rj = new ResponseJson();
                    
                    valid = validateFileKPIOperativo(request, nameFile, session.getId_acceso());
                    rj = (ResponseJson) valid.getResponse();
                   
                    if(rj.isSucessfull()){
                        ResultInteger resultFile = masivasDAO.insertFileKPI(nameFile, session.getId_acceso(), getCurrentDate());
                        masivasDAO.cleanTmpKPI(idPeriodo);
                        masivasDAO.insertMetasKPIOperativos((List<HashMap>) valid.getData(), resultFile.getResult());
                        
                        MetasMasivasModel data = new MetasMasivasModel();
                        data.setListData((List<HashMap>) valid.getData());
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
    
    public OutputJson loadData(HttpServletRequest request){
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
            
        try{
            UserDTO session = autenticacion.isValidToken(request);
            if(session != null){
                int idPeriodo = Integer.valueOf(request.getParameter("id_periodo"));
                int idEtad = Integer.valueOf(request.getParameter("id_etad"));
                MetasMasivasDAO masivasDAO = new MetasMasivasDAO();
                ResultInteger result = masivasDAO.validateExistMetasKPIOperativo(idEtad, idPeriodo);
                
                if(result.getResult().equals(0)){
                    masivasDAO.loadMetasKPIOperativos(idEtad, idPeriodo);
                    response = messageForValidate(true);
                }else{
                    response = messageForValidate(false);
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
    
    public OutputJson rewriteData(HttpServletRequest request){
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
            
        try{
            UserDTO session = autenticacion.isValidToken(request);
            if(session != null){
                int idPeriodo = Integer.valueOf(request.getParameter("id_periodo"));
                int idEtad = Integer.valueOf(request.getParameter("id_etad"));
                
                MetasMasivasDAO masivasDAO = new MetasMasivasDAO();
                masivasDAO.rewriteDataKPIOperativos(idEtad, idPeriodo, session.getId_acceso(), getCurrentDate());
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
    
    public OutputJson downloadTemplate(HttpServletRequest request){
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
            
        try{
            int idEtad = Integer.valueOf(request.getParameter("id_etad"));
            UserDTO session = autenticacion.isValidToken(request);
            if(session != null){
                String pathFile = Configuration.PATH_DOWNLOAD_FILE;
                File paths = new File(pathFile);
                if(!paths.exists()){
                    paths.mkdirs();
                }
                
                String outputFile = pathFile+"KPI_Template.csv";
                boolean alreadyExists = new File(outputFile).exists();

                if(alreadyExists){
                    File newFile = new File(outputFile);
                    newFile.delete();
                }     

                try {
                    CsvWriter csvOutput = new CsvWriter(new FileWriter(outputFile, true), ',');
                    
                    KPIOperativosDAO kPIOperativosDAO = new KPIOperativosDAO();
                    List<PetCatKpiOperativo> listKPIOperativos = kPIOperativosDAO.getListKPIOperativosByEtad(idEtad);
                            
                    csvOutput.write("KPI");
                    csvOutput.write("Tipo");
                    csvOutput.write("UM");
                    csvOutput.write("Meta");
                    csvOutput.endRecord();
                    for(PetCatKpiOperativo kpi: listKPIOperativos){
                        csvOutput.write(kpi.getValor());
                        csvOutput.write((kpi.getTipo_kpi()==1 ? "Mas es mejor":"Menos es Mejor"));
                        csvOutput.write(kpi.getUnidad_medida());
                        csvOutput.endRecord();                   
                    }
                    csvOutput.close();
                    
                    byte[] bFile = Files.readAllBytes(new File(outputFile).toPath());
                    StringBuilder file = new StringBuilder();
                    file.append(Base64.encode(bFile));

                    response.setMessage(file.toString());
                    response.setSucessfull(true);
                } catch (IOException e) {
                    response.setMessage(MSG_ERROR + e.getMessage());
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
    
    public ResponseJson messageForValidate(boolean result){
        ResponseJson response = new ResponseJson();
        if(result){
            response.setMessage(MSG_SUCESS);
            response.setSucessfull(true);
        }else{
            response.setMessage("999");
            response.setSucessfull(false);
        }
        return response;
    }
}
