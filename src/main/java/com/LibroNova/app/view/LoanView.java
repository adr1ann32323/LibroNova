package com.LibroNova.app.view;

import com.LibroNova.app.controller.LoanController;
import com.LibroNova.app.domain.Loan;
import com.LibroNova.app.domain.User;
import com.LibroNova.app.errors.DataAccessException;
import com.LibroNova.app.util.SessionContext;

import javax.swing.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class LoanView {

    private final LoanController loanController;

    public LoanView(LoanController loanController) {
        this.loanController = loanController;
    }

    public void showMenu() {
        User currentUser = SessionContext.getCurrentUser();
        if (currentUser == null) {
            JOptionPane.showMessageDialog(null, "No hay sesión activa.");
            return;
        }

        if ("STAFF".equalsIgnoreCase(currentUser.getRole())) {
            showStaffMenu();
        } else {
            showMemberMenu(currentUser);
        }
    }

    private void showStaffMenu() {
        String[] options = { "Registrar préstamo", "Registrar devolución", "Listar todos los préstamos", "Volver" };
        int choice;
        do {
            choice = JOptionPane.showOptionDialog(
                    null,
                    "Gestión de Préstamos",
                    "Menú de Préstamos STAFF",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]);

            switch (choice) {
                case 0 -> registerLoan();
                case 1 -> returnBook();
                case 2 -> listAllLoans();
            }
        } while (choice != 3 && choice != JOptionPane.CLOSED_OPTION);
    }

    private void showMemberMenu(User currentUser) {
        String[] options = { "Ver mis préstamos", "Volver" };
        int choice;
        do {
            choice = JOptionPane.showOptionDialog(
                    null,
                    "Bienvenido, " + currentUser.getName(),
                    "Mis Préstamos",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]);

            if (choice == 0)
                listUserLoans(currentUser.getId());
        } while (choice != 1 && choice != JOptionPane.CLOSED_OPTION);
    }

    // ========================
    // FUNCIONES STAFF
    // ========================

    private void registerLoan() {
        try {
            int idUser = Integer.parseInt(JOptionPane.showInputDialog("ID del usuario:"));
            int idBook = Integer.parseInt(JOptionPane.showInputDialog("ID del libro:"));

            LocalDate today = LocalDate.now();
            LocalDate dueDate = today.plusDays(7); 

            Loan loan = new Loan(idUser, idBook, Date.valueOf(today), Date.valueOf(dueDate));

            Map<String, Object> response = loanController.createLoan(loan);

            if ((int) response.get("status") == 200) {
                JOptionPane.showMessageDialog(null, "Préstamo registrado correctamente.");
            } else {
                JOptionPane.showMessageDialog(null, "Error: " + response.get("error"));
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al registrar préstamo: " + e.getMessage());
        }
    }

    private void returnBook() {
        try {
            int idLoan = Integer.parseInt(JOptionPane.showInputDialog("ID del préstamo a devolver:"));
            Map<String, Object> response = loanController.returnBook(idLoan);

            if ((int) response.get("status") == 200) {
                JOptionPane.showMessageDialog(null, "Libro devuelto correctamente.");
            } else {
                JOptionPane.showMessageDialog(null, "Error: " + response.get("error"));
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al devolver libro: " + e.getMessage());
        }
    }

    private void listAllLoans() {
        try {
            List<Loan> loans = loanController.listAll();
            if (loans.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No hay préstamos registrados.");
                return;
            }

            StringBuilder sb = new StringBuilder("Préstamos registrados:\n");
            for (Loan l : loans) {
                sb.append(l).append("\n");
            }

            JTextArea textArea = new JTextArea(sb.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new java.awt.Dimension(400, 250));
            JOptionPane.showMessageDialog(null, scrollPane, "Listado de Préstamos", JOptionPane.INFORMATION_MESSAGE);

        } catch (DataAccessException e) {
            JOptionPane.showMessageDialog(null, "Error al listar préstamos: " + e.getMessage());
        }
    }

    // ========================
    // FUNCIONES MEMBER
    // ========================

    private void listUserLoans(int userId) {
        try {
            List<Loan> loans = loanController.listByUser(userId);
            if (loans.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No tienes préstamos activos.");
                return;
            }

            StringBuilder sb = new StringBuilder("Tus préstamos:\n");
            for (Loan l : loans) {
                sb.append(l).append("\n");
            }

            JTextArea textArea = new JTextArea(sb.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new java.awt.Dimension(400, 250));
            JOptionPane.showMessageDialog(null, scrollPane, "Mis Préstamos", JOptionPane.INFORMATION_MESSAGE);

        } catch (DataAccessException e) {
            JOptionPane.showMessageDialog(null, "Error al consultar tus préstamos: " + e.getMessage());
        }
    }
}
