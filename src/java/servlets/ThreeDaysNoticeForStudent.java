/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import database.tables.EditBooksInLibraryTable;
import database.tables.EditBooksTable;
import database.tables.EditBorrowingTable;
import database.tables.EditStudentsTable;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mainClasses.Book;
import mainClasses.Borrowing;

/**
 *
 * @author kostas
 */
@WebServlet(name = "ThreeDaysNoticeForStudent", urlPatterns = {"/ThreeDaysNoticeForStudent"})
public class ThreeDaysNoticeForStudent extends HttpServlet {

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
        EditBorrowingTable ebt = new EditBorrowingTable();
        ArrayList<Borrowing> bors = new ArrayList<>();
        ArrayList<Book> res = new ArrayList<>();
        EditBooksTable bt = new EditBooksTable();
        EditBooksInLibraryTable eblt = new EditBooksInLibraryTable();
        Date today = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yy/MM/dd");
        try (PrintWriter out = response.getWriter()) {
            int id = st.databaseToStudent_ret(username).getUser_id();
            bors = ebt.requested_borrowedBorUser(id);
            for (int i = 0; i < bors.size(); i++) {
                String todate = bors.get(i).getToDate();
                Date datetodate = formatter.parse(todate);
                long millies = Math.abs(datetodate.getTime() - today.getTime());
                long diffdays = TimeUnit.DAYS.convert(millies, TimeUnit.MILLISECONDS);
                if (diffdays == 3) {
                    Book b = bt.databaseToBooksISBNBook(eblt.databaseToBookInLibraryBasedBCID(bors.get(i).getBookcopy_id()).getIsbn());
                    res.add(b);
                }
            }

            if (res.isEmpty()) {
                response.setStatus(200);
            } else {
                out.println(bt.booksToJson(res));
                response.setStatus(201);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ThreeDaysNoticeForStudent.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ThreeDaysNoticeForStudent.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(ThreeDaysNoticeForStudent.class.getName()).log(Level.SEVERE, null, ex);
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
