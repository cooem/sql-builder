import module.Connector;
import module.DataType;

import java.sql.Connection;

public class Main {

    public static void main(String[] args){
        System.out.println("Hello World");

        Connection con = Connector.getConnection();

        System.out.println(con);
    }
}
