import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;


public class VideoClient {

    private static final Logger logger = Logger.getLogger(VideoClient.class.getSimpleName());
    private static final int PORT = 1983;
    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;

    private static final int DEFAULT_BUFFER_SIZE = 1024;

    public String connect(String host) throws Exception {
        socket = new Socket(host, PORT);

        writer = new PrintWriter(
                new BufferedWriter(
                        new OutputStreamWriter(
                                new DataOutputStream(socket.getOutputStream())
                        ), DEFAULT_BUFFER_SIZE
                )
        );

        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        return readServerOutput();
    }

    public void disconnect() throws Exception {
        if (!socket.isClosed()) { //only attempt to close if it hasn't already been closed
            reader.close();
            writer.close();
            socket.close();
        }
    }

    /*
     * send a command to the server and return the response
     */
    public String sendCommand(String command) throws Exception {
        if (writer != null) {
            writer.println(command);

            return readServerOutput();
        } else {
            return "Not connected.";
        }
    }

    /*
     * read the messages sent from the server
     */
    private String readServerOutput() throws Exception {

        StringBuffer sb = new StringBuffer();
        String line;

        while ((line = reader.readLine()) != null) { //receiving a null here means the server has closed the connection
            if (line.equals(ServerUtils.getEOM())) { //EOM (End of Message) is defined in the FileServer class
                break;
            }
            sb.append(line);
            sb.append('\n');
        }

        logger.log(Level.INFO, "Server output reader");

        //remove trailing newline
        sb.deleteCharAt((sb.length() - 1));
        return sb.toString();
    }
}



