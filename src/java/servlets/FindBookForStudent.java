/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import database.tables.EditBooksTable;
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

/**
 *
 * @author kdido
 */
@WebServlet(name = "FindBookForStudent", urlPatterns = {"/FindBookForStudent"})
public class FindBookForStudent extends HttpServlet {

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

        String genre = request.getParameter("genre");
        String author = request.getParameter("author");
        if (author.equals("")) {
            author = null;
        }
        String title = request.getParameter("title");
        if (title.equals("")) {
            title = null;
        }
        int fY;
        if (request.getParameter("fromYear").equals("")) {
            fY = 0;
        } else {
            fY = Integer.parseInt(request.getParameter("fromYear"));
        }
        int tY;
        if (request.getParameter("toYear").equals("")) {
            tY = 0;
        } else {
            tY = Integer.parseInt(request.getParameter("toYear"));
        }
        int fP;
        if (request.getParameter("fromPage").equals("")) {
            fP = 0;
        } else {
            fP = Integer.parseInt(request.getParameter("fromPage"));
        }
        int tP;
        if (request.getParameter("toPage").equals("")) {
            tP = 0;
        } else {
            tP = Integer.parseInt(request.getParameter("toPage"));
        }

        EditBooksTable ebt = new EditBooksTable();
        ArrayList<Book> res = new ArrayList<Book>();

        try (PrintWriter out = response.getWriter()) {
            res = ebt.databaseToBooks(genre, fY, tY, title, author, fP, tP);
            if (res == null) {
                response.setStatus(403);
            } else {
                response.setStatus(200);
                out.println(ebt.booksToJson(res));
            }
        } catch (SQLException ex) {
            Logger.getLogger(FindBookForStudent.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FindBookForStudent.class.getName()).log(Level.SEVERE, null, ex);
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
