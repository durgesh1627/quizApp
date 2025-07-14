package servlets;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.util.Enumeration;

public class SubmitAnswerServlet extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        // 🛡️ Null-safe parsing
        String totalParam = req.getParameter("total");
        int totalQuestions = 0;
        if (totalParam != null && !totalParam.isEmpty()) {
            try {
                totalQuestions = Integer.parseInt(totalParam);
            } catch (NumberFormatException e) {
                out.println("<p style='color:red;'>❌ Invalid total questions parameter.</p>");
                return;
            }
        } else {
            out.println("<p style='color:red;'>❌ Missing total parameter!</p>");
            return;
        }

        int score = 0;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/durgesh", "root", "1627");

            for (int i = 1; i <= totalQuestions; i++) {
                String userAnswer = req.getParameter("q" + i);
                String correctAnswer = "";

                PreparedStatement pst = con.prepareStatement("SELECT correct_option FROM quizApp WHERE id = ?");
                pst.setInt(1, i);
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    correctAnswer = rs.getString(1);
                }

                if (userAnswer != null && userAnswer.equals(correctAnswer)) {
                    score++;
                }
            }

            // ✅ Store the score (if user logged in)
            HttpSession session = req.getSession(false);
            String username = (session != null) ? (String) session.getAttribute("username") : "guest";

            PreparedStatement insert = con.prepareStatement("INSERT INTO quizResult (username, score) VALUES (?, ?)");
            insert.setString(1, username);
            insert.setInt(2, score);
            insert.executeUpdate();

            out.println("<h1>✅ Quiz Submitted</h1>");
            out.println("<p>Your Score: " + score + " / " + totalQuestions + "</p>");
            out.println("<a href='dashboard.html'>Go to Dashboard</a>");

            con.close();
        } catch (Exception e) {
            out.println("<p style='color:red;'>❌ Error: " + e.getMessage() + "</p>");
        }
    }
}
