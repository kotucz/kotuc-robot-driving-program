package eurobot.kuba.remote;

import eurobot.kuba.KubaPuppet;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kotuc
 */
public final class Server implements StringMessageListener {

    public static final int DEFAULT_PORT = 6666;
    final private ServerSocket socket;
    final List<Client> clients = new LinkedList<Client>();
    final KubaPuppet puppet;
    final MessageDecoder decoder;

    Server(ServerSocket socket, KubaPuppet puppet) {
        this.socket = socket;
        this.puppet = puppet;

        puppet.server = this;
        decoder = new MessageDecoder(puppet);

        Client client = new Client(System.in, System.out);
        addRemoteClient(client);

    }

    public static Server createServer(KubaPuppet puppet, int port) throws IOException {
        return new Server(new ServerSocket(port), puppet);
    }

    public void start() {
        new Thread() {
            public void run() {
                System.out.println("Listennint on " + socket.getLocalPort() + " " + socket.getLocalPort() + " " + socket.toString());
                while (true) {
                    try {
                        Socket client = socket.accept();
                        addRemoteClient(new SocketClient(client, client.getInputStream(), client.getOutputStream()));
                    } catch (IOException ex) {
                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }.start();
    }
    
    void addRemoteClient(Client client) {
        clients.add(client);
        client.setListener(decoder);
        client.start();
    }

    public synchronized void broadcoastMessage(String line) {
        System.out.println("Broadcast (" + clients.size() + " clients): " + line);
        for (Client client : clients) {
            client.sendMessage(line);
        }

    }

    private  void reportPosition(int x, int y, int degrees) {
        broadcoastMessage("at " + x + " " + y + " " + degrees);
    }

    public void message(String message) {
        broadcoastMessage(message);
    }
}
