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
import database.tables.EditStudentsTable;
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
import mainClasses.BorrowingBook;
import mainClasses.JSON_Converter;
import mainClasses.Librarian;

/**
 *
 * @author kdido
 */
@WebServlet(name = "BorrowingStatusForStudent1", urlPatterns = {"/BorrowingStatusForStudent1"})
public class BorrowingStatusForStudent1 extends HttpServlet {

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
        String username = request.getParameter("username");
        EditStudentsTable st = new EditStudentsTable();
        EditBorrowingTable bt = new EditBorrowingTable();
        EditLibrarianTable lt = new EditLibrarianTable();
        EditBooksInLibraryTable blt = new EditBooksInLibraryTable();
        EditBooksTable bbt = new EditBooksTable();
        try (PrintWriter out = response.getWriter()) {
            int userid = st.databaseToStudent_ret(username).getUser_id();
            ArrayList<Borrowing> bors = bt.borrowedBorUser(userid);
            ArrayList<BorrowingBook> res = new ArrayList<>();
            for (int i = 0; i < bors.size(); i++) {
                BookInLibrary book = blt.databaseToBookInLibraryBasedBCID(bors.get(i).getBookcopy_id());
                System.out.println("book isbn" + book.getIsbn());
                Librarian l = lt.databaseToLibrarianID(book.getLibrary_id());
                System.out.println("lib name" + l.getLibraryname());
                Book b = bbt.databaseToBooksISBNBook(book.getIsbn());
                System.out.println("title " + b.getTitle());
                BorrowingBook bb = new BorrowingBook();
                bb.setAuthors(b.getAuthors());
                bb.setIsbn(b.getIsbn());
                bb.setLibrary(l.getLibraryname());
                bb.setPhoto(b.getPhoto());
                bb.setStatus("requested");
                bb.setTitle(b.getTitle());
                res.add(bb);
            }
            JSON_Converter jc = new JSON_Converter();
            out.println(jc.booksBorToJson(res));
            response.setStatus(200);
        } catch (SQLException ex) {
            Logger.getLogger(BorrowingStatusForStudent.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BorrowingStatusForStudent.class.getName()).log(Level.SEVERE, null, ex);
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
