import com.sun.xml.internal.stream.writers.UTF8OutputStreamWriter;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class HttpServer {

    private static ArrayList<String> getListFiles(){
        ArrayList<String> arr = new ArrayList<String>();
        String path = System.getProperty("user.dir");
        File folder = new File(path);
        for (File file : folder.listFiles()) {
            arr.add(file.getName());
        }
        return arr;
    }

    private static String getBody(String comment){
        String st = "<html><body><h1>Hello " + System.getProperty("user.name") + "! List of files in the directory<ul>\n";
        if ((comment != null) && (comment.length() != 0)) st += "Method -" + comment.substring(0, 4);
        for (String str: getListFiles()) {
            st += " <li>" + str + "</li>\n";
        }
        st += "</h1></body></html>";
        return st;
    }

    private static String getHTTPHeader(int length, boolean is404){
        String stOK = "HTTP/1.1 200 OK";
        String st404 = "HTTP/1.1 404 Not Found";
        String header = is404 ? st404 : stOK +  "\r\n" +
                "Server: YarServer/2009-09-09\r\n" +
                "Content-Type: text/html;charset=utf-8\r\n" +
                "Content-Length: " + length + "\r\n" +
                "Connection: close\r\n\r\n";
        return header;
    }


    public static void main(String[] args) throws Throwable {
        ServerSocket ss = new ServerSocket(8080);
        while (true) {
            Socket s = ss.accept();
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            String str;
            while(true) {
                str = br.readLine();
                break;
            }
            String body = getBody(str);
            boolean is404 = true;
            if ((str != null) && (str.length() != 0) && (str.substring(0, 3).equals("GET"))) is404 = false ;

            String header = getHTTPHeader(body.length(), is404);
            String result = header + body;

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(s.getOutputStream() );
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
            bufferedWriter.write(result);
            bufferedWriter.flush();

        }
    }
}