package robotour.hardware.arduino;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import robotour.util.log.events.CompassEvent;
import robotour.util.log.events.EventListener;
import robotour.util.log.events.OdometrEvent;
import robotour.util.log.events.SonarEvent;
import robotour.util.log.events.Tags;
import robotour.util.log.events.UnknownEvent;

/**
 *
 * @author Kotuc
 */
public class EventReceiver implements MessageReceived {

    final EventListener listener;

    public EventReceiver(EventListener listener) {
        this.listener = listener;
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
                    listener.eventRecieved(
                            CompassEvent.createCompassEvent10deg(val, arduinoTime, currentTimeMillis));
                    break;
                case 'l':
                    listener.eventRecieved(
                            SonarEvent.createSonarEventCM(Tags.left, val, arduinoTime, currentTimeMillis));
                    break;
                case 'c':
                    listener.eventRecieved(
                            SonarEvent.createSonarEventCM(Tags.center, val, arduinoTime, currentTimeMillis));
                    break;
                case 'r':
                    listener.eventRecieved(
                            SonarEvent.createSonarEventCM(Tags.right, val, arduinoTime, currentTimeMillis));
                    break;
                case 'e':
                    listener.eventRecieved(
                            OdometrEvent.createOdometrEventTicks(val, arduinoTime, currentTimeMillis));
                    break;
                default:
                    listener.eventRecieved(
                            new UnknownEvent("unknown event: \"" + new String(bytes) + "\" " + Arrays.toString(bytes), currentTimeMillis));

            }
            //        System.out.print("Msg:");
            //        for (Byte byte1 : buffer) {
            //            System.out.print(" " + byte1);
            //        }
            //        System.out.println();
        } catch (IOException ex) {
            Logger.getLogger(SLIP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

