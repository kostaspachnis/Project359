/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import database.tables.EditBooksInLibraryTable;
import database.tables.EditBorrowingTable;
import database.tables.EditStudentsTable;
import java.io.IOException;
import java.util.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mainClasses.BookInLibrary;
import mainClasses.Borrowing;

/**
 *
 * @author kostas
 */
@WebServlet(name = "BorrowBookForStudent", urlPatterns = {"/BorrowBookForStudent"})
public class BorrowBookForStudent extends HttpServlet {

    public static Date nextMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        if (calendar.get(Calendar.MONTH) == Calendar.DECEMBER) {
            calendar.set(Calendar.MONTH, Calendar.JANUARY);
            calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1);
        } else {
            calendar.roll(Calendar.MONTH, true);
        }

        return calendar.getTime();
    }

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
            System.out.println("Inside the servlet");
            EditStudentsTable st = new EditStudentsTable();
            String ids = request.getParameter("libid");
            int id = Integer.parseInt(ids);
            System.out.print("libid= ");
            System.out.println(id);
            String isbn = request.getParameter("isbn");
            System.out.println("isbn= " + isbn);
            String idStud = request.getParameter("username");
            int idSStud = st.databaseToStudent_ret(idStud).getUser_id();
            System.out.print("studid= ");
            System.out.println(idSStud);
            EditBooksInLibraryTable eblt = new EditBooksInLibraryTable();
            EditBorrowingTable brt = new EditBorrowingTable();
            Date today = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yy/MM/dd");
            String date = formatter.format(today);
            System.out.println("date = " + date);

            if (!isbn.equals("")) {
                eblt.updateBookAv(isbn, "false", id);
                BookInLibrary bil = eblt.retBook(isbn, id);
                Borrowing bor = new Borrowing();

                bor.setBookcopy_id(bil.getBookcopy_id());
                bor.setUser_id(idSStud);
                bor.setFromDate(date);
                bor.setToDate(formatter.format(nextMonth(today)));
                bor.setStatus("requested");

                brt.createNewBorrowing(bor);
                response.setStatus(200);
                System.out.println("Successful Insertion!");
            } else {
                response.setStatus(403);
            }
        } catch (SQLException ex) {
            Logger.getLogger(BorrowBookForStudent.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BorrowBookForStudent.class.getName()).log(Level.SEVERE, null, ex);
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
