package com.LibroNova.app.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.LibroNova.app.config.DBconfig;
import com.LibroNova.app.dao.ILoanDao;
import com.LibroNova.app.domain.Loan;
import com.LibroNova.app.errors.DataAccessException;

public class LoanDaoJdbc implements ILoanDao {

    @Override
    public void create(Loan loan) throws DataAccessException {
        Loan newLoan = loan;
        String sql = "INSERT INTO loan (id_user, id_book, loan_date, due_date) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBconfig.connect();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, loan.getUserId());
            ps.setInt(2, loan.getBookId());
            ps.setDate(3, new java.sql.Date(loan.getLoanDate().getTime()));
            ps.setDate(4, new java.sql.Date(loan.getReturnDate().getTime())); // due_date
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) newLoan.setId(rs.getInt(1));

        } catch (SQLException e) {
            throw new DataAccessException("Error al crear préstamo", e);
        }
    }

    @Override
    public Loan findById(int id) throws DataAccessException {
        String sql = "SELECT * FROM loan WHERE id_loan = ?";
        try (Connection conn = DBconfig.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapLoan(rs);
            }
            return null;

        } catch (SQLException e) {
            throw new DataAccessException("Error al buscar préstamo por ID", e);
        }
    }

    @Override
    public List<Loan> findAll() throws DataAccessException {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT * FROM loan";
        try (Connection conn = DBconfig.connect();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                loans.add(mapLoan(rs));
            }

        } catch (SQLException e) {
            throw new DataAccessException("Error al listar préstamos", e);
        }
        return loans;
    }

    @Override
    public List<Loan> listByUser(int idUser) throws DataAccessException {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT * FROM loan WHERE id_user = ?";
        try (Connection conn = DBconfig.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUser);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                loans.add(mapLoan(rs));
            }

        } catch (SQLException e) {
            throw new DataAccessException("Error al listar préstamos del usuario", e);
        }
        return loans;
    }

    @Override
    public void update(Loan loan) throws DataAccessException {
        String sql = "UPDATE loan SET return_date = ?, status = ?, fine = ? WHERE id_loan = ?";
        try (Connection conn = DBconfig.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, new java.sql.Date(loan.getReturnDate().getTime()));
            ps.setString(2, loan.getStatus());
            ps.setDouble(3, loan.getFine());
            ps.setInt(4, loan.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DataAccessException("Error al actualizar préstamo", e);
        }
    }

    @Override
    public void delete(int id) throws DataAccessException {
        String sql = "DELETE FROM loan WHERE id_loan = ?";
        try (Connection conn = DBconfig.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error al eliminar préstamo", e);
        }
    }

    private Loan mapLoan(ResultSet rs) throws SQLException {
        Loan loan = new Loan();
        loan.setId(rs.getInt("id_loan"));
        loan.setUserId(rs.getInt("id_user"));
        loan.setBookId(rs.getInt("id_book"));
        loan.setLoanDate(rs.getDate("loan_date"));
        loan.setReturnDate(rs.getDate("return_date"));
        loan.setStatus(rs.getString("status"));
        loan.setFine(rs.getDouble("fine"));
        loan.setCreatedAt(rs.getTimestamp("created_at"));
        return loan;
    }
}
