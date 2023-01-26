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
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String address = request.getParameter("address");
        String personalpage = request.getParameter("personalpage");
        String gender = request.getParameter("gender");
        String libname = request.getParameter("libraryname");
        String libinfo = request.getParameter("libraryinfo");
        String country = request.getParameter("country");
        String city = request.getParameter("city");
        String phone = request.getParameter("telephone");
        String firstname = request.getParameter("firstname");
        String lastname = request.getParameter("lastname");
        String lon = request.getParameter("lon");
        String lat = request.getParameter("lat");
        String birthdate = request.getParameter("birthdate");
        PrintWriter out = response.getWriter();
        Librarian librarian = new Librarian();

        EditLibrarianTable libTable = new EditLibrarianTable();

        try {
            
            if (!birthdate.equals("")) {
                libTable.updateLibrarianBirthdate(username, birthdate);
            }
            
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

            if (!libname.equals("")) {
                libTable.updateLibrarianLibname(username, libname);
            }
            
            if (!libinfo.equals("")) {
                libTable.updateLibrarianLibinfo(username, libinfo);
            }

            if (!country.equals("")) {
                libTable.updateLibrarianCountry(username, country);
            }

            if (!city.equals("")) {
                libTable.updateLibrarianCity(username, city);
            }
            if (!firstname.equals("")) {
                libTable.updateLibrarianFN(username, firstname);
            }

            if (!lastname.equals("")) {
                libTable.updateLibrarianLN(username, lastname);
            }
            if (!phone.equals("")) {
                libTable.updateLibrarianTel(username, phone);
            }

            if (!lon.equals("")) {
                libTable.updateLibrarianLon(username, lon);
            }

            if (!lat.equals("")) {
                libTable.updateLibrarianLat(username, lat);
            }

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
