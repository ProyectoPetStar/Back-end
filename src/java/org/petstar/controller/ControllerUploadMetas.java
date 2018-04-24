package org.petstar.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.apache.axis.encoding.Base64;
import org.petstar.configurations.Configuration;
import org.petstar.model.OutputJson;
import org.petstar.model.ResponseJson;
import com.csvreader.CsvReader;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import static org.petstar.configurations.Validation.*;
import static org.petstar.configurations.utils.convertUtilToSql;
import org.petstar.dao.CatalogosDAO;
import org.petstar.dao.ForecastDAO;
import org.petstar.dao.LineasDAO;
import org.petstar.dao.PeriodosDAO;
import org.petstar.dto.ForecastDTO;
import org.petstar.dto.PeriodosDTO;
import org.petstar.dto.ResultInteger;
import org.petstar.model.UploadMetasDataResponseJson;

/**
 *
 * @author Tech-Pro
 */
public class ControllerUploadMetas {
    private List<ForecastDTO> listRows = new ArrayList<>();
    private static final String TABLE_GRUPOS = "pet_cat_grupo";
    private static final String TABLE_TURNOS = "pet_cat_turno";
    
    public OutputJson loadCombobox(HttpServletRequest request) throws Exception{
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        
        PeriodosDAO periodosDAO = new PeriodosDAO();
        LineasDAO lineasDAO = new LineasDAO();
        CatalogosDAO catalogosDAO = new CatalogosDAO();
        UploadMetasDataResponseJson data = new UploadMetasDataResponseJson();
        
        data.setListLineas(lineasDAO.getLineasData());
        data.setListPeriodos(periodosDAO.getPeriodos());
        data.setListGrupos(catalogosDAO.getCatalogos(TABLE_GRUPOS));
        data.setListTurnos(catalogosDAO.getCatalogos(TABLE_TURNOS));
        
        output.setData(data);
        response.setMessage("OK");
        response.setSucessfull(true);
        
        output.setResponse(response);
        return output;
    }
    
    public OutputJson uploadFileMetas(HttpServletRequest request) 
            throws FileNotFoundException, IOException, ParseException, Exception {
        
        OutputJson output = new OutputJson();
        ResponseJson response = new ResponseJson();
        StringBuilder stringFile = new StringBuilder();
        stringFile.append(request.getParameter("file"));
        int idPeriodo = Integer.parseInt(request.getParameter("id_periodo"));
        int idLinea =  Integer.parseInt(request.getParameter("id_linea"));
        
        ForecastDAO forecastDAO = new ForecastDAO();
        ResultInteger foundFiles = forecastDAO.validateLoadedFiles(idPeriodo, idLinea);
        
        if(foundFiles.getResult() < 1){
            SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
            Date date = new Date();
            String nameFile = formato.format(date) + ".csv";

            PeriodosDAO periodosDAO = new PeriodosDAO();
            PeriodosDTO periodosDTO = periodosDAO.getPeriodoById(idPeriodo);
            int year = periodosDTO.getAnio();
            int month = periodosDTO.getMes();
            boolean save = saveFIle(stringFile, nameFile);
            if(save){
                boolean valid = validateFile(nameFile, month, year, idLinea);
                if(valid){

                    java.sql.Date fecha = new java.sql.Date(Calendar.getInstance().getTime().getTime());
                    forecastDAO.saveFile(nameFile, idPeriodo, idLinea, 1, fecha);
                    ResultInteger idFile= forecastDAO.getIdFile(nameFile);
                    UploadMetasDataResponseJson data = new UploadMetasDataResponseJson();

                    forecastDAO.loadForecast(listRows, idLinea, idFile.getResult(), idPeriodo);
                    data.setListMetas(listRows);
                    output.setData(data);
                    response.setMessage("OK");
                    response.setSucessfull(true);

                }else{
                    response.setMessage("El archivo contiene errores.");
                    response.setSucessfull(false);
                    deleteFiles(nameFile);
                }
            }else{
                response.setMessage("Error al carga el archivo.");
                response.setSucessfull(false);
            }
        }else{
            response.setMessage("Ya hay archivos cargados para este periodo.");
            response.setSucessfull(false);
        }
        
        output.setResponse(response);
               
        return output;
    }
    
