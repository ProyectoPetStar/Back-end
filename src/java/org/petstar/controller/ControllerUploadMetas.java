/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.petstar.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.apache.axis.encoding.Base64;
import org.petstar.configurations.Configuration;
import org.petstar.model.OutputJson;
import org.petstar.model.ResponseJson;

/**
 *
 * @author Tech-Pro
 */
public class ControllerUploadMetas {
    
    public OutputJson uploadFileMetas(HttpServletRequest request) throws FileNotFoundException{
        OutputJson output = new OutputJson();
        ResponseJson response = new ResponseJson();
        
        try{
            StringBuilder stringFile = new StringBuilder();
            stringFile.append(request.getParameter("file"));
            String pathFile = Configuration.PATH_UPLOAD_FILE ;
            String nameFile = UUID.randomUUID() + ".csv";
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
                    response.setMessage("OK");
                    response.setSucessfull(true);
                }else{
                    response.setMessage("Error al cargar el archivo.");
                    response.setSucessfull(false);
                }
            }
           
        }catch(IOException ex){
            response.setMessage("Error: " + ex.getMessage());
            response.setSucessfull(false);
        }
        output.setResponse(response);
               
        return output;
    }
}
