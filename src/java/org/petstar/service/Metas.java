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
 * Servlet de Metas
 * @author Tech-Pro
 */
@WebServlet(name = "Metas", urlPatterns = {"/Metas"})
public class Metas extends HttpServlet {

    /**
     * Permite trabajar con las opciones de Metas, 
     * se permite el CRUD de Metas, así como tambien las asignaciones de las mismas
     * Procesa las peticiones HTTP, ya sean métodos <code>GET</code> o <code>POST</code>
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
                case "getAllMetas":
                   output = controllerMetas.getAllMetas(request);
                   break;
                case "getMetaById":
                   output = controllerMetas.getMetaById(request);
                   break;
                case "insertNewMeta":
                    output = controllerMetas.insertNewMeta(request);
                    break;
                case "updateMeta":
                    output = controllerMetas.updateMeta(request);
                    break;
                case "deleteMeta":
                    output = controllerMetas.deleteMeta(request);
                    break;
                case "loadCombobox":
                    output = controllerMetas.loadCombobox(request);
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
