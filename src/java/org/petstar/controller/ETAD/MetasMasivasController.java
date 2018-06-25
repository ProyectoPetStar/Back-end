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
import org.petstar.dao.LineasDAO;
import org.petstar.dao.PeriodosDAO;
import org.petstar.dto.UserDTO;
import org.petstar.model.ETAD.MetasMasivasModel;
import org.petstar.model.OutputJson;
import org.petstar.model.ResponseJson;
import static org.petstar.configurations.Tools.saveFIle;
import static org.petstar.configurations.Tools.listRows;
import static org.petstar.configurations.Tools.validateFileObjetivosEstrategicosAnual;
import static org.petstar.configurations.Tools.validateFileMetasEstrategicasAnual;
import static org.petstar.configurations.Tools.validateFileKPIOperativoAnual;
import static org.petstar.configurations.utils.getCurrentDate;
import org.petstar.dao.ETAD.KPIOperativosDAO;
import org.petstar.dao.ETAD.MetasEstrategicasDAO;
import org.petstar.dao.ETAD.MetasMasivasDAO;
import org.petstar.dao.ETAD.ObjetivosOperativosDAO;
import org.petstar.dto.ETAD.KPIOperativosDTO;
import org.petstar.dto.ETAD.MetasEstrategicasDTO;
import org.petstar.dto.ETAD.ObjetivosOperativosDTO;
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
                Date date = getCurrentDate();
                String nameFile = tipoMeta+"_"+frecuencia+"_"+formato.format(date) + ".csv";
                
                boolean save = saveFIle(stringFile, nameFile);
                if(save){
                    MetasMasivasDAO masivasDAO = new MetasMasivasDAO();
                    OutputJson valid = new OutputJson();
                    ResponseJson rj = new ResponseJson();
                    /**
                     * Tipos de Metas
                     * 1.- Metas Estrategicas
                     * 2.- Metas Operativas
                     * 3.- KPI Operativo
                     */
                    switch(tipoMeta){
                        case"1":
                            if(frecuencia.equals("anual")){
                                valid = validateFileMetasEstrategicasAnual(request, nameFile, session.getId_acceso());
                                rj = (ResponseJson) valid.getResponse();
                            }
                        break;
                        case"2":
                            if(frecuencia.equals("anual")){
                                valid = validateFileObjetivosEstrategicosAnual(request, nameFile, session.getId_acceso());
                                rj = (ResponseJson) valid.getResponse();
                            }
                        break;
                        case"3":
                            if(frecuencia.equals("anual")){
                                valid = validateFileKPIOperativoAnual(request, nameFile, session.getId_acceso());
                                rj = (ResponseJson) valid.getResponse();
                            }
                        break;
                    }
                    
                    Date fecha = getCurrentDate();
                    if(rj.isSucessfull()){
                        switch(tipoMeta){
                            case"1":
                                if(frecuencia.equals("anual")){
                                    ResultInteger resultFile = masivasDAO.insertFileKPI(nameFile, session.getId_acceso(), fecha);
                                    masivasDAO.insertMetasEstrategicasAnuales((List<HashMap>) valid.getData(),resultFile.getResult());
                                }
                            break;
                            case"2":
                                if(frecuencia.equals("anual")){
                                    ResultInteger resultFile = masivasDAO.insertFileKPI(nameFile, session.getId_acceso(), fecha);
                                    masivasDAO.insertTMPObjetivosOperativos((List<HashMap>) valid.getData(),resultFile.getResult());
                                }
                            break;
                            case"3":
                                if(frecuencia.equals("anual")){
                                    ResultInteger resultFile = masivasDAO.insertFileKPI(nameFile, session.getId_acceso(), fecha);
                                    masivasDAO.insertKPIOperativos((List<HashMap>) valid.getData(),resultFile.getResult());
                                }
                            break;
                        }
                        
                        MetasMasivasModel data = new MetasMasivasModel();
                        data.setListData(listRows(nameFile));
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
                int idEtad = Integer.valueOf(request.getParameter("id_etad"));
                int year = Integer.valueOf(request.getParameter("anio"));
                String frecuencia = request.getParameter("frecuencia");
                String tipoMeta = request.getParameter("tipo_meta");
                
                MetasMasivasDAO masivasDAO = new MetasMasivasDAO();
                ResultInteger result = new ResultInteger();
                /**
                * Tipos de Metas
                * 1.- Metas Estrategicas
                * 2.- Metas Operativas
                * 3.- KPI Operativo
                */
                switch(tipoMeta){
                    case"1":
                        if(frecuencia.equals("anual")){
                            result = masivasDAO.validateExistDataMetasEstrategiasAnuales(idEtad, year);
                            if(result.getResult().equals(0)){
                                masivasDAO.loadDataAnualMetasEstrategicas(idEtad, year);
                                response = messageForValidate(true);
                            }else{
                                response = messageForValidate(false);
                            }
                        }
                        break;
                    case"2":
                        if(frecuencia.equals("anual")){
                            result = masivasDAO.validateExistDataMetasOperativasAnuales(idEtad, year);
                            if(result.getResult().equals(0)){
                                masivasDAO.loadDataAnualObjetivosOperativos(idEtad, year);
                                response = messageForValidate(true);
                            }else{
                                response = messageForValidate(false);
                            }
                        }
                        break;
                    case"3":
                        if(frecuencia.equals("anual")){
                            result = masivasDAO.validateExistDataMetasKPIOperativoAnuales(idEtad, year);
                            if(result.getResult().equals(0)){
                                masivasDAO.loadDataAnualKPIOperativos(idEtad, year);
                                response = messageForValidate(true);
                            }else{
                                response = messageForValidate(false);
                            }
                        }
                        break;
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
                int idEtad = Integer.valueOf(request.getParameter("id_etad"));
                int year = Integer.valueOf(request.getParameter("anio"));
                String frecuencia = request.getParameter("frecuencia");
                String tipoMeta = request.getParameter("tipo_meta");
                
                MetasMasivasDAO masivasDAO = new MetasMasivasDAO();
                /**
                * Tipos de Metas
                * 1.- Metas Estrategicas
                * 2.- Metas Operativas
                * 3.- KPI Operativo
                */
                switch(tipoMeta){
                    case"1":
                        if(frecuencia.equals("anual")){
                            masivasDAO.rewriteDataAnualMetasEstrategicas(idEtad, year);
                        }
                        break;
                    case"2":
                        if(frecuencia.equals("anual")){
                            masivasDAO.rewriteDataAnualObjetivosOperativos(idEtad, year);
                        }
                        break;
                    case"3":
                        if(frecuencia.equals("anual")){
                            masivasDAO.rewriteDataAnualKPIOperativos(idEtad, year);
                        }
                        break;
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
    
    public OutputJson downloadTemplate(HttpServletRequest request){
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
            
        try{
            String tipoMeta = request.getParameter("tipo_meta");
            String frecuencia = request.getParameter("frecuencia");
            UserDTO session = autenticacion.isValidToken(request);
            if(session != null){
                String pathFile = Configuration.PATH_DOWNLOAD_FILE;
                
                String outputFile = pathFile+tipoMeta+"Template.csv";
                boolean alreadyExists = new File(outputFile).exists();

                if(alreadyExists){
                    File newFile = new File(outputFile);
                    newFile.delete();
                }     

                try {
                    CsvWriter csvOutput = new CsvWriter(new FileWriter(outputFile, true), ',');
                    /**
                     * Tipos de Metas
                     * 1.- Metas Estrategicas
                     * 2.- Metas Operativas
                     * 3.- KPI Operativo
                     */
                    switch(tipoMeta){
                        case"1":
                            MetasEstrategicasDAO estrategicasDAO = new MetasEstrategicasDAO();
                            if(frecuencia.equals("anual")){
                                List<MetasEstrategicasDTO> listEstrategicas = estrategicasDAO.getListMetasEstrategicasAnuales();
                                csvOutput.write("Meta");
                                csvOutput.write("UM");
                                csvOutput.write("Valor");
                                csvOutput.endRecord();
                                for(MetasEstrategicasDTO meta: listEstrategicas){
                                    csvOutput.write(meta.getValor());
                                    csvOutput.write(meta.getUnidad_medida_me());
                                    csvOutput.endRecord();                   
                                }
                            }
                        break;
                        case "2":
                            ObjetivosOperativosDAO operativosDAO = new ObjetivosOperativosDAO();
                            List<ObjetivosOperativosDTO> listObjetivos = operativosDAO.getListObjetivosOperativos();
                            if(frecuencia.equals("anual")){
                                csvOutput.write("Objetivo");
                                csvOutput.write("UM");
                                csvOutput.write("Meta");
                                csvOutput.endRecord();
                                for(ObjetivosOperativosDTO objetivo: listObjetivos){
                                    csvOutput.write(objetivo.getValor());
                                    csvOutput.write(objetivo.getUnidad_medida_objetivo_operativo());
                                    csvOutput.endRecord();                   
                                }
                            }
                            break;
                        case"3":
                            KPIOperativosDAO kPIOperativosDAO = new KPIOperativosDAO();
                            List<KPIOperativosDTO> listKPIOperativos = kPIOperativosDAO.getListKPIOperativos();
                            if(frecuencia.equals("anual")){
                                csvOutput.write("KPI");
                                csvOutput.write("Tipo");
                                csvOutput.write("UM");
                                csvOutput.write("Meta");
                                csvOutput.endRecord();
                                for(KPIOperativosDTO kpi: listKPIOperativos){
                                    csvOutput.write(kpi.getValor());
                                    csvOutput.write(kpi.getTipo_kpi());
                                    csvOutput.write(kpi.getUnidad_medida_kpi_operativo());
                                    csvOutput.endRecord();                   
                                } 
                            }
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
