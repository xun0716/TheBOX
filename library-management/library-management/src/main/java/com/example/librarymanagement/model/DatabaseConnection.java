import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Descriptors.Descriptor;


public class DatabaseConnection {
    private static final String URL = System.getenv("DB_URL");
    private static final String USER = System.getenv("DB_USER");
    private static final String PASSWORD = System.getenv("DB_PASSWORD");

    public static Connection getConnection() {
        try {
            // 手動加載 MySQL JDBC 驅動
            Class.forName("com.mysql.cj.jdbc.Driver"); // 對應 MySQL JDBC 8.x 驅動
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connection successful!");
            return conn;
        } catch (SQLException e) {
            System.err.println("Failed to connect to the database: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found: " + e.getMessage());
        }
        return null;
    }

    public static void main(String[] args) {
        // 輸出環境變數以檢查
        System.out.println("DB_URL: " + URL);
        System.out.println("DB_USER: " + USER);

        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn != null) {
                // 做一些資料庫操作
                // 可以在這裡進行資料庫操作，例如查詢等
            }
        } catch (SQLException e) {
            System.err.println("Failed to close the connection: " + e.getMessage());
        }
    }
}
