package com.LibroNova.app.service;

import java.util.List;

import com.LibroNova.app.dao.IBookDao;
import com.LibroNova.app.domain.Book;
import com.LibroNova.app.errors.BadRequestException;
import com.LibroNova.app.errors.ConflictException;
import com.LibroNova.app.errors.DataAccessException;
import com.LibroNova.app.errors.NotFoundException;
import com.LibroNova.app.errors.ServiceException;

public class BookService {

    private final IBookDao bookDao;

    public BookService(IBookDao bookDao) {
        this.bookDao = bookDao;
    }

    // Crear libro con validación ISBN único
    public Book createBook(Book book) throws BadRequestException, ConflictException, ServiceException, DataAccessException {
        if (book.getTitle() == null || book.getTitle().isBlank() || book.getIsbn() == null || book.getIsbn().isBlank() || book.getCategory() == null || book.getCategory().isBlank()) {
            throw new BadRequestException("Título, ISBN y categoría son obligatorios");
        }

        if (book.getStock() < 0) {
            throw new BadRequestException("El stock no puede ser negativo");
        };

        if (book.getPrice() < 0) {
            throw new BadRequestException("El precio no puede ser negativo");
        };

        Book existing = bookDao.findByIsbn(book.getIsbn());
        if (existing != null) {
            throw new ConflictException("ISBN ya registrado");
        }

        try {
            return bookDao.create(book);
        } catch (DataAccessException e) {
            throw e; // propagamos DataAccessException
        } catch (Exception e) {
            throw new ServiceException("Error interno al crear el libro", e);
        }
    }

    public List<Book> listBooks() throws ServiceException, DataAccessException {
        try {
            return bookDao.listAll();
        } catch (DataAccessException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error interno al listar libros", e);
        }
    }

    // Actualizar stock de libro con validación
    public boolean updateStock(int idBook, int newStock) throws BadRequestException, NotFoundException, ServiceException, DataAccessException {
        if (newStock < 0) {
            throw new BadRequestException("El stock no puede ser negativo");
        }

        boolean updated = bookDao.updateStock(idBook, newStock);
        if (!updated) {
            throw new NotFoundException("Libro no encontrado");
        }

        return true;
    }

    // Buscar libro por ISBN
    public Book findByIsbn(String isbn) throws NotFoundException, ServiceException, DataAccessException {
        try {
            Book book = bookDao.findByIsbn(isbn);
            if (book == null) throw new NotFoundException("Libro no encontrado");
            return book;
        } catch (DataAccessException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error interno al buscar libro", e);
        }
    }
}
