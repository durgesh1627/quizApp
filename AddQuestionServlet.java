package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import db.DBConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AddQuestionServlet extends HttpServlet {
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        String question = req.getParameter("question");
        String option1 = req.getParameter("option1");
        String option2 = req.getParameter("option2");
        String option3 = req.getParameter("option3");
        String option4 = req.getParameter("option4");
        String correct = req.getParameter("correct");

        try {
            Connection con = DBConnection.getConnection();

            String query = "INSERT INTO quizApp (question, option1, option2, option3, option4, correct_option) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, question);
            pst.setString(2, option1);
            pst.setString(3, option2);
            pst.setString(4, option3);
            pst.setString(5, option4);
            pst.setString(6, correct);
                  
            int i = pst.executeUpdate();

            if (i > 0) {
                out.println("<h3>✅ Question Added Successfully!</h3>");
            } else {
                out.println("<h3>❌ Failed to add question.</h3>");
            }

            con.close();
        } catch (Exception e) {
            out.println("❌ Error: " + e.getMessage());
        }
    }
}
