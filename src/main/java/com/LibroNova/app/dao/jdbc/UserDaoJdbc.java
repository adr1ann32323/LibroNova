package com.LibroNova.app.dao.jdbc;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.LibroNova.app.config.DBconfig;
import com.LibroNova.app.dao.IUserDao;
import com.LibroNova.app.domain.User;
import com.LibroNova.app.errors.DataAccessException;

public class UserDaoJdbc implements IUserDao {

    @Override
    public User create(User user) throws DataAccessException {
        String sql = "INSERT INTO user_account (name, email, password, role, active) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBconfig.connect();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getRole());
            ps.setBoolean(5, user.isActive());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    user.setId(rs.getInt(1));
                }
            }
            return user;

        } catch (SQLException e) {
            throw new DataAccessException("Error al crear el usuario", e);
        }
    }

    @Override
    public User findByEmail(String email) throws DataAccessException {
        String sql = "SELECT * FROM user_account WHERE email = ?";
        try (Connection conn = DBconfig.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
            return null;

        } catch (SQLException e) {
            throw new DataAccessException("Error al buscar el usuario por email", e);
        }
    }

    @Override
    public User findById(int id) throws DataAccessException {
        String sql = "SELECT * FROM user_account WHERE id_user = ?";
        try (Connection conn = DBconfig.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
            return null;

        } catch (SQLException e) {
            throw new DataAccessException("Error al buscar el usuario por ID", e);
        }
    }

    @Override
    public List<User> listAll() throws DataAccessException {
        String sql = "SELECT * FROM user_account ORDER BY created_at DESC";
        List<User> users = new ArrayList<>();

        try (Connection conn = DBconfig.connect();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }

        } catch (SQLException e) {
            throw new DataAccessException("Error al listar los usuarios", e);
        }

        return users;
    }

    @Override
    public User update(User user) throws DataAccessException {

        String sql = "UPDATE user_account SET name = ?, email = ?, password = ?, role = ?, active = ? WHERE id_user = ?";
        try (Connection conn = DBconfig.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getRole());
            ps.setBoolean(5, user.isActive());
            ps.setInt(6, user.getId());

            ps.executeUpdate();
            return user;

        } catch (SQLException e) {
            throw new DataAccessException("Error al actualizar el usuario", e);
        }
    }

    // =====================================================
    // MÉTODO AUXILIAR PARA EVITAR REPETIR CÓDIGO
    // =====================================================
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id_user"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setRole(rs.getString("role"));
        user.setActive(rs.getBoolean("active"));
        user.setCreatedAt(rs.getTimestamp("created_at"));
        return user;
    }
}
