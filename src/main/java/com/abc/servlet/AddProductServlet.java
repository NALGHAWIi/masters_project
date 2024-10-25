package com.abc.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.abc.dao.RetailDataImp;
import com.abc.model.RetailModule;


@WebServlet("/addProduct")
public class AddProductServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Forward to addProduct.jsp to render the form
        request.getRequestDispatcher("addProduct.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Handle form submission to add product

        String productName = request.getParameter("product_name");
        double price = Double.parseDouble(request.getParameter("price"));
        String productDescription = request.getParameter("product_description");

        // Create a new RetailModule instance
        RetailModule newProduct = new RetailModule();
        newProduct.setProduct_name(productName);
        newProduct.setPrice(price);
        newProduct.setProduct_description(productDescription);

        // Access DAO and add product logic
        RetailDataImp retailData = new RetailDataImp();
        retailData.create(newProduct); // Use the create method from RetailDataImp

        // Redirect back to the index after adding the product
        response.sendRedirect("/products");
    }
}
