package com.LibroNova.app.dao;

import com.LibroNova.app.domain.Loan;
import com.LibroNova.app.errors.DataAccessException;

import java.util.List;

public interface ILoanDao {
    void create(Loan loan) throws DataAccessException;

    Loan findById(int id) throws DataAccessException;

    List<Loan> findAll() throws DataAccessException;

    List<Loan> listByUser(int idUser) throws DataAccessException;

    void update(Loan loan) throws DataAccessException;

    void delete(int id) throws DataAccessException;
}
