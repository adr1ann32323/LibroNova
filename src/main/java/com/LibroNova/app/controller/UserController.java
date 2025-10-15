package com.LibroNova.app.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.LibroNova.app.domain.User;
import com.LibroNova.app.errors.BadRequestException;
import com.LibroNova.app.errors.ConflictException;
import com.LibroNova.app.errors.DataAccessException;
import com.LibroNova.app.errors.NotFoundException;
import com.LibroNova.app.errors.ServiceException;
import com.LibroNova.app.errors.UnauthorizedException;
// import com.LibroNova.app.service.UserService;
import com.LibroNova.app.service.IUserService;
public class UserController {

    private final IUserService service;

    public UserController(IUserService service) {
        this.service = service;
    }

    // =====================================================
    // CREAR USUARIO
    // =====================================================
    public Map<String, Object> createUser(Map<String, String> data) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Extraer datos del map
            String name = data.get("name");
            String email = data.get("email");
            String password = data.get("password");
            String role = data.get("role");
            boolean active = Boolean.parseBoolean(data.getOrDefault("active", "true"));

            User user = new User(name, email, password, role, active);
            User created = service.createUser(user);

            response.put("status", 200);
            response.put("data", created);

        } catch (BadRequestException e) {
            response.put("status", 400);
            response.put("error", e.getMessage());

        } catch (ConflictException e) {
            response.put("status", 409);
            response.put("error", e.getMessage());

        } catch (UnauthorizedException e) {
            response.put("status", 401);
            response.put("error", e.getMessage());

        } catch (NotFoundException e) {
            response.put("status", 404);
            response.put("error", e.getMessage());

        } catch (ServiceException e) {
            response.put("status", 500);
            response.put("error", "Error interno del servidor: " + e.getMessage());

        } catch (DataAccessException e) {
            response.put("status", 500);
            response.put("error", "Error de acceso a datos: " + e.getMessage());

        } finally {
            System.out.println("→ Operación finalizada"); // visible (requisito)
        }

        return response;
    }

    // =====================================================
    // OBTENER USUARIO POR EMAIL
    // =====================================================
    public Map<String, Object> getUserByEmail(String email) {
        Map<String, Object> response = new HashMap<>();

        try {
            User user = service.getUserByEmail(email);
            response.put("status", 200);
            response.put("data", user);

        } catch (NotFoundException e) {
            response.put("status", 404);
            response.put("error", e.getMessage());

        } catch (ServiceException e) {
            response.put("status", 500);
            response.put("error", "Error interno del servidor: " + e.getMessage());

        } catch (DataAccessException e) {
            response.put("status", 500);
            response.put("error", "Error de acceso a datos: " + e.getMessage());

        } finally {
            System.out.println("→ Operación finalizada");
        }

        return response;
    }

    // =====================================================
    // LISTAR TODOS LOS USUARIOS
    // =====================================================
    public Map<String, Object> listUsers() {
        Map<String, Object> response = new HashMap<>();

        try {
            List<User> users = service.listUsers();
            response.put("status", 200);
            response.put("data", users);

        } catch (ServiceException e) {
            response.put("status", 500);
            response.put("error", "Error interno del servidor: " + e.getMessage());

        } catch (DataAccessException e) {
            response.put("status", 500);
            response.put("error", "Error de acceso a datos: " + e.getMessage());

        } finally {
            System.out.println("→ Operación finalizada");
        }

        return response;
    }
    
    // =====================================================
    // ACTUALIZAR ESTADO ACTIVO/INACTIVO
    public Map<String, Object> updateUserStatus(int idUser, boolean active) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean ok = service.updateUserStatus(idUser, active);
            response.put("status", 200);
            response.put("data", ok);

        } catch (BadRequestException e) {
            response.put("status", 400);
            response.put("error", e.getMessage());

        } catch (NotFoundException e) {
            response.put("status", 404);
            response.put("error", e.getMessage());

        } catch (ServiceException e) {
            response.put("status", 500);
            response.put("error", "Error interno del servidor: " + e.getMessage());

        } catch (DataAccessException e) {
            response.put("status", 500);
            response.put("error", "Error de acceso a datos: " + e.getMessage());

        } finally {
            System.out.println("→ Operación finalizada");
        }
    
        return response;
    };
    // =====================================================
    // LOGIN
    // =====================================================
    public Map<String, Object> login(Map<String, String> data) {
        Map<String, Object> response = new HashMap<>();

        try {
            String email = data.get("email");
            String password = data.get("password");

            User user = service.login(email, password);
            response.put("status", 200);
            response.put("data", user);

        } catch (UnauthorizedException e) {
            response.put("status", 401);
            response.put("error", e.getMessage());

        } catch (ServiceException e) {
            response.put("status", 500);
            response.put("error", "Error interno del servidor: " + e.getMessage());

        } catch (DataAccessException e) {
            response.put("status", 500);
            response.put("error", "Error de acceso a datos: " + e.getMessage());

        } finally {
            System.out.println("→ Operación finalizada");
        }

        return response;
    }
}
