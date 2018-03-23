/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.petstar.controller;

import io.jsonwebtoken.lang.Strings;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.petstar.dao.CatalogosDAO;
import org.petstar.dao.LineasDAO;
import org.petstar.dao.ProductosDAO;
import org.petstar.dto.ResultInteger;
import org.petstar.model.OutputJson;
import org.petstar.model.ProductosDataResponseJson;
import org.petstar.model.ResponseJson;

/**
 *
 * @author Tech-Pro
 */
public class ControllerProductos {
    private static final String MSG_SUCESS = "OK";
    private static final String MSG_LOGOUT = "Inicie sesión nuevamente";
    private static final String MSG_ERROR  = "Descripción de error: ";
    private static final String MSG_INVALID= "Ya existe un producto con esos valores.";
    private static final String MSG_NO_EXIST= "El producto no existe.";
    private static final String TABLE_TURNOS= "pet_cat_turnos";
    private static final String TABLE_GRUPOS= "pet_cat_grupos";
    
    /**
     * Metodo para cargar las listas de los combos
     * @param request
     * @return 
     */
    public OutputJson loadCombobox(HttpServletRequest request){
        int idGrupo = Integer.parseInt(request.getParameter("id_grupo"));
        int perfil = Integer.parseInt(request.getParameter("perfil"));
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        
        try{
            if(autenticacion.isValidToken(request)){
                if(perfil == 5){
                    if(getCurrentGrupoValido(idGrupo)){
                        ProductosDAO productosDAO = new ProductosDAO();
                        CatalogosDAO catalogosDAO = new CatalogosDAO();
                        LineasDAO lineasDAO = new LineasDAO();
                        ProductosDataResponseJson pdrj = new ProductosDataResponseJson();

                        pdrj.setListGrupos(catalogosDAO.getCatalogos(TABLE_GRUPOS));
                        pdrj.setListTurnos(catalogosDAO.getCatalogos(TABLE_TURNOS));
                        pdrj.setListProductos(productosDAO.getDataCarProductos());
                        pdrj.setListLineas(lineasDAO.getLineasData());

                        output.setData(pdrj);
                        response.setSucessfull(true);
                        response.setMessage(MSG_SUCESS);
                    }else{
                        response.setSucessfull(false);
                        response.setMessage("Fuera de horario.");
                    }
                }else{
                    ProductosDAO productosDAO = new ProductosDAO();
                    CatalogosDAO catalogosDAO = new CatalogosDAO();
                    LineasDAO lineasDAO = new LineasDAO();
                    ProductosDataResponseJson pdrj = new ProductosDataResponseJson();

                    pdrj.setListGrupos(catalogosDAO.getCatalogos(TABLE_GRUPOS));
                    pdrj.setListTurnos(catalogosDAO.getCatalogos(TABLE_TURNOS));
                    pdrj.setListProductos(productosDAO.getDataCarProductos());
                    pdrj.setListLineas(lineasDAO.getLineasData());

                    output.setData(pdrj);
                    response.setSucessfull(true);
                    response.setMessage(MSG_SUCESS);
                }
            }else{
                response.setSucessfull(false);
                response.setMessage(MSG_LOGOUT);
            }
        }catch(Exception ex){
            response.setSucessfull(false);
            response.setMessage(MSG_ERROR + ex.getMessage());
        }
        output.setResponse(response);
        return output;
    }
    
    /**
     * Metodo que devuelve la lista de productos
     * @param request
     * @return 
     */
    public OutputJson getAllCarProductos(HttpServletRequest request){
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        
        try{
            if(autenticacion.isValidToken(request)){
                ProductosDAO productosDAO = new ProductosDAO();
                ProductosDataResponseJson data = new ProductosDataResponseJson();
                
                data.setListProductos(productosDAO.getDataCarProductos());
                output.setData(data);
                
                response.setSucessfull(true);
                response.setMessage(MSG_SUCESS);
            }else{
                response.setSucessfull(false);
                response.setMessage(MSG_LOGOUT);
            }
        } catch (Exception ex){
            response.setSucessfull(false);
            response.setMessage(MSG_ERROR + ex.getMessage());
        }
        
        output.setResponse(response);
        return output;
    }
    
    /**
     * Metodo que devuelve un producto en especifico
     * @param request
     * @return 
     */
    public OutputJson getCarProductoById(HttpServletRequest request){
        int idProducto = Integer.parseInt(request.getParameter("id_producto"));
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        
        try{
            if(autenticacion.isValidToken(request)){
                ProductosDAO productosDAO = new ProductosDAO();
                ResultInteger result = productosDAO.validaIfExistCarProducto(idProducto);
                if(result.getResult().equals(1)){
                    ProductosDataResponseJson data = new ProductosDataResponseJson();
                
                    data.setProducto(productosDAO.getDataCarProductosById(idProducto));
                    output.setData(data);
                    response.setSucessfull(true);
                    response.setMessage(MSG_SUCESS);
                }else{
                    response.setSucessfull(false);
                    response.setMessage(MSG_NO_EXIST);
                }
            }else{
                response.setSucessfull(false);
                response.setMessage(MSG_LOGOUT);
            }
        } catch(Exception ex){
            response.setSucessfull(false);
            response.setMessage(MSG_ERROR + ex.getMessage());
        }
        output.setResponse(response);
        return output;
    }
    
