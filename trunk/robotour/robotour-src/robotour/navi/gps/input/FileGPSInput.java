package robotour.navi.gps.input;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kotuc
 */
class FileGPSInput extends GPSInput implements Runnable {

    private final GPSInput gps = this;
    private final BufferedReader reader;

    FileGPSInput(File file) throws IOException {
        reader = new BufferedReader(new FileReader(file));

        new Thread(this).start();
    }

    public void run() {
//        Thread.currentThread().setDaemon(true);
        try {
            String line;
//            while ((line = reader.readLine()) != null) {

            while (true) {
                line = reader.readLine();
                if (line == null) {
//                    System.err.println("null");
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(FileGPSInput.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    gps.parseNMEA(line);
                }


                try {
                    Thread.sleep(10);
                } catch (InterruptedException ex) {
                    Logger.getLogger(FileGPSInput.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(FileGPSInput.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
