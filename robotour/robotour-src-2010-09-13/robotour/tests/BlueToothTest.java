package robotour.tests;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;

/**
 *
 * @author Kotuc
 */
public class BlueToothTest {

    public static void main(String[] args) throws BluetoothStateException {
        DiscoveryAgent discoveryAgent = LocalDevice.getLocalDevice().getDiscoveryAgent();
        RemoteDevice[] retrieveDevices = discoveryAgent.retrieveDevices(DiscoveryAgent.CACHED);



        System.out.println("Bluetooth " + retrieveDevices.length + " devices");

        for (RemoteDevice remoteDevice : retrieveDevices) {
            try {
                System.out.println("BT " + remoteDevice.getFriendlyName(false));

                discoveryAgent.searchServices(null, new UUID[]{new UUID(0x1101)}, remoteDevice, new DiscoveryListener() {

                    public void deviceDiscovered(RemoteDevice arg0, DeviceClass arg1) {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }

                    public void servicesDiscovered(int arg0, ServiceRecord[] arg1) {
                        System.out.println("servicesDiscovered "+arg0);
                        for (ServiceRecord serviceRecord : arg1) {
                            System.out.println("sr "+serviceRecord);
                            
                        }
                    }

                    public void serviceSearchCompleted(int arg0, int arg1) {
                        System.out.println("serviceSearchCompleted "+ arg0+" "+arg1);
                    }

                    public void inquiryCompleted(int arg0) {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }
                });

//                Connector.open();
            } catch (IOException ex) {
                Logger.getLogger(BlueToothTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            Thread.sleep(20000);
        } catch (InterruptedException ex) {
            Logger.getLogger(BlueToothTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
