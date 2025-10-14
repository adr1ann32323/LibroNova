package com.LibroNova.app.service;

import com.LibroNova.app.config.DBconfig;
import com.LibroNova.app.dao.IBookDao;
import com.LibroNova.app.dao.ILoanDao;
import com.LibroNova.app.domain.Loan;
import com.LibroNova.app.errors.DataAccessException;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class LoanServiceSinRollback {

    private final ILoanDao loanDao;
    private final IBookDao bookDao;

    public LoanServiceSinRollback(ILoanDao loanDao, IBookDao bookDao) {
        this.loanDao = loanDao;
        this.bookDao = bookDao;
    }

    public void registerLoan(Loan loan) throws DataAccessException {
        try (Connection conn = DBconfig.connect()) {
            conn.setAutoCommit(false);

            bookDao.updateStock(loan.getBookId(), getNewStock(conn, loan.getBookId(), -1));
            loanDao.create(loan);

            conn.commit();
            log("Prestamo creado correctamente: " + loan);

        } catch (Exception e) {
            log("Error registrando prestamo: " + e.getMessage());
            throw new DataAccessException("Error en transacción de prestamo", e);
        }
    }

    public void returnBook(int loanId) throws DataAccessException {
        try (Connection conn = DBconfig.connect()) {
            conn.setAutoCommit(false);

            Loan loan = loanDao.findById(loanId);
            if (loan == null)
                throw new DataAccessException("Prestamo no encontrado");

            // Calcular multa si se pasa del due_date
            LocalDate due = loan.getReturnDate().toLocalDate();
            LocalDate today = LocalDate.now();

            double fine = 0.0;
            if (today.isAfter(due)) {
                long daysLate = java.time.temporal.ChronoUnit.DAYS.between(due, today);
                fine = daysLate * 1000;
                loan.setStatus("LATE");
            } else {
                loan.setStatus("RETURNED");
            }

            loan.setFine(fine);
            loan.setReturnDate(java.sql.Date.valueOf(today));

            // Actualizar loan y devolver stock
            loanDao.update(loan);
            bookDao.updateStock(loan.getBookId(), getNewStock(conn, loan.getBookId(), +1));

            conn.commit();
            log("Devolución procesada correctamente: " + loan);

        } catch (Exception e) {
            log("Error procesando devolución: " + e.getMessage());
            throw new DataAccessException("Error en transacción de devolución", e);
        }
    }

    public List<Loan> listAll() throws DataAccessException {
        return loanDao.findAll();
    }

    public List<Loan> listByUser(int userId) throws DataAccessException {
        return loanDao.listByUser(userId);
    }

    // Helper privado para leer stock actual y actualizarlo
    private int getNewStock(Connection conn, int idBook, int delta) throws SQLException {
        String sql = "SELECT available_copies FROM book WHERE id_book = ?";
        try (var ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idBook);
            var rs = ps.executeQuery();
            if (rs.next()) {
                int current = rs.getInt("available_copies");
                return current + delta;
            }
            throw new SQLException("Libro no encontrado");
        }
    }

    private void log(String msg) {
        try (FileWriter fw = new FileWriter("app.log", true)) {
            fw.write(java.time.LocalDateTime.now() + " - " + msg + "\n");
        } catch (IOException e) {
            System.err.println("Error al escribir en log: " + e.getMessage());
        }
    }
}
