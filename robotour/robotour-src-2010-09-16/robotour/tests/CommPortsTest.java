package robotour.tests;
import gnu.io.CommPortIdentifier;
import java.util.Enumeration;

/**
 *
 * @author Tomas Kotula
 */
class CommPortsTest {
    
    /** Creates a new instance of ComPorts */
    public CommPortsTest() {
    }
    
    public static void main(String[] args) {
        try {
            
            Enumeration portList = CommPortIdentifier.getPortIdentifiers();
            
            CommPortIdentifier portId = null;
          
            int i = 0;
            
            while (portList.hasMoreElements()) {
                i++;
                portId = (CommPortIdentifier) portList.nextElement();
                System.out.println("Found port: "+i+" - "+portId.getName()+" - type: "+((portId.getPortType() == CommPortIdentifier.PORT_SERIAL)?"serial":"?")+"  "+"; owned: "+portId.isCurrentlyOwned()+" by "+portId.getCurrentOwner());
            }

            System.out.println("Test end. "+i+" ports found");
                        
        } catch (Exception ex) {
            System.out.println("Test ended with Exception");
            ex.printStackTrace();
            
        }    
    }
    
}
