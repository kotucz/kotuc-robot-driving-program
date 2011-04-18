/*
 * Ports.java
 *
 */
package robotour.hardware;

import gnu.io.CommPortIdentifier;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Helper class to find port by name
 * @author Kotuc
 */
public final class Ports {

    /** Noninstantiable */
    private Ports() {
    }

    private static class PortsHolder {

        private static final Map<String, CommPortIdentifier> portMap = createMap();

        private static HashMap<String, CommPortIdentifier> createMap() {
            Logger logger = Logger.getLogger("Ports");
            HashMap<String, CommPortIdentifier> map = new HashMap<String, CommPortIdentifier>();
            try {

                logger.log(Level.INFO, "Ports: searching: ");

                Enumeration portList = CommPortIdentifier.getPortIdentifiers();

                CommPortIdentifier portId = null;

                while (portList.hasMoreElements()) {
                    portId = (CommPortIdentifier) portList.nextElement();
                    map.put(portId.getName(), portId);
                    logger.log(Level.INFO, "Found port: " + portId.toString() + "  " + portId.getName() + "  " + portId.getPortType());
                }

                logger.log(Level.INFO, "Found " + map.size() + " ports");

            } catch (Exception ex) {
                logger.log(Level.SEVERE, "failed", ex);
            }
            return map;
        }
    }

    /**
     *  
     * @param portName
     * @return 
     */
    public static CommPortIdentifier getPortIdentifier(String portName) {
        return PortsHolder.portMap.get(portName);
    }

    public static String getSomeName() {
        return PortsHolder.portMap.keySet().iterator().next();
//        return PortsHolder.portMap.get(portName);
    }


}
