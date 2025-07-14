package servlets;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

import db.DBConnection;
import javax.servlet.annotation.WebServlet;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {

    
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            Connection con = DBConnection.getConnection();

            // Check if username already exists
            PreparedStatement check = con.prepareStatement("SELECT * FROM users WHERE username = ?");
            check.setString(1, username);
            ResultSet rs = check.executeQuery();

            if (rs.next()) {
                out.println("<h3 style='color:red;'>Username already exists. Try another!</h3>");
            } else {
                // Insert new user into quizApp table
                PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO users (username, password) VALUES (?, ?)"
                );
                ps.setString(1, username);
                ps.setString(2, password); // plain text for now

                int i = ps.executeUpdate();
                if (i > 0) {
                    out.println("<h3 style='color:green;'>Registration successful!</h3>");
                    response.sendRedirect("index.html"); // after successful registration
                } else {
                    out.println("<h3 style='color:red;'>Registration failed!</h3>");
                }

                ps.close();
            }

            rs.close();
            con.close();

        } catch (Exception e) {
            out.println("<h3 style='color:red;'>Error: " + e.getMessage() + "</h3>");
            e.printStackTrace();
        }
    }
}
