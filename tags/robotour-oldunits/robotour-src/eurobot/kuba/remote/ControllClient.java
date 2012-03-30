package eurobot.kuba.remote;

import java.io.IOException;
import java.net.Socket;
import robotour.navi.basic.Azimuth;

/**
 *
 * @author Kotuc
 */
public class ControllClient implements StringMessageListener {

    final SocketClient socket;

    static ControllClient connect(String host, int port) throws IOException {
        return new ControllClient(host, port);
    }

    private ControllClient(String host, int port) throws IOException {
        this.socket = new SocketClient(new Socket(host, port));
        socket.setListener(this);
        socket.start();

    }

    public void message(String line) {
        System.out.println("Received: " + line);
        try {
            String[] split = line.split("\\s");
            if ("pos".equals(split[0])) {

                double x = Double.parseDouble(split[1]);
                double y = Double.parseDouble(split[2]);
                Azimuth.valueOfDegrees(Double.valueOf(split[3]));
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
