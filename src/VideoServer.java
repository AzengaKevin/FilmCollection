import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VideoServer {

    private static final Logger logger = Logger.getLogger(VideoServer.class.getSimpleName());
    private int portNumber = 1983;
    private Socket clientSocket;
    private ServerSocket serverSocket;
    private PrintWriter out;
    private BufferedReader in;
    private String filename = "videos.ser";

    private VideoServerEngine engine;

    public VideoServer() {
        init();

        try {
            startServer();
        } catch (IOException e) {
            System.err.println("Unrecoverable error whilst starting the server: " + e.getMessage());
        }
    }

    private void init() {

        List<Video> videos = null;

        try {
            videos = VideoFileParser.fromSerialized(filename);
        } catch (FileNotFoundException e) {
            System.err.format("File not found. %n%n%s%n", e);
        } catch (ClassNotFoundException e) {
            System.err.format("Class not found. %n%n%s%n", e);
        } catch (IOException e) {
            System.err.format("Error while attempting to read from file: %s%n", e);
        }

        engine = new VideoServerEngine(videos);
    }

    private void startServer() throws IOException {
        //some statements are missing from here
        serverSocket = new ServerSocket(portNumber);

        System.out.printf("Server started on port %d ...", serverSocket.getLocalPort());

        clientSocket = serverSocket.accept();

        out = new PrintWriter(
                new BufferedWriter(
                        new OutputStreamWriter(
                                new DataOutputStream(clientSocket.getOutputStream())
                        )
                )
        );

        in = new BufferedReader(
                new InputStreamReader(
                        new DataInputStream(clientSocket.getInputStream())
                )
        );

        //we only make it this far if the code above doesn't throw an exception
        welcomeClient();

        //user interaction starts (and ends) here
        mainLoop();

        //shut down
        closeAll();
    }

    private void welcomeClient() {
        //welcome the client and show the available commands
        String commands = engine.getAvailableCommands();
        String ip = clientSocket.getInetAddress().getHostAddress();
        String local = serverSocket.getInetAddress().getHostAddress();
        sendOutputToClient("Welcome IP address '" + ip + "' to the Video Library Server. Available commands:\n" + commands);
    }

    //our shutdown method. close all open streams
    private void closeAll() {
        //out of the loop. all done, close the connection
        try {
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            System.err.println("Error while shutting down: " + e.getMessage());
        }
    }

    private void mainLoop() {
        //receive and respond to input
        boolean moreInput = true;

        while (moreInput) {
            try {
                String inputLine = null;
                while ((inputLine = in.readLine()) != null) {
                    String output = engine.parseCommand(inputLine.trim());
                    sendOutputToClient(output);
                }
                moreInput = false;
            } catch (IOException e) {
                System.err.println("Error reading from client: " + e.getMessage());
                moreInput = false;
            } catch (Exception e) {
                //engine has thrown some sort of student-based hissy fit;
                sendOutputToClient(e.toString());
            }
        }
    }

    private void sendOutputToClient(String s) {
        out.println(s);
        out.println(ServerUtils.getEOM()); //The client won't know it has reached the end of our message without this
        out.flush();
    }

    public static void main(String[] args) throws IOException {
        new VideoServer();
    }
}
