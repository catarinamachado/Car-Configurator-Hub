package CCH.dataaccess;

import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CCHConnection {

    private static Connection connection;

    public static void startConnection(String driver, String url, String user, String password) {
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url + "?user=" + user + "&password=" + password);
        }
        catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return connection;
    }
}
