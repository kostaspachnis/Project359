/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import database.tables.EditBooksInLibraryTable;
import database.tables.EditBooksTable;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mainClasses.Book;
import mainClasses.BookInLibrary;

/**
 *
 * @author kostas
 */
@WebServlet(name = "GuestUserShowBooks", urlPatterns = {"/GuestUserShowBooks"})
public class GuestUserShowBooks extends HttpServlet {

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

    public boolean containsName(final ArrayList<Book> list, final String isbn) {
        return list.stream().filter(o -> o.getIsbn().equals(isbn)).findFirst().isPresent();
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
            EditBooksInLibraryTable blt = new EditBooksInLibraryTable();
            EditBooksTable bt = new EditBooksTable();

            ArrayList<BookInLibrary> bltList = blt.databaseToBooksInLibraryAvailable();
            ArrayList<Book> btList = new ArrayList<>();

            for (int i = 0; i < bltList.size(); i++) {
                String isbn = bltList.get(i).getIsbn();
                Book book = bt.databaseToBooksISBNBook(isbn);

                if (!containsName(btList, isbn)) {
                    btList.add(book);
                }
            }

            Collections.sort(btList, (o1, o2) -> (o1.getGenre().compareTo(o2.getGenre())));

            if (!btList.isEmpty()) {
                response.setStatus(200);
                out.println(bt.booksToJson(btList));
            } else {
                response.setStatus(403);
            }

        } catch (SQLException ex) {
            Logger.getLogger(GuestUserShowBooks.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GuestUserShowBooks.class.getName()).log(Level.SEVERE, null, ex);
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
