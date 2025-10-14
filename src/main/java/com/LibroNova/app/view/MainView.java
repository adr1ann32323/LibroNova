package com.LibroNova.app.view;

import com.LibroNova.app.controller.UserController;
import com.LibroNova.app.domain.User;
import com.LibroNova.app.util.SessionContext;

import javax.swing.JOptionPane;
import java.util.HashMap;
import java.util.Map;

public class MainView {

    private final UserController userController;
    private final MemberView memberView;
    private final StaffView staffView;
    private final BookView bookView;

    public MainView(UserController userController,
            MemberView memberView,
            StaffView staffView,
            BookView bookView) {
        this.userController = userController;
        this.memberView = memberView;
        this.staffView = staffView;
        this.bookView = bookView;
    }

    public void start() {
        boolean loginOk = false;

        while (!loginOk) {
            // LOGIN
            String email = JOptionPane.showInputDialog("Ingrese su email:");
            String password = JOptionPane.showInputDialog("Ingrese su contraseña:");

            // Crear el HashMap con los datos de login
            Map<String, String> loginData = new HashMap<>();
            loginData.put("email", email);
            loginData.put("password", password);

// Llamar al controlador pasando el HashMap
            Map<String, Object> response = userController.login(loginData);

// Procesar la respuesta
            if ((int) response.get("status") == 200) {
                User user = (User) response.get("data");

                if (!user.isActive()) {
                    JOptionPane.showMessageDialog(null, "Usuario inactivo. Contacte con administración.");
                } else {
                    SessionContext.setCurrentUser(user);

                    loginOk = true;

                    if ("MEMBER".equals(user.getRole())) {
                        memberView.showMenu();
                    } else {
                        staffView.showMenu();
                    }
                }
            } else {
                int status = (int) response.get("status");
                String error = (String) response.get("error");

                switch (status) {
                    case 400 -> JOptionPane.showMessageDialog(null, "Datos inválidos: " + error);
                    case 401 -> JOptionPane.showMessageDialog(null, "No autorizado: " + error);
                    case 404 -> JOptionPane.showMessageDialog(null, "Usuario no encontrado: " + error);
                    case 500 -> JOptionPane.showMessageDialog(null, "Error interno del servidor");
                    default -> JOptionPane.showMessageDialog(null, "Error: " + error);
                }
            }


            // Preguntar si desea cerrar sesión
            int option = JOptionPane.showConfirmDialog(null, "¿Cerrar sesión?", "Salir",
                    JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                break;
            }
        }
    }
}
