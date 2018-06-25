/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.petstar.configurations;

import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;

/**
 * Clase que permite definir atributos y metodos de trabajo; 
 * @author Tech-Pro
 */
public class PoolDataSource {

    private static final String USERNAME = "mxetad";
    private static final String PASSWORD = "Tech-Pr0+18";
    private static final String BD = "etad";
    private static final String HOSTNAME = "52.205.249.130";
    //private static final String HOSTNAME = "192.168.60.2";
    private static final String PORT = "1433";
    private static final String CLASS_NAME = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static final String URL = "jdbc:sqlserver://" + HOSTNAME + ":" + PORT + ";DatabaseName=" + BD + ";user=" + USERNAME + ";Password=" + PASSWORD + "";

    private static BasicDataSource dataSource;

    /**
     * Conexion a DB
     * Metodo que se encarga de realizar la conexion a la base de datos, 
     * según los atributos de arriba.
     * @return 
     */
    public static synchronized DataSource getDataSource() {
        if (dataSource == null) {
            BasicDataSource bds = new BasicDataSource();
            bds.setDriverClassName(CLASS_NAME);
            bds.setUrl(URL);
            bds.setMinIdle(5);
            bds.setMaxIdle(10);
            bds.setMaxOpenPreparedStatements(100);
            dataSource = bds;
        }
        return dataSource;
    }

}
