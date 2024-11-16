package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3307/attendance_db";
    private static final String USER = "root";
    private static final String PASSWORD = "hoaithan258";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    static {
        try {
            // Nạp driver MySQL
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Phương thức để lấy kết nối cơ sở dữ liệu
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
