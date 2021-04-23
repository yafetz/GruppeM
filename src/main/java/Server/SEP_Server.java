package Server;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

import java.io.IOException;
import java.sql.*;

public class SEP_Server {
    Server server;
    Connection conn = null;
    public SEP_Server(){
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/sep?" + "user=root&password=");
        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        server = new Server(8080);
        server.setHandler(new AbstractHandler() {
            @Override
            public void handle(String s, Request request, HttpServletRequest clientrequest, HttpServletResponse response) throws IOException, ServletException {
                System.out.println("String: "+s);
                System.out.println("Request: "+request);
                System.out.println("HttpServletRequest: "+clientrequest);
                System.out.println("HttpServletResponse: "+response);
                response.setStatus(HttpServletResponse.SC_OK);
                if(s.equals("/")){

                }else if(s.equals("/login")){
                    ResultSet user = executeSQL("Select * FROM user");
                    try {
                        boolean logedIn = false;
                        while (user.next()) {
                            int id = user.getInt("ID");
                            int serverMatrikelnummer = user.getInt("Matrikelnummer");
                            String serverPassword = user.getString("Password").trim();
                            int Matrikelnummer = Integer.valueOf(clientrequest.getParameter("matr"));
                            String Password = clientrequest.getParameter("password").trim();
                            if(serverMatrikelnummer == Matrikelnummer && serverPassword.equals(Password)){
                                response.getWriter().println("true");
                                logedIn = true;
                                break;
                            }
                        }
                        if(!logedIn){
                            response.getWriter().println("false");
                        }
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
                request.setHandled(true);
            }
        });
    }

    public void startServer(){
        try {
            server.start();
            server.join();
        } catch (Exception e) {
            System.out.println("Fehler beim starten!");
        }
    }

    public static void main(String[] args){
        SEP_Server s = new SEP_Server();
        s.startServer();
    }

    public ResultSet executeSQL(String statement){
        Statement stmt = null;

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(statement);
            return rs;
        }
        catch (SQLException ex){
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return null;
    }
}
