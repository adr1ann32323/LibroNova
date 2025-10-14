package com.LibroNova.app;

import com.LibroNova.app.controller.BookController;
import com.LibroNova.app.controller.LoanController;
import com.LibroNova.app.controller.UserController;
import com.LibroNova.app.view.*;
import com.LibroNova.app.dao.jdbc.BookDaoJdbc;
import com.LibroNova.app.dao.jdbc.LoanDaoJdbc;
import com.LibroNova.app.dao.jdbc.UserDaoJdbc;
import com.LibroNova.app.service.BookService;
import com.LibroNova.app.service.LoanService;
import com.LibroNova.app.service.UserService;

public class Main {

    public static void main(String[] args) {

        // ================================
        // Inicialización de dependencias
        // ================================

        // DAO
        UserDaoJdbc userDao = new UserDaoJdbc();
        BookDaoJdbc bookDao = new BookDaoJdbc();
        LoanDaoJdbc loanDao = new LoanDaoJdbc();
        // Service
        UserService userService = new UserService(userDao);
        BookService bookService = new BookService(bookDao);
        LoanService loanService = new LoanService(loanDao, bookDao);
        // Controller
        UserController userController = new UserController(userService);
        BookController bookController = new BookController(bookService);
        LoanController loanController = new LoanController(loanService);

        // ================================
        // Inicialización de Views
        // ================================
        // === View ===
        UserView userView = new UserView(userController);
        BookView bookView = new BookView(bookController);
        LoanView loanView = new LoanView(loanController);
// === Staff View ===
        StaffView staffView = new StaffView(loanView, bookView, userView);

// === Member View ===
        MemberView memberView = new MemberView();

// === Main View ===
        MainView mainView = new MainView(userController, memberView, staffView, bookView);
        // Iniciar aplicación
        // ================================
        mainView.start();
    }
}
