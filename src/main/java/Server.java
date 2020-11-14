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

                StringBuilder result = new StringBuilder();
                while (inputStream.available() > 0) {
                    result.append((char) inputStream.read());
                }

                String request = result.toString();
                String[] requestSplit = request.split(" ");

                RequestHandler(requestSplit,socket, request);
                socket.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void RequestHandler(String[] requestSplit, Socket socket, String requestString) {
        String request = requestSplit[0];
        if (!request.isEmpty()) {
            String[] httpVersion = requestSplit[2].split("\\r?\\n");
            RequestContext requestContext =  new RequestContext(request, requestSplit[1], httpVersion[0], requestString);
            new RequestHandler(request, socket, requestContext);
        }
    }
}





