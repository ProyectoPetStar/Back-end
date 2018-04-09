/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.petstar.controller;

import javax.servlet.http.HttpServletRequest;
import org.petstar.dao.CatalogosDAO;
import org.petstar.dao.LineasDAO;
import org.petstar.dao.ProductosDAO;
import org.petstar.dto.ResultInteger;
import org.petstar.model.OutputJson;
import org.petstar.model.ProductosAsignacionesResponseJson;
import org.petstar.model.ProductosDataResponseJson;
import org.petstar.model.ResponseJson;
import static org.petstar.configurations.utils.getDateCorrect;
import static org.petstar.configurations.utils.getCurrentDayByTurno;
import static org.petstar.configurations.utils.getTurno;

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
     * Llenado de listas
     * Metodo para cargar los valores de los combos a utilizar
     * @param request
     * @return 
     */
    public OutputJson loadCombobox(HttpServletRequest request){
        int idGrupo = Integer.parseInt(request.getParameter("id_grupo"));
        int idLinea = Integer.parseInt(request.getParameter("id_linea"));
        int perfil = Integer.parseInt(request.getParameter("perfil"));
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        
        try{
            if(autenticacion.isValidToken(request)){
                if(perfil == 5){
                    if(validateIfCanEdit(idGrupo,idLinea)){
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
     * Lista de productos
     * Metodo que devuelve la lista de productos existentes segun el modelo
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
     * Producto en Especifico
     * Metodo que devuelve los datos de un producto en especifico
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
     * Registrar Nuevo Producto
     * Metodo para dar de alta un Nuevo producto en el sistema
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
     * Modificación de Productos
     * Metodo que permite actualizar la información de un producto en especifico
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
     * Asignación de valor a Producto
     * Metodo para asignacion de valores correspondientes por dia a los diferentes productos de la linea
     * @param request
     * @return 
     */
//    public OutputJson registraAsignacionByProducto(HttpServletRequest request){
//        int perfil = Integer.parseInt(request.getParameter("perfil"));
//        int idGrupo = Integer.parseInt(request.getParameter("id_grupo"));
//        int idProducto = Integer.parseInt(request.getParameter("id_producto"));
//        int idTurno = Integer.parseInt(request.getParameter("id_turno"));
//        int idLinea =Integer.parseInt(request.getParameter("id_linea"));
//        String dia = request.getParameter("dia_producto");
//        Float valor = Float.parseFloat(request.getParameter("valor"));
//                
//        ResponseJson response = new ResponseJson();
//        OutputJson output = new OutputJson();
//        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
//        
//        try{
//            if(autenticacion.isValidToken(request)){
//                ProductosDAO productosDAO = new ProductosDAO();
//                if(idTurno == 0){
//                    idTurno = getTurno();
//                }
//                ResultInteger result = productosDAO.validaForRegistrarAsignacion(idProducto, idTurno, dia);
//                if(result.getResult().equals(0)){
//                    if(perfil == 5){
//                        if(validateIfCanEdit(idGrupo, idLinea)){
//                            productosDAO.asignaValorByProducto(idTurno, idGrupo, idProducto, dia, valor);
//                            response.setSucessfull(true);
//                            response.setMessage(MSG_SUCESS);
//                        }else{
//                            response.setMessage("Fuera de Horario.");
//                            response.setSucessfull(false);
//                        }
//                    }else{
//                        productosDAO.asignaValorByProducto(idTurno, idGrupo, idProducto, dia, valor);
//                        response.setSucessfull(true);
//                        response.setMessage(MSG_SUCESS);
//                    }
//                }else{
//                    response.setSucessfull(false);
//                    response.setMessage("Ya existe un valor para ese producto");
//                }
//            }else{
//                response.setSucessfull(false);
//                response.setMessage(MSG_LOGOUT);
//            }
//        } catch(Exception ex){
//            response.setSucessfull(false);
//            response.setMessage(MSG_ERROR + ex.getMessage());
//        }
//        output.setResponse(response);
//        return output;
//    }
    
    /**
     * Metodo que devuelve todas las asignaciones del día
     * @param request
     * @return 
     */
//    public OutputJson getAllAsignacionesByDays(HttpServletRequest request){
//        String fechaInicio = request.getParameter("fecha_inicio");
//        String fechaFin = request.getParameter("fecha_fin");
//        
//        fechaInicio = getDateCorrect(fechaInicio);
//        fechaFin = getDateCorrect(fechaFin);
//        
//        ResponseJson response = new ResponseJson();
//        OutputJson output = new OutputJson();
//        ProductosAsignacionesResponseJson parj = new ProductosAsignacionesResponseJson();
//        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
//        
//        try{
//            if(autenticacion.isValidToken(request)){
//                ProductosDAO productosDAO = new ProductosDAO();
//                
//                parj.setListProductosAsignacion(productosDAO.getAllAsignacionesByDays(fechaInicio, fechaFin));
//                output.setData(parj);
//                response.setSucessfull(true);
//                response.setMessage(MSG_SUCESS);
//            }else{
//                response.setMessage(MSG_LOGOUT);
//                response.setSucessfull(false);
//            }
//        } catch(Exception ex){
//            response.setMessage(MSG_ERROR + ex.getMessage());
//            response.setSucessfull(false);
//        }
//        output.setResponse(response);
//        return  output;
//    }
    
    /**
     * Metodo que modifica una asignacion
     * @param request
     * @return 
     */
//    public OutputJson updateAsignacion(HttpServletRequest request){
//        ResponseJson response = new ResponseJson();
//        OutputJson output = new OutputJson();
//        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
//        
//        try{
//            if(autenticacion.isValidToken(request)){
//                ProductosDAO productosDAO = new ProductosDAO();
//                
//                productosDAO.updateAsignacion();
//                response.setSucessfull(true);
//                response.setMessage(MSG_SUCESS);
//            }else{
//                response.setSucessfull(false);
//                response.setMessage(MSG_LOGOUT);
//            }
//        }catch(Exception ex){
//            response.setSucessfull(false);
//            response.setMessage(MSG_ERROR + ex.getMessage());
//        }
//        output.setResponse(response);
//        return output;
//    }
    
    /**
     * Visualización de Metas
     * Metodo que devuelve el listado de metas dependiendo el perfil, al perfil 3 le muestra todo, al perfil 4 las no liberadas, perfil 5 los del día
     * @param request
     * @return 
     */
    public OutputJson getAllAsignacionesMetasByDays(HttpServletRequest request){
        int perfil = Integer.parseInt(request.getParameter("perfil"));
        String fechaInicio = request.getParameter("fecha_inicio");
        String fechaFin = request.getParameter("fecha_fin");
        int idGrupo = Integer.parseInt(request.getParameter("id_grupo"));
        int idLinea = Integer.parseInt(request.getParameter("id_linea"));
        
        int turno = getTurno();
        fechaInicio = getDateCorrect(fechaInicio);
        fechaFin = getDateCorrect(fechaFin);

        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        
        try{
            if(autenticacion.isValidToken(request)){
                ProductosDAO productosDAO = new ProductosDAO();
                ProductosAsignacionesResponseJson parj = new ProductosAsignacionesResponseJson();
                
                switch(perfil){
                    case 3:
                        parj.setListMetasAsignacion(productosDAO.getAllAsignacionesMetasByDays(fechaInicio, fechaFin));
                        output.setData(parj);
                        response.setSucessfull(true);
                        response.setMessage(MSG_SUCESS);
                        break;
                    case 4:
                        parj.setListMetasAsignacion(productosDAO.getAllAsignacionesMetasByDays(idGrupo, turno, idLinea));
                        output.setData(parj);
                        response.setSucessfull(true);
                        response.setMessage(MSG_SUCESS);
                        break;
                    case 5:
                        if(validateIfCanEdit(idGrupo, idLinea)){
                            parj.setListMetasAsignacion(productosDAO.getAllAsignacionesMetasByDays(idGrupo, turno, idLinea, fechaFin)); 
                            output.setData(parj);
                            response.setSucessfull(true);
                            response.setMessage(MSG_SUCESS);
                        }else{
                            response.setSucessfull(false);
                            response.setMessage("Fuera de horario.");
                        }
                        break;
                }
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
     * Productos para asignar
     * Metodo que devulve la lista de productos que tiene cada linea para que capturen los valores correspondientes
     * @param request
     * @return 
     */
    public OutputJson getAllProductosForAsignacion(HttpServletRequest request){
        String dia = request.getParameter("dia");
        int idTurno = Integer.parseInt(request.getParameter("id_turno"));
        int idGrupo = Integer.parseInt(request.getParameter("id_grupo"));
        int idLinea = Integer.parseInt(request.getParameter("id_linea"));
                
        dia = getDateCorrect(dia);
        
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        
        try{
            if(autenticacion.isValidToken(request)){
                ProductosDAO productosDAO = new ProductosDAO();
                ProductosAsignacionesResponseJson parj = new ProductosAsignacionesResponseJson();
                
                parj.setListProductosAsignacion(productosDAO.getAllProductosByDayAndLineaAndGrupo(idLinea, idTurno, idGrupo, dia));
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
     * Validación para poder capturar
     * Metodo que permite realiza la validacion para que entren los integradores a capturar los valores a los productos
     * @param idGrupo
     * @param idLinea
     * @return
     * @throws Exception 
     */
    public boolean validateIfCanEdit(int idGrupo, int idLinea) throws Exception{
        boolean valid;
        
        int turno = getTurno();
        if(turno != 0){
            String diaMeta = getCurrentDayByTurno(turno);
            
            ProductosDAO productosDAO = new ProductosDAO();
            ResultInteger result = productosDAO.validaGrupoTurno(idGrupo, turno,idLinea, diaMeta);
            
            valid = (result.getResult().equals(1));
        }else{
            valid = false;
        }
        
        return valid;
    }  
    
}
