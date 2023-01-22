/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import com.google.gson.Gson;
import database.tables.EditBooksInLibraryTable;
import database.tables.EditLibrarianTable;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mainClasses.BookInLibrary;
import mainClasses.Librarian;

/**
 *
 * @author kostas
 */
@WebServlet(name = "ShowLibrariesForStudent", urlPatterns = {"/ShowLibrariesForStudent"})
public class ShowLibrariesForStudent extends HttpServlet {

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

        try (PrintWriter out = response.getWriter()) {
            String isbn = request.getParameter("isbn");
            System.out.println(isbn);
            EditBooksInLibraryTable eblt = new EditBooksInLibraryTable();
            EditLibrarianTable lt = new EditLibrarianTable();
            ArrayList<BookInLibrary> av_book_libs = eblt.databaseToBooksInLibraryAvailableLib(isbn);
            ArrayList<Librarian> libs = new ArrayList<>();

            int num = 0;
            for (int i = 0; i < av_book_libs.size(); i++) {
                num = av_book_libs.get(i).getLibrary_id();
                Librarian l = lt.databaseToLibrarianID(num);
                System.out.println(l.getLibrary_id());
                libs.add(l);
            }

            Gson gson = new Gson();
            String json = gson.toJson(libs);
            out.println(json);
            response.setStatus(200);

        } catch (SQLException ex) {
            Logger.getLogger(ShowLibrariesForStudent.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ShowLibrariesForStudent.class.getName()).log(Level.SEVERE, null, ex);
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
