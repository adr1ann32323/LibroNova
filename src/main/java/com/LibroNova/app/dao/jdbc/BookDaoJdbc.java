package com.LibroNova.app.dao.jdbc;

import com.LibroNova.app.dao.IBookDao;
import com.LibroNova.app.domain.Book;
import com.LibroNova.app.config.DBconfig;
import com.LibroNova.app.errors.DataAccessException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDaoJdbc implements IBookDao {

    @Override
    public Book create(Book book) throws DataAccessException {
        String sql = "INSERT INTO book (isbn, title, author, publisher, year, copies, available_copies) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBconfig.connect();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, book.getIsbn());
            ps.setString(2, book.getTitle());
            ps.setString(3, book.getAuthor());
            ps.setString(4, null); // publisher opcional
            ps.setInt(5, 0); // year opcional
            ps.setInt(6, book.getStock());
            ps.setInt(7, book.getStock());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    book.setId(rs.getInt(1));
                }
            }

            return book;

        } catch (SQLException e) {
            throw new DataAccessException("Error al crear el libro", e);
        }
    }

    @Override
    public Book findByIsbn(String isbn) throws DataAccessException {
        String sql = "SELECT * FROM book WHERE isbn = ?";
        try (Connection conn = DBconfig.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, isbn);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBook(rs);
                }
            }

            return null;

        } catch (SQLException e) {
            throw new DataAccessException("Error al buscar el libro por ISBN", e);
        }
    }

    @Override
    public List<Book> listAll() throws DataAccessException {
        String sql = "SELECT * FROM book ORDER BY created_at DESC";
        List<Book> books = new ArrayList<>();

        try (Connection conn = DBconfig.connect();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }

        } catch (SQLException e) {
            throw new DataAccessException("Error al listar los libros", e);
        }

        return books;
    }

    @Override
    public boolean updateStock(int idBook, int newStock) throws DataAccessException {
        String sql = "UPDATE book SET copies = ?, available_copies = ? WHERE id_book = ?";
        try (Connection conn = DBconfig.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, newStock);
            ps.setInt(2, newStock);
            ps.setInt(3, idBook);

            int updated = ps.executeUpdate();
            return updated > 0;

        } catch (SQLException e) {
            throw new DataAccessException("Error al actualizar stock del libro", e);
        }
    }

    // =================================
    // Método auxiliar
    // =================================
    private Book mapResultSetToBook(ResultSet rs) throws SQLException {
        Book book = new Book();
        book.setId(rs.getInt("id_book"));
        book.setIsbn(rs.getString("isbn"));
        book.setTitle(rs.getString("title"));
        book.setAuthor(rs.getString("author"));
        book.setStock(rs.getInt("copies"));
        book.setAvailable(rs.getInt("available_copies") > 0);
        book.setCreatedAt(rs.getTimestamp("created_at"));
        return book;
    }
}
