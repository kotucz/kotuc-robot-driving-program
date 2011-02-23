package robotour.tests;

import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;
import java.io.IOException;
import robotour.hardware.I2CUSB;
import robotour.util.I2CBuffer;

/**
 *
 * @author Tomas
 */
public class I2CBufferTest {
    public static void main(String[] args) throws PortInUseException, UnsupportedCommOperationException, IOException {
        I2CBuffer buffer = I2CBuffer.createBuffer(I2CUSB.getI2C(args[0]));
    }
}
