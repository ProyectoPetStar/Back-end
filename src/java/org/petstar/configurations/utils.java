/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.petstar.configurations;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;

/**
 * Clase que permite definir metodos recurrentes; 
 * @author Tech-Pro
 */
public class utils {
    
    /**
     * Día Actual 
     * Metodo que de acuerdo al turno devuelve el día correcto, esto aplica
     * especialmente para el turno 3 que se debe de resta un día al dia actual
     * @param turno
     * @return 
     */
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
    
    /**
     * Fecha Correcta
     * Metodo que cambia el formato de la fecha recibida por uno valido 
     * para la base de datos
     * @param fecha
     * @return 
     */
    public static String getDateCorrect(String fecha){
        DateFormat formatInput = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat formatOutput = new SimpleDateFormat("MM/dd/yyyy");
	Date date = null;
	
	try {
            date = formatInput.parse(fecha);
	} catch (ParseException e) {
            e.printStackTrace();
	}
		
	String validDay = formatOutput.format(date); 
	return validDay;
    }
    
    /**
     * Between
     * Metodo que verifica que un determinado valor se encuntre entre
     * los valores permitidos
     * @param x
     * @param lower
     * @param upper
     * @return 
     */
    public static boolean isBetween(int x, int lower, int upper) {
        return lower <= x && x <= upper;
    }
    
    /**
     * Turno Acual
     * Metodo que devuelve el turno actual de acuerdo a la hora actual
     * del sistema
     * @return 
     */
    public static int getTurno(){
        
        Date date = new Date();
        DateFormat hourFormat = new SimpleDateFormat("HH");
        DateFormat minuFormat = new SimpleDateFormat("mm");
        
        int hh = Integer.parseInt(hourFormat.format(date));
        int mm = Integer.parseInt(minuFormat.format(date));
        
        int turno;
        
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
    
    public static int obtenerAnio(Date date){
        if (null == date){
            return 0;
        }else{
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
            return Integer.parseInt(dateFormat.format(date));
        }
    }
    
    public static int obtenerMes(Date date){
        if (null == date){
            return 0;
        }else{
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM");
            return Integer.parseInt(dateFormat.format(date));
        }
    }
    
    public static java.sql.Date convertUtilToSql(java.util.Date uDate) {
        java.sql.Date sDate = new java.sql.Date(uDate.getTime());
	return sDate;
    }
    
    public static java.sql.Date convertStringToSql(String fecha) throws ParseException{
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date parsed = format.parse(fecha);
        java.sql.Date sql = new java.sql.Date(parsed.getTime());
        return sql;
    }
}
