package com.LibroNova.app.service;

import java.util.Date;
import java.util.List;

import com.LibroNova.app.domain.User;
import com.LibroNova.app.errors.*;

public class UserServiceDecorator implements IUserService {

    private final IUserService baseService;

    public UserServiceDecorator(IUserService baseService) {
        this.baseService = baseService;
    }

    // =====================================================
    // CREAR USUARIO (Decorado)
    // =====================================================
    @Override
    public User createUser(User user)
            throws BadRequestException, ConflictException, ServiceException, DataAccessException {

        // 🔹 Establecer valores por defecto
        if (user.getRole() == null || user.getRole().isBlank()) {
            user.setRole("ASISTENTE");
        }

        if (!user.isActive()) {
            user.setActive(true);
        }

        if (user.getCreatedAt() == null) {
            user.setCreatedAt(new java.sql.Timestamp(new Date().getTime()));
        }

        // Delegar al servicio original
        return baseService.createUser(user);
    }

    // =====================================================
    // MÉTODOS RESTANTES → delegan al servicio base
    // =====================================================

    @Override
    public User getUserByEmail(String email)
            throws NotFoundException, ServiceException, DataAccessException {
        return baseService.getUserByEmail(email);
    }

    @Override
    public User getUserById(int id)
            throws NotFoundException, ServiceException, DataAccessException {
        return baseService.getUserById(id);
    }

    @Override
    public List<User> listUsers()
            throws ServiceException, DataAccessException {
        return baseService.listUsers();
    }

    @Override
    public User login(String email, String password)
            throws UnauthorizedException, ServiceException, DataAccessException {
        return baseService.login(email, password);
    }

    @Override
    public boolean updateUserStatus(int idUser, boolean active)
            throws NotFoundException, ServiceException, DataAccessException {
        return baseService.updateUserStatus(idUser, active);
    }
}
