/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import database.tables.EditLibrarianTable;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mainClasses.Librarian;

/**
 *
 * @author kostas
 */
@WebServlet(name = "UpdateLibrarian", urlPatterns = {"/UpdateLibrarian"})
public class UpdateLibrarian extends HttpServlet {

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
        String username = request.getParameter("username_new");
        String email = request.getParameter("email_new");
        String password = request.getParameter("password_new");
        String address = request.getParameter("address_new");
        String personalpage = request.getParameter("personalpage_new");
        String gender = request.getParameter("gender_new");
        String birthdate = request.getParameter("birthdate_new");
        String country = request.getParameter("country_new");
        String city = request.getParameter("city_new");
        String phone = request.getParameter("telephone_new");
        String firstname = request.getParameter("firstname_new");
        String lastname = request.getParameter("lastname_new");
        String lon = request.getParameter("lon_new");
        String lat = request.getParameter("lat_new");
        PrintWriter out = response.getWriter();
        Librarian librarian = new Librarian();

        EditLibrarianTable libTable = new EditLibrarianTable();

        try {

            if (!password.equals("")) {
                libTable.updateLibrarianPass(username, password);
            }

            if (!address.equals("")) {
                libTable.updateLibrarianAddress(username, address);
            }

            if (!personalpage.equals("")) {
                libTable.updateLibrarianPP(username, personalpage);
            }

            if (!gender.equals("")) {
                libTable.updateLibrarianSex(username, gender);
            }

            if (!birthdate.equals("")) {
                libTable.updateLi(username, birthdate);
            }

            if (!country.equals("")) {
                libTable.updateStudentCountry(username, country);
            }

            if (!city.equals("")) {
                libTable.updateStudentCity(username, city);
            }
            if (!firstname.equals("")) {
                libTable.updateStudentFN(username, firstname);
            }

            if (!lastname.equals("")) {
                libTable.updateStudentLN(username, lastname);
            }
            if (!phone.equals("")) {
                libTable.updateStudentPhone(username, phone);
            }

            if (!lon.equals("")) {
                libTable.updateStudentLon(username, lon);
            }

            if (!lat.equals("")) {
                libTable.updateStudentLat(username, lat);
            }

            student = libTable.databaseToStudent_ret(username);
            String output = libTable.studentToJSON(student);
            out.print(output);
            response.setStatus(200);

        } catch (ClassNotFoundException | SQLException e) {
            // TODO Auto-generated catch block
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
