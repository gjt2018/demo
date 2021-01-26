import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import javax.print.attribute.standard.Severity;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.rmi.server.ExportException;

public class demo {
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8001), 0);
        server.createContext("/test",new TestHandler() );
        server.start();
//        System.out.println("hello world");
//        System.out.println("mod by gjt");
//        System.out.println("creat new branch: dev-gjt");

    }
    static class TestHandler implements HttpHandler{
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String respone = "hello world";
            httpExchange.sendResponseHeaders(200,0);
            OutputStream os = httpExchange.getResponseBody();
            os.write(respone.getBytes());
            os.close();
        }
    }


}
