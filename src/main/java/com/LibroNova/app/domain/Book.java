package com.LibroNova.app.domain;


import java.sql.Timestamp;

public class Book {
    private int id;
    private String title;
    private String author;
    private String isbn;
    private int stock;
    private boolean available;
    private Timestamp createdAt;

    public Book() {}

    public Book(int id, String title, String author, String isbn, int stock, boolean available, Timestamp createdAt) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.stock = stock;
        this.available = available;
        this.createdAt = createdAt;
    }

    public Book(String title, String author, String isbn, int stock, boolean available) {
        this(0, title, author, isbn, stock, available, null);
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

    @Override
    public String toString() {
        return id + " - " + title + " (" + author + ") " +
                "[Stock: " + stock + ", " + (available ? "Disponible" : "Agotado") + "]";
    }
}
