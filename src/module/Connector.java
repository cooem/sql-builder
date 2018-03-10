package module;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class Connector {

    private static Connection connection;

    public static Connection getConnection() throws UnsupportedOperationException {
        Properties prop = new Properties();
        InputStream input = null;

        try {
            input = new FileInputStream(".env");

            prop.load(input);

            connection = DriverManager.getConnection(prop.getProperty("DB_URL")+prop.getProperty("DB_DATABASE"), prop.getProperty("DB_USERNAME"), prop.getProperty("DB_PASSWORD"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

}
