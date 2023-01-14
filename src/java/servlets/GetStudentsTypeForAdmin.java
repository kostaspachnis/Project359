/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import com.google.gson.Gson;
import database.tables.EditStudentsTable;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mainClasses.Student;

/**
 *
 * @author kostas
 */
@WebServlet(name = "GetStudentsTypeForAdmin", urlPatterns = {"/GetStudentsTypeForAdmin"})
public class GetStudentsTypeForAdmin extends HttpServlet {

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
        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {
            EditStudentsTable studTable = new EditStudentsTable();
            ArrayList<Student> studList = studTable.databaseToStudents();
            int bsc = 0, msc = 0, phd = 0;
            Dictionary result = new Hashtable();

            if (!studList.isEmpty()) {
                response.setStatus(200);

                for (int i = 0; i < studList.size(); i++) {
                    if (studList.get(i).getStudent_type().equals("BSc")) {
                        bsc += 1;
                        System.out.println("bsc");
                    } else if (studList.get(i).getStudent_type().equals("MSc")) {
                        msc += 1;
                        System.out.println("msc");
                    } else {
                        phd += 1;
                        System.out.println("phd");
                    }
                }

                result.put("BSc", bsc);
                result.put("MSc", msc);
                result.put("PHd", phd);

                Gson gson = new Gson();
                String json = gson.toJson(result);
                out.println(json);

            } else {
                response.setStatus(403);
            }
        } catch (SQLException ex) {
            Logger.getLogger(GetStudentsForAdmin.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GetStudentsForAdmin.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        // processRequest(request, response);
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
