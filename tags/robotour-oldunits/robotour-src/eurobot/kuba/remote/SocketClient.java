package eurobot.kuba.remote;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SocketClient extends Client {

    final Socket socket;

    public SocketClient(Socket client, InputStream inputStream, OutputStream outputStream) {
        super(inputStream, outputStream);
        this.socket = client;
    }

    public SocketClient(Socket client) throws IOException {
        super(client.getInputStream(), client.getOutputStream());
        this.socket = client;
    }
}
