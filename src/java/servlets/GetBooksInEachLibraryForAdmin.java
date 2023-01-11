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
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
@WebServlet(name = "GetBooksInEachLibraryForAdmin", urlPatterns = {"/GetBooksInEachLibraryForAdmin"})
public class GetBooksInEachLibraryForAdmin extends HttpServlet {

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
            EditBooksTable bookTable = new EditBooksTable();
            EditBooksInLibraryTable bkt = new EditBooksInLibraryTable();
            EditLibrarianTable lbt = new EditLibrarianTable();
            ArrayList<Book> bookList = bookTable.databaseToBooks();
            ArrayList<BookInLibrary> bkl = bkt.databaseToBooksInLibrary();
            ArrayList<String> res = new ArrayList<String>();
            Book book;
            BookInLibrary bookL;
            Librarian library;
            String bookISBN, bookLISBN, libname;
            int libID;

            Map<String, Integer> result = new HashMap<String, Integer>();

            if (!bookList.isEmpty() && !bkl.isEmpty()) {
                response.setStatus(200);

                for (int i = 0; i < bookList.size(); i++) {
                    book = bookList.get(i);
                    bookISBN = book.getIsbn();
                    for (int j = 0; j < bkl.size(); j++) {
                        bookL = bkl.get(j);
                        bookLISBN = bookL.getIsbn();
                        if (bookISBN.equals(bookLISBN)) {
                            libID = bookL.getLibrary_id();
                            library = lbt.databaseToLibrarianID(libID);
                            libname = library.getLibraryname();
                            res.add(libname);
                        }
                    }
                }

                for (String s : res) {
                    Integer k = result.get(s);
                    result.put(s, (k == null) ? 1 : k + 1);
                }

                out.println(result);
            } else {
                response.setStatus(403);
            }
        } catch (SQLException ex) {
            Logger.getLogger(GetStudent.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GetStudent.class.getName()).log(Level.SEVERE, null, ex);
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
