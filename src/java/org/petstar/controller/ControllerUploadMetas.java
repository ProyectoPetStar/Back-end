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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import static org.petstar.configurations.Validation.*;
import org.petstar.dao.ForecastDAO;
import org.petstar.dto.ForecastDTO;

/**
 *
 * @author Tech-Pro
 */
public class ControllerUploadMetas {
    private List<ForecastDTO> listRows = new ArrayList<>();
    
    public OutputJson uploadFileMetas(HttpServletRequest request) 
            throws FileNotFoundException, IOException, ParseException, Exception {
        
        OutputJson output = new OutputJson();
        ResponseJson response = new ResponseJson();
        StringBuilder stringFile = new StringBuilder();
        stringFile.append(request.getParameter("file"));
        String monthAndYear = request.getParameter("mes_anio");
        int idLinea =  Integer.parseInt(request.getParameter("id_linea"));
        
        SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
        Date date = new Date();
        String nameFile = formato.format(date) + ".csv";
        
        boolean save = saveFIle(stringFile, nameFile);
        
        if(save){
            boolean valid = validateFile(nameFile, monthAndYear, idLinea);
            if(valid){
                ForecastDAO forecastDAO = new ForecastDAO();
                forecastDAO.loadForecast(listRows);
                response.setMessage("OK");
                response.setSucessfull(true);
                
            }else{
                response.setMessage("Error. El archivo tiene errores.");
                response.setSucessfull(false);
                deleteFiles(nameFile);
            }
        }else{
            response.setMessage("Error al carga el archivo.");
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
    
    public boolean validateFile(String nameFile, String monthAndYear, int idLinea) 
            throws FileNotFoundException, IOException, ParseException{
        
        String pathFile = Configuration.PATH_UPLOAD_FILE + nameFile;
        boolean bandera = true;
        try {
            
            CsvReader csvReader = new CsvReader(pathFile);
            csvReader.readHeaders();
             
            while (csvReader.readRecord()) {
                boolean validDay = validateDay(csvReader.get("Dia"), monthAndYear);
                boolean validTurno = validateTurno(csvReader.get("Turno"));
                boolean validGrupo = validateGrupo( csvReader.get("Grupo"));
                boolean validMeta = validateMetaMolido(csvReader.get("Meta"));
                boolean validTmp = validateTMP(csvReader.get("TMP"));
                boolean validVel = validateVelocidad(csvReader.get("Vel"));
                
                if(!validDay || !validTurno || !validGrupo || !validMeta || !validTmp || !validVel){
                    bandera = false;
                }else{
                    SimpleDateFormat formatoDelTexto = new SimpleDateFormat("dd/MM/yyyy");
                    String strfecha = csvReader.get("Dia") + "/" + monthAndYear;
                    Date fecha = null;
                    try {
                        fecha = formatoDelTexto.parse(strfecha);
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }
                    java.sql.Date sDate = convertUtilToSql(fecha);
                    listRows.add(new ForecastDTO(sDate, csvReader.get("Turno"),
                            csvReader.get("Grupo"),idLinea,
                            Float.parseFloat(csvReader.get("Meta")),
                            Float.parseFloat(csvReader.get("TMP")),
                            Float.parseFloat(csvReader.get("Vel"))
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
    
    public java.sql.Date convertUtilToSql(java.util.Date uDate) {

		java.sql.Date sDate = new java.sql.Date(uDate.getTime());
		return sDate;
	}
    
    public void saveRows(String dia, String mes, String turno, String grupo, 
            int idLinea, String meta, String tmp, String vel) throws ParseException{
    }

    public List<ForecastDTO> getListRows() {
        return listRows;
    }

    public void setListRows(List<ForecastDTO> listRows) {
        this.listRows = listRows;
    }
}