    public boolean saveFIle(StringBuilder file64, String nameFile){
        boolean estatus;
        try{
            StringBuilder stringFile = new StringBuilder();
            stringFile.append(file64);
            String pathFile = Configuration.PATH_UPLOAD_FILE ;
            
            File paths = new File(pathFile);
            
            if(!paths.exists()){
                paths.mkdirs();
            }
                
            byte[] imageBytes = Base64.decode(stringFile.toString());
            
            try (FileOutputStream file = new FileOutputStream(pathFile + nameFile)) {
                file.write(imageBytes);
                file.close();
                String files = pathFile + nameFile;
                File fichero = new File(files);
                
                if(fichero.exists()){
                    estatus = true;
                }else{
                    estatus = false;
                }
            }
           
        }catch(IOException ex){
            ex.printStackTrace();
            estatus=false;
        }
        
        return estatus;
    }
    
    public boolean validateFile(String nameFile, int month, int year, int idLinea) 
            throws FileNotFoundException, IOException, ParseException{
        
        String pathFile = Configuration.PATH_UPLOAD_FILE + nameFile;
        boolean bandera = true;
        try {
            
            CsvReader csvReader = new CsvReader(pathFile);
            csvReader.readHeaders();
             
            while (csvReader.readRecord()) {
                boolean validDay = validateDay(csvReader.get("Dia"), month, year);
                boolean validTurno = validateTurno(csvReader.get("Turno"));
                boolean validGrupo = validateGrupo( csvReader.get("Grupo"));
                boolean validMeta = validateMetaMolido(csvReader.get("Meta"));
                boolean validTmp = validateTMP(csvReader.get("TMP"));
                boolean validVel = validateVelocidad(csvReader.get("Vel"));
                
                if(!validDay || !validTurno || !validGrupo || !validMeta || !validTmp || !validVel){
                    bandera = false;
                }else{
                    SimpleDateFormat formatoDelTexto = new SimpleDateFormat("dd/MM/yyyy");
                    String strfecha = csvReader.get("Dia") + "/" + month + "/" + year;
                    Date fecha = null;
                    try {
                        fecha = formatoDelTexto.parse(strfecha);
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }
                    
                    BigDecimal meta = new BigDecimal(csvReader.get("Meta"));
                    BigDecimal tmp = new BigDecimal(csvReader.get("TMP"));
                    BigDecimal vel = new BigDecimal(csvReader.get("Vel"));
                    java.sql.Date sDate = convertUtilToSql(fecha);
                    
                    listRows.add(new ForecastDTO(sDate, csvReader.get("Turno"),
                            csvReader.get("Grupo"),idLinea, meta, tmp, vel
                    )); 
                }
            }
             
            csvReader.close();
             
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bandera;
    }
    
    public void deleteFiles(String nameFile){
        String pathFile = Configuration.PATH_UPLOAD_FILE + nameFile;
        
        File fichero = new File(pathFile);
        fichero.delete();
    }
    
    public OutputJson procesarFile(HttpServletRequest request) throws Exception{
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        
        int idPeriodo = Integer.parseInt(request.getParameter("id_periodo"));
        int idLinea =  Integer.parseInt(request.getParameter("id_linea"));
        
        ForecastDAO forecastDAO = new ForecastDAO();
        forecastDAO.procesarFile(idLinea, idPeriodo);
        
        response.setMessage("OK");
        response.setSucessfull(true);
        output.setResponse(response);
        
        return output;
    }
       
    public List<ForecastDTO> getListRows() {
        return listRows;
    }

    public void setListRows(List<ForecastDTO> listRows) {
        this.listRows = listRows;
    }
}
