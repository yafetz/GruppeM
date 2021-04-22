package Server;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

import java.io.IOException;

public class SEP_Server {
    Server server;
    public SEP_Server(){
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
                    System.out.println(clientrequest.getParameter("name"));
                    System.out.println(clientrequest.getParameter("names"));
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
}
