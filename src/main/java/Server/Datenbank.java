package Server;

import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import java.sql.SQLException;

public class Datenbank {
    JdbcPooledConnectionSource connection;

    public Datenbank(String url, String user, String password){
        try{
            connection = new JdbcPooledConnectionSource(url,user,password);
        } catch (SQLException throwables) {
            System.out.println("Verbindung konnte nicht aufgebaut werden;");
        }
    }

    public JdbcPooledConnectionSource getConnection(){
        return connection;
    }
}
