package module;

import java.sql.Connection;
import java.sql.DriverManager;

public class Connector {

    private static Connection connection;
    private static final String DATABASE = "sqlbuilder";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    private static final String URL = "jdbc:mysql://localhost/"+DATABASE;

    public static Connection getConnection() throws UnsupportedOperationException {
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

}
