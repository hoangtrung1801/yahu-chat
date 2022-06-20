package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    private static DatabaseConnection dbCon = new DatabaseConnection();

//    private static final String url = "jdbc:sqlserver://localhost:1433;" + "databaseName=chat_realtime";
    private static final String url = "jdbc:mysql://localhost:4306/chat_realtime";
    private static final String username = "sa";
    private static final String password = "123456";

    public static Connection conn;
    private static Statement stmt;

    private DatabaseConnection() {
        createConnection();
    }

    public static DatabaseConnection connect() {
        if(dbCon == null) {
            dbCon = new DatabaseConnection();
            System.out.println("Connected to database");
        }
        return dbCon;
    }

    private void createConnection() {
        try {
            conn = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws SQLException {
    }
}
