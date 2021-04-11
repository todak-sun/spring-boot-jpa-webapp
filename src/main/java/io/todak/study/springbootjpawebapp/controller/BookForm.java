package io.todak.study.springbootjpawebapp.controller;

import io.todak.study.springbootjpawebapp.domain.item.Book;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class BookForm {

    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    private String author;
    private String isbn;

    public BookForm(Book book) {
        this.id = book.getId();
        this.name = book.getName();
        this.price = book.getPrice();
        this.stockQuantity = book.getStockQuantity();
        this.author = book.getAuthor();
        this.isbn = book.getIsbn();
    }

    public Book toEntity() {
        Book book = new Book();
        book.setId(this.id);
        book.setName(this.name);
        book.setPrice(this.price);
        book.setStockQuantity(this.stockQuantity);
        book.setAuthor(this.author);
        book.setIsbn(this.isbn);
        return book;
    }

}
