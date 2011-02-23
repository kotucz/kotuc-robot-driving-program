/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package robotour.rmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 *

 *
 * @author Tomas
 */
class DemoServer implements DemoInterface {

    public String print(String text) {
        System.out.println("Prislo: " + text);
        return "REsult";
    }

    /**
     *
     * Run rmiserver (on default port) in the directory where all needed classes are
     * or set CLASSPATH where to find classes.
     * @param args
     */
    public static void main(String[] args) {
        //System.out.println(""+System.getProperty("java.library.path"));

//        if (System.getSecurityManager() == null) {
//            System.setSecurityManager(new SecurityManager());
//        }
        try {
            String name = "RobotServer";
            DemoInterface engine = new DemoServer();
            DemoInterface stub =
                    (DemoInterface) UnicastRemoteObject.exportObject(engine, 0);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(name, stub);
            System.out.println("Robot Server bound");
        } catch (Exception e) {
            System.err.println("Robot Server exception:");
            e.printStackTrace();
        }

    }
}
