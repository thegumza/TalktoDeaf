package project.se.model;

import java.util.List;

/**
 * Created by wiwat on 2/12/2015.
 */
public class Book {

    public final int id;
    public final String book_name;
    public final String book_description;
    public final String book_page_number;
    public final String book_price;
    public final String book_author;
    public final String book_publisher;
    public final String book_image;
    private List<Book> book;
    public Book(int id, String book_name, String book_description, String book_page_number, String book_price, String book_author, String book_publisher, String book_image ) {
        this.id = id;
        this.book_name = book_name;
        this.book_description = book_description;
        this.book_page_number = book_page_number;
        this.book_price = book_price;
        this.book_author = book_author;
        this.book_publisher = book_publisher;
        this.book_image = book_image;
    }


    public List<Book> getBook() {
        return book;
    }

    public int getId() {
        return id;
    }

    public String getBook_name() {
        return book_name;
    }

    public String getBook_description() {
        return book_description;
    }

    public String getBook_page_number() {
        return book_page_number;
    }

    public String getBook_price() {
        return book_price;
    }

    public String getBook_author() {
        return book_author;
    }

    public String getBook_publisher() {
        return book_publisher;
    }

    public String getBook_image() {
        return book_image;
    }
}
