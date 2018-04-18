package org.petstar.configurations;


import javax.servlet.http.HttpServletResponse;

/**
 * Clase que permite definir variables de directorios de trabajo; 
 * asi como las cabeceras de los servlets.
 * @author admin
 */

public class Configuration {
    
    public static final String HEADER_STRING = "Authorization";
    public static final String PATH_UPLOAD_FILE = "C:\\petstar\\upfiles\\";
    
    /**
     * Método que permite establecer las cabeceras de un servlet, en particular 
     * este método define el cruce de dominios y el tipo de retorno del servlet 
     * como un formato JSON. 
     *
     * @param response respuesta del servlet*/
    public static void setHeadersJson(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type , Authorization");
        response.setHeader("Allow", "GET, HEAD, POST, TRACE, OPTIONS");
        response.setContentType("application/json;charset=UTF-8");
    }
    
    /**
     * Método que permite establecer las cabeceras de un servlet, en particular 
     * este método define el cruce de dominios y el tipo de retorno del servlet 
     * como un archivo sin formato especifico. 
     *
     * @param response respuesta del servlet*/
    public static void setHeadersFile(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setHeader("Allow", "GET, HEAD, POST, TRACE, OPTIONS");
    }
    
    /**
     * Método que permite establecer las cabeceras de un servlet, en particular 
     * este método define el cruce de dominios y el tipo de retorno del servlet 
     * como un formato CSV. 
     * @param response 
     */
    public static void setHeadersCSV(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setHeader("Allow", "GET, HEAD, POST, TRACE, OPTIONS");
        response.setCharacterEncoding("charset=ISO-8859-1"); 
        response.setContentType("application/csv;charset=ISO-8859-1");
    }
}