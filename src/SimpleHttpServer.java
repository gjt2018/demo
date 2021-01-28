import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimpleHttpServer {
    public static void main(String[] args) throws Exception{
        ExecutorService pool = Executors.newCachedThreadPool();
//        ExecutorService pool1 = Executors.newCachedThreadPool();
        HttpServer server = HttpServer.create(new InetSocketAddress(8000),0);
        HttpServer server1 = HttpServer.create(new InetSocketAddress(8001),0);
        server.createContext("/info",new InfoHeader() );
        server.createContext("/get",new GetHeader() );
        server1.createContext("/test",new TestHandler());

        server.setExecutor(pool);
        server1.setExecutor(pool);

        server.start();
        server1.start();

    }
    //8001:test
    static class TestHandler implements HttpHandler{
        @Override
        public void handle(HttpExchange exchange) {
            String response = "hello world";
            System.err.println("test线程名："+Thread.currentThread().getName());
            try{
                //获得查询字符串(get)
                if(exchange.getRequestMethod().equalsIgnoreCase("GET")) {
                    String queryString = exchange.getRequestURI().getQuery();
                    Map<String, String> queryStringInfo = formData2Dic(queryString);
                    System.out.println("GET:"+queryStringInfo.toString());
                    response = response + "\n"+queryString;
                }else {
                    //获得表单提交数据(post)
                    String postString = IOUtils.toString(exchange.getRequestBody());
                    Map<String, String> postInfo = formData2Dic(postString);
                    System.out.println("POST:"+postString.toString());
                    response = response + "\n"+postString;
                }

                exchange.sendResponseHeaders(200,0);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }catch (IOException ie) {
                ie.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static Map<String,String> formData2Dic(String formData ) {
        Map<String,String> result = new HashMap<>();
        if(formData== null || formData.trim().length() == 0) {
            return result;
        }
        final String[] items = formData.split("&");
        Arrays.stream(items).forEach(item ->{
            final String[] keyAndVal = item.split("=");
            if( keyAndVal.length == 2) {
                try{
                    final String key = URLDecoder.decode( keyAndVal[0],"utf8");
                    final String val = URLDecoder.decode( keyAndVal[1],"utf8");
                    result.put(key,val);
                }catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
        return result;
    }
    //8000:info
    static class InfoHeader implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            System.err.println("info线程名："+Thread.currentThread().getName());
            String response = "Use /get to download a PDF";
            t.sendResponseHeaders(200,response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();

        }
    }
    //8000:get
    static class GetHeader implements HttpHandler {
        public void handle(HttpExchange t ) throws IOException{
            System.err.println("get线程名："+Thread.currentThread().getName());
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
