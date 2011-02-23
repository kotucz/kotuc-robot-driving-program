package eurobot.kuba;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kotuc
 */
public class Server {

    private static final int PORT = 6060;
    private ServerSocket socket;

    public Server() throws IOException {
        socket = new ServerSocket(6060);
    }

    void start() {
        new Thread(new Listen()).start();
    }

    class Listen implements Runnable {

        public void run() {
            System.out.println("Listennint on " + PORT);
            while (true) {
                try {
                    Socket client = socket.accept();
                    new ClientListener(client, client.getInputStream(), client.getOutputStream()).start();
                } catch (IOException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    class ClientListener implements Runnable {

        final Socket client;
        final InputStream inputStream;
        final OutputStream outputStream;
        final PrintWriter out;
        final BufferedReader in;

        public ClientListener(Socket client, InputStream inputStream, OutputStream outputStream) {
            this.client = client;
            this.inputStream = inputStream;
            this.outputStream = outputStream;
            this.out = new PrintWriter(outputStream, true);
            this.in = new BufferedReader(
                    new InputStreamReader(
                    inputStream));
        }

        void start() {
            new Thread(this).start();
        }

        public void run() {
            System.out.println("Client listening " + client.getInetAddress());
            String inputLine, outputLine;
            try {
                // initiate conversation with client
                while ((inputLine = in.readLine()) != null) {
                    outputLine = "In: " + inputLine;
                    out.println(outputLine);
                    if (outputLine.equals("Bye.")) {
                        break;
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);                
            } finally {
                System.out.println("Closing");
                close();
            }
        }

        void close() {
            
        }

    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.start();
    }
}
