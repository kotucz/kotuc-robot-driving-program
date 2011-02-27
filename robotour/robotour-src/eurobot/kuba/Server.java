package eurobot.kuba;

import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import robotour.hardware.arduino.SerialComm;

/**
 *
 * @author Kotuc
 */
public class Server {

    private static final int PORT = 6060;
    final private ServerSocket socket;
    final List<ClientListener> clients = new LinkedList<ClientListener>();
    final KubaPuppet puppet;

    Server(ServerSocket socket, KubaPuppet puppet) {
        this.socket = socket;
        this.puppet = puppet;
    }

    public static Server createServer(KubaPuppet puppet, int port) throws IOException {
        return new Server(new ServerSocket(port), puppet);
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
                    interpretMessage(inputLine);
//                    if (outputLine.equals("Bye.")) {
//                        break;
//                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                System.out.println("Closing");
                close();
            }
        }

        void close() {
            clients.remove(this);
            if (out != null) {
                out.close();
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        void sendMessage(String line) {
            out.println(line);
        }
    }

    public synchronized void broadcoastMessage(String line) {
        System.out.println("Broadcast (" + clients.size() + " clients): " + line);
        for (ClientListener clientListener : clients) {
            clientListener.sendMessage(line);
        }

    }

    public synchronized void interpretMessage(String line) {
        System.out.println("Income: " + line);
        try {
            String[] split = line.split("\\s");
            if ("lr".equals(split[0])) {
                int left = Integer.parseInt(split[1]);
                int right = Integer.parseInt(split[2]);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reportPosition(int x, int y, int degrees) {
        broadcoastMessage("at " + x + " " + y + " " + degrees);
    }

    public static void main(String[] args) throws IOException, PortInUseException, UnsupportedCommOperationException {
        Server server = Server.createServer(
                new KubaPuppet(
                new KubaOutProtocol(
                SerialComm.openSerialComm("com1"))), PORT);
        server.start();
    }
}
