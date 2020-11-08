import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {

    public static void main(String[] args) throws IOException {

        ServerSocket server = new ServerSocket(8080);
        System.out.println("Listening for connection on port 8080 ....");
        StartServer(server);
    }

    public  static void StartServer(ServerSocket server) throws IOException{
        try {
            while (true) {
                Socket socket = server.accept();
                InputStream inputStream = socket.getInputStream();
                PrintWriter out = new PrintWriter(socket.getOutputStream());

                StringBuilder result = new StringBuilder();
                do {
                    result.append((char) inputStream.read());
                } while (inputStream.available() > 0);
                System.out.println(result);

                String request = result.toString();

                RequestHandler(request, out);

                socket.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void RequestHandler(String request, PrintWriter out){
        if(request.contains("POST")) { {
                new RequestContext("POST", "/messages", "HTTP/1.1");
                out.println("HTTP/1.0 200 OK");
                out.println("Content-Type: text/html");
                out.println("");
                out.println("Hallo Postman this is a POST reply");
            }
            out.flush();
        }

        if(request.contains("GET"))
        {
            out.println("HTTP/1.0 200 OK");
            out.println("Content-Type: text/html");
            out.println("");
            out.println("Hallo Postman this is a GET reply");
            out.flush();
        }
    }
}





