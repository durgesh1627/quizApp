package servlets;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class AuthServlet extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        String mode = req.getParameter("mode"); // login or register
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String role = req.getParameter("role");

        try {
            // Load JDBC driver and connect to database
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/durgesh", "root", "1627");

            if (mode.equals("register")) {
                // Registration mode
                String email = req.getParameter("email");
                String confirmPassword = req.getParameter("confirmPassword");

                if (!password.equals(confirmPassword)) {
                    out.println("<h3 style='color:red;'>❌ Passwords do not match</h3>");
                    return;
                }

                // Check if username already exists
                PreparedStatement check = con.prepareStatement("SELECT * FROM users WHERE username = ?");
                check.setString(1, username);
                ResultSet rs = check.executeQuery();
                if (rs.next()) {
                    out.println("<h3 style='color:red;'>❌ Username already exists</h3>");
                    return;
                }

                // Register user
                PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO users (username, password, email, role) VALUES (?, ?, ?, ?)");
                ps.setString(1, username);
                ps.setString(2, password); // You can hash this later
                ps.setString(3, email);
                ps.setString(4, role);
                ps.executeUpdate();

                out.println("<h3 style='color:green;'>✅ Registration successful! Please login.</h3>");
                out.println("<a href='index.html'>Back to login</a>");

            } else if (mode.equals("login")) {
                // Login mode
                PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM users WHERE username = ? AND password = ? AND role = ?");
                ps.setString(1, username);
                ps.setString(2, password);
                ps.setString(3, role);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    // Login success
                    HttpSession session = req.getSession();
                    session.setAttribute("username", username);
                    session.setAttribute("role", role);

                    if (role.equals("admin")) {
                        res.sendRedirect("admin_dashboard.html");
                    } else {
                        res.sendRedirect("dashboard.html");
                    }
                } else {
                    out.println("<h3 style='color:red;'>❌ Invalid credentials</h3>");
                    out.println("<a href='index.html'>Try again</a>");
                }
            }

            con.close();
        } catch (Exception e) {
            out.println("<p style='color:red;'>Error: " + e.getMessage() + "</p>");
            e.printStackTrace();
        }
    }
}
