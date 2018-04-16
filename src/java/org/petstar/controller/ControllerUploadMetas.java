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
import static org.petstar.configurations.Validation.*;

/**
 *
 * @author Tech-Pro
 */
public class ControllerUploadMetas {
    
    public OutputJson uploadFileMetas(HttpServletRequest request) throws FileNotFoundException, IOException {
        OutputJson output = new OutputJson();
        ResponseJson response = new ResponseJson();
        StringBuilder stringFile = new StringBuilder();
        stringFile.append(request.getParameter("file"));
        
        SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
        Date date = new Date();
        String nameFile = formato.format(date) + ".csv";
        
        boolean save = saveFIle(stringFile, nameFile);
        
        if(save){
            boolean valid = validateFile(nameFile);
            if(valid){
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
    
    public boolean validateFile(String nameFile) throws FileNotFoundException, IOException{
        String pathFile = Configuration.PATH_UPLOAD_FILE + nameFile;
        boolean bandera = true;
        try {
                        
            CsvReader csvReader = new CsvReader(pathFile);
            csvReader.readHeaders();
            String mes = "1";
            String year = "2018";
 
            while (csvReader.readRecord()) {
                boolean validDay = validateDay(csvReader.get("Dia"), mes, year);
                boolean validTurno = validateTurno(csvReader.get("Turno"));
                boolean validGrupo = validateGrupo( csvReader.get("Grupo"));
                boolean validMeta = validateMetaMolido(csvReader.get("Meta"));
                boolean validTmp = validateTMP(csvReader.get("TMP"));
                boolean validVel = validateVelocidad(csvReader.get("Vel"));
                
                if(!validDay || !validTurno || !validGrupo || !validMeta || !validTmp || !validVel){
                    bandera = false;
                    System.out.println("-----------------------------" + (csvReader.getCurrentRecord() + 2) );
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
}
