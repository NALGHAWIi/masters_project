package com.abc.dao;

import com.abc.model.RetailModule;
import com.abc.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
// import java.util.Optional;

public class RetailDataImp implements RetailAccessObject {

    @Override
    public void create(RetailModule product) {
        create(product, "products"); // Default to "products" table
    }

    public void create(RetailModule product, String table) {
        // table = (table != null && !table.isEmpty()) ? table : "products";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                String.format("INSERT INTO %s (product_name, price, product_description) VALUES (?, ?, ?)", table))) {
            preparedStatement.setString(1, product.getProduct_name());
            preparedStatement.setDouble(2, product.getPrice());
            preparedStatement.setString(3, product.getProduct_description());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Other methods in your RetailDataImp class...

    // @Override
    // public void create(RetailModule product, String table) {
    //     table = (table != null && !table.isEmpty()) ? table : "products";

    //     try (Connection connection = DatabaseConnection.getConnection();
    //          PreparedStatement preparedStatement = connection.prepareStatement(
    //                  "INSERT INTO products (product_name, price, product_description) VALUES (?, ?, ?)")) {
    //         preparedStatement.setString(1, product.getProduct_name());
    //         preparedStatement.setDouble(2, product.getPrice());
    //         preparedStatement.setString(3, product.getProduct_description());
    //         preparedStatement.executeUpdate();
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //     }
    // }

    @Override
    public RetailModule read(int productId) {
        return read(productId, "products");
    }

    public RetailModule read(int productId, String table) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                String.format("SELECT * FROM %s WHERE product_name = ?", table))) {
            preparedStatement.setInt(1, productId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                RetailModule product = new RetailModule();
                product.setProduct_id(resultSet.getInt("product_id"));
                product.setProduct_name(resultSet.getString("product_name"));
                product.setProduct_description(resultSet.getString("product_description"));
                product.setPrice(resultSet.getDouble("price"));
                return product;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public RetailModule readByName(String productName, String table) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     String.format("SELECT * FROM %s WHERE product_name = ?", table))) {
            preparedStatement.setString(1, productName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                RetailModule product = new RetailModule();
                product.setProduct_id(resultSet.getInt("product_id"));
                product.setProduct_name(resultSet.getString("product_name"));
                product.setProduct_description(resultSet.getString("product_description"));
                product.setPrice(resultSet.getDouble("price"));
                return product;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    

    @Override
    public List<RetailModule> getAll() {
        return getAll("products"); // Default to "products" table
    }

    public List<RetailModule> getAll(String table) {
        List<RetailModule> productList = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                String.format("SELECT * FROM %s", table))) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                RetailModule product = new RetailModule();
                product.setProduct_id(resultSet.getInt("product_id"));
                product.setProduct_name(resultSet.getString("product_name"));
                product.setProduct_description(resultSet.getString("product_description"));
                product.setPrice(resultSet.getDouble("price"));
                productList.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productList;
    }

    @Override
    public void delete(int product_id) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "DELETE FROM products WHERE product_id = ?")) {
            preparedStatement.setInt(1, product_id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

// public class RetailDataImp implements RetailAccessObject {

//     private Map<Integer, RetailModule> products = new HashMap<>();

//     @Override
//     public void create(RetailModule product) {
//         products.put(product.getProduct_id(), product);
//     }

//     @Override
//     public RetailModule read(int productId) {
//         return products.get(productId);
//     }

//     public void addProduct(String productName, double price) {
//         int productId = generateProductId();
        
//         // Creating a new product with the generated ID, name, and price
//         RetailModule newProduct = new RetailModule();
//         newProduct.setProduct_id(productId);
//         newProduct.setProduct_name(productName);
//         newProduct.setPrice(price);
        
//         // Adding the newly created product to the products map
//         this.create(newProduct);
//     }

//     private int generateProductId() {
//         return products.size() + 1;
//     }
// }
