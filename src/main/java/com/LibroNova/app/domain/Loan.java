package com.LibroNova.app.domain;


import java.sql.Date;
import java.sql.Timestamp;

public class Loan {
    private int id;
    private int userId;
    private int bookId;
    private Date loanDate;
    private Date returnDate;
    private String status; // ACTIVE, RETURNED, LATE
    private double fine;
    private Timestamp createdAt;

    public Loan() {}

    public Loan(int id, int userId, int bookId, Date loanDate, Date returnDate,
                String status, double fine, Timestamp createdAt) {
        this.id = id;
        this.userId = userId;
        this.bookId = bookId;
        this.loanDate = loanDate;
        this.returnDate = returnDate;
        this.status = status;
        this.fine = fine;
        this.createdAt = createdAt;
    }

    public Loan(int userId, int bookId, Date loanDate, Date returnDate) {
        this(0, userId, bookId, loanDate, returnDate, "ACTIVE", 0.00, null);
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }

    public Date getLoanDate() { return loanDate; }
    public void setLoanDate(Date loanDate) { this.loanDate = loanDate; }

    public Date getReturnDate() { return returnDate; }
    public void setReturnDate(Date returnDate) { this.returnDate = returnDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public double getFine() { return fine; }
    public void setFine(double fine) { this.fine = fine; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "Préstamo #" + id +
                " | Usuario: " + userId +
                " | Libro: " + bookId +
                " | Estado: " + status +
                " | Multa: $" + fine +
                " | Fecha de préstamo: " + loanDate +
                (returnDate != null ? " | Fecha de devolución: " + returnDate : "");
                
    }
}