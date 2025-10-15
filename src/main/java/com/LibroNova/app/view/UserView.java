package com.LibroNova.app.view;

import com.LibroNova.app.controller.UserController;
import com.LibroNova.app.domain.User;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserView {

    private final UserController userController;

    public UserView(UserController userController) {
        this.userController = userController;
    }

    public void showMenu() {
        String[] options = {"Listar usuarios", "Crear usuario", "Actualizar estado", "Salir"};
        int choice;

        do {
            choice = JOptionPane.showOptionDialog(
                    null,
                    "Gestión de usuarios",
                    "User Menu",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            switch (choice) {
                case 0 -> listUsers();
                case 1 -> createUser();
                case 2 -> updateUserStatus();
            }

        } while (choice != 3 && choice != JOptionPane.CLOSED_OPTION);
    }

    // ----------------------------------------
    // Listar todos los usuarios
    // ----------------------------------------
    private void listUsers() {
        Map<String, Object> response = userController.listUsers();

        if ((int) response.get("status") == 200) {
            List<User> users = (List<User>) response.get("data");
            StringBuilder sb = new StringBuilder("Usuarios:\n");
            for (User u : users) sb.append(u).append("\n");
            JOptionPane.showMessageDialog(null, sb.toString());
        } else {
            JOptionPane.showMessageDialog(null, "Error: " + response.get("error"));
        }
    }

    // ----------------------------------------
    // Crear un usuario nuevo
    // ----------------------------------------
    private void createUser() {
        String name = JOptionPane.showInputDialog("Nombre:");
        String email = JOptionPane.showInputDialog("Email:");
        String password = JOptionPane.showInputDialog("Password:");
        String role = JOptionPane.showInputDialog("Rol (MEMBER/STAFF):");
        String activeStr = JOptionPane.showInputDialog("Activo? (true/false):");

        Map<String, String> data = new HashMap<>();
        data.put("name", name);
        data.put("email", email);
        data.put("password", password);
        data.put("role", role);
        data.put("active", activeStr);

        Map<String, Object> response = userController.createUser(data);

        if ((int) response.get("status") == 200) {
            JOptionPane.showMessageDialog(null, "Usuario creado:\n" + response.get("data"));
        } else {
            JOptionPane.showMessageDialog(null, "Error: " + response.get("error"));
        }
    }

    // ----------------------------------------
    // Actualizar el estado activo/inactivo
    // ----------------------------------------
    
    private void updateUserStatus() {
        String idStr = JOptionPane.showInputDialog("ID del usuario:");
        String activeStr = JOptionPane.showInputDialog("Activo? (true/false):");

        try {
            int id = Integer.parseInt(idStr);
            boolean active = Boolean.parseBoolean(activeStr);

            Map<String, Object> response = userController.updateUserStatus(id, active);

            if ((int) response.get("status") == 200) {
                JOptionPane.showMessageDialog(null, "Estado actualizado correctamente");
            } else {
                JOptionPane.showMessageDialog(null, "Error: " + response.get("error"));
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID inválido");
        }
    }
}
