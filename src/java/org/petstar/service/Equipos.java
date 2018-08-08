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
import org.petstar.controller.ControllerEquipos;
import org.petstar.model.OutputJson;
import org.petstar.model.ResponseJson;

/**
 *
 * @author Tech-Pro
 */
@WebServlet(name = "Equipos", urlPatterns = {"/Equipos"})
public class Equipos extends HttpServlet {

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
        ControllerEquipos controllerEquipos = new ControllerEquipos();
        Gson gson = new Gson();
        
        try{
            String action = request.getParameter("action");
            switch(action){
                case "getAllEquipos":
                    output = controllerEquipos.getAllEquipos(request);
                    break;
                case "getEquipoById":
                    output = controllerEquipos.getEquipoById(request);
                    break;
                case "insertEquipo":
                    output = controllerEquipos.insertEquipo(request);
                    break;
                case "updateEquipo":
                    output = controllerEquipos.updateEquipo(request);
                    break;
                case "blockEquipo":
                    output = controllerEquipos.blockEquipo(request);
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
        return "Short description";
    }// </editor-fold>

}
