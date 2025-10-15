package com.LibroNova.app.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.LibroNova.app.domain.Book;
import com.LibroNova.app.errors.BadRequestException;
import com.LibroNova.app.errors.ConflictException;
import com.LibroNova.app.errors.DataAccessException;
import com.LibroNova.app.errors.NotFoundException;
import com.LibroNova.app.errors.ServiceException;
import com.LibroNova.app.service.BookService;

public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    public Map<String, Object> createBook(Map<String, String> data) {
        Map<String, Object> response = new HashMap<>();

        try {
            String title = data.get("title");
            String author = data.get("author");
            String isbn = data.get("isbn");
            String category = data.get("category");
            int stock = Integer.parseInt(data.getOrDefault("stock", "1"));
            double price = Double.parseDouble(data.getOrDefault("price", "0.0"));
            

            Book book = new Book(title, author, isbn,category, stock, true,price);
            book = bookService.createBook(book);

            response.put("status", 200);
            response.put("data", book);

        } catch (BadRequestException e) {
            response.put("status", 400);
            response.put("error", e.getMessage());

        } catch (ConflictException e) {
            response.put("status", 409);
            response.put("error", e.getMessage());

        } catch (ServiceException e) {
            response.put("status", 500);
            response.put("error", "Error interno del servidor");

        } catch (DataAccessException e) {
            response.put("status", 500);
            response.put("error", "Error de acceso a datos");

        } finally {
            System.out.println("→ Operación crear libro finalizada");
        }

        return response;
    }

    public Map<String, Object> listBooks() {
        Map<String, Object> response = new HashMap<>();

        try {
            List<Book> books = bookService.listBooks();
            response.put("status", 200);
            response.put("data", books);

        } catch (ServiceException e) {
            response.put("status", 500);
            response.put("error", "Error interno del servidor");
        } catch (DataAccessException e) {
            response.put("status", 500);
            response.put("error", "Error de acceso a datos");
        } finally {
            System.out.println("→ Operación listar libros finalizada");
        }

        return response;
    }

    public Map<String, Object> updateStock(int idBook, int newStock) {
        Map<String, Object> response = new HashMap<>();

        try {
            boolean ok = bookService.updateStock(idBook, newStock);
            response.put("status", 200);
            response.put("data", ok);

        } catch (BadRequestException e) {
            response.put("status", 400);
            response.put("error", e.getMessage());

        } catch (NotFoundException e) {
            response.put("status", 404);
            response.put("error", e.getMessage());

        } catch (ServiceException | DataAccessException e) {
            response.put("status", 500);
            response.put("error", "Error interno del servidor");
        } finally {
            System.out.println("→ Operación actualizar stock finalizada");
        }

        return response;
    }

    public Map<String, Object> getBookByIsbn(String isbn) {
        Map<String, Object> response = new HashMap<>();

        try {
            Book book = bookService.findByIsbn(isbn);
            response.put("status", 200);
            response.put("data", book);

        } catch (NotFoundException e) {
            response.put("status", 404);
            response.put("error", e.getMessage());

        } catch (ServiceException e) {
            response.put("status", 500);
            response.put("error", "Error interno del servidor");

        } catch (DataAccessException e) {
            response.put("status", 500);
            response.put("error", "Error de acceso a datos");

        } finally {
            System.out.println("→ Operación obtener libro por ISBN finalizada");
        }

        return response;
    }
}
