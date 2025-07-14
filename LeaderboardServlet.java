package servlets;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class LeaderboardServlet extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/durgesh", "root", "1627");

            String query = "SELECT username, score FROM quizResult ORDER BY score DESC LIMIT 10";
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            out.println("<html><head><title>Leaderboard</title></head><body style='font-family:sans-serif;'>");
            out.println("<h2>🏆 Top 10 Scores</h2>");
            out.println("<table border='1' cellpadding='10'>");
            out.println("<tr><th>Rank</th><th>Username</th><th>Score</th></tr>");

            int rank = 1;
            while (rs.next()) {
                out.println("<tr><td>" + rank + "</td><td>" + rs.getString("username") + "</td><td>" + rs.getInt("score") + "</td></tr>");
                rank++;
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
