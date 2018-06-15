package org.petstar.configurations;

import com.csvreader.CsvReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.axis.encoding.Base64;
import org.petstar.model.OutputJson;
import org.petstar.model.ResponseJson;
import static org.petstar.configurations.utils.getCurrentDate;
import static org.petstar.configurations.Validation.validateDecimales;

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
    
    public static OutputJson validateFileMetasEstrategicasAnual(HttpServletRequest request, String nameFile, int usuario) throws Exception{
        OutputJson out = new OutputJson();
        ResponseJson res = new ResponseJson();
        int year = Integer.valueOf(request.getParameter("anio"));
        int idEtad = Integer.valueOf(request.getParameter("id_etad"));
        String pathFile = Configuration.PATH_UPLOAD_FILE + nameFile;
        List<HashMap> listdata = new ArrayList<>();
        
        try {
            CsvReader csvReader = new CsvReader(pathFile);
            csvReader.readHeaders();
            Date date = getCurrentDate();
            res.setSucessfull(true);
            
            while (csvReader.readRecord()) {
                boolean validValor = validateDecimales(csvReader.get("Valor"));
                if(validValor){
                    HashMap map = new HashMap();
                    map.put("meta", csvReader.get("Meta"));
                    map.put("um", csvReader.get("UM"));
                    map.put("valor", csvReader.get("Valor"));
                    map.put("year", year);
                    map.put("idEtad", idEtad);
                    map.put("usuario", usuario);
                    map.put("fecha", date);
                    listdata.add(map);
                    out.setData(listdata);
                }else{
                    res.setSucessfull(false);
                    res.setMessage("Invalido");
                }
            }
            csvReader.close();
        }catch(IOException ex){
            res.setSucessfull(false);
            res.setMessage(ex.getMessage());
        }
        out.setResponse(res);
        return out;
    }
    
    public static OutputJson validateFileObjetivosEstrategicosAnual(HttpServletRequest request, String nameFile, int usuario) throws Exception{
        OutputJson out = new OutputJson();
        ResponseJson res = new ResponseJson();
        int year = Integer.valueOf(request.getParameter("anio"));
        int idEtad = Integer.valueOf(request.getParameter("id_etad"));
        String pathFile = Configuration.PATH_UPLOAD_FILE + nameFile;
        List<HashMap> listdata = new ArrayList<>();
        
        try {
            CsvReader csvReader = new CsvReader(pathFile);
            csvReader.readHeaders();
            Date date = getCurrentDate();
            res.setSucessfull(true);
            
            while (csvReader.readRecord()) {
                boolean validMeta = validateDecimales(csvReader.get("Meta"));
                if(validMeta){
                    HashMap map = new HashMap();
                    map.put("objetivo", csvReader.get("Objetivo"));
                    map.put("um", csvReader.get("UM"));
                    map.put("meta", csvReader.get("Meta"));
                    map.put("year", year);
                    map.put("idEtad", idEtad);
                    map.put("usuario", usuario);
                    map.put("fecha", date);
                    listdata.add(map);
                    out.setData(listdata);
                }else{
                    res.setSucessfull(false);
                    res.setMessage("Invalido");
                }
            }
            csvReader.close();
        }catch(IOException ex){
            res.setSucessfull(false);
            res.setMessage(ex.getMessage());
        }
        out.setResponse(res);
        return out;
    }
    
    public static OutputJson validateFileKPIOperativoAnual(HttpServletRequest request, String nameFile, int usuario) throws Exception{
        OutputJson out = new OutputJson();
        ResponseJson res = new ResponseJson();
        int year = Integer.valueOf(request.getParameter("anio"));
        int idEtad = Integer.valueOf(request.getParameter("id_etad"));
        String pathFile = Configuration.PATH_UPLOAD_FILE + nameFile;
        List<HashMap> listdata = new ArrayList<>();
        
        try {
            CsvReader csvReader = new CsvReader(pathFile);
            csvReader.readHeaders();
            Date date = getCurrentDate();
            res.setSucessfull(true);
            
            while (csvReader.readRecord()) {
                boolean validMeta = validateDecimales(csvReader.get("Meta"));
                if(validMeta){
                    HashMap map = new HashMap();
                    map.put("kpi", csvReader.get("KPI"));
                    map.put("tipo", csvReader.get("Tipo"));
                    map.put("um", csvReader.get("UM"));
                    map.put("meta", csvReader.get("Meta"));
                    map.put("year", year);
                    map.put("idEtad", idEtad);
                    map.put("usuario", usuario);
                    map.put("fecha", date);
                    listdata.add(map);
                    out.setData(listdata);
                }else{
                    res.setSucessfull(false);
                    res.setMessage("Invalido");
                }
            }
            csvReader.close();
        }catch(IOException ex){
            res.setSucessfull(false);
            res.setMessage(ex.getMessage());
        }
        out.setResponse(res);
        return out;
    }
}
