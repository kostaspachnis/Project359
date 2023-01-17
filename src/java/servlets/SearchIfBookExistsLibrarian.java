/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import database.tables.EditBooksInLibraryTable;
import database.tables.EditBooksTable;
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
import mainClasses.Book;
import mainClasses.BookInLibrary;
import mainClasses.Librarian;

/**
 *
 * @author kdido
 */
@WebServlet(name = "SearchIfBookExistsLibrarian", urlPatterns = {"/SearchIfBookExistsLibrarian"})
public class SearchIfBookExistsLibrarian extends HttpServlet {

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

        try {
            String username = request.getParameter("username");
            String isbn = request.getParameter("book_isbn");

            System.out.println("username: " + username);
            System.out.println("isbn: " + isbn);
            
            EditLibrarianTable lt = new EditLibrarianTable();
            Librarian l = lt.databaseToLibrarianId(username);
            int id = l.getLibrary_id();

            EditBooksTable ebt = new EditBooksTable();
            EditBooksInLibraryTable elt = new EditBooksInLibraryTable();
            Book book = ebt.databaseToBooksISBNBook(isbn);
            BookInLibrary book2 = new BookInLibrary();
            book2.setAvailable("false");
            book2.setIsbn(isbn);
            book2.setLibrary_id(id);

            if (book != null && elt.isBookin(isbn, id) == 0) {
                elt.createNewBookInLibrary(book2);
                response.setStatus(201);
            } else if (elt.isBookin(isbn, id) == 1) {
                response.setStatus(405);
            } else {
                response.setStatus(406);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SearchIfBookExistsLibrarian.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SearchIfBookExistsLibrarian.class.getName()).log(Level.SEVERE, null, ex);
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
