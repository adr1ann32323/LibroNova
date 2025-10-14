package com.LibroNova.app.view;

import com.LibroNova.app.domain.User;
import com.LibroNova.app.util.SessionContext;

import javax.swing.JOptionPane;

public class StaffView {

    private final LoanView loanView;
    private final BookView bookView;
    private final UserView userView;

    // 🔹 Constructor con las vistas necesarias
    public StaffView(LoanView loanView, BookView bookView, UserView userView) {
        this.loanView = loanView;
        this.bookView = bookView;
        this.userView = userView;
    }

    public void showMenu() {
        User currentUser = SessionContext.getCurrentUser();
        if (currentUser == null) {
            JOptionPane.showMessageDialog(null, "No hay usuario logueado.");
            return;
        }

        String[] options = {"Gestionar préstamos", "Gestionar libros", "Gestionar usuarios", "Gestionar incidentes", "Salir"};
        int choice;

        do {
            choice = JOptionPane.showOptionDialog(
                    null,
                    "Bienvenido, " + currentUser.getName(),
                    "Menú Staff",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            switch (choice) {
                case 0 -> manageLoans();
                case 1 -> manageBooks();
                case 2 -> manageUsers();
                case 3 -> manageIncidents();
            }

        } while (choice != 4 && choice != JOptionPane.CLOSED_OPTION);
    }

    private void manageLoans() {
        loanView.showMenu();
    }

    private void manageBooks() {
        bookView.showMenu();
    }

    private void manageUsers() {
        userView.showMenu();
    }

    private void manageIncidents() {
        JOptionPane.showMessageDialog(null, "Función gestionar incidentes aún no implementada");
    }
}
