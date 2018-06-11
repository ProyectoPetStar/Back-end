package org.petstar.controller;

import javax.servlet.http.HttpServletRequest;
import org.petstar.dao.CatalogosDAO;
import org.petstar.dao.RazonParoDAO;
import org.petstar.dto.RazonParoDTO;
import org.petstar.dto.ResultInteger;
import org.petstar.dto.UserDTO;
import org.petstar.model.OutputJson;
import org.petstar.model.RazonParoResponseJson;
import org.petstar.model.ResponseJson;

/**
 *
 * @author Tech-Pro
 */
public class ControllerRazonParo {
    private static final String MSG_SUCESS = "OK";
    private static final String MSG_LOGOUT = "Inicie sesión nuevamente";
    private static final String MSG_ERROR  = "Descripción de error: ";
    private static final String MSG_INVALID= "Ya existe una razon de paro con los mismos valores.";
    private static final String MSG_NOEXIST= "La razon de Paro no existe.";
    private static final String TABLE_RAZON= "pet_cat_razon_paro";
    private static final String TABLE_FUENT= "pet_cat_fuentes_paro";
    
    /**
     * Consulta Gemeral
     * Metodo que devulve la lista con todas las razones de paro
     * @param request
     * @return 
     */
    public OutputJson getAllRazon(HttpServletRequest request){
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        
        try{
            UserDTO sesion = autenticacion.isValidToken(request);
            if(sesion != null){
                RazonParoDAO razonParoDAO = new RazonParoDAO();
                RazonParoResponseJson data = new RazonParoResponseJson();
                
                data.setListRazonParo(razonParoDAO.getAllRazones());
                output.setData(data);
                response.setMessage(MSG_SUCESS);
                response.setSucessfull(true);
            }else{
                response.setMessage(MSG_LOGOUT);
                response.setSucessfull(false);
            }
        }catch(Exception ex){
            response.setMessage(MSG_ERROR + ex.getMessage());
            response.setSucessfull(false);
        }
        
        output.setResponse(response);
        return output;
    }
    
    /**
     * Consulta Especifica
     * Metodo que devulve una razon de paro segun el ID
     * @param request
     * @return 
     */
    public OutputJson getRazonById(HttpServletRequest request){
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        
        try{
            int idRazon = Integer.valueOf(request.getParameter("id_razon"));
            UserDTO sesion = autenticacion.isValidToken(request);
            if(sesion != null){
                CatalogosDAO catalogosDAO = new CatalogosDAO();
                ResultInteger result = catalogosDAO.validaExistID(TABLE_RAZON, "id_razon_paro", idRazon);
                if(result.getResult().equals(1)){
                    RazonParoDAO razonParoDAO = new RazonParoDAO();
                    RazonParoResponseJson data = new RazonParoResponseJson();

                    data.setListFuentesParo(catalogosDAO.getCatalogosActive(TABLE_FUENT));
                    data.setRazonParo(razonParoDAO.getRazonById(idRazon));
                    output.setData(data);
                    response.setMessage(MSG_SUCESS);
                    response.setSucessfull(true);
                }else{
                    response.setMessage(MSG_NOEXIST);
                    response.setSucessfull(false);
                }
            }else{
                response.setMessage(MSG_LOGOUT);
                response.setSucessfull(false);
            }
        }catch(Exception ex){
            response.setMessage(MSG_ERROR + ex.getMessage());
            response.setSucessfull(false);
        }
        
        output.setResponse(response);
        return output;
    }
    
    /**
     * Registro de Razones de paro
     * Metodo que se encarga de la validacion y registro de una razon de paro
     * @param request
     * @return 
     */
    public OutputJson insertRazon(HttpServletRequest request){
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        
        try{
            RazonParoDTO newRazon = new RazonParoDTO();
            newRazon.setId_fuente_paro(Integer.valueOf(request.getParameter("id_fuente")));
            newRazon.setDescripcion(request.getParameter("descripcion"));
            newRazon.setValor(request.getParameter("valor"));
            
            UserDTO sesion = autenticacion.isValidToken(request);
            if(sesion != null){
                RazonParoDAO razonParoDAO = new RazonParoDAO();
                
                ResultInteger result = razonParoDAO.validaForInsert(newRazon);
                if(result.getResult().equals(0)){
                    razonParoDAO.insertRazones(newRazon);
                    
                    response.setMessage(MSG_SUCESS);
                    response.setSucessfull(true);
                }else{
                    response.setMessage(MSG_INVALID);
                    response.setSucessfull(false);
                }
            }else{
                response.setMessage(MSG_LOGOUT);
                response.setSucessfull(false);
            }
        }catch(Exception ex){
            response.setMessage(MSG_ERROR + ex.getMessage());
            response.setSucessfull(false);
        }
        
        output.setResponse(response);
        return output;
    }
    
