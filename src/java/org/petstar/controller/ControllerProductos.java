package org.petstar.controller;

import javax.servlet.http.HttpServletRequest;
import org.petstar.dao.CatalogosDAO;
import org.petstar.dao.LineasDAO;
import org.petstar.dao.ProductosDAO;
import org.petstar.dto.ResultInteger;
import org.petstar.model.OutputJson;
import org.petstar.model.ProductosDataResponseJson;
import org.petstar.model.ResponseJson;
import org.petstar.dto.ProductosDTO;
import org.petstar.dto.UserDTO;

/**
 *
 * @author Tech-Pro
 */
public class ControllerProductos {
    private static final String MSG_SUCESS = "OK";
    private static final String MSG_LOGOUT = "Inicie sesión nuevamente";
    private static final String MSG_ERROR  = "Descripción de error: ";
    private static final String MSG_INVALID= "Ya existe un producto con los mismos valores.";
    private static final String MSG_NO_EXIST= "El producto no existe.";
    private static final String TABLE_PRODUC= "pet_cat_producto";
    private static final String TABLE_TIPPRO= "pet_cat_tipo_producto";
    
    /**
     * Lista de productos
     * Metodo que devuelve la lista de productos existentes segun el modelo
     * @param request
     * @return 
     */
    public OutputJson getAllProductos(HttpServletRequest request){
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        
        try{
            UserDTO sesion = autenticacion.isValidToken(request);
            if(sesion != null){
                ProductosDAO productosDAO = new ProductosDAO();
                ProductosDataResponseJson data = new ProductosDataResponseJson();
                
                data.setListProductos(productosDAO.getAllProductos());
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
    public OutputJson getProductoById(HttpServletRequest request){
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        
        try{
            int idProducto = Integer.parseInt(request.getParameter("id_producto"));
            
            UserDTO sesion = autenticacion.isValidToken(request);
            if(sesion != null){
                CatalogosDAO catalogosDAO = new CatalogosDAO();
                
                ResultInteger result = catalogosDAO.validaExistID(TABLE_PRODUC, "id_producto", idProducto);
                if(result.getResult().equals(1)){
                    LineasDAO lineasDAO = new LineasDAO();
                    ProductosDAO productosDAO = new ProductosDAO();
                    ProductosDataResponseJson data = new ProductosDataResponseJson();
                
                    data.setProducto(productosDAO.getProductoById(idProducto));
                    data.setListTipoProducto(catalogosDAO.getCatalogos(TABLE_TIPPRO));
                    data.setListLineas(lineasDAO.getLineasData());
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
    public OutputJson insertProductos(HttpServletRequest request){        
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        
        try{
            ProductosDTO newProducto = new ProductosDTO();
            newProducto.setId_linea(Integer.parseInt(request.getParameter("id_linea")));
            newProducto.setValor(request.getParameter("valor"));
            newProducto.setDescripcion(request.getParameter("descripcion"));
            newProducto.setId_tipo_producto(Integer.valueOf(request.getParameter("id_tipo_producto")));
            
            UserDTO sesion = autenticacion.isValidToken(request);
            if(sesion != null){
                ProductosDAO productosDAO = new ProductosDAO();
                ResultInteger result = productosDAO.validaForInsert(newProducto);
                if(result.getResult().equals(0)){
                    productosDAO.insertProducto(newProducto);
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
    public OutputJson updateProductos(HttpServletRequest request){
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        
        try{
            ProductosDTO producto = new ProductosDTO();
            producto.setId_producto(Integer.valueOf(request.getParameter("id_producto")));
            producto.setId_linea(Integer.valueOf(request.getParameter("id_linea")));
            producto.setId_tipo_producto(Integer.valueOf(request.getParameter("id_tipo_producto")));
            producto.setValor(request.getParameter("valor"));
            producto.setDescripcion(request.getParameter("descripcion"));
            
            UserDTO sesion = autenticacion.isValidToken(request);
            if(sesion != null){
                ProductosDAO productosDAO = new ProductosDAO();
                ResultInteger result = productosDAO.validaForUpdate(producto);
                if(result.getResult().equals(0)){
                    productosDAO.updateProducto(producto);
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
    
    public OutputJson blockProducto(HttpServletRequest request){
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        
        try{
            int idProducto = Integer.valueOf(request.getParameter("id_producto"));
            int activo = Integer.valueOf(request.getParameter("activo"));
            
            UserDTO sesion = autenticacion.isValidToken(request);
            if(sesion != null){
                ProductosDAO productosDAO = new ProductosDAO();
                productosDAO.blockProducto(idProducto, activo);
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
    
    public OutputJson loadLIsts(HttpServletRequest request){
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        
        try{
            UserDTO sesion = autenticacion.isValidToken(request);
            if(sesion != null){
                CatalogosDAO catalogosDAO = new CatalogosDAO();
                LineasDAO lineasDAO = new LineasDAO();
                ProductosDataResponseJson data = new ProductosDataResponseJson();
                
                data.setListTipoProducto(catalogosDAO.getCatalogos(TABLE_TIPPRO));
                data.setListLineas(lineasDAO.getLineasData());
                output.setData(data);
                response.setSucessfull(true);
                response.setMessage(MSG_SUCESS);
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
}
