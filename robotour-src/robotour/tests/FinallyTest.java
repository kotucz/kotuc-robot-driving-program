/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package robotour.tests;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kotuc
 */
class FinallyTest {

    public static void main(String[] args) {
        try {
            Runtime.getRuntime().addShutdownHook(new Thread() {

                @Override
                public void run() {
                    System.out.println("HAHA Good 2");
//                    while (true) {
//                        System.out.println("working shutdown...");
//                        try {
//                            Thread.sleep(10);
//                        } catch (InterruptedException ex) {
//                            Logger.getLogger(FinallyTest.class.getName()).log(Level.SEVERE, null, ex);
//                        }
//                    }
                }
            });
            while (true) {
                System.out.println("working...");
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ex) {
                    Logger.getLogger(FinallyTest.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } finally {
            System.out.println("HAHA Good 1");
        }
    }
}
