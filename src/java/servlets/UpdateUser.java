/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import database.tables.EditStudentsTable;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.sql.SQLException;
import mainClasses.Student;

/**
 *
 * @author kdido
 */
@WebServlet(name = "UpdateUser", urlPatterns = {"/UpdateUser"})
public class UpdateUser extends HttpServlet {

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
        System.out.print(firstname + "----------------" + lastname);
        String lon = request.getParameter("lon_new");
        String lat = request.getParameter("lat_new");
        PrintWriter out = response.getWriter();
        Student student = new Student();

        EditStudentsTable studentsTable = new EditStudentsTable();

        try {

            if (!password.equals("")) {
                studentsTable.updateStudentPass(username, password);
            }

            if (!address.equals("")) {
                studentsTable.updateStudentAdd(username, address);
            }


            if (!personalpage.equals("")) {
                studentsTable.updateStudentPP(username, personalpage);
            }

            if (!gender.equals("")) {
                studentsTable.updateStudentGender(username, gender);
            }

            if (!birthdate.equals("")) {
                studentsTable.updateStudentBD(username, birthdate);
            }

            if (!country.equals("")) {
                studentsTable.updateStudentCountry(username, country);
            }

            if (!city.equals("")) {
                studentsTable.updateStudentCity(username, city);
            }
            if (!firstname.equals("")) {
                studentsTable.updateStudentFN(username, firstname);
            }

            if (!lastname.equals("")) {
                studentsTable.updateStudentLN(username, lastname);
            }
            if (!phone.equals("")) {
                studentsTable.updateStudentPhone(username, phone);
            }


            if (!lon.equals("")) {
                studentsTable.updateStudentLon(username, lon);
            }

            if (!lat.equals("")) {
                studentsTable.updateStudentLat(username, lat);
            }

            student = studentsTable.databaseToStudent_ret(username);
            String output = studentsTable.studentToJSON(student);
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
