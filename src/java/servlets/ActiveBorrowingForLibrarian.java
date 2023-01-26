/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import database.tables.EditBooksInLibraryTable;
import database.tables.EditBooksTable;
import database.tables.EditBorrowingTable;
import database.tables.EditLibrarianTable;
import database.tables.EditStudentsTable;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
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
        ArrayList<BookInLibrary> bbs = new ArrayList<>();
        ArrayList<Borrowing> borst = new ArrayList<>();
        ArrayList<Student> sts = new ArrayList<>();
        ArrayList<String> stats = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("yy/MM/dd");
        try {
            int id = lt.databaseToLibrarianId(libname).getLibrary_id();
            System.out.println(id);
            bors = bt.borrowed_returnedBor();
            for (int i = 0; i < bors.size(); i++) {
                BookInLibrary bil = eblt.databaseToBookInLibraryBasedBCID(bors.get(i).getBookcopy_id());
                if (bil.getLibrary_id() == id) {
                    bbs.add(bil);
                    Student stu = st.idToStud(bors.get(i).getUser_id());
                    sts.add(stu);
                    stats.add(bors.get(i).getStatus());
                    borst.add(bors.get(i));
                }
            }

            if (bbs.isEmpty()) {
                response.setStatus(403);
            } else {
                OutputStream out = new FileOutputStream("librarian.pdf");
                Document doc = new Document();
                PdfWriter writer = PdfWriter.getInstance(doc, out);
                doc.open();
                for (int i = 0; i < bbs.size(); i++) {
                    String first_name = sts.get(i).getFirstname();
                    System.out.println("fn= " + first_name);
                    String last_name = sts.get(i).getLastname();
                    String university = sts.get(i).getUniversity();
                    String email = sts.get(i).getEmail();
                    String tel = sts.get(i).getTelephone();
                    String isbn = bbs.get(i).getIsbn();
                    System.out.println(isbn);
                    Book b = ebt.databaseToBooksISBNBook(isbn);
                    String title = b.getTitle();
                    System.out.println("title= " + title);
                    String photo = b.getPhoto();
                    String status = stats.get(i);
                    String fromdate = borst.get(i).getFromDate();
                    String todate = borst.get(i).getToDate();
                    String pdfout = "";
                    int identifier = i + 1;
                    pdfout += "Borrowing Status " + identifier + "\n";
                    pdfout += "Student's First Name: " + first_name + "\n";
                    pdfout += "Student's Last Name: " + last_name + "\n";
                    pdfout += "Student's University: " + university + "\n";
                    pdfout += "Student's Email: " + email + "\n";
                    pdfout += "Student's Telephone: " + tel + "\n";
                    pdfout += "Book's ISBN: " + isbn + "\n";
                    pdfout += "Book's Title: " + title + "\n";
                    pdfout += "Book's Photo: " + photo + "\n";
                    pdfout += "Book's Borrowing Status: " + status + "\n";
                    pdfout += "Book's From Date: " + fromdate + "\n";
                    pdfout += "Book's To Date: " + todate + "\n\n";

                    doc.add(new Paragraph(pdfout));
                }
                doc.close();
                response.setStatus(200);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ActiveBorrowingForLibrarian.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ActiveBorrowingForLibrarian.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DocumentException ex) {
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
