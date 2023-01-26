/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.tables;

import mainClasses.Librarian;
import com.google.gson.Gson;
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
public class EditLibrarianTable {

    public void addLibrarianFromJSON(String json) throws ClassNotFoundException {
        Librarian lib = jsonToLibrarian(json);
        addNewLibrarian(lib);
    }

    public Librarian jsonToLibrarian(String json) {
        Gson gson = new Gson();

        Librarian lib = gson.fromJson(json, Librarian.class);
        return lib;
    }

    public String librarianToJSON(Librarian lib) {
        Gson gson = new Gson();

        String json = gson.toJson(lib, Librarian.class);
        return json;
    }

    public ArrayList<String> librariansToJSON(ArrayList<Librarian> lts) {
        ArrayList<String> libs = new ArrayList<String>();
        for (int i = 0; i < lts.size(); i++) {
            Gson gson = new Gson();
            String json = gson.toJson(lts.get(i), Librarian.class);
            libs.add(json);
        }
        return libs;
    }

    public void updateLibrarian(String username, String personalpage) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String update = "UPDATE librarians SET personalpage='" +  personalpage + "' WHERE username = '" + username + "'";
        stmt.executeUpdate(update);
    }

    public void printLibrarianDetails(String username, String password) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM librarians WHERE username = '" + username + "' AND password='" + password + "'");
            while (rs.next()) {
                System.out.println("===Result===");
                DB_Connection.printResults(rs);
            }

        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }

    public Librarian databaseToLibrarian(String username, String password) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM librarians WHERE username = '" + username + "' AND password='" + password + "'");
            rs.next();
            String json = DB_Connection.getResultsToJSON(rs);
            Gson gson = new Gson();
            Librarian lib = gson.fromJson(json, Librarian.class);
            return lib;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public Librarian usernameToLibrarian(String username) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM librarians WHERE username = '" + username + "'");
            System.out.println("SELECT * FROM librarians WHERE username = '" + username + "'");
            rs.next();
            String json = DB_Connection.getResultsToJSON(rs);
            Gson gson = new Gson();
            Librarian user = gson.fromJson(json, Librarian.class);
            return user;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public Librarian emailToLibrarian(String email) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM librarians WHERE password = '" + email + "'");
            System.out.println("SELECT * FROM librarians WHERE password = '" + email + "'");
            rs.next();
            String json = DB_Connection.getResultsToJSON(rs);
            Gson gson = new Gson();
            Librarian user = gson.fromJson(json, Librarian.class);
            return user;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public ArrayList<Librarian> databaseToLibrarians() throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ArrayList<Librarian> librarians=new ArrayList<Librarian>();
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM librarians");
            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                Librarian lib = gson.fromJson(json, Librarian.class);
                librarians.add(lib);
            }
            return librarians;

        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }


    public void createLibrariansTable() throws SQLException, ClassNotFoundException {

        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        String query = "CREATE TABLE librarians"
                + "(library_id INTEGER not NULL AUTO_INCREMENT, "
                + "    username VARCHAR(30) not null unique,"
                + "    email VARCHAR(200) not null unique,	"
                + "    password VARCHAR(32) not null,"
                + "    firstname VARCHAR(30) not null,"
                + "    lastname VARCHAR(30) not null,"
                + "    birthdate DATE not null,"
                + "    gender  VARCHAR (7) not null,"
                + "    country VARCHAR(30) not null,"
                + "    city VARCHAR(50) not null,"
                + "    address VARCHAR(50) not null,"
                + "    libraryname VARCHAR(100) not null,"
                + "    libraryinfo VARCHAR(1000) not null,"
                + "    lat DOUBLE,"
                + "    lon DOUBLE,"
                + "    telephone VARCHAR(14),"
                + "    personalpage VARCHAR(200),"
                + " PRIMARY KEY (library_id))";
        stmt.execute(query);
        stmt.close();
    }

    /**
     * Establish a database connection and add in the database.
     *
     * @throws ClassNotFoundException
     */
    public void addNewLibrarian(Librarian lib) throws ClassNotFoundException {
        try {
            Connection con = DB_Connection.getConnection();

            Statement stmt = con.createStatement();

            String insertQuery = "INSERT INTO "
                    + " librarians (username,email,password,firstname,lastname,birthdate,gender,country,city,address,"
                    + "libraryname,libraryinfo,lat,lon,telephone,personalpage)"
                    + " VALUES ("
                    + "'" + lib.getUsername() + "',"
                    + "'" + lib.getEmail() + "',"
                    + "'" + lib.getPassword() + "',"
                    + "'" + lib.getFirstname() + "',"
                    + "'" + lib.getLastname() + "',"
                    + "'" + lib.getBirthdate() + "',"
                    + "'" + lib.getGender() + "',"
                    + "'" + lib.getCountry() + "',"
                    + "'" + lib.getCity() + "',"
                    + "'" + lib.getAddress() + "',"
                      + "'" + lib.getLibraryname()+ "',"
                   + "'" + lib.getLibraryinfo()+ "',"
                    + "'" + lib.getLat() + "',"
                    + "'" + lib.getLon() + "',"
                    + "'" + lib.getTelephone() + "',"
                   + "'" + lib.getPersonalpage()+ "'"
                    + ")";
            //stmt.execute(table);
            System.out.println(insertQuery);
            stmt.executeUpdate(insertQuery);
            System.out.println("# The libarian was successfully added in the database.");

            /* Get the member id from the database and set it to the member */
            stmt.close();

        } catch (SQLException ex) {
            Logger.getLogger(EditLibrarianTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // librariansToJson converter (Project Function)
    public ArrayList<String> librariansToJson(ArrayList<Librarian> lbs) {
        ArrayList<String> librarians = new ArrayList<String>();
        for (int i = 0; i < lbs.size(); i++) {
            Gson gson = new Gson();
            String json = gson.toJson(lbs.get(i), Librarian.class);
            librarians.add(json);
        }
        return librarians;
    }

    // Returns username, firstname and lastname of all librarians in a table (Project Function)
    public ArrayList<Librarian> databaseToLibrariansAdmin() throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ArrayList<Librarian> librarians = new ArrayList<Librarian>();
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT library_id, username, firstname, lastname FROM librarians");
            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                System.out.println(json);
                Gson gson = new Gson();
                Librarian librarian = gson.fromJson(json, Librarian.class);
                librarians.add(librarian);
            }
            return librarians;

        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    // Delete librarian from database (Project Function)
    public void deleteLibrarian(String username) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String update = "DELETE FROM librarians WHERE username='" + username + "'";
        stmt.executeUpdate(update);
    }

    // (Project Function)
    public Librarian databaseToLibrarianID(int id) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM librarians WHERE library_id = '" + id + "'");
            rs.next();
            String json = DB_Connection.getResultsToJSON(rs);
            Gson gson = new Gson();
            Librarian lib = gson.fromJson(json, Librarian.class);
            return lib;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    // Update Librarian Functions (Project Functions)
    public void updateLibrarianEmail(String username, String email) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String update = "UPDATE librarians SET email='" + email + "' WHERE username = '" + username + "'";
        stmt.executeUpdate(update);
    }

    public void updateLibrarianPass(String username, String pass) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String update = "UPDATE librarians SET password='" + pass + "' WHERE username = '" + username + "'";
        stmt.executeUpdate(update);
    }

    public void updateLibrarianFN(String username, String fn) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String update = "UPDATE librarians SET firstname='" + fn + "' WHERE username = '" + username + "'";
        stmt.executeUpdate(update);
    }

    public void updateLibrarianLN(String username, String ln) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String update = "UPDATE librarians SET lastname='" + ln + "' WHERE username = '" + username + "'";
        stmt.executeUpdate(update);
    }

    public void updateLibrarianSex(String username, String sex) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String update = "UPDATE librarians SET gender='" + sex + "' WHERE username = '" + username + "'";
        stmt.executeUpdate(update);
    }

    public void updateLibrarianCountry(String username, String country) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String update = "UPDATE librarians SET country='" + country + "' WHERE username = '" + username + "'";
        stmt.executeUpdate(update);
    }

    public void updateLibrarianCity(String username, String city) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String update = "UPDATE librarians SET city='" + city + "' WHERE username = '" + username + "'";
        stmt.executeUpdate(update);
    }

    public void updateLibrarianAddress(String username, String address) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String update = "UPDATE librarians SET address='" + address + "' WHERE username = '" + username + "'";
        stmt.executeUpdate(update);
    }

    public void updateLibrarianLibname(String username, String libname) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String update = "UPDATE librarians SET libraryname='" + libname + "' WHERE username = '" + username + "'";
        stmt.executeUpdate(update);
    }

    public void updateLibrarianLibinfo(String username, String libinfo) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String update = "UPDATE librarians SET libraryinfo='" + libinfo + "' WHERE username = '" + username + "'";
        stmt.executeUpdate(update);
    }

    public void updateLibrarianLat(String username, String lat) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String update = "UPDATE librarians SET lat='" + lat + "' WHERE username = '" + username + "'";
        stmt.executeUpdate(update);
    }

    public void updateLibrarianLon(String username, String lon) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String update = "UPDATE librarians SET lon='" + lon + "' WHERE username = '" + username + "'";
        stmt.executeUpdate(update);
    }

    public void updateLibrarianTel(String username, String tel) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String update = "UPDATE librarians SET telephone='" + tel + "' WHERE username = '" + username + "'";
        stmt.executeUpdate(update);
    }

    public void updateLibrarianPP(String username, String pp) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String update = "UPDATE librarians SET personalpage='" + pp + "' WHERE username = '" + username + "'";
        stmt.executeUpdate(update);
    }
    
    // Returns the library_id of a librarian (Project Function)
    public Librarian databaseToLibrarianId(String username) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT library_id FROM librarians WHERE username = '" + username + "'");
            rs.next();
            String json = DB_Connection.getResultsToJSON(rs);
            Gson gson = new Gson();
            Librarian lib = gson.fromJson(json, Librarian.class);
            return lib;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    //
    public Librarian databaseToLibrarianLibName(String name) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT library_id FROM librarians WHERE libraryname= '" + name + "'");
            rs.next();
            String json = DB_Connection.getResultsToJSON(rs);
            Gson gson = new Gson();
            Librarian lib = gson.fromJson(json, Librarian.class);
            return lib;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }
}
