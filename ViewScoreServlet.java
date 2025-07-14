package servlets;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class ViewScoreServlet extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        HttpSession session = req.getSession(false);
        String username = (session != null) ? (String) session.getAttribute("username") : "guest";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/durgesh", "root", "1627");

            PreparedStatement pst = con.prepareStatement("SELECT score FROM quizResult WHERE username = ?");
            pst.setString(1, username);
            ResultSet rs = pst.executeQuery();

            out.println("<html><body style='font-family:sans-serif;'>");
            out.println("<h2>📊 Scores for " + username + "</h2>");
            out.println("<ul>");

            boolean hasScores = false;
            while (rs.next()) {
                hasScores = true;
                out.println("<li>Score: " + rs.getInt("score") + "</li>");
            }

            if (!hasScores) {
                out.println("<p>No scores found yet.</p>");
            }

            out.println("</ul>");
            out.println("<a href='dashboard.html'>← Back to Dashboard</a>");
            out.println("</body></html>");

            con.close();

        } catch (Exception e) {
            out.println("<p style='color:red;'>Error: " + e.getMessage() + "</p>");
        }
    }
}
