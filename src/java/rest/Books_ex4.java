/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import com.google.gson.Gson;
import database.tables.EditBooksTable;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import mainClasses.Book;

/**
 * REST Web Service
 *
 * @author kostas
 */
@Path("book_modif")
public class Books_ex4 {

    /**
     * Creates a new instance of bookAPI
     */
    public Books_ex4() {
    }

    @POST
    @Path("/book")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response addBook(String boook) throws ClassNotFoundException, SQLException {
        Gson gson = new Gson();
        Book book = gson.fromJson(boook, Book.class);
        EditBooksTable ebt = new EditBooksTable();
        if (ebt.databaseToBooksISBN(book.getIsbn()) == 1) {
            return Response.status(Response.Status.CONFLICT).type("application/json").entity("{\"error\":\"Book Exists\"}").build();
        } else {
            ebt.createNewBook(book);
            return Response.status(Response.Status.OK).type("application/json").entity("{\"success\":\"Book Added\"}").build();
        }
    }

    @GET
    @Path("/books/{genre}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getBook(@PathParam("genre") String genre,
            @QueryParam("fromYear") String fromYear,
            @QueryParam("toYear") String toYear) throws SQLException {
        Response.Status status = Response.Status.OK;
        EditBooksTable ebt = new EditBooksTable();
        ArrayList<Book> books = new ArrayList<Book>();

        int fromyear = Integer.parseInt(fromYear);
        int toyear = Integer.parseInt(toYear);

        books = ebt.databaseToBooksFromTo(genre, fromyear, toyear);

        if (!books.isEmpty()) {
            String json = new Gson().toJson(books);
            return Response.status(status).type("application/json").entity(json).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).type("application/json").entity("{\"error\":\"Book genre not exists\"}").build();
        }
    }

    @PUT
    @Path("bookpages/{isbn}/{pages}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateBook(String isbn, int pages, @HeaderParam("Accept") String acceptHeader) throws SQLException, ClassNotFoundException {
        Gson gson = new Gson();
        Book book = gson.fromJson(isbn, Book.class);
        EditBooksTable ebt = new EditBooksTable();
        if (ebt.databaseToBooksISBN(book.getIsbn()) == 0) {
            return Response.status(Response.Status.NOT_FOUND).type("application/json").entity("{\"error\":\"Book Does not Exists\"}").build();
        } else if (pages <= 0) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).type("application/json").entity("{\"error\":\"Pages must be over 0\"}").build();
        } else {
            ebt.updateBookPages(isbn, pages);
            return Response.status(Response.Status.OK).type("application/json").entity("{\"success\":\"Quantity Updated\"}").build();
        }
    }

    @DELETE
    @Path("/bookdeletion/{isbn}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteBook(@PathParam("isbn") String isbn) throws SQLException, ClassNotFoundException {
        Response.Status status = Response.Status.OK;
        EditBooksTable ebt = new EditBooksTable();
        if (ebt.databaseToBooksISBN(isbn) == 1) {
            ebt.deleteBook(isbn);
            return Response.status(status).type("application/json").entity("{\"success\":\"Book Deleted\"}").build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).type("application/json").entity("{\"error\":\"Book Does not Exists\"}").build();
        }
    }
}
