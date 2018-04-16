package org.petstar.configurations;

import java.util.Calendar;

/**
 * @author Tech-Pro
 */
public class Validation {
    
    public static boolean validateDay(String day, String month, String year){
        if(day.matches("^[0-9]{1,2}$")){
            
            Calendar calendar = Calendar.getInstance();
            calendar.set(Integer.valueOf(year), ((Integer.valueOf(month) -1)), 1);
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
        }else if(meta.matches("^[0-9]*[.]{0,1}[0-9]{0,2}$")){
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
        }else if(velocidad.matches("^[0-9]*[.]{0,1}[0-9]{0,2}$")){
            return true;
        }else{
            return false;
        }
    }
}
