package servlets;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class EditQuestionServlet extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        String question = req.getParameter("question");
        String option1 = req.getParameter("opt1");
        String option2 = req.getParameter("opt2");
        String option3 = req.getParameter("opt3");
        String option4 = req.getParameter("opt4");
        String correctOption = req.getParameter("correct");

        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/durgesh", "root", "1627");

            String query = "UPDATE quizApp SET question=?, option1=?, option2=?, option3=?, option4=?, correct_option=? WHERE id=?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, question);
            pst.setString(2, option1);
            pst.setString(3, option2);
            pst.setString(4, option3);
            pst.setString(5, option4);
            pst.setString(6, correctOption);
            pst.setInt(7, id);

            int status = pst.executeUpdate();

            if (status > 0) {
                out.println("<h3 style='color:green;'>✅ Question Updated Successfully!</h3>");
            } else {
                out.println("<h3 style='color:red;'>❌ Failed to Update Question!</h3>");
            }

            con.close();
        } catch (Exception e) {
            out.println("<h3 style='color:red;'>Error: " + e.getMessage() + "</h3>");
            e.printStackTrace();
        }
    }
}
