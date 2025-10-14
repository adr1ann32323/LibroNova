package com.LibroNova.app.config;

import com.LibroNova.app.errors.DataAccessException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBconfig {
    private static String url;
    private static String user;
    private static String pass;

    public static Connection connect() throws DataAccessException {
        try (InputStream input = DBconfig.class.getClassLoader()
                .getResourceAsStream("config/application.properties")) {

            Properties props = new Properties();
            props.load(input);
            url = props.getProperty("db.url");
            user = props.getProperty("db.user");
            pass = props.getProperty("db.password");

            return DriverManager.getConnection(url, user, pass);

        } catch (SQLException | FileNotFoundException e) {
            throw new DataAccessException("Error al conectar con la base de datos", e);
        } catch (IOException e) {
            throw new DataAccessException("Error al leer archivo de configuración", e);
        }
    }
}

