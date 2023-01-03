/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.tables;

import mainClasses.Book;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
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
public class EditBooksTable {

    public void addBookFromJSON(String json) throws ClassNotFoundException {
        Book bt = jsonToBook(json);
        createNewBook(bt);
    }

    public Book jsonToBook(String json) {
        Gson gson = new Gson();
        Book btest = gson.fromJson(json, Book.class);
        return btest;
    }

    public String bookToJSON(Book bt) {
        Gson gson = new Gson();

        String json = gson.toJson(bt, Book.class);
        return json;
    }

    public ArrayList<String> booksToJson(ArrayList<Book> bts) {
        ArrayList<String> books = new ArrayList<String>();
        for (int i = 0; i < bts.size(); i++) {
            Gson gson = new Gson();
            String json = gson.toJson(bts.get(i), Book.class);
            books.add(json);
        }
        return books;
    }

    public ArrayList<Book> databaseToBooks() throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ArrayList<Book> books = new ArrayList<Book>();
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM books");
            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                Book book = gson.fromJson(json, Book.class);
                books.add(book);
            }
            return books;

        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public ArrayList<Book> databaseToBooks(String genre) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ArrayList<Book> books = new ArrayList<Book>();
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM books WHERE genre= '" + genre + "'");
           
            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                Book book = gson.fromJson(json, Book.class);
                books.add(book);
            }
            return books;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public int databaseToBooksISBN(String isbn) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ArrayList<Book> books = new ArrayList<Book>();
        ResultSet rs;

        try {
            rs = stmt.executeQuery("SELECT * FROM books WHERE isbn= '" + isbn + "'");

            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                Book book = gson.fromJson(json, Book.class);
                books.add(book);

            }
            if (books.isEmpty()) {
                return 0;
            } else {
                return 1;
            }
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return 0;
    }

    // MINE FOR REST API
    public ArrayList<Book> databaseToBooksFromTo(String genre, int fromYear, int toYear) {

        ArrayList<Book> booksList = new ArrayList<Book>();
        ResultSet res;
        String query = null;

        if (genre.equals("all")) {
            if (fromYear == 0 && toYear == 0) {
                query = "SELECT * FROM books";
            } else if (fromYear != 0 && toYear == 0) {
                query = "SELECT * FROM books WHERE publicationyear >= '" + fromYear + "'";
            } else if (fromYear == 0 && toYear != 0) {
                query = "SELECT * FROM books WHERE publicationyear <= '" + toYear + "'";
            } else {
                query = "SELECT * FROM books WHERE publicationyear >= '" + fromYear + "' AND publicationyear <= '" + toYear + "'";
            }
        } else {
            if (fromYear == 0 && toYear == 0) {
                query = "SELECT * FROM books WHERE genre '" + genre + "'";
            } else if (fromYear != 0 && toYear == 0) {
                query = "SELECT * FROM books WHERE publicationyear >= '" + fromYear + "' AND genre '" + genre + "'";
            } else if (fromYear == 0 && toYear != 0) {
                query = "SELECT * FROM books WHERE publicationyear <= '" + toYear + "' AND genre '" + genre + "'";
            } else {
                query = "SELECT * FROM books WHERE publicationyear >= '" + fromYear + "' AND publicationyear <= '" + toYear + "' AND genre '" + genre + "'";
            }
        }

        try {
            Connection con = DB_Connection.getConnection();
            Statement stmt = con.createStatement();
            res = stmt.executeQuery(query);
            while (res.next()) {
                String json = DB_Connection.getResultsToJSON(res);
                Gson gson = new Gson();
                Book book = gson.fromJson(json, Book.class);
                booksList.add(book);
            }
            return booksList;
        } catch (JsonSyntaxException | SQLException | ClassNotFoundException e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public void updateBook(String isbn, String url) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        Book bt = new Book();

        String update = "UPDATE books SET url='" + url + "'" + "WHERE isbn = '" + isbn + "'";
        //stmt.executeUpdate(update);
    }

    // MINE FOR REST API
    public void updateBookPages(String isbn, int pages) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        Book bt = new Book();

        String update = "UPDATE books SET pages='" + pages + "'" + "WHERE isbn = '" + isbn + "'";
        stmt.executeUpdate(update);
    }

    public void deleteBook(String isbn) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String deleteQuery = "DELETE FROM books WHERE isbn='" + isbn + "'";
        stmt.executeUpdate(deleteQuery);
        stmt.close();
        con.close();
    }

    public void createBooksTable() throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String sql = "CREATE TABLE books "
                + "(isbn VARCHAR(13) not NULL, "
                + "title VARCHAR(500) not null,"
                + "authors VARCHAR(500)  not null, "
                + "genre VARCHAR(500)  not null, "
                + "pages INTEGER not null , "
                + "publicationyear INTEGER not null , "
                + "url VARCHAR (500), "
                + "photo VARCHAR (500), "
                + "PRIMARY KEY ( isbn ))";
        stmt.execute(sql);
        stmt.close();
        con.close();

    }

    /**
     * Establish a database connection and add in the database.
     *
     * @throws ClassNotFoundException
     */
    public void createNewBook(Book bt) throws ClassNotFoundException {
        try {
            Connection con = DB_Connection.getConnection();

            Statement stmt = con.createStatement();

            String insertQuery = "INSERT INTO "
                    + " books (isbn,title,authors,genre,pages,publicationyear,url,photo) "
                    + " VALUES ("
                    + "'" + bt.getIsbn() + "',"
                    + "'" + bt.getTitle() + "',"
                    + "'" + bt.getAuthors() + "',"
                    + "'" + bt.getGenre() + "',"
                    + "'" + bt.getPages() + "',"
                    + "'" + bt.getPublicationyear() + "',"
                    + "'" + bt.getUrl() + "',"
                    + "'" + bt.getPhoto() + "'"
                    + ")";
            //stmt.execute(table);
            System.out.println(insertQuery);
            stmt.executeUpdate(insertQuery);
            System.out.println("# The book was successfully added in the database.");

            /* Get the member id from the database and set it to the member */
            stmt.close();

        } catch (SQLException ex) {
            Logger.getLogger(EditBooksTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
