package com.LibroNova.app.service;

import java.util.List;

import com.LibroNova.app.domain.User;
import com.LibroNova.app.errors.BadRequestException;
import com.LibroNova.app.errors.ConflictException;
import com.LibroNova.app.errors.DataAccessException;
import com.LibroNova.app.errors.NotFoundException;
import com.LibroNova.app.errors.ServiceException;
import com.LibroNova.app.errors.UnauthorizedException;

public interface IUserService {

    // Crear usuario
    User createUser(User user)
            throws BadRequestException, ConflictException, ServiceException, DataAccessException;

    // Obtener usuario por email
    User getUserByEmail(String email)
            throws NotFoundException, ServiceException, DataAccessException;

    // Obtener usuario por ID
    User getUserById(int id)
            throws NotFoundException, ServiceException, DataAccessException;

    // Listar todos los usuarios
    List<User> listUsers()
            throws ServiceException, DataAccessException;

    // Login (autenticación)
    User login(String email, String password)
            throws UnauthorizedException, ServiceException, DataAccessException;

    // Actualizar estado (activo/inactivo)
    boolean updateUserStatus(int idUser, boolean active)
            throws NotFoundException, ServiceException, DataAccessException;
}
