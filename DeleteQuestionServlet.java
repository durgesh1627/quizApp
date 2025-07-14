package servlets;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.sql.*;

import javax.servlet.annotation.WebServlet;

@WebServlet("/DeleteQuestionServlet")
public class DeleteQuestionServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        try {
            int id = Integer.parseInt(req.getParameter("id"));

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/durgesh", "root", "1627");

            PreparedStatement pst = con.prepareStatement("DELETE FROM quizApp WHERE id = ?");
            pst.setInt(1, id);
            int result = pst.executeUpdate();

            if (result > 0) {
                res.sendRedirect("ManageQuizServlet");
            } else {
                out.println("<h3 style='color:red;'>❌ Failed to delete question with ID: " + id + "</h3>");
            }

            con.close();

        } catch (Exception e) {
            out.println("<p style='color:red;'>Error: " + e.getMessage() + "</p>");
            e.printStackTrace();
        }
    }
}
