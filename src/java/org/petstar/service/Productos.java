/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.petstar.service;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.petstar.configurations.Configuration;
import org.petstar.controller.ControllerProductos;
import org.petstar.model.OutputJson;
import org.petstar.model.ResponseJson;

/**
 *
 * @author Tech-Pro 
*/
@WebServlet(name = "Productos", urlPatterns = {"/Productos"})
public class Productos extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Configuration.setHeadersJson(response);
        PrintWriter out = response.getWriter();
        OutputJson output = new OutputJson();
        ControllerProductos controller = new ControllerProductos();
        Gson gson = new Gson();
        
        try{
            String action = request.getParameter("action");
            switch(action){
                case "loadCombobox":
                    output = controller.loadCombobox(request);
                    break;
                case "getAllDataCarProductos":
                    output = controller.getAllCarProductos(request);
                    break;
                case "getCarProductoById":
                    output = controller.getCarProductoById(request);
                    break;
                case "insertNewCarProductos":
                    output = controller.insertNewCarProductos(request);
                    break;
                case "updateCarProductos":
                    output = controller.updateCarProductos(request);
                    break;
                case "getAllAsignacionMetasByDays":
                    output = controller.getAllAsignacionesMetasByDays(request);
                    break;
                case "registraAsignacionByProducto":
                    output = controller.registraAsignacionByProducto(request);
                    break;
                case "getAllAsignacionesByDay":
                    output = controller.getAllAsignacionesByDays(request);
            }
        } catch(Exception ex){
            ResponseJson responseJson = new ResponseJson();
            responseJson.setSucessfull(false);
            responseJson.setMessage(ex.getMessage());
            output.setResponse(responseJson);
        } finally{
            out.print(gson.toJson(output));
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * 
     * @param req
     * @param response
     * @throws ServletException
     * @throws IOException 
     */
    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        Configuration.setHeadersJson(response);
    }
    
    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Productos";
    }// </editor-fold>

}
