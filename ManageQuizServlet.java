package servlets;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class ManageQuizServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/durgesh", "root", "1627");

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM quizApp");

            out.println("<html><body style='font-family:sans-serif;'>");
            out.println("<h2>🛠️ Manage Quiz Questions</h2>");
            out.println("<table border='1' cellpadding='10'>");
            out.println("<tr><th>ID</th><th>Question</th><th>Correct Option</th><th>Edit</th><th>Delete</th></tr>");

            while (rs.next()) {
                int id = rs.getInt("id");
                String question = rs.getString("question_text");
                String correct = rs.getString("correct_option");

                out.println("<tr>");
                out.println("<td>" + id + "</td>");
                out.println("<td>" + question + "</td>");
                out.println("<td>" + correct + "</td>");
                out.println("<td><a class='btn' href='EditQuestionServlet?id=" + id + "'>Edit</a></td>");
                out.println("<td><a class='btn delete' href='DeleteQuestionServlet?id=" + id + "'>Delete</a></td>");
                out.println("</tr>");
            }

            out.println("</table>");
            out.println("<br><a href='dashboard.html'>← Back to Dashboard</a>");
            out.println("</body></html>");

            con.close();
        } catch (Exception e) {
            out.println("<p style='color:red;'>Error: " + e.getMessage() + "</p>");
        }
    }
}
