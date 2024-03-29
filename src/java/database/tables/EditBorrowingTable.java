/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.tables;

import com.google.gson.Gson;
import mainClasses.Borrowing;
import database.DB_Connection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mike
 */
public class EditBorrowingTable {

   
    public void addBorrowingFromJSON(String json) throws ClassNotFoundException{
         Borrowing r=jsonToBorrowing(json);
         createNewBorrowing(r);
    }
    
    
     public Borrowing databaseToBorrowing(int id) throws SQLException, ClassNotFoundException{
         Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM borrowing WHERE borrowing_id= '" + id + "'");
            rs.next();
            String json=DB_Connection.getResultsToJSON(rs);
            Gson gson = new Gson();
            Borrowing bt = gson.fromJson(json, Borrowing.class);
            return bt;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }
    
    
    

      
     public Borrowing jsonToBorrowing(String json) {
        Gson gson = new Gson();
        Borrowing r = gson.fromJson(json, Borrowing.class);
        return r;
    }
     
         
      public String borrowingToJSON(Borrowing r) {
        Gson gson = new Gson();

        String json = gson.toJson(r, Borrowing.class);
        return json;
    }


    public void updateBorrowing(int borrowingID, int userID, String status) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String updateQuery = "UPDATE borrowing SET status='";//...
        
        stmt.executeUpdate(updateQuery);
        stmt.close();
        con.close();
    }

    public void updateBor(int copyid, int userid, String status) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String updateQuery = "UPDATE borrowing SET status='" + status + "' WHERE bookcopy_id = '" + copyid + "' AND user_id = '" + userid + "'";

        stmt.executeUpdate(updateQuery);
        stmt.close();
        con.close();
    }

    public void updateBorrowingLib(int copyid, String status) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String updateQuery = "UPDATE borrowing SET status='" + status + "' WHERE bookcopy_id='" + copyid + "'";

        stmt.executeUpdate(updateQuery);
        stmt.close();
        con.close();
    }

    public void deleteBorrowing(int randevouzID) throws SQLException, ClassNotFoundException{
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String deleteQuery = "DELETE FROM borrowing WHERE borrowing_id='" + randevouzID + "'";
        stmt.executeUpdate(deleteQuery);
        stmt.close();
        con.close();
    }



    public void createBorrowingTable() throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String sql = "CREATE TABLE borrowing "
                + "(borrowing_id INTEGER not NULL AUTO_INCREMENT, "
                + " bookcopy_id INTEGER not NULL, "
                + " user_id INTEGER not NULL, "
                + " fromdate DATE not NULL, "
                + " todate DATE not NULL, "
                + " status VARCHAR(15) not NULL, "
                + "FOREIGN KEY (user_id) REFERENCES students(user_id), "
                + "FOREIGN KEY (bookcopy_id) REFERENCES booksinlibraries(bookcopy_id), "
                + " PRIMARY KEY (borrowing_id))";
        stmt.execute(sql);
        stmt.close();
        con.close();

    }

    /**
     * Establish a database connection and add in the database.
     *
     * @throws ClassNotFoundException
     */
    public void createNewBorrowing(Borrowing bor) throws ClassNotFoundException {
        try {
            Connection con = DB_Connection.getConnection();

            Statement stmt = con.createStatement();

            String insertQuery = "INSERT INTO "
                    + " borrowing (bookcopy_id,user_id,fromDate,toDate,status)"
                    + " VALUES ("
                    + "'" + bor.getBookcopy_id() + "',"
                    + "'" + bor.getUser_id() + "',"
                    + "'" + bor.getFromDate() + "',"
                    + "'" + bor.getToDate() + "',"
                    + "'" + bor.getStatus() + "'"
                    + ")";
            //stmt.execute(table);

            stmt.executeUpdate(insertQuery);
            System.out.println("# The borrowing was successfully added in the database.");

            /* Get the member id from the database and set it to the member */
            stmt.close();

        } catch (SQLException ex) {
            Logger.getLogger(EditBorrowingTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ArrayList<Borrowing> requestedBor() throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ArrayList<Borrowing> bors = new ArrayList<Borrowing>();
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM borrowing WHERE status='requested'");
            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                Borrowing b = gson.fromJson(json, Borrowing.class);
                bors.add(b);
            }
            return bors;

        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public ArrayList<Borrowing> returnedBor() throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ArrayList<Borrowing> bors = new ArrayList<Borrowing>();
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM borrowing WHERE status='returned'");
            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                Borrowing b = gson.fromJson(json, Borrowing.class);
                bors.add(b);
            }
            return bors;

        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public ArrayList<Borrowing> successUserBor(int userid) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ArrayList<Borrowing> bors = new ArrayList<Borrowing>();
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM borrowing WHERE status='successEnd' AND user_id='" + userid + "'");
            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                Borrowing b = gson.fromJson(json, Borrowing.class);
                bors.add(b);
            }
            return bors;

        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public ArrayList<Borrowing> requestedBorUser(int userid) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ArrayList<Borrowing> bors = new ArrayList<Borrowing>();
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM borrowing WHERE user_id='" + userid + "' AND status='requested'");
            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                Borrowing b = gson.fromJson(json, Borrowing.class);
                bors.add(b);
            }
            return bors;

        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public ArrayList<Borrowing> borrowedBorUser(int userid) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ArrayList<Borrowing> bors = new ArrayList<Borrowing>();
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM borrowing WHERE user_id='" + userid + "' AND status='borrowed'");
            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                Borrowing b = gson.fromJson(json, Borrowing.class);
                bors.add(b);
            }
            return bors;

        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public ArrayList<Borrowing> requested_borrowedBorUser(int userid) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ArrayList<Borrowing> bors = new ArrayList<Borrowing>();
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM borrowing WHERE user_id='" + userid + "' AND (status='requested' OR status='borrowed')");
            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                int fr = json.indexOf("fromdate");
                int to = json.indexOf("todate");
                int i = fr + 11, j = to + 9;
                int i_end = fr + 21, j_end = to + 19;
                String fromdate = "", todate = "";
                while (i < i_end && j < j_end) {
                    fromdate += json.charAt(i);
                    todate += json.charAt(j);
                    i++;
                    j++;
                }
                Gson gson = new Gson();
                Borrowing b = gson.fromJson(json, Borrowing.class);
                b.setFromDate(fromdate);
                b.setToDate(todate);
                bors.add(b);
            }
            return bors;

        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public ArrayList<Borrowing> borrowed_returnedBor() throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ArrayList<Borrowing> bors = new ArrayList<Borrowing>();
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM borrowing WHERE status='borrowed' OR status='returned'");
            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                int fr = json.indexOf("fromdate");
                int to = json.indexOf("todate");
                int i = fr + 11, j = to + 9;
                int i_end = fr + 21, j_end = to + 19;
                String fromdate = "", todate = "";
                while (i < i_end && j < j_end) {
                    fromdate += json.charAt(i);
                    todate += json.charAt(j);
                    i++;
                    j++;
                }
                Gson gson = new Gson();
                Borrowing b = gson.fromJson(json, Borrowing.class);
                b.setFromDate(fromdate);
                b.setToDate(todate);
                bors.add(b);
            }
            return bors;

        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }
}
