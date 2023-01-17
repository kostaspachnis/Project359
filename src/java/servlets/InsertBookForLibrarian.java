/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import database.tables.EditBooksInLibraryTable;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mainClasses.JSON_Converter;
import mainClasses.Book;
import database.tables.EditBooksTable;
import database.tables.EditLibrarianTable;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import mainClasses.BookInLibrary;
import mainClasses.Librarian;

/**
 *
 * @author kostas
 */
@WebServlet(name = "InsertBookForLibrarian", urlPatterns = {"/InsertBookForLibrarian"})
public class InsertBookForLibrarian extends HttpServlet {

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
            String username = request.getParameter("username");
            EditLibrarianTable lt = new EditLibrarianTable();
            Librarian libman = lt.databaseToLibrarianId(username);
            int id = libman.getLibrary_id();
            JSON_Converter jc = new JSON_Converter();
            Book book = jc.jsonToBook(request.getReader());
            EditBooksInLibraryTable blt = new EditBooksInLibraryTable();
            BookInLibrary book2 = new BookInLibrary();

            book2.setIsbn(book.getIsbn());
            book2.setAvailable("false");
            book2.setLibrary_id(id);
            EditBooksTable bkt = new EditBooksTable();

            if (bkt.databaseToBooksISBNBook(book.getIsbn()) != null) {
                response.setStatus(205);
            } else {
                bkt.createNewBook(book);
                blt.createNewBookInLibrary(book2);
                response.setStatus(200);
            }
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
        } catch (SQLException ex) {
            Logger.getLogger(InsertBookForLibrarian.class.getName()).log(Level.SEVERE, null, ex);
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
