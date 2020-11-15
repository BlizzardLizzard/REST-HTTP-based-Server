import java.io.*;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;

public class RequestHandler {
    public String request;
    public int numberOfEntriesInDir = 0;

    public RequestHandler(String request, Socket socket, RequestContext requestContext) throws FileNotFoundException {
        this.request = request;
        switch (request) {
            case "POST" -> {
                PostRequest(requestContext);
                PrintReply(socket);
            }
            case "GET" -> GetRequest(requestContext, socket);
            case "DELETE" -> DeleteRequest(socket, requestContext);
            case "PUT" -> PutRequest(socket, requestContext);
        }
    }

    public void PostRequest(RequestContext requestContext) {
        try {
            numberOfEntriesInDir = Objects.requireNonNull(new File("messages/").listFiles()).length;
            numberOfEntriesInDir += 1;
            File postFile = new File("messages/");
            FileWriter writer = new FileWriter("messages/" + numberOfEntriesInDir + ".txt");

            /*String[] filesInDir = postFile.list();
            int currentNumberOfEntriesInDir = filesInDir.length;
            String[] lastFileName = filesInDir[currentNumberOfEntriesInDir-1].split("\\.");
            int lastFileNumber = Integer.parseInt(lastFileName[0]);
            if(lastFileNumber > numberOfEntriesInDir){
                numberOfEntriesInDir = lastFileNumber;
                PostRequest(requestContext);
            }
            System.out.println(lastFileNumber);*/

            writer.write(requestContext.getMessage());
            writer.close();
            System.out.println(numberOfEntriesInDir);
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void GetRequest(RequestContext requestContext, Socket socket) throws FileNotFoundException {
        PrintWriter out = null;
        String path = requestContext.getURI();
        String[] pathSplit = path.split("/");
        File getFile = new File("messages/");
        String[] pathNames = getFile.list();
        int numberOfStrings = pathSplit.length;

        try {
           out = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.println("HTTP/1.0 200 OK");
        out.println("Content-Type: text/html");
        out.println("");
        if(numberOfStrings <= 2){
            assert pathNames != null;
            for(String pathname : pathNames){
                out.println("Entry: " + pathname);
            }
        }else{
            File getRequest = new File("messages/" + pathSplit[2] + ".txt");
            if(getRequest.exists()){
                Scanner reader = new Scanner(getRequest);
                String message = " ";
                while(reader.hasNextLine()){
                    message = reader.nextLine();
                }
                reader.close();
                out.println("Message from ID " + pathSplit[2] + ": " + message);
            }else{
                out.println("Message ID doesn't exist!");
            }
        }
        out.flush();
    }

    public void DeleteRequest(Socket socket, RequestContext requestContext){
        PrintWriter out = null;
        String path = requestContext.getURI();
        String[] pathSplit = path.split("/");
        int numberOfStrings = pathSplit.length;
        try {
            out = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
            if(numberOfStrings > 2){
                File deleteFile = new File("messages/" + pathSplit[2] + ".txt");
                if(deleteFile.delete()){
                    out.println("HTTP/1.0 200 OK");
                    out.println("Content-Type: text/html");
                    out.println("");
                    out.println(pathSplit[2] + ".txt has been successfully deleted!");
                }else{
                    out.println("HTTP/1.0 200 OK");
                    out.println("Content-Type: text/html");
                    out.println("");
                    out.println("File could not be found!");
                }
            }else{
                out.println("HTTP/1.0 200 OK");
                out.println("Content-Type: text/html");
                out.println("");
                out.println("Please enter an ID!");
            }
        out.flush();
    }

    public void PutRequest(Socket socket, RequestContext requestContext){
        String path = requestContext.getURI();

    }

    public void PrintReply(Socket socket){
        PrintWriter out = null;
        try {
            out = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.println("HTTP/1.0 200 OK");
        out.println("Content-Type: text/html");
        out.println("");
        out.println(numberOfEntriesInDir);
        out.flush();
    }
}


