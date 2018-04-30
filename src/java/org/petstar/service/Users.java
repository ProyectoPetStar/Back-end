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
import org.petstar.controller.ControllerUsers;
import org.petstar.model.ResponseJson;
import org.petstar.model.OutputJson;

/**
 * Clase que administra el control de acceso al sistema, en base al usuario y
 * contraseña proporcionados
 *
 * @author Tech-Pro
 */
@WebServlet(name = "Users", urlPatterns = {"/Users"})
public class Users extends HttpServlet {

    /**
     * Procesa las peticiones HTTP, ya sean métodos <code>GET</code> o
     * <code>POST</code> respectivamente.
     *
     * @param request petición servlet
     * @param response respuesta servlet
     * @throws ServletException si se produce un error específico del servlet
     * throws IOException si se produce un error de E / S
     * @throws java.io.IOException
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Configuration.setHeadersJson(response);
    
        PrintWriter out = response.getWriter();
        OutputJson output = new OutputJson();
        ControllerUsers controller = new ControllerUsers();

        Gson gson = new Gson();
        try {
            String action = request.getParameter("action");
            switch (action) {
                case "getUsersSonarh":
                   output =  controller.getUsersSonarh(request);
                   break;
                case "getPerfilSonarh":
                   output =  controller.getPerfilUserSonarhById(request);
                   break;
                case "insertUserETAD":
                    output = controller.insertUsersETAD(request);
                    break;
                case "getUsersETAD":
                    output = controller.getUsersETAD(request);
                    break;
                case "getPerfil":
                    output = controller.getPerfilUserSonarh(request);
                    break;
                case "changePassword":
                    output = controller.changePasswordUser(request);
                    break;
                case "updatePerfil":
                    output = controller.updatePerfilUser(request);
                    break;
                case "deleteUser":
                    output = controller.deleteUsersETAD(request);
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

    // <editor-fold defaultstate="collapsed" desc="Métodos HttpServlet.">
    /**
     * Maneja los métodos HTTP <code>GET</code>.
     *
     * @param request petición servlet
     * @param response respuesta servlet
     * @throws ServletException si se produce un error específico del servlet
     * throws IOException si se produce un error de E / S
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Maneja los métodos HTTP <code>POST</code>.
     *
     * @param request petición servlet
     * @param response respuesta servlet
     * @throws ServletException si se produce un error específico del servlet
     * throws IOException si se produce un error de E / S
     * @throws java.io.IOException
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
     * Proporciona una información breve del servlet.
     *
     * @return String que contiene descripción del servlet
     */
    @Override
    public String getServletInfo() {
        return "Login";
    }
    // </editor-fold>

}
