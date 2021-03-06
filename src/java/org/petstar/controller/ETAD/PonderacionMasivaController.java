package org.petstar.controller.ETAD;

import com.csvreader.CsvWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.axis.encoding.Base64;
import org.petstar.configurations.Configuration;
import static org.petstar.configurations.Tools.saveFIle;
import static org.petstar.configurations.Tools.validateFilePonderacionKPIOperativo;
import static org.petstar.configurations.Tools.validateFilePonderacionObjetivoOperativo;
import static org.petstar.configurations.utils.getCurrentDate;
import org.petstar.controller.ControllerAutenticacion;
import org.petstar.dao.CatalogosDAO;
import org.petstar.dao.ETAD.KPIOperativosDAO;
import org.petstar.dao.ETAD.ObjetivosOperativosDAO;
import org.petstar.dao.ETAD.PonderacionDAO;
import org.petstar.dao.ETAD.PonderacionMasivaDAO;
import org.petstar.dao.PeriodosDAO;
import org.petstar.dto.ETAD.PetCatKpiOperativo;
import org.petstar.dto.ETAD.PetCatObjetivoOperativo;
import org.petstar.dto.ETAD.PetPonderacionObjetivoOperativo;
import org.petstar.dto.ETAD.ValidacionKPI;
import org.petstar.dto.ResultInteger;
import org.petstar.dto.UserDTO;
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
                ObjetivosOperativosDAO objetivosDAO= new ObjetivosOperativosDAO();
                CatalogosDAO catalogosDAO = new CatalogosDAO();
                PeriodosDAO periodosDAO = new PeriodosDAO();
                
                data.setListPeriodos(periodosDAO.getAllPeriodos());
                data.setListEtads(catalogosDAO.getCatalogosActive("pet_cat_etad"));
                data.setYearsForKPI(periodosDAO.yearsForPonderacionKpi());
                data.setListYearsOP(periodosDAO.yearsWithPonderacionObjetivos());
                data.setListYears(periodosDAO.yearsWithoutPonderacionObjetivos());
                data.setListObjetivosOperativos(objetivosDAO.getAllObjetivosOperativosActive());
                
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
                int tipoPond = Integer.valueOf(request.getParameter("tipo_ponderacion"));
                
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
                        case 1:
                            valid = validateFilePonderacionObjetivoOperativo(request, nameFile, session.getId_acceso());
                            rj = (ResponseJson) valid.getResponse();
                        break;
                        case 2:
                            valid = validateFilePonderacionKPIOperativo(request, nameFile, session.getId_acceso());
                            rj = (ResponseJson) valid.getResponse();
                        break;
                    }
                    
                    if(rj.isSucessfull()){
                        PonderacionResponse data = new PonderacionResponse();
                        Date fecha = getCurrentDate();
                        ResultInteger idFile = masivaDAO.saveFile(nameFile, session.getId_acceso(), fecha);
                        switch(tipoPond){
                            case 1:
                                masivaDAO.insertPonderacionObjetivosOperativos((List<HashMap>) valid.getData(),idFile.getResult());
                                data.setListData((List<HashMap>) valid.getData());
                                output.setData(data);
                                response = messageForValidate(true, MSG_SUCESS);
                            break;
                            case 2:
                                int idEtad = Integer.valueOf(request.getParameter("id_etad"));
                                masivaDAO.cleanTmpKpiOperativos(anio, idEtad);
                                masivaDAO.insertPonderacionKPIOperativos((List<HashMap>) valid.getData(),idFile.getResult());
                                List<ValidacionKPI> listResult = masivaDAO.comparacionPonderacionKPI(anio,idEtad);
                                boolean bandera = true;
                                for(ValidacionKPI row:listResult){
                                    if(row.getSuma() != row.getPonderacion()){
                                        bandera = false;
                                    }
                                }
                                if(bandera){
                                    data.setListData((List<HashMap>) valid.getData());
                                    output.setData(data);
                                    response = messageForValidate(true, MSG_SUCESS);
                                }else{
                                    masivaDAO.cleanTmpKpiOperativos(anio, idEtad);
                                    response = messageForValidate(false, MSG_INVALID);
                                }
                            break;
                        }
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
                int year = Integer.valueOf(request.getParameter("anio"));
                int tipoPond = Integer.valueOf(request.getParameter("tipo_ponderacion"));
                
                PonderacionMasivaDAO masivaDAO = new PonderacionMasivaDAO();
                ResultInteger result = new ResultInteger();
                /**
                * Tipos de Ponderacion
                * 1.- Ponderacion Anual de Objetivos Operativos
                * 2.- Ponderacion Anual de KPI operativos
                */
                switch(tipoPond){
                    case 1:
                        result = masivaDAO.validateExistDataObjetivosOperativos(year);
                        if(result.getResult().equals(0)){
                            masivaDAO.loadDataObjetivosOperativos(year);
                            response = messageForValidate(true, MSG_SUCESS);
                        }else{
                            response = messageForValidate(false, "999");
                        }
                    break;
                    case 2:
                        int idEtad = Integer.valueOf(request.getParameter("id_etad"));
                        result = masivaDAO.validateExistDataKPIOperativos(year, idEtad);
                        if(result.getResult().equals(0)){
                            masivaDAO.loadDataKPIOperativos(year, idEtad);
                            response = messageForValidate(true, MSG_SUCESS);
                        }else{
                            response = messageForValidate(false, "999");
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
                int year = Integer.valueOf(request.getParameter("anio"));
                int tipoPond = Integer.valueOf(request.getParameter("tipo_ponderacion"));
                
                PonderacionMasivaDAO masivaDAO = new PonderacionMasivaDAO();
                /**
                * Tipos de Ponderacion
                * 1.- Ponderacion Anual de Objetivos Operativos
                * 2.- Ponderacion Anual de KPI operativos
                */
                switch(tipoPond){
                    case 1:
                        masivaDAO.rewriteDataAnualObjetivosOperativos(year);
                    break;
                    case 2:
                        int idEtad = Integer.valueOf(request.getParameter("id_etad"));
                        masivaDAO.rewriteDataAnualKPIOperativos(year, idEtad);
                    break;
                }
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
            int tipoPond = Integer.valueOf(request.getParameter("tipo_ponderacion"));
            UserDTO session = autenticacion.isValidToken(request);
            if(session != null){
                String pathFile = Configuration.PATH_DOWNLOAD_FILE;
                File paths = new File(pathFile);
                if(!paths.exists()){
                    paths.mkdirs();
                }
                
                String outputFile = pathFile+"Ponderacion"+tipoPond+"Template.csv";
                boolean alreadyExists = new File(outputFile).exists();

                if(alreadyExists){
                    File newFile = new File(outputFile);
                    newFile.delete();
                }     

                try {
                    CsvWriter csvOutput = new CsvWriter(new FileWriter(outputFile, true), ',');
                    /**
                     * Tipos de Ponderacion
                     * 1.- Ponderacion Anual de Objetivos Operativos
                     * 2.- Ponderacion Anual de KPI operativos
                     */
                    switch(tipoPond){
                        case 1:
                            ObjetivosOperativosDAO operativosDAO = new ObjetivosOperativosDAO();
                            List<PetCatObjetivoOperativo> listObjetivos = operativosDAO.getAllObjetivosOperativosActive();
                            csvOutput.write("Objetivo");
                            csvOutput.write("Ponderacion");
                            csvOutput.endRecord();
                            for(PetCatObjetivoOperativo row: listObjetivos){
                                csvOutput.write(row.getValor());
                                csvOutput.endRecord();                   
                            }
                        break;
                        case 2:
                            int year = Integer.valueOf(request.getParameter("anio"));
                            int idEtad = Integer.valueOf(request.getParameter("id_etad"));
                            PonderacionDAO ponderacionDAO = new PonderacionDAO();
                            KPIOperativosDAO kpioDAO = new KPIOperativosDAO();
                            List<PetPonderacionObjetivoOperativo> listPonObj = ponderacionDAO.getPonderacionObejtivos(year);
                            
                            csvOutput.write("Clasificacion");
                            csvOutput.write("KPI");
                            csvOutput.write("Ponderacion");
                            csvOutput.endRecord();
                            for(PetPonderacionObjetivoOperativo row:listPonObj){
                                List<PetCatKpiOperativo> listKPIs = kpioDAO.
                                        getKPIOperativosByObjetivoAndEtad(row.getId_objetivo_operativo(), idEtad);
                                if(!listKPIs.isEmpty()){
                                    csvOutput.write("OBJ");
                                    csvOutput.write(row.getObjetivoOperativo().getValor());
                                    csvOutput.write(String.valueOf(row.getPonderacion()));
                                    csvOutput.endRecord();
                                    for(PetCatKpiOperativo field:listKPIs){
                                        csvOutput.write("KPI");
                                        csvOutput.write(field.getValor());
                                        csvOutput.endRecord();
                                    }
                                }
                            }
                        break;
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
    
    public ResponseJson messageForValidate(boolean result, String mensaje){
        ResponseJson response = new ResponseJson();
        if(result){
            response.setMessage(mensaje);
            response.setSucessfull(true);
        }else{
            response.setMessage(mensaje);
            response.setSucessfull(false);
        }
        return response;
    }
}
