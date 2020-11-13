import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


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
                while (inputStream.available() > 0) {
                    result.append((char) inputStream.read());
                }

                String request = result.toString();
                String[] requestSplit = request.split(" ");

                RequestHandler(requestSplit, out, request);
                socket.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void RequestHandler(String[] requestSplit, PrintWriter out, String requestString) {
        String request = requestSplit[0];
        ArrayList<RequestContext> requestContexts = new ArrayList<>();
        if (!request.isEmpty()) {
            String[] httpVersion = requestSplit[2].split("\\r?\\n");
            requestContexts.add(new RequestContext(request, requestSplit[1], httpVersion[0], requestString));
            if (request.equals("POST")) {
                out.println("HTTP/1.0 200 OK");
                out.println("Content-Type: text/html");
                out.println("");
                out.println("Hallo Postman this is a POST reply");
                out.flush();
            }
            if (request.equals("GET")) {
                out.println("HTTP/1.0 200 OK");
                out.println("Content-Type: text/html");
                out.println("");
                out.println("Hallo Postman this is a GET reply");
                out.flush();
            }
        }
    }
}





