package DatabaseUtility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/TrainSchedulerDB?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";         // 改成你的帳號
    private static final String PASSWORD = "appleleo101";   // 改成你的密碼

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
