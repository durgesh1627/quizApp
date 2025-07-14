package servlets;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

import db.DBConnection;
import javax.servlet.annotation.WebServlet;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM users WHERE username = ? AND password = ?"
            );
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // ✅ Login success
                HttpSession session = request.getSession();
                session.setAttribute("username", username);

                response.sendRedirect("dashboard.html");
            } else {
                // ❌ Login failed
                out.println("<h3 style='color:red;'>Invalid username or password!</h3>");
            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {
            out.println("<h3 style='color:red;'>Login Error: " + e.getMessage() + "</h3>");
            e.printStackTrace();
        }
    }
}
