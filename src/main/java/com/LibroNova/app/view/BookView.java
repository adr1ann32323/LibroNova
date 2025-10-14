package com.LibroNova.app.view;

import com.LibroNova.app.controller.BookController;
import com.LibroNova.app.domain.Book;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookView {

    private final BookController bookController;

    public BookView(BookController bookController) {
        this.bookController = bookController;
    }

    public void showMenu() {
        String[] options = {"Listar libros", "Crear libro", "Actualizar stock", "Salir"};
        int choice;

        do {
            choice = JOptionPane.showOptionDialog(
                    null,
                    "Gestión de libros",
                    "Book Menu",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            switch (choice) {
                case 0 -> listBooks();
                case 1 -> createBook();
                case 2 -> updateStock();
            }

        } while (choice != 3 && choice != JOptionPane.CLOSED_OPTION);
    }

    private void listBooks() {
        Map<String, Object> response = bookController.listBooks();

        if ((int) response.get("status") == 200) {
            List<Book> books = (List<Book>) response.get("data");
            StringBuilder sb = new StringBuilder("Libros:\n");
            for (Book b : books) sb.append(b).append("\n");
            JOptionPane.showMessageDialog(null, sb.toString());
        } else {
            JOptionPane.showMessageDialog(null, response.get("error"));
        }
    }

    private void createBook() {
        String title = JOptionPane.showInputDialog("Título:");
        String author = JOptionPane.showInputDialog("Autor:");
        String isbn = JOptionPane.showInputDialog("ISBN:");
        String stockStr = JOptionPane.showInputDialog("Stock:");

        Map<String, String> data = new HashMap<>();
        data.put("title", title);
        data.put("author", author);
        data.put("isbn", isbn);
        data.put("stock", stockStr);

        Map<String, Object> response = bookController.createBook(data);

        if ((int) response.get("status") == 200) {
            JOptionPane.showMessageDialog(null, "Libro creado:\n" + response.get("data"));
        } else {
            JOptionPane.showMessageDialog(null, "Error: " + response.get("error"));
        }
    }

    private void updateStock() {
        String idStr = JOptionPane.showInputDialog("ID del libro:");
        String stockStr = JOptionPane.showInputDialog("Nuevo stock:");

        try {
            int id = Integer.parseInt(idStr);
            int stock = Integer.parseInt(stockStr);
            Map<String, Object> response = bookController.updateStock(id, stock);

            if ((int) response.get("status") == 200) {
                JOptionPane.showMessageDialog(null, "Stock actualizado correctamente");
            } else {
                JOptionPane.showMessageDialog(null, "Error: " + response.get("error"));
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Valores numéricos inválidos");
        }
    }
}
