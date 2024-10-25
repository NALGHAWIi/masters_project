package com.abc.dao;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import com.abc.model.RetailModule;
import com.abc.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


public class RetailDataImpTest {

    private RetailDataImp retailDataImp;
    private RetailModule product;
    private Connection connection;
    private String table = "test_products";

    @Before
    public void setUp() {
        connection = DatabaseConnection.getConnection();

        try (Statement stmt = connection.createStatement()) {
            stmt.execute("TRUNCATE TABLE test_products");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        retailDataImp = new RetailDataImp();
        product = new RetailModule();
        product.setProduct_name("Test Product");
        product.setProduct_description("Test Description");
        product.setPrice(100.0);
    }

    @After
    public void tearDown() {
        if (product != null && product.getProduct_id() != 0) {
            retailDataImp.delete(product.getProduct_id());
        }
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("TRUNCATE TABLE test_products");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close(); // Close the connection
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testCreateProduct() {
        RetailModule product = new RetailModule();
        product.setProduct_name("Read_by_name_test_product");
        product.setPrice(50.0);
        product.setProduct_description("test description");
        retailDataImp.create(product, table);

        RetailModule retrievedProduct = retailDataImp.readByName(product.getProduct_name(), table);
        assertNotNull("Product should not be null after creation", retrievedProduct);
        assertEquals("Product name should match", product.getProduct_name(), retrievedProduct.getProduct_name());
        assertEquals("Product description should match", product.getProduct_description(), retrievedProduct.getProduct_description());
        assertEquals("Product price should match", product.getPrice(), retrievedProduct.getPrice(), 0.01);
    }

    @Test
    public void testGetAllProducts() {
        // Create a few products (you can add more if needed)
        RetailModule product1 = new RetailModule();
        product1.setProduct_name("Product 1");
        product1.setPrice(50.0);
        retailDataImp.create(product1, table);

        RetailModule product2 = new RetailModule();
        product2.setProduct_name("Product 2");
        product2.setPrice(75.0);
        retailDataImp.create(product2, table);

        // Retrieve all products
        List<RetailModule> productList = retailDataImp.getAll(table);
        assertNotNull("Product list should not be null", productList);
        assertTrue("Product list should contain at least two products", productList.size() >= 2);
    }
}