    /**
     * Modificacion de Razones de paro
     * Metodo que se encarga de la validacion y actualizacion de los datos
     * @param request
     * @return 
     */
    public OutputJson updateRazon(HttpServletRequest request){
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        
        try{
            RazonParoDTO newRazon = new RazonParoDTO();
            newRazon.setId_razon_paro(Integer.valueOf(request.getParameter("id_razon")));
            newRazon.setId_fuente_paro(Integer.valueOf(request.getParameter("id_fuente")));
            newRazon.setDescripcion(request.getParameter("descripcion"));
            newRazon.setValor(request.getParameter("valor"));
            
            UserDTO sesion = autenticacion.isValidToken(request);
            if(sesion != null){
                RazonParoDAO razonParoDAO = new RazonParoDAO();
                
                ResultInteger result = razonParoDAO.validaForUpdate(newRazon);
                if(result.getResult().equals(0)){
                    razonParoDAO.updateRazones(newRazon);
                    
                    response.setMessage(MSG_SUCESS);
                    response.setSucessfull(true);
                }else{
                    response.setMessage(MSG_INVALID);
                    response.setSucessfull(false);
                }
            }else{
                response.setMessage(MSG_LOGOUT);
                response.setSucessfull(false);
            }
        }catch(Exception ex){
            response.setMessage(MSG_ERROR + ex.getMessage());
            response.setSucessfull(false);
        }
        
        output.setResponse(response);
        return output;
    }
    
    /**
     * Bloqueo de Razones
     * Metodo para Habilitar o Deshabilitar un registro
     * @param request
     * @return 
     */
    public OutputJson blockRazon(HttpServletRequest request){
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        
        try{
            int idRazon = Integer.valueOf(request.getParameter("id_razon"));
            int activo = Integer.valueOf(request.getParameter("activo"));
                        
            UserDTO sesion = autenticacion.isValidToken(request);
            if(sesion != null){
                RazonParoDAO razonParoDAO = new RazonParoDAO();
                
                razonParoDAO.blockRazones(idRazon, activo);
                response.setMessage(MSG_SUCESS);
                response.setSucessfull(true);
            }else{
                response.setMessage(MSG_LOGOUT);
                response.setSucessfull(false);
            }
        }catch(Exception ex){
            response.setMessage(MSG_ERROR + ex.getMessage());
            response.setSucessfull(false);
        }
        
        output.setResponse(response);
        return output;
    }
    
    /**
     * Llenado de listas
     * Metodo que carga las listas de los catalogos que se usaran
     * @param request
     * @return 
     */
    public OutputJson loadList(HttpServletRequest request){
        ControllerAutenticacion autenticacion = new ControllerAutenticacion();
        ResponseJson response = new ResponseJson();
        OutputJson output = new OutputJson();
        
        try{
            UserDTO sesion = autenticacion.isValidToken(request);
            if(sesion != null){
                CatalogosDAO catalogosDAO = new CatalogosDAO();
                RazonParoResponseJson data = new RazonParoResponseJson();

                data.setListFuentesParo(catalogosDAO.getCatalogosActive(TABLE_FUENT));    
                output.setData(data);
                response.setMessage(MSG_SUCESS);
                response.setSucessfull(true);
            }else{
                response.setMessage(MSG_LOGOUT);
                response.setSucessfull(false);
            }
        }catch(Exception ex){
            response.setMessage(MSG_ERROR + ex.getMessage());
            response.setSucessfull(false);
        }
        
        output.setResponse(response);
        return output;
    }
}
