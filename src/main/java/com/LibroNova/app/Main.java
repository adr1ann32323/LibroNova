package com.LibroNova.app;

import com.LibroNova.app.controller.*;
import com.LibroNova.app.dao.jdbc.*;
import com.LibroNova.app.service.*;
import com.LibroNova.app.view.*;

public class Main {

    public static void main(String[] args) {

        // ================================
        // Inicialización de dependencias
        // ================================

        // DAO
        UserDaoJdbc userDao = new UserDaoJdbc();
        BookDaoJdbc bookDao = new BookDaoJdbc();
        LoanDaoJdbc loanDao = new LoanDaoJdbc();

        // ================================
        // Servicios
        // ================================
        IUserService baseService = new UserService(userDao);
        IUserService userService = new UserServiceDecorator(baseService);
        BookService bookService = new BookService(bookDao);
        LoanService loanService = new LoanService(loanDao, bookDao);

        // ================================
        // Controladores
        // ================================
        UserController userController = new UserController(userService);
        BookController bookController = new BookController(bookService);
        LoanController loanController = new LoanController(loanService);

        // ================================
        // Vistas
        // ================================
        UserView userView = new UserView(userController);
        BookView bookView = new BookView(bookController);
        LoanView loanView = new LoanView(loanController);

        StaffView staffView = new StaffView(loanView, bookView, userView);
        MemberView memberView = new MemberView();
        MainView mainView = new MainView(userController, memberView, staffView, bookView);

        // ================================
        // Iniciar aplicación
        // ================================
        mainView.start();
    }
}
