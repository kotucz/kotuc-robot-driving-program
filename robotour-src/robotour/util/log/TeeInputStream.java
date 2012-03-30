package robotour.util.log;

/**
 *
 * @author http://java.sun.com/developer/technicalArticles/Streams/WritingIOSC/
 */
import java.io.*;

public class TeeInputStream extends InputStream {

    OutputStream tee;
    InputStream in;

    public TeeInputStream(InputStream chainedStream,
            OutputStream teeStream) {
        in = chainedStream;
        tee = teeStream;
    }

    @Override
    public int available() throws IOException {
        return in.available();
    }
    
    @Override
    public int read() throws IOException {
        int c = in.read();

        tee.write(c);
        tee.flush();

        return c;
    }

    /**
     * Closes both, chained and tee, streams.
     */
    @Override
    public void close() throws IOException {
        in.close();
        tee.close();
    }

    /** Test driver */
    public static void main(
            String args[]) throws Exception {
        FileInputStream fis =
                new FileInputStream("test.out");
        TeeInputStream tis =
                new TeeInputStream(fis, System.err);
        BufferedReader reader = new BufferedReader(new InputStreamReader(tis));

        System.out.println(reader.readLine());
        System.out.println(reader.readLine());

        fis.close();
    }
}

