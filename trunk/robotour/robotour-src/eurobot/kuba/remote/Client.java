package eurobot.kuba.remote;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import robotour.util.ShutdownManager;
import robotour.util.Shutdownable;

class Client implements Runnable, Shutdownable {

    final BufferedReader in;
    final InputStream inputStream;
    final PrintWriter out;
    final OutputStream outputStream;

    StringMessageListener listener;

    public Client(InputStream inputStream, OutputStream outputStream) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.out = new PrintWriter(outputStream, true);
        this.in = new BufferedReader(new InputStreamReader(inputStream));
        ShutdownManager.registerStutdown(this);
    }

    void close() {
//        Server.clients.remove(this);
        if (out != null) {
            sendMessage("Closing ... Bye.");
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

    public void run() {
        //            System.out.println("Client listening " + client.getInetAddress());
        String inputLine;
        //            String outputLine;
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
            System.out.println("Closing "+this);
            close();
        }
    }

    void sendMessage(String line) {
        out.println(line);
    }

    public void start() {
        new Thread(this).start();
    }

    private void interpretMessage(String inputLine) {
        if (listener!=null) {
            listener.message(inputLine);
        } else {
            System.err.println(this+" null message listener ");
        }
    }

    public void setListener(StringMessageListener listener) {
        this.listener = listener;
    }

    public void shutdown() {
        close();
    }

    

}