    /**
     * Metodo para insertar un Nuevo producto
     * @param request
     * @return 
     */
    public OutputJson insertNewCarProductos(HttpServletRequest request){
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        
        try{
            if(autenticacion.isValidToken(request)){
                ProductosDAO productosDAO = new ProductosDAO();
                ResultInteger result = productosDAO.validaForInsertCarProducto();
                if(result.getResult().equals(0)){
                    productosDAO.insertNewCarProducto();
                    response.setSucessfull(true);
                    response.setMessage(MSG_SUCESS);
                }else{
                    response.setSucessfull(false);
                    response.setMessage(MSG_INVALID);
                }
            }else{
                response.setSucessfull(false);
                response.setMessage(MSG_LOGOUT);
            }
        } catch(Exception ex){
            response.setSucessfull(false);
            response.setMessage(MSG_ERROR + ex.getMessage());
        }
        output.setResponse(response);
        return output;
    }
    
    /**
     * Metodo que permite modificar un producto
     * @param request
     * @return 
     */
    public OutputJson updateCarProductos(HttpServletRequest request){
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        
        try{
            if(autenticacion.isValidToken(request)){
                ProductosDAO productosDAO = new ProductosDAO();
                ResultInteger result = productosDAO.validaForUpdateCarProducto();
                if(result.getResult().equals(0)){
                    productosDAO.updateCarProducto();
                    response.setSucessfull(true);
                    response.setMessage(MSG_SUCESS);
                }else{
                    response.setSucessfull(false);
                    response.setMessage(MSG_INVALID);
                }
            }else{
                response.setSucessfull(false);
                response.setMessage(MSG_LOGOUT);
            }
        } catch (Exception ex){
            response.setSucessfull(false);
            response.setMessage(MSG_ERROR + ex.getMessage());
        }
        output.setResponse(response);
        return output;
    }
    
    /**
     * Metodo que permite la eliminacion de un producto
     * @param request
     * @return 
     */
    public OutputJson deleteCarProductos(HttpServletRequest request){
        int idProducto = Integer.parseInt(request.getParameter("id_producto"));
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        
        try{
            if(autenticacion.isValidToken(request)){
                ProductosDAO productosDAO = new ProductosDAO();
                ResultInteger result = productosDAO.validaIfExistCarProducto(idProducto);
                if(result.getResult().equals(1)){
                    productosDAO.deleteCarProducto(idProducto);
                    response.setSucessfull(true);
                    response.setMessage(MSG_SUCESS);
                }else{
                    response.setSucessfull(false);
                    response.setMessage(MSG_NO_EXIST);
                }
            }else{
                response.setSucessfull(false);
                response.setMessage(MSG_LOGOUT);
            }
        } catch(Exception ex){
            response.setSucessfull(false);
            response.setMessage(MSG_ERROR + ex.getMessage());
        }
        output.setResponse(response);
        return output;
    }
    
    /**
     * Metodo que permite la validacion para que entren los usuarios a capturar
     * @param idGrupo
     * @return
     * @throws Exception 
     */
    public boolean getCurrentGrupoValido(int idGrupo) throws Exception{
        boolean valid;
        String diaMeta;
        Date date = new Date();
        
        DateFormat hourFormat = new SimpleDateFormat("HH:mm");
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String hora = hourFormat.format(date);

        int turno = getTurno(hora);
        if(turno != 0){
            if(turno == 3) {
                diaMeta = dateFormat.format(date.getTime()-86400000);
            }else{
                diaMeta = dateFormat.format(date);
            }
            
            ProductosDAO productosDAO = new ProductosDAO();
            ResultInteger result = productosDAO.validaGrupoTurno(idGrupo, turno, diaMeta);
            if(result.getResult().equals(1)){
                valid = true;
            }else{
                valid = false;
            }
        }else{
            valid = false;
        }
        
        return valid;
    }
    
    /**
     * Metodo que devulve el turno segun la hora del sistema
     * @param hora
     * @return 
     */
    public int getTurno(String hora){
        int turno;
        String[] horas = Strings.split(hora, ":");
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
    
    /**
     * Metodo para realizar comparaciones
     * @param x
     * @param lower
     * @param upper
     * @return 
     */
    public static boolean isBetween(int x, int lower, int upper) {
        return lower <= x && x <= upper;
    }
}
