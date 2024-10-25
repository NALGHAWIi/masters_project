<%@ page import="java.util.List" %>
<%@ page import="com.abc.model.RetailModule" %>
<!DOCTYPE html>
<html>
<head>
    <title>All Products</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            max-width: 1000px;
            padding: 20px;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin: 20px 0;
        }
        th, td {
            border: 1px solid #ccc;
            padding: 10px;
            text-align: left;
        }
        th {
            background-color: #f4f4f4;
        }
        button {
            padding: 10px 20px;
            margin-bottom: 20px;
            background-color: #4CAF50;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
        button:hover {
            background-color: #45a049;
        }
    </style>
</head>
<body>
    <h1>All Products</h1>
    <button type="button" onclick="window.location.href='addProduct'">Add Product</button>
    <table>
        <tr>
            <th>Product ID</th>
            <th>Product Name</th>
            <th>Price</th>
            <th>Product Description</th>
        </tr>
        <!-- Loop through the product list -->
        <%
            List<RetailModule> products = (List<RetailModule>) request.getAttribute("products");
            if (products != null) {
                for (RetailModule product : products) {
        %>
        <tr>
            <td><%= product.getProduct_id() %></td>
            <td><%= product.getProduct_name() %></td>
            <td><%= product.getPrice() %></td>
            <td><%= product.getProduct_description() %></td>
        </tr>
        <%
                }
            } else {
        %>
        <tr>
            <td colspan="4">No products available</td>
        </tr>
        <%
            }
        %>
    </table>
</body>
</html>
