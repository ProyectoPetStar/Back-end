package org.petstar.configurations;

import com.csvreader.CsvReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.axis.encoding.Base64;

/**
 *
 * @author Tech-Pro
 */
public class Tools {
    /**
     * Metodo para guardar archivo
     * Este metodo crea un archivo y lo guarda en el path especificado, 
     * se necesita la cadena del archivo codificado en base 64, 
     * y el nombre que va a tomar el archivo.
     * @param encodedFile
     * @param nameFile
     * @return 
     */
    public static boolean saveFIle(StringBuilder encodedFile, String nameFile){
        boolean estatus;
        try{
            StringBuilder stringFile = new StringBuilder();
            stringFile.append(encodedFile);
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
    
    public static List<HashMap> listRows(String nameFile){
        String pathFile = Configuration.PATH_UPLOAD_FILE + nameFile;
        List<HashMap> listdata = new ArrayList<>();
        
        try {
            CsvReader csvReader = new CsvReader(pathFile);
            while (csvReader.readRecord()) {
                HashMap map = new HashMap();
                
                for(int i=0; i <csvReader.getColumnCount(); i++){
                    map.put("column"+i, csvReader.get(i));
                }
                listdata.add(map);
            }
            csvReader.close();
        }catch(IOException ex){
        }
        
        return listdata;
    }
}
