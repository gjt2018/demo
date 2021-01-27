import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SimpleURLConnection {
    public static void main(String[] args) {
//        System.out.println("simple URLConnection");
        GetDemo demo = new GetDemo();
        demo.doGet();
    }
    static class GetDemo{
        public void doGet(){
            HttpURLConnection httpURLConnection = null;
            try{
                //1.得到访问地址
                URL url = new URL("http://127.0.0.1:8001/test");
                //2.得到访问网络访问对象java.net.HttpURLConnection
                httpURLConnection = (HttpURLConnection) url.openConnection();
                //3.设置请求参数
                //3.1设置是否向HttpURLConnection输出
                httpURLConnection.setDoOutput(false);
                //3.2设置是否从HttpURLConnection读入
                httpURLConnection.setDoInput(true);
                //3.3设置请求方式  默认为GET
                httpURLConnection.setRequestMethod("GET");
                //3.4设置是否使用缓存
                httpURLConnection.setUseCaches(true);
                //3.5设置此HTTPURLConnection实例是否应该自动执行HTTP重定向
                httpURLConnection.setInstanceFollowRedirects(true);
                //3.6设置超时时间
                httpURLConnection.setConnectTimeout(2000);
                //连接
                httpURLConnection.connect();

                //4.得到响应状态码的返回值 responseCode
                int code = httpURLConnection.getResponseCode();
                //5.如果返回值正常，数据在网络中以流的形式得到服务端返回的数据
                String msg = "";
                if( code == 200 )
                {
                    //从流中读取响应信息
                    BufferedReader reader
                            = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    String line = "";
                    while ( (line = reader.readLine()) != null ) //循环从流中读数据
                        msg += line + "\n";
                    reader.close();
                }
                //显示结果
                System.out.println(msg);

            }catch (IOException e){
                e.printStackTrace();
//                System.out.println("转发出错，错误信息："+e.getLocalizedMessage()+";"+e.getClass());
            }finally {
                //6.断块连接，释放资源
                if( null != httpURLConnection )
                {
                    try{
                        httpURLConnection.disconnect();
                    }catch (Exception e){
                        e.printStackTrace();
//                        System.out.println("httpURLConnection 流关闭异常："+ e.getLocalizedMessage());
                    }
                }
            }
        }
    }

}
