package com.LibroNova.app.service;

import com.LibroNova.app.errors.*;
import com.LibroNova.app.dao.IUserDao;
import com.LibroNova.app.domain.User;

import java.util.List;

public class UserService {

    private final IUserDao userDao;

    public UserService(IUserDao userDao) {
        this.userDao = userDao;
    }

    // =====================================================
    // CREAR USUARIO
    // =====================================================
    public User createUser(User user) throws BadRequestException, ConflictException, ServiceException, DataAccessException {
        // Validaciones básicas
        if (user.getName() == null || user.getName().isBlank() ||
                user.getEmail() == null || user.getEmail().isBlank() ||
                user.getPassword() == null || user.getPassword().isBlank() ||
                user.getRole() == null || user.getRole().isBlank()) {
            throw new BadRequestException("Todos los campos son obligatorios");
        }

        // Verificar si ya existe un usuario con el mismo email
        try {
            User existing = userDao.findByEmail(user.getEmail());
            if (existing != null) {
                throw new ConflictException("El correo ya está registrado");
            }

            return userDao.create(user);
        } catch (DataAccessException dae) {
            throw dae; // Propagar
        } catch (Exception e) {
            throw new ServiceException("Error al crear el usuario", e);
        }
    }

    // =====================================================
    // OBTENER USUARIO POR EMAIL
    // =====================================================
    public User getUserByEmail(String email) throws NotFoundException, ServiceException, DataAccessException {
        try {
            User user = userDao.findByEmail(email);
            if (user == null) {
                throw new NotFoundException("Usuario no encontrado con email: " + email);
            }
            return user;
        } catch (DataAccessException dae) {
            throw dae;
        } catch (Exception e) {
            throw new ServiceException("Error al obtener usuario por email", e);
        }
    }

    // =====================================================
    // OBTENER USUARIO POR ID
    // =====================================================
    public User getUserById(int id) throws NotFoundException, ServiceException, DataAccessException {
        try {
            User user = userDao.findById(id);
            if (user == null) {
                throw new NotFoundException("Usuario no encontrado con ID: " + id);
            }
            return user;
        } catch (DataAccessException dae) {
            throw dae;
        } catch (Exception e) {
            throw new ServiceException("Error al obtener usuario por ID", e);
        }
    }

    // =====================================================
    // LISTAR TODOS LOS USUARIOS
    // =====================================================
    public List<User> listUsers() throws ServiceException, DataAccessException {
        try {
            return userDao.listAll();
        } catch (DataAccessException dae) {
            throw dae;
        } catch (Exception e) {
            throw new ServiceException("Error al listar usuarios", e);
        }
    }

    // =====================================================
    // AUTENTICACIÓN SIMPLIFICADA
    // =====================================================
    public User login(String email, String password)
        throws UnauthorizedException, ServiceException, DataAccessException {
    try {
        User user = userDao.findByEmail(email);

        if (user == null || !user.getPassword().equals(password)) {
            throw new UnauthorizedException("Correo o contraseña incorrectos");
        }

        if (!user.isActive()) {
            throw new UnauthorizedException("Usuario inactivo");
        }

        return user;

    } catch (UnauthorizedException e) {
        throw e;

    } catch (DataAccessException dae) {
        throw dae;

    } catch (Exception e) {
        throw new ServiceException("Error interno al acceder a los datos en Login.", e);
    }
}


}
