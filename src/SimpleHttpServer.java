import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class SimpleHttpServer {
    public static void main(String[] args) throws Exception{
        HttpServer server = HttpServer.create(new InetSocketAddress(8000),0);
        server.createContext("/info",new InfoHeader() );
        server.createContext("/get",new GetHeader() );
        server.setExecutor(null);
        server.start();

    }

    static class InfoHeader implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            String response = "Use /get to download a PDF";
            t.sendResponseHeaders(200,response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();

        }
    }

    static class GetHeader implements HttpHandler {
        public void handle(HttpExchange t ) throws IOException{
            Headers h = t.getResponseHeaders();
            h.add("Content-Type","application/pdf");

            File file= new File("C:/Users/34234/Desktop/1.pdf");
            byte [] bytearry = new byte[(int)file.length()];
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            bis.read(bytearry, 0, bytearry.length);

            t.sendResponseHeaders(200,file.length());
            OutputStream os = t.getResponseBody();
            os.write(bytearry,0, bytearry.length);
            os.close();

        }

    }
}
