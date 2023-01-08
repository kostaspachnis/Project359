/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import database.tables.EditLibrarianTable;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import mainClasses.JSON_Converter;
import mainClasses.Librarian;
import database.tables.EditStudentsTable;
/**
 *
 * @author kostas
 */
@WebServlet(name = "RegisterLibrarian", urlPatterns = {"/RegisterLibrarian"})
public class RegisterLibrarian extends HttpServlet {

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
        // processRequest(request, response);
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

        JSON_Converter jc = new JSON_Converter();
        Librarian librarian = jc.jsonToLibrarian(request.getReader());
        EditLibrarianTable librarianTable = new EditLibrarianTable();
        EditStudentsTable studentTable = new EditStudentsTable();

        try {
            if (librarianTable.usernameToLibrarian(librarian.getUsername()) != null || studentTable.usernameToStudent(librarian.getUsername()) != null) {
                response.setStatus(403);
            } else if (librarianTable.emailToLibrarian(librarian.getEmail()) != null || studentTable.emailToStudent(librarian.getEmail()) != null) {
                response.setStatus(403);
            } else {
                librarianTable.addNewLibrarian(librarian);
                response.setStatus(200);
            }
        } catch (ClassNotFoundException | SQLException e) {
            // TODO Auto-generated catch block
        }
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
