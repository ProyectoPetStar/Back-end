/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.petstar.controller;

import io.jsonwebtoken.lang.Strings;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.petstar.dao.CatalogosDAO;
import org.petstar.dao.LineasDAO;
import org.petstar.dao.ProductosDAO;
import org.petstar.dto.MetasAsignacionDTO;
import org.petstar.dto.ResultInteger;
import org.petstar.model.OutputJson;
import org.petstar.model.ProductosAsignacionesResponseJson;
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
        int idLinea = Integer.parseInt(request.getParameter("id_linea"));
        String medida = request.getParameter("tipo_medida");
        String producto = request.getParameter("producto");
        
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        
        try{
            if(autenticacion.isValidToken(request)){
                ProductosDAO productosDAO = new ProductosDAO();
                ResultInteger result = productosDAO.validaForInsertCarProducto(idLinea, producto);
                if(result.getResult().equals(0)){
                    productosDAO.insertNewCarProducto(idLinea, producto, medida);
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
        int idProducto = Integer.parseInt(request.getParameter("id_producto"));
        int idLinea = Integer.parseInt(request.getParameter("id_linea"));
        String producto = request.getParameter("producto");
        String medida = request.getParameter("tipo_medida");
        int posicion = Integer.parseInt(request.getParameter("posicion"));
        int activo = Integer.parseInt(request.getParameter("activo"));
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        
        try{
            if(autenticacion.isValidToken(request)){
                ProductosDAO productosDAO = new ProductosDAO();
                ResultInteger result = productosDAO.validaForUpdateCarProducto(idProducto, idLinea, producto);
                if(result.getResult().equals(0)){
                    productosDAO.updateCarProducto(idProducto, idLinea, producto, medida, posicion, activo);
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
     * Metodo para asignacion de valores a productos
     * @param request
     * @return 
     */
    public OutputJson registraAsignacionByProducto(HttpServletRequest request){
        int perfil = Integer.parseInt(request.getParameter("perfil"));
        int idGrupo = Integer.parseInt(request.getParameter("id_grupo"));
        int idProducto = Integer.parseInt(request.getParameter("id_producto"));
        int idTurno = Integer.parseInt(request.getParameter("id_turno"));
        String dia = request.getParameter("dia_producto");
        Float valor = Float.parseFloat(request.getParameter("valor"));
                
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        
        try{
            if(autenticacion.isValidToken(request)){
                ProductosDAO productosDAO = new ProductosDAO();
                if(idTurno == 0){
                    Date date = new Date();
                    DateFormat hourFormat = new SimpleDateFormat("HH:mm");
                    String hora = hourFormat.format(date);
                    idTurno = getTurno(hora);
                }
                ResultInteger result = productosDAO.validaForRegistrarAsignacion(idProducto, idTurno, dia);
                if(result.getResult().equals(0)){
                    if(perfil == 5){
                        if(getCurrentGrupoValido(idGrupo)){
                            productosDAO.asignaValorByProducto(idTurno, idGrupo, idProducto, dia, valor);
                            response.setSucessfull(true);
                            response.setMessage(MSG_SUCESS);
                        }else{
                            response.setMessage("Fuera de Horario.");
                            response.setSucessfull(false);
                        }
                    }else{
                        productosDAO.asignaValorByProducto(idTurno, idGrupo, idProducto, dia, valor);
                        response.setSucessfull(true);
                        response.setMessage(MSG_SUCESS);
                    }
                }else{
                    response.setSucessfull(false);
                    response.setMessage("Ya existe un valor para ese producto");
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
     * Metodo que devuelve todas las asignaciones del día
     * @param request
     * @return 
     */
    public OutputJson getAllAsignacionesByDays(HttpServletRequest request){
        String fechaInicio = request.getParameter("fecha_inicio");
        String fechaFin = request.getParameter("fecha_fin");
        
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        ProductosAsignacionesResponseJson parj = new ProductosAsignacionesResponseJson();
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        
        try{
            if(autenticacion.isValidToken(request)){
                ProductosDAO productosDAO = new ProductosDAO();
                
                parj.setListProductosAsignacion(productosDAO.getAllAsignacionesByDays(fechaInicio, fechaFin));
                output.setData(parj);
                response.setSucessfull(true);
                response.setMessage(MSG_SUCESS);
            }else{
                response.setMessage(MSG_LOGOUT);
                response.setSucessfull(false);
            }
        } catch(Exception ex){
            response.setMessage(MSG_ERROR + ex.getMessage());
            response.setSucessfull(false);
        }
        output.setResponse(response);
        return  output;
    }
    
    /**
     * Metodo que modifica una asignacion
     * @param request
     * @return 
     */
    public OutputJson updateAsignacion(HttpServletRequest request){
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        
        try{
            if(autenticacion.isValidToken(request)){
                ProductosDAO productosDAO = new ProductosDAO();
                
                productosDAO.updateAsignacion();
                response.setSucessfull(true);
                response.setMessage(MSG_SUCESS);
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
    
    public OutputJson getAllAsignacionesMetasByDays(HttpServletRequest request) throws ParseException{
        String fechaInicio = request.getParameter("fecha_inicio");
        String fechaFin = request.getParameter("fecha_fin");
        
        

        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        
        try{
            if(autenticacion.isValidToken(request)){
                ProductosDAO productosDAO = new ProductosDAO();
                ProductosAsignacionesResponseJson parj = new ProductosAsignacionesResponseJson();
                
                parj.setListMetasAsignacion(productosDAO.getAllAsignacionesMetasByDays(fechaInicio, fechaFin));
                output.setData(parj);
                response.setSucessfull(true);
                response.setMessage(MSG_SUCESS);
            }else{
                response.setMessage(MSG_LOGOUT);
                response.setSucessfull(false);
            }
        } catch(Exception ex){
            response.setMessage(MSG_ERROR + ex.getMessage());
            response.setSucessfull(false);
        }
        output.setResponse(response);
        return  output;
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
