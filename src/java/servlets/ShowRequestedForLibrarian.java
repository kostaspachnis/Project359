/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import com.google.gson.Gson;
import database.tables.EditBooksInLibraryTable;
import database.tables.EditBooksTable;
import database.tables.EditBorrowingTable;
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
import mainClasses.Book;
import mainClasses.BookInLibrary;
import mainClasses.Borrowing;

/**
 *
 * @author kostas
 */
@WebServlet(name = "ChangeRequestedForLibrarian", urlPatterns = {"/ChangeRequestedForLibrarian"})
public class ShowRequestedForLibrarian extends HttpServlet {

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
        String isbn = request.getParameter("isbn");
        EditLibrarianTable lt = new EditLibrarianTable();
        EditBorrowingTable bt = new EditBorrowingTable();
        EditBooksInLibraryTable eblt = new EditBooksInLibraryTable();
        EditBooksTable ebt = new EditBooksTable();
        try (PrintWriter out = response.getWriter()) {
            ArrayList<Borrowing> bors = bt.requestedBor();
            ArrayList<Integer> ids = new ArrayList<>();
            int id = lt.databaseToLibrarianId(request.getParameter("libname")).getLibrary_id();
            ArrayList<BookInLibrary> books = eblt.retBooksFalse(id);
            ArrayList<BookInLibrary> store = new ArrayList<>();
            ArrayList<Book> res = new ArrayList<>();

            for (int i = 0; i < bors.size(); i++) {
                for (int j = 0; j < books.size(); j++) {
                    if (books.get(j).getBookcopy_id() == bors.get(i).getBookcopy_id()) {
                        ids.add(books.get(j).getBookcopy_id());
                    }
                }
            }

            for (int i = 0; i < ids.size(); i++) {
                BookInLibrary b = eblt.databaseToBookInLibraryBasedBCID(ids.get(i));
                store.add(b);
            }

            for (int i = 0; i < store.size(); i++) {
                Book bk = ebt.databaseToBooksISBNBook(store.get(i).getIsbn());
                res.add(bk);
            }

            Gson gson = new Gson();
            String json = gson.toJson(res);
            out.println(json);

        } catch (SQLException ex) {
            Logger.getLogger(ShowRequestedForLibrarian.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ShowRequestedForLibrarian.class.getName()).log(Level.SEVERE, null, ex);
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
