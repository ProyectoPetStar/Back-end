/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.petstar.configurations;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Tech-Pro
 */
public class utils {
    
    public static String getCurrentDayByTurno(int turno){
        String validDay;
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        
        if(turno == 3) {
            validDay = dateFormat.format(date.getTime()-86400000);
        }else{
            validDay = dateFormat.format(date);
        }
        
        return validDay;
    }
    
    public static String getDateCorrect(String fecha){
        String[] cadena = fecha.split("/");
        
        String year = cadena[2];
        String mont = cadena[1];
        String day  = cadena[0];
        
        String newFecha = mont + "/" + day + "/" + year;
        
        return newFecha;
    }
    
    public static boolean isBetween(int x, int lower, int upper) {
        return lower <= x && x <= upper;
    }
}
