package com.LibroNova.app.util;

import com.LibroNova.app.domain.User;

public class SessionContext {
    private static User currentUser;

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void logout() {
        currentUser = null;
    }
}
