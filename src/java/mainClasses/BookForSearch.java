/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mainClasses;

/**
 *
 * @author kostas
 */
public class BookForSearch {

    String genre, title, author;
    int fromPn, toPn, fromY, toY;

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getFromPn() {
        return fromPn;
    }

    public void setFromPn(int fromPn) {
        this.fromPn = fromPn;
    }

}
