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
    
    public static int getTurno(){
        
        Date date = new Date();
        DateFormat hourFormat = new SimpleDateFormat("HH:mm");
        String hora = hourFormat.format(date);
        
        int turno;
        String[] horas = hora.split(":");
        int hh = Integer.parseInt(horas[0]);
        int mm = Integer.parseInt(horas[1]);
        
        if((hh==6 && isBetween(mm, 40, 59)) || (hh==7 && isBetween(mm, 0, 11))){
            turno = 3;
        }else if((hh==14 && isBetween(mm, 40, 59)) || (hh==15 && isBetween(mm, 0, 11))){
            turno = 1;
        }else if((hh==22 && isBetween(mm, 40, 59)) || (hh==23 && isBetween(mm, 0, 11))){
            turno = 2;
        }else{
            turno = 0;
        }
               
        return turno;
    }
}
