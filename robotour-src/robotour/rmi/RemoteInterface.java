/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package robotour.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Tomas
 */
interface RemoteInterface extends Remote {

    String print(String text) throws RemoteException;
}
