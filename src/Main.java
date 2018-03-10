import module.Builder;
import module.Connector;

import java.io.DataInputStream;
import java.sql.Connection;

public class Main {

    public static void main(String[] args){
        System.out.println("Hello World");

        Connection con = Connector.getConnection();

        System.out.println(con);
    }
}
