package com.abc.util;

import java.sql.Connection;
import java.sql.DriverManager;
public class DatabaseConnection {

    public static Connection getConnection() {
        Connection connection = null;
        try {
            // Get connection details from environment variables
            String url = System.getenv("DB_URL");
            String user = System.getenv("DB_USERNAME");
            String password = System.getenv("DB_PASSWORD");
            String driver = System.getenv("DB_DRIVER");

            // Load the JDBC driver
            Class.forName(driver);

            // Establish the connection
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connection Successful");

        } catch (Exception e) {
            System.err.println("Connection error: " + e.getMessage());
        }
        return connection;
    }
}
