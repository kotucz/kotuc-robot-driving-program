package robotour.hardware.arduino;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import robotour.iface.MeasureException;

/**
 *
 * @author Kotuc
 * @see http://www.tcpipguide.com/free/t_SerialLineInternetProtocolSLIP-2.htm
 */
public class SimpleMessageInterpreter implements MessageReceived {

    private final SimpleArduinoRobot robot;

    public SimpleMessageInterpreter(SimpleArduinoRobot robot) {
        this.robot = robot;
    }

    public void messageRecieved(byte[] bytes) {
        try {
            long currentTimeMillis = System.currentTimeMillis();

            DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bytes));

            //        int time = toInt32(bytes, 0);
            int arduinoTime = dis.readInt();

            byte type = dis.readByte();

//            short val = toInt16(bytes, 5);
            short val = dis.readShort();

            switch (type) {
                case 'a':
//                    System.out.println("azimuth*10: " + val);
                    robot.compass.measuredDegTen(val);
                    try {
                        robot.odometry.updatedAzimuth(robot.compass.getAzimuth());
                    } catch (MeasureException ex) {
                        Logger.getLogger(SimpleMessageInterpreter.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    break;
                case 'l':
                    System.out.println("left cm: " + val);
                    robot.leftSrf.measuredCM(val);
                    break;
                case 'c':
                    System.out.println("center cm: " + val);
                    robot.centerSrf.measuredCM(val);

                    break;
                case 'r':
                    System.out.println("right cm: " + val);
                    robot.rightSrf.measuredCM(val);

                    break;
                case 'e':
                    System.out.println("encoder ticks: " + val);
                    robot.odometer.setTicks(val);
                    robot.odometry.updatedOdometer(robot.odometer.getChange());

                    break;
                default:
                    System.err.println("unknown event: \"" + new String(bytes) + "\" " + Arrays.toString(bytes));

            }
            //        System.out.print("Msg:");
            //        for (Byte byte1 : buffer) {
            //            System.out.print(" " + byte1);
            //        }
            //        System.out.println();
        } catch (IOException ex) {
            Logger.getLogger(SimpleMessageInterpreter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
