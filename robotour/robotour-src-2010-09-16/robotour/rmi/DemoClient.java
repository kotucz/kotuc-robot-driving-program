/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package robotour.rmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author Tomas
 */
class DemoClient {

    public static void main(String args[]) {
//        if (System.getSecurityManager() == null) {
//            System.setSecurityManager(new SecurityManager());
//        }
        if (args.length < 1) {
            System.err.println("Usage: java robotour.rmi.DemoClient <hostname>");
        } else {
            try {
                String name = "RobotServer";
                Registry registry = LocateRegistry.getRegistry(args[0]);
                System.out.println("Registry found: "+registry);
                for (String string : registry.list()) {
                    System.out.println("Remote: " + string);
                }
                DemoInterface comp = (DemoInterface) registry.lookup(name);
                System.out.println("Prisel Vysledek :" + comp.print("Tohle vypis"));
            } catch (Exception e) {
                System.err.println("Client exception:");
                e.printStackTrace();
            }
        }
    }
}
