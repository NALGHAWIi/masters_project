package com.abc.util;

import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertNotNull;

public class DatabaseConnectionTest {

    @Test
    public void testDatabaseConnection() {
        try {
            Connection connection = DatabaseConnection.getConnection();
            assertNotNull("Database connection should not be null", connection);
            // You can also perform additional checks if needed (e.g., execute a query)
            connection.close(); // Close the connection after testing
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
