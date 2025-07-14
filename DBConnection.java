package db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    public static Connection getConnection() {
        Connection con = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/durgesh", // DB ka sahi naam
                "root",                                // MySQL username
                "1627"                                 // MySQL password
            );
        } catch (Exception e) {
            System.out.println("DB Connection Error: " + e.getMessage());
        }
        return con;
    }
}
