/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.petstar.configurations;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
    public static java.sql.Date getCurrentDayByTurno(int turno){
        Date date = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(date.getTime());
                
        if(turno == 3) {
            cal.add(Calendar.DATE, -1);
        }
        
        return new java.sql.Date(cal.getTimeInMillis());
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
        
        if((hh>=23 && isBetween(mm, 0, 59)) || (hh<=6 && isBetween(mm, 0, 59))){
            turno = 3;
        }else if((hh>=7 && isBetween(mm, 0, 59)) && (hh<=14 && isBetween(mm, 0, 59))){
            turno = 1;
        }else if((hh>=15 && isBetween(mm, 0, 59)) && (hh<=22 && isBetween(mm, 0, 59))){
            turno = 2;
        }else{
            turno = 0;
        }
               
        return turno;
    }
    /**
     * Turno Acual para salvar produccion
     * Metodo que devuelve el turno actual de acuerdo a la hora actual del sistema
     * @return 
     */
    public static int getTurnoForSaveProduction(){
        
        Date date = new Date();
        DateFormat hourFormat = new SimpleDateFormat("HH");
        DateFormat minuFormat = new SimpleDateFormat("mm");
        
        int hh = Integer.parseInt(hourFormat.format(date));
        int mm = Integer.parseInt(minuFormat.format(date));
        
        int turno;
        
        if((hh==23 && isBetween(mm, 11, 59)) || (isBetween(hh, 0, 6)) || (hh==7 && isBetween(mm, 0, 10))){
            turno = 3;
        }else if((hh==7 && isBetween(mm, 11, 59)) || (isBetween(hh, 8, 14)) || (hh==15 && isBetween(mm, 0, 10))){
            turno = 1;
        }else if((hh==15 && isBetween(mm, 11, 59)) ||(isBetween(hh, 16, 22)) || (hh==23 && isBetween(mm, 0, 10))){
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
    
    public static java.sql.Date getDateFirstDay(int anio, int mes) {
        Calendar calendario=Calendar.getInstance();
        calendario.set(anio, mes-1, 1);
        java.sql.Date fecha = new java.sql.Date(calendario.getTimeInMillis());
        return fecha;
    }
    
    public static java.sql.Date getDateLastDay(int anio, int mes) {
        Calendar calendario=Calendar.getInstance();
        calendario.set(anio, mes-1, getUltimoDiaMes(anio, mes));
        java.sql.Date fecha = new java.sql.Date(calendario.getTimeInMillis());
        
        return fecha;
    }
    
    public static int getUltimoDiaMes (int anio, int mes) {
        Calendar cal=Calendar.getInstance();
	cal.set(anio, mes-1, 1);
	return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
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
    
    public static java.sql.Date getCurrentDate(){
        java.sql.Date fecha = new java.sql.Date(Calendar.getInstance().getTime().getTime());
        return fecha;
    }
    
    public static java.sql.Date sumarFechasDias(java.sql.Date fch, int dias) {
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(fch.getTime());
        cal.add(Calendar.DATE, dias);
        return new java.sql.Date(cal.getTimeInMillis());
    }
    
    /**
     * 
     * @param fch
     * @param dias
     * @return 
     */
    public static java.util.Date sumarFechasDias(java.util.Date fch, int dias) {
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(fch.getTime());
        cal.add(Calendar.DATE, dias);
        return new java.util.Date(cal.getTimeInMillis());
    }
    
    public static String convertSqlToDay(java.sql.Date fecha){
        SimpleDateFormat format = new SimpleDateFormat("dd");
        String sFecha = format.format(fecha);
        
        return sFecha;
    }
    
    public static String convertSqlToDay(java.sql.Date fecha, SimpleDateFormat formato){
        String sFecha = formato.format(fecha);
        
        return sFecha;
    }
    
    public static java.util.Date convertStringToDate(String fecha, SimpleDateFormat formato) throws ParseException{
        SimpleDateFormat format = new SimpleDateFormat("HH:mm"); 
        java.util.Date hora = format.parse(fecha);
        //java.util.Date hora = format.parse(fecha);
        Calendar calendario = GregorianCalendar.getInstance();
	calendario.setTime(hora);
	java.util.Date diaCompleto = calendario.getTime();
        
        return diaCompleto;
    }
    
    public static BigDecimal getTiempoParo(java.util.Date fechaInicio, java.util.Date fechaTermino){
        Calendar calFechaInicial=Calendar.getInstance();
        Calendar calFechaFinal=Calendar.getInstance();
        
        calFechaInicial.setTime(fechaInicio);
        calFechaFinal.setTime(fechaTermino);
        
        BigDecimal minutos = new BigDecimal(cantidadTotalMinutos(calFechaInicial, calFechaFinal));
        BigDecimal divisor = new BigDecimal(60);
        BigDecimal tiempo = minutos.divide(divisor, 2, RoundingMode.CEILING);
                
        return tiempo;
    }
          
    public static long cantidadTotalMinutos(Calendar fechaInicial, Calendar fechaFinal) {
        long totalMinutos = 0;
	totalMinutos = ((fechaFinal.getTimeInMillis() - fechaInicial.getTimeInMillis()) / 1000 / 60);
	return totalMinutos;
    }
    
    public static BigDecimal getTotalHoras(Date fechaInicio, Date fechaTermino){
        if(fechaInicio.equals(fechaTermino)){
            return new BigDecimal(24);
        }else{
            Calendar calFechaInicial=Calendar.getInstance();
            Calendar calFechaFinal=Calendar.getInstance();
            fechaInicio= sumarFechasDias(fechaInicio, -1);
            calFechaInicial.setTime(fechaInicio);
            //fechaTermino=sumarFechasDias(fechaTermino,1);
            calFechaFinal.setTime(fechaTermino);

            BigDecimal totalMilisegundos = new BigDecimal(calFechaFinal.getTimeInMillis() - calFechaInicial.getTimeInMillis());
            BigDecimal totalDias = totalMilisegundos.divide(new BigDecimal(3600*24*1000), RoundingMode.CEILING);
            BigDecimal totalHoras = totalDias.multiply(new BigDecimal(24));

            return totalHoras;
        }
    }
    
    public static BigDecimal getPorcentajeParo(BigDecimal tiempoParo, BigDecimal tiempoDisponible){
        BigDecimal calculo = tiempoParo.divide(tiempoDisponible, 5, RoundingMode.CEILING);
        BigDecimal porcentaje = calculo.multiply(new BigDecimal(100), new MathContext(10));
        return  porcentaje;
    }
    
    public static String getNameOfMes(int mes) {
        String[] meses = {"ENERO","FEBRERO","MARZO","ABRIL"
                ,"MAYO","JUNIO","JULIO","AGOSTO","SEPTIEMBRE"
	        ,"OCTUBRE","NOVIEMBRE","DICIEMBRE"};
        
        return meses[mes - 1];
    }
}
