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
import mainClasses.Student;

/**
 *
 * @author kostas
 */
@WebServlet(name = "ActiveBorrowingForLibrarian", urlPatterns = {"/ActiveBorrowingForLibrarian"})
public class ActiveBorrowingForLibrarian extends HttpServlet {

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
        String libname = request.getParameter("libname");
        EditLibrarianTable lt = new EditLibrarianTable();
        EditBorrowingTable bt = new EditBorrowingTable();
        EditBooksInLibraryTable eblt = new EditBooksInLibraryTable();
        EditBooksTable ebt = new EditBooksTable();
        EditStudentsTable st = new EditStudentsTable();
        ArrayList<Borrowing> bors = new ArrayList<>();
        ArrayList<Book> res = new ArrayList<>();
        try (PrintWriter out = response.getWriter()) {
            int id = lt.databaseToLibrarianId(libname).getLibrary_id();
            bors = bt.borrowed_returnedBor();
            for (int i = 0; i < bors.size(); i++) {
                BookInLibrary bil = eblt.databaseToBookInLibraryBasedBCID(bors.get(i).getBookcopy_id());
                if (bil.getLibrary_id() == id) {
                    int userid = bors.get(i).getUser_id();
                    Student s = st.idToStud(userid);
                    String firstname = s.getFirstname();
                    String lastname = s.getLastname();
                    String email = s.getEmail();
                    String uni = s.getUniversity();
                    String tel = s.getTelephone();
                    String status = bors.get(i).getStatus();
                    Book b = ebt.databaseToBooksISBNBook(bil.getIsbn());
                    res.add(b);
                }
            }

            if (!res.isEmpty()) {
                out.println(ebt.booksToJson(res));
                response.setStatus(200);
            } else {
                response.setStatus(403);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ActiveBorrowingForLibrarian.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ActiveBorrowingForLibrarian.class.getName()).log(Level.SEVERE, null, ex);
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
