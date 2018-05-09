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
import org.petstar.controller.ControllerProduccion;
import org.petstar.model.OutputJson;
import org.petstar.model.ResponseJson;

/**
 *
 * @author Tech-Pro
 */
@WebServlet(name = "Produccion", urlPatterns = {"/Produccion"})
public class Produccion extends HttpServlet {

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
        
        PrintWriter printWriter = response.getWriter();
        OutputJson output = new OutputJson();
        ControllerProduccion controllerProduccion = new ControllerProduccion();
        Gson gson = new Gson();
        
        try{
            String action = request.getParameter("action");
            switch(action){
                case "loadCombobox":
                    output = controllerProduccion.loadCombobox(request);
                    break;
                case "getProduccionByPeriodo":
                    output = controllerProduccion.getProduccionByPeriodo(request);
                    break;
                case "insertProduccion":
                    output = controllerProduccion.insertProduccion(request);
                    break;
                case "getDetailsByIdMeta":
                    output = controllerProduccion.getDetailsByIdMeta(request);
                    break;
                case "getDetailsProduccion":
                    output = controllerProduccion.getDetailsProducion(request);
                    break;
                case "updateProduccion":
                    output = controllerProduccion.updateProduccion(request);
                    break;
                case "getProducuccionForLiberar":
                    output = controllerProduccion.getProduccionForLiberar(request);
                    break;
                case "liberarDatos":
                    output = controllerProduccion.liberarDatos(request);
                    break;
            }
        }catch(Exception ex){
            ResponseJson responseJson = new ResponseJson();
            responseJson.setMessage(ex.getMessage());
            responseJson.setSucessfull(false);
            output.setResponse(responseJson);
        }finally{
            printWriter.print(gson.toJson(output));
            printWriter.close();
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
        return "Produccion";
    }// </editor-fold>

}
