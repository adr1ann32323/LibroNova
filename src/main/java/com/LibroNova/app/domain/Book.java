package com.LibroNova.app.domain;


import java.sql.Timestamp;
//isbn, title, author, category, publisher, year, total_copies, available_copies, reference_price, is_active
public class Book {
    private int id;
    private String title;
    private String author;
    private String isbn;
    private String category;
    private String publisher;
    private int year;
    private int stock;
    private boolean available;
    private Timestamp createdAt;
    private double price;

    public Book() {}

    public Book(int id, String title, String author, String isbn, String category,int stock, boolean available, Timestamp createdAt,double price) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.category=category;
        this.stock = stock;
        this.available = available;
        this.createdAt = createdAt;
        this.price=price;
    }

    public Book(String title, String author, String isbn,String category ,int stock, boolean available,double price) {
        this(0, title, author, isbn, category,stock, available, null,price);
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public int getYear() {return year;}
    public void setYear(int year) {this.year = year;}

    public String getPublisher() {return publisher;}
    public void setPublisher(String publisher) {this.publisher = publisher;}

    public String getCategory() {return category;}
    public void setCategory(String category) {this.category = category;}

    public double getPrice() {return price;}
    public void setPrice(double price) {this.price = price;}


    @Override
    public String toString() {
        return id + " - " + title + " (" + author + ") " +
                "[Stock: " + stock + ", " + (available ? "Disponible" : "Agotado") + "]";
    }
}
