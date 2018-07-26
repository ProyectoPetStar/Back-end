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
import static org.petstar.configurations.Validation.validateEnteros;
import static org.petstar.configurations.Validation.validateDecimales;
import org.petstar.dao.ETAD.KPIOperativosDAO;
import org.petstar.dto.ETAD.PetCatKpiOperativo;

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
            estatus=false;
        }
        return estatus;
    }
    
    public static OutputJson validateFileKPIOperativo(HttpServletRequest request, String nameFile, int usuario) throws Exception{
        OutputJson out = new OutputJson();
        ResponseJson res = new ResponseJson();
        int idPeriodo = Integer.valueOf(request.getParameter("id_periodo"));
        int idEtad = Integer.valueOf(request.getParameter("id_etad"));
        String pathFile = Configuration.PATH_UPLOAD_FILE + nameFile;
        List<HashMap> listdata = new ArrayList<>();
        
        try {
            CsvReader csvReader = new CsvReader(pathFile);
            csvReader.readHeaders();
            res.setSucessfull(true);
            
            while (csvReader.readRecord()) {
                boolean validMeta = validateDecimales(csvReader.get("Meta"));
                if(validMeta){
                    HashMap map = new HashMap();
                    map.put("kpi", csvReader.get("KPI"));
                    map.put("tipo", csvReader.get("Tipo"));
                    map.put("um", csvReader.get("UM"));
                    map.put("meta", csvReader.get("Meta"));
                    map.put("periodo", idPeriodo);
                    map.put("idEtad", idEtad);
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
    
    public static OutputJson validateFilePonderacionObjetivoOperativo(HttpServletRequest request, String nameFile, int usuario) throws Exception{
        OutputJson out = new OutputJson();
        ResponseJson res = new ResponseJson();
        int year = Integer.valueOf(request.getParameter("anio"));
        String pathFile = Configuration.PATH_UPLOAD_FILE + nameFile;
        List<HashMap> listdata = new ArrayList<>();
        
        try {
            CsvReader csvReader = new CsvReader(pathFile);
            csvReader.readHeaders();
            Date date = getCurrentDate();
            res.setSucessfull(true);
            int total = 0;
            
            while (csvReader.readRecord()) {
                boolean validMeta = validateEnteros(csvReader.get("Ponderacion"));
                if(validMeta){
                    HashMap map = new HashMap();
                    total = total + Integer.valueOf(csvReader.get("Ponderacion"));
                    map.put("objetivo", csvReader.get("Objetivo"));
                    map.put("ponderacion", csvReader.get("Ponderacion"));
                    map.put("year", year);
                    map.put("usuario", usuario);
                    map.put("fecha", date);
                    listdata.add(map);
                }else{
                    res.setSucessfull(false);
                    res.setMessage("Invalido");
                }
            }
            if(total == 100){
                out.setData(listdata);
            }else{
                res.setSucessfull(false);
                res.setMessage("Suma Invalida");
            }
            
            csvReader.close();
        }catch(IOException ex){
            res.setSucessfull(false);
            res.setMessage(ex.getMessage());
        }
        out.setResponse(res);
        return out;
    }
    
    public static OutputJson validateFilePonderacionKPIOperativo(HttpServletRequest request, String nameFile, int usuario) throws Exception{
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
            
            int sumaPonderacion = 0;
            while (csvReader.readRecord()) {
                boolean validMeta = validateEnteros(csvReader.get("Ponderacion"));
                if(validMeta){
                    sumaPonderacion = sumaPonderacion + Integer.valueOf(csvReader.get("Ponderacion"));
                    HashMap map = new HashMap();
                    map.put("tipo", csvReader.get("Clasificacion"));
                    map.put("kpi", csvReader.get("KPI"));
                    map.put("ponderacion", csvReader.get("Ponderacion"));
                    map.put("year", year);
                    map.put("idEtad", idEtad);
                    map.put("usuario", usuario);
                    map.put("fecha", date);
                    listdata.add(map);
                }else{
                    res.setSucessfull(false);
                    res.setMessage("Invalido");
                }
            }
            csvReader.close();
            if(sumaPonderacion == 200){
                out.setData(listdata);
            }else{
                res.setSucessfull(false);
                res.setMessage("Invalido");
            }
        }catch(IOException ex){
            res.setSucessfull(false);
            res.setMessage(ex.getMessage());
        }
        out.setResponse(res);
        return out;
    }
    
    public static boolean validaCorrectFileMetas(int idEtad, String nameFile) throws Exception{
        String pathFile = Configuration.PATH_UPLOAD_FILE + nameFile;
        boolean bandera = true;
        try {
            CsvReader csvReader = new CsvReader(pathFile);
            csvReader.readHeaders();
            KPIOperativosDAO kPIOperativosDAO = new KPIOperativosDAO();
            List<PetCatKpiOperativo> listKPIOperativos = kPIOperativosDAO.getListKPIOperativosByEtad(idEtad);
            ArrayList Lista = new ArrayList();
            for(PetCatKpiOperativo row:listKPIOperativos){
                Lista.add(row.getValor());
            }
            
            while (csvReader.readRecord()) {
                if(!Lista.contains(csvReader.get("KPI"))){
                    bandera = false;
                }
            }
            csvReader.close();
        }catch(IOException ex){
            bandera= false;
        }
        
        return bandera;
    }
}
