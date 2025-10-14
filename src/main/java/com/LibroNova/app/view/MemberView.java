package com.LibroNova.app.view;

import com.LibroNova.app.util.SessionContext;
import com.LibroNova.app.domain.User;

import javax.swing.JOptionPane;

public class MemberView {

    public void showMenu() {
        User currentUser = SessionContext.getCurrentUser();
        if (currentUser == null) {
            JOptionPane.showMessageDialog(null, "No hay usuario logueado.");
            return;
        }

        String[] options = {"Ver préstamos", "Solicitar préstamo", "Devolver libro", "Salir"};
        int choice;

        do {
            choice = JOptionPane.showOptionDialog(
                    null,
                    "Bienvenido, " + currentUser.getName(),
                    "Menú Member",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            switch (choice) {
                case 0 -> viewLoans();
                case 1 -> requestLoan();
                case 2 -> returnLoan();
            }

        } while (choice != 3 && choice != JOptionPane.CLOSED_OPTION);
    }

    private void viewLoans() {
        // Llamar a LoanController para listar préstamos propios
        JOptionPane.showMessageDialog(null, "Función ver préstamos aún no implementada");
    }

    private void requestLoan() {
        // Llamar a LoanController para solicitar préstamo
        JOptionPane.showMessageDialog(null, "Función solicitar préstamo aún no implementada");
    }

    private void returnLoan() {
        // Llamar a LoanController para devolver libro
        JOptionPane.showMessageDialog(null, "Función devolver libro aún no implementada");
    }
}
