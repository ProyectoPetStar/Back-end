package org.petstar.configurations;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import static org.petstar.configurations.utils.obtenerAnio;
import static org.petstar.configurations.utils.obtenerMes;

/**
 * @author Tech-Pro
 */
public class Validation {
    
    public static boolean validateDay(String day, int month, int anio){
        if(day.matches("^[0-9]{1,2}$")){
            SimpleDateFormat formatoDelTexto = new SimpleDateFormat("dd/MM/yyyy");
            String strfecha = "01/" + month + "/" + anio;
            Date fecha = null;
            try {
                fecha = formatoDelTexto.parse(strfecha);
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
            int year = obtenerAnio(fecha);
            int mes = obtenerMes(fecha);
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, mes -1, 1);
            int lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            
            if(Integer.valueOf(day)>lastDay || Integer.valueOf(day)<1){
                return false;
            }else{
                return true;
            }
        }else{
            return false;
        }
    }
    
    public static boolean validateTurno(String turno){
        if(turno.matches("^[1-3]$")){
            return true;
        }else{
            return false;
        }
    }
    
    public static boolean validateGrupo(String grupo){
        if(grupo.matches("^[A-D]$")){
            return true;
        }else{
            return false;
        }
    }
    
    public static boolean validateMetaMolido(String meta){
        if(meta.equals("")){
            return false;
        }else if(meta.matches("^[0-9]*[.]{0,1}[0-9]{0,3}$")){
                return true;
        }else{
            return false;
        }
    }
    
    public static boolean validateTMP(String tmp){
        if(tmp.equals("")){
            return false;
        }else if(tmp.matches("^[0-9]*[.]{0,1}[0-9]{0,2}$")){
            return true;
        }else{
            return false;
        }
    }
    
    public static boolean validateVelocidad(String velocidad){
        if(velocidad.equals("")){
            return false;
        }else if(velocidad.matches("^[0-9]*[.]{0,1}[0-9]{0,3}$")){
            return true;
        }else{
            return false;
        }
    }
    
    public static boolean validateDecimales(String valor){
        if(valor.equals("")){
            return false;
        }else if(valor.matches("^[0-9]*[.]{0,1}[0-9]{0,4}$")){
            return true;
        }else{
            return false;
        }
    }
    
    public static boolean validateEnteros(String valor){
        boolean bandera = false;
        if(valor.equals("")){
            bandera = false;
        }else if(valor.matches("^[0-9]{0,3}$")){
            int num = Integer.valueOf(valor);
            if(num >= 0 && num <= 100){
                bandera = true;
            }else{
                bandera = false;
            }
        }
        return bandera;
    }
}
