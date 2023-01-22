/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.tables;

import com.google.gson.Gson;
import database.tables.EditBooksTable;
import database.DB_Connection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import mainClasses.BookInLibrary;

/**
 *
 * @author mountant
 */
public class EditBooksInLibraryTable {

    
    public void addBookInLibraryFromJSON(String json) throws ClassNotFoundException{
         BookInLibrary msg=jsonTobookInLibrary(json);
         createNewBookInLibrary(msg);
    }
    public String bookInLibraryToJSON(BookInLibrary tr) {
        Gson gson = new Gson();

        String json = gson.toJson(tr, BookInLibrary.class);
        return json;
    }

    public ArrayList<String> booksInLibraryToJson(ArrayList<BookInLibrary> bts) {
        ArrayList<String> books = new ArrayList<String>();
        for (int i = 0; i < bts.size(); i++) {
            Gson gson = new Gson();
            String json = gson.toJson(bts.get(i), BookInLibrary.class);
            books.add(json);
        }
        return books;
    }

    public BookInLibrary jsonTobookInLibrary(String json) {
        Gson gson = new Gson();
        BookInLibrary tr = gson.fromJson(json, BookInLibrary.class);
        return tr;
    }
    
    public BookInLibrary databaseToBookInLibrary(int id) throws SQLException, ClassNotFoundException{
         Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM booksinlibraries WHERE library_id= '" + id + "'");
            rs.next();
            String json=DB_Connection.getResultsToJSON(rs);
            Gson gson = new Gson();
            BookInLibrary tr  = gson.fromJson(json, BookInLibrary.class);
            return tr;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public void createBooksInLibrary() throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String sql = "CREATE TABLE booksinlibraries "
                + "(bookcopy_id INTEGER not NULL AUTO_INCREMENT, "
                + "isbn  VARCHAR(13) not null,"
                + "library_id INTEGER not null,"
                + "available VARCHAR(5) not null,"
                + "FOREIGN KEY (isbn) REFERENCES books(isbn), "
                + "FOREIGN KEY (library_id) REFERENCES librarians(library_id), "
                + "PRIMARY KEY ( bookcopy_id ))";
        stmt.execute(sql);
        stmt.close();
        con.close();

    }
    
    public void updateBookInLibrary(String bookcopy_id, String available) throws SQLException, ClassNotFoundException{
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        String update="UPDATE booksinlibraries SET available='"+available+"'"+ " WHERE bookcopy_id = '"+bookcopy_id+"'";
        stmt.executeUpdate(update);
    }

    /**
     * Establish a database connection and add in the database.
     *
     * @throws ClassNotFoundException
     */
    public void createNewBookInLibrary(BookInLibrary bi) throws ClassNotFoundException {
        try {
            Connection con = DB_Connection.getConnection();

            Statement stmt = con.createStatement();

            String insertQuery = "INSERT INTO "
                    + " booksinlibraries (isbn,library_id,available) "
                    + " VALUES ("
                    + "'" + bi.getIsbn() + "',"
                    + "'" + bi.getLibrary_id()+ "',"
                    + "'" + bi.getAvailable()+ "'"
                    + ")";
            //stmt.execute(table);
            System.out.println(insertQuery);
            stmt.executeUpdate(insertQuery);
            System.out.println("# The entry was successfully added in the database.");

            /* Get the member id from the database and set it to the member */
            stmt.close();
                 con.close();
        } catch (SQLException ex) {
            Logger.getLogger(EditBooksTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // (Project Function)
    public ArrayList<BookInLibrary> databaseToBooksInLibrary() throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ArrayList<BookInLibrary> bkl = new ArrayList<BookInLibrary>();
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM booksinlibraries");
            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                BookInLibrary tr = gson.fromJson(json, BookInLibrary.class);
                bkl.add(tr);
            }
            return bkl;

        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }
    
    // Returns a book in library based on ISBN (Project Function)
    public BookInLibrary databaseToBookInLibraryISBN(String isbn) throws SQLException, ClassNotFoundException{
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM booksinlibraries WHERE isbn= '" + isbn + "'");
            rs.next();
            String json=DB_Connection.getResultsToJSON(rs);
            Gson gson = new Gson();
            BookInLibrary tr  = gson.fromJson(json, BookInLibrary.class);
            return tr;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    // Returns all available books (Project Function)
    public ArrayList<BookInLibrary> databaseToBooksInLibraryAvailable() throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ArrayList<BookInLibrary> bkl = new ArrayList<BookInLibrary>();
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM booksinlibraries WHERE available='true'");
            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                BookInLibrary tr = gson.fromJson(json, BookInLibrary.class);
                bkl.add(tr);
            }
            return bkl;

        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    // Set availability to true (Project Function)
    public void updateBookAv(String isbn, String av, int libid) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String update = "UPDATE booksinlibraries SET available='" + av + "' WHERE isbn = '" + isbn + "' AND library_id = '" + libid + "'";
        stmt.executeUpdate(update);
    }

    // (Project Function)
    public ArrayList<BookInLibrary> databaseToBooksInLibraryLibid(int libid) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ArrayList<BookInLibrary> bkl = new ArrayList<BookInLibrary>();
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM booksinlibraries WHERE library_id = '" + libid + "'");
            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                BookInLibrary tr = gson.fromJson(json, BookInLibrary.class);
                bkl.add(tr);
            }
            return bkl;

        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    //
    public int isBookin(String isbn, int libid) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ArrayList<BookInLibrary> bkl = new ArrayList<BookInLibrary>();
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM booksinlibraries WHERE library_id = '" + libid + "' AND isbn = '" + isbn + "'");
            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                BookInLibrary tr = gson.fromJson(json, BookInLibrary.class);
                bkl.add(tr);
            }

            if (!bkl.isEmpty()) {
                return 1;
            }

        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return 0;
    }

    // Returns an available book and its libraries(Project Function)
    public ArrayList<BookInLibrary> databaseToBooksInLibraryAvailableLib(String isbn) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ArrayList<BookInLibrary> libs = new ArrayList<BookInLibrary>();
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM booksinlibraries WHERE available='true' AND isbn='" + isbn + "'");
            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                BookInLibrary tr = gson.fromJson(json, BookInLibrary.class);
                libs.add(tr);
            }
            return libs;

        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public BookInLibrary retBook(String isbn, int libid) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM booksinlibraries WHERE library_id = '" + libid + "' AND isbn = '" + isbn + "'");
            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                BookInLibrary tr = gson.fromJson(json, BookInLibrary.class);
                return tr;
            }

        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public ArrayList<BookInLibrary> retBooksFalse(int libid) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ResultSet rs;
        ArrayList<BookInLibrary> bl = new ArrayList<>();
        try {
            rs = stmt.executeQuery("SELECT * FROM booksinlibraries WHERE library_id = '" + libid + "' WHERE available='false'");
            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                BookInLibrary tr = gson.fromJson(json, BookInLibrary.class);
                bl.add(tr);
            }
            return bl;

        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public BookInLibrary databaseToBookInLibraryBasedBCID(int id) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM booksinlibraries WHERE bookcopy_id= '" + id + "'");
            rs.next();
            String json = DB_Connection.getResultsToJSON(rs);
            Gson gson = new Gson();
            BookInLibrary tr = gson.fromJson(json, BookInLibrary.class);
            return tr;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }
}
