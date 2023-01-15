/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import database.tables.EditBooksInLibraryTable;
import database.tables.EditLibrarianTable;
import java.io.IOException;
import java.sql.SQLException;
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
 * @author kdido
 */
@WebServlet(name = "BookAvailabilityLibrarian", urlPatterns = {"/BookAvailabilityLibrarian"})
public class BookAvailabilityLibrarian extends HttpServlet {

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
        
        try {
            String librarian_username = request.getParameter("username");
            EditLibrarianTable lbt = new EditLibrarianTable();
            Librarian lib = lbt.databaseToLibrarianId(librarian_username);
            int id = lib.getLibrary_id();
            EditBooksInLibraryTable blt = new EditBooksInLibraryTable();
            String book_isbn = request.getParameter("isbn");
            BookInLibrary book = blt.databaseToBookInLibraryISBN(book_isbn);
            
            if (book.getAvailable().equals("true")) {
                book.setAvailable("false");
                blt.updateBookAv(book.getIsbn(), "false");
            }
            else if (book.getAvailable().equals("false")) {
                book.setAvailable("true");
                blt.updateBookAv(book.getIsbn(), "true");
            }
            
            response.setStatus(200);
            
        } catch (SQLException ex) {
            Logger.getLogger(BookAvailabilityLibrarian.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BookAvailabilityLibrarian.class.getName()).log(Level.SEVERE, null, ex);
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
