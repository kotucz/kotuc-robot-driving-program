package robotour.arduino;

import java.io.IOException;
import java.io.OutputStream;

class SLIPOutputStream extends OutputStream {

    private final OutputStream out;
    private byte chsum = 0;

    public SLIPOutputStream(OutputStream out) {
        this.out = out;
    }

    @Override
    public void write(int b) throws IOException {
        chsum ^= b;
        switch ((byte) b) {
            case SLIP.SLIP_END:
                // escape end character
                out.write(SLIP.SLIP_ESC);
                out.write(SLIP.SLIP_ESC_END);
                break;
            case SLIP.SLIP_ESC:
                // escape escape character
                out.write(SLIP.SLIP_ESC);
                out.write(SLIP.SLIP_ESC_ESC);
                break;
            default:
                // no special character has no special meaning
                out.write(b);
        }
    }

    public void sendMessage(byte[] buffer) throws IOException {
        start();
        write(buffer);
        end();
    }

    public void start() throws IOException {
        sendEnd();
        chsum = 0;
    }

    public void end() throws IOException {
        write(chsum);
        sendEnd();
        flush();
    }

    private void sendEnd() throws IOException {
        out.write(SLIP.SLIP_END);
    }

    @Override
    public void flush() throws IOException {
        out.flush();
    }
}
