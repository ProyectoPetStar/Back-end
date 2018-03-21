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
import org.petstar.controller.ControllerMetas;
import org.petstar.model.OutputJson;
import org.petstar.model.ResponseJson;

/**
 *
 * @author Tech-Pro
 */
@WebServlet(name = "Metas", urlPatterns = {"/Metas"})
public class Metas extends HttpServlet {

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
        
        ControllerMetas controllerMetas = new ControllerMetas();
        
        Gson gson = new Gson();
        try {
            String action = request.getParameter("action");
            switch (action) {
                case "getDataMetasCatalog":
                   output = controllerMetas.getMetasDataCarga(request);
                   break;
                case "getDataMetasCatalogById":
                   output = controllerMetas.getMetasDataCargaById(request);
                   break;
                case "insertNewMetaCatalog":
                    output = controllerMetas.insertNewMetaCarga(request);
                    break;
                case "updateNewMetaCatalog":
                    output = controllerMetas.updateMetaCarga(request);
                    break;
                case "loadComboxData":
                    output = controllerMetas.loadCombosCatalogs(request);
                    break;
                case "asignaValorMeta":
                    output = controllerMetas.registraAsignacionMeta(request);
                    break;
                case "getAllAsignacionesByYear":
                    output = controllerMetas.getAllAsignacionesByYear(request);
                    break;
                case "getAsignacionById":
                    output = controllerMetas.getAsignacionById(request);
                    break;
            }
        } catch (Exception ex) {
            ResponseJson reponseJson = new ResponseJson();
            reponseJson.setSucessfull(false);
            reponseJson.setMessage(ex.getMessage());
            output.setResponse(reponseJson);
        } finally {
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
        return "Metas";
    }// </editor-fold>

}
