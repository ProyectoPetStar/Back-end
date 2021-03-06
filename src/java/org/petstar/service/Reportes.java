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
import org.petstar.controller.ControllerReportes;
import org.petstar.model.OutputJson;
import org.petstar.model.ResponseJson;

/**
 *
 * @author Tech-Pro
 */
@WebServlet(name = "Reportes", urlPatterns = {"/Reportes"})
public class Reportes extends HttpServlet {

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
        ControllerReportes controllerReportes = new ControllerReportes();
        Gson gson = new Gson();
        
        try{
            String action = request.getParameter("action");
            switch(action){
                case"loadCombobox":
                    output = controllerReportes.loadCombobox(request);
                    break;
                case"getOEEFallasByLinea":
                    output = controllerReportes.getOEEFallasByLinea(request);
                    break;
                case"reporteEficiencia":
                    output = controllerReportes.getReporteEficiencia(request);
                    break;
                case"reporteDiarioProduccion":
                    output = controllerReportes.getReporteDiarioProduccion(request);
                    break;
                case"reporteDailyPerformance":
                    output = controllerReportes.getReportDailyPerformance(request);
                    break;
                case"reporteJUCODI":
                    output = controllerReportes.getReporteJUCODI(request);
                    break;
                case"reporteVelocidadPromedio":
                    output = controllerReportes.getReporteVelocidadPromedio(request);
                    break;
                case"reporteSubproductos":
                    output = controllerReportes.getReporteSubproductos(request);
                    break;
                case"reportePerformance":
                    output = controllerReportes.getReportePerformance(request);
                    break;
                case"reporteFallas":
                    output = controllerReportes.getReporteFallas(request);
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

    /**
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
        return "Reportes";
    }// </editor-fold>

}
