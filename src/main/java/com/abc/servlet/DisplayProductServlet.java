package com.abc.servlet;

import com.abc.dao.RetailDataImp;
import com.abc.model.RetailModule;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/products")
public class DisplayProductServlet extends HttpServlet {
    private RetailDataImp retailDataImp = new RetailDataImp();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<RetailModule> productList = retailDataImp.getAll();

        request.setAttribute("products", productList);

        request.getRequestDispatcher("/displayProducts.jsp").forward(request, response);
    }
}
