import java.awt.geom.Point2D;
import java.io.*;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;

public class RequestHandler {
    public String request;
    public int numberOfEntriesInDir = 0;

    public RequestHandler(String request, Socket socket, RequestContext requestContext) throws FileNotFoundException {
        this.request = request;
        if(request.equals("POST")){
            PostRequest(requestContext);
            PrintReply(socket);
        }
        if(request.equals("GET")){
            GetRequest(requestContext ,socket);
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

        try {
           out = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.println("HTTP/1.0 200 OK");
        out.println("Content-Type: text/html");
        out.println("");
        if(!path.startsWith("/messages/")){
            assert pathNames != null;
            for(String pathname : pathNames){
                out.println("Entry: " + pathname);
            }
        }else{
            File getRequest = new File("messages/" + pathSplit[2] + ".txt");
            if(getRequest.exists()){
                Scanner reader = new Scanner(getRequest);
                String message = null;
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


/*Wenn ich eine Datei lösche dann habe ich eine Datei die genauso heißt wie die nächste höhere und bleibe bei einer Zahl stehen. Also sollte immer verglichen werden ob die Zahl die wir von den Einträgen im Dir haben kleiner ist als die die wir als txt gespeichert haben. floglich muss geschaut werden wenn der letzte eintrag nicht größer als die anzhal im dir ist dann kann man noormal weiterabreiten ist das aber nciht der fall dann muss die zahl genommen werden die vom letzten eintrag im dir kommt.*/