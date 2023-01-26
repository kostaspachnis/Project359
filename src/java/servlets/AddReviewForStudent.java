/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import database.tables.EditBooksInLibraryTable;
import database.tables.EditBorrowingTable;
import database.tables.EditReviewsTable;
import database.tables.EditStudentsTable;
import java.io.IOException;
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
import mainClasses.Borrowing;
import mainClasses.Review;

/**
 *
 * @author kostas
 */
@WebServlet(name = "AddReviewForStudent", urlPatterns = {"/AddReviewForStudent"})
public class AddReviewForStudent extends HttpServlet {

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
        String student_username = request.getParameter("username");
        String reviewtxt = request.getParameter("review");
        System.out.println(reviewtxt);
        String score = request.getParameter("score");
        EditStudentsTable st = new EditStudentsTable();
        EditBooksInLibraryTable eblt = new EditBooksInLibraryTable();
        EditReviewsTable rt = new EditReviewsTable();
        EditBorrowingTable bt = new EditBorrowingTable();
        try {
            int no_match = 0;
            int id = st.databaseToStudent_ret(student_username).getUser_id();
            ArrayList<Borrowing> bors = bt.successUserBor(id);
            for (int i = 0; i < bors.size(); i++) {
                int search_id = bors.get(i).getBookcopy_id();
                BookInLibrary b = eblt.databaseToBookInLibraryBasedBCID(search_id);
                if (b.getIsbn().equals(isbn) && b.getAvailable().equals("true")) {
                    Review rev = new Review();
                    rev.setIsbn(isbn);
                    rev.setReviewScore(score);
                    rev.setReviewText(reviewtxt);
                    rev.setUser_id(id);
                    rt.createNewReview(rev);
                    break;
                } else {
                    no_match += 1;
                }
            }
            if (no_match == 0) {
                response.setStatus(200);
            } else {
                response.setStatus(403);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AddReviewForStudent.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AddReviewForStudent.class.getName()).log(Level.SEVERE, null, ex);
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
