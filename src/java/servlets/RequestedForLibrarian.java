/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

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
@WebServlet(name = "RequestedForLibrarian", urlPatterns = {"/RequestedForLibrarian"})
public class RequestedForLibrarian extends HttpServlet {

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
        String isbn = request.getParameter("isbn");
        EditLibrarianTable lt = new EditLibrarianTable();
        EditBorrowingTable bt = new EditBorrowingTable();
        EditBooksInLibraryTable eblt = new EditBooksInLibraryTable();
        EditBooksTable ebt = new EditBooksTable();
        try (PrintWriter out = response.getWriter()) {
            ArrayList<Borrowing> bors = bt.requestedBor();

            ArrayList<BookInLibrary> ids = new ArrayList<>();
            int id = lt.databaseToLibrarianId(request.getParameter("libname")).getLibrary_id();
            System.out.print("libid= ");
            System.out.println(id);

            ArrayList<BookInLibrary> books = eblt.retBooksFalse(id);
            ArrayList<Book> res = new ArrayList<>();

            int i;
            for (i = 0; i < bors.size(); i++) {
                for (int j = 0; j < books.size(); j++) {
                    if (books.get(j).getBookcopy_id() == bors.get(i).getBookcopy_id()) {
                        System.out.println(books.get(j).getBookcopy_id());
                        System.out.println(bors.get(i).getBookcopy_id());
                        ids.add(books.get(j));
                    }
                }
            }

            System.out.println(i);

            for (int j = 0; j < ids.size(); j++) {
                Book bk = ebt.databaseToBooksISBNBook(ids.get(j).getIsbn());
                System.out.println("title= " + bk.getTitle());
                res.add(bk);
            }
            out.println(ebt.booksToJson(res));

        } catch (SQLException ex) {
            Logger.getLogger(RequestedForLibrarian.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RequestedForLibrarian.class.getName()).log(Level.SEVERE, null, ex);
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
        //processRequest(request, response);
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
