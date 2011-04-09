package robotour.util.log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import robotour.util.log.events.EventLog;
import robotour.util.log.events.EventsXmlWriter;

/**
 *
 * @author Kotuc
 */
public class SerialLog {

    private static File logDirectory;
    private static File runLogDirectory;

    public static File getLogDirectory() {
        if (logDirectory == null) {
//            logDirectory = new File("./logs/run-" + System.currentTimeMillis() + "/");
//            logDirectory = new File("./logs/run-" + createReadableTime() + "/");
            logDirectory = new File(System.getProperty("user.home") + "/EurobotLogs/");
            logDirectory.mkdirs();
            logDirectory.mkdir();
        }
        return logDirectory;
    }

    public static File getRunLogDirectory() {
        if (runLogDirectory == null) {
//            logDirectory = new File("./logs/run-" + System.currentTimeMillis() + "/");
//            logDirectory = new File("./logs/run-" + createReadableTime() + "/");
            runLogDirectory = new File(getLogDirectory(), "/run-" + createReadableTime() + "/");
            runLogDirectory.mkdirs();
            runLogDirectory.mkdir();
        }
        return runLogDirectory;
    }

    public static String createReadableTime() {
//        new Date().getH;
        final Calendar cal = Calendar.getInstance();
        String time = "" + (cal.get(Calendar.YEAR)+"-"
                + 100 * (cal.get(Calendar.MONTH) + 1)
                + cal.get(Calendar.DAY_OF_MONTH)) + "-"
                + 10000 * cal.get(Calendar.HOUR_OF_DAY)
                + 100 * cal.get(Calendar.MINUTE)
                + cal.get(Calendar.SECOND);
        return time;
    }

    public static File createLogFile(String name) {
//        File file = new File(getLogDirectory(), name + "-" + System.currentTimeMillis() + ".log");
        File file = new File(getRunLogDirectory(), name + "-" + createReadableTime() + ".log");
        return file;
    }

    public static OutputStream getLoggedOutputStream(OutputStream originalOutput, String name) {
        try {
            return getLoggedOutputStream(originalOutput, createLogFile(name));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SerialLog.class.getName()).log(Level.SEVERE, null, ex);
            return originalOutput;
        }
    }

    public static OutputStream getLoggedOutputStream(OutputStream originalOutput, File logFile) throws FileNotFoundException {
        FileOutputStream fos = new FileOutputStream(logFile);
        TeeOutputStream tos = new TeeOutputStream(originalOutput, fos);
        return tos;
    }

    public static InputStream getLoggedInputStream(InputStream originalInput, String name) {
        try {
            return getLoggedInputStream(originalInput, createLogFile(name));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SerialLog.class.getName()).log(Level.SEVERE, null, ex);
            return originalInput;
        }
    }

    public static InputStream getLoggedInputStream(InputStream originalInput, File logFile) throws FileNotFoundException {
        FileOutputStream fos = new FileOutputStream(logFile);
        TeeInputStream tis = new TeeInputStream(originalInput, fos);
        return tis;
    }

   
    public static void saveEvents(EventLog eventLog) {

        File file = new File(getRunLogDirectory(), "events-" + createReadableTime() + ".xml");
        try {

            new EventsXmlWriter(new FileOutputStream(file)).write(eventLog);

        } catch (Exception ex) {
            Logger.getLogger(SerialLog.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
//    static void chain(InputStream inputStream, OutputStream outputStream) {
//
//    }
}
