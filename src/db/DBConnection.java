package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static DBConnection dBonnection;
    private Connection connection;

    private DBConnection(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/coronasystem", "root", "password");

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static DBConnection getInstance(){
        return (dBonnection == null)? dBonnection = new DBConnection() : dBonnection;
    }

    public Connection getConnection(){
        return connection;
    }
}
