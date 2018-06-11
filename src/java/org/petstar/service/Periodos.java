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
import org.petstar.controller.ControllerPeriodos;
import org.petstar.model.OutputJson;
import org.petstar.model.ResponseJson;

/**
 *
 * @author Tech-Pro
 */
@WebServlet(name = "Periodos", urlPatterns = {"/Periodos"})
public class Periodos extends HttpServlet {

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
        ControllerPeriodos controllerPeriodos = new ControllerPeriodos();
        Gson gson = new Gson();
        
        try{
            String action = request.getParameter("action");
            switch(action){
                case"getAllPeriodos":
                    output = controllerPeriodos.getAllPeriodos(request);
                    break;
                case"openPeriodo":
                    output = controllerPeriodos.openPeriodo(request);
                    break;
                case"saveDetailsPeriodo":
                    output = controllerPeriodos.saveDetailsPeriodo(request);
                    break;
                case"closePeriodo":
                    output = controllerPeriodos.changeEstatusPeriodo(request, 1);
                    break;
                case"reOpenPeriodo":
                    output = controllerPeriodos.changeEstatusPeriodo(request, 0);
                    break;
                case"getDetailsByPeriodo":
                    output = controllerPeriodos.getDetailsByPeriodo(request);
                    break;
                case"updateDetailsPeriodo":
                    output = controllerPeriodos.updateDetailsPeriodo(request);
                    break;
            }
        } catch(Exception ex) {
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
        return "Periodos";
    }// </editor-fold>

}
