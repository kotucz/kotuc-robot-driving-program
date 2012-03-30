/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package robotour.hardware.oldmotors;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 *
 * @author Kotuc
 */
enum KoscaksPatent {
    INSTANCE;
    
    private final float[] rezs = new float[] {5600, 12000, 27000, 56000, 100000, 220000, 470000, -1};
    
    private final String[] captions = new String [] {"0 (5k6)", "1 (12k)", "2 (27k)", "3 (56k)", "4 (100k)", "5 (220k)", "6 (470k)", "7 (direction)",};

    public String getCaption(int bit) {
        return captions[bit];
    }   
    
    public float getRezistence(int bits) {
        float rsum = 0;
        float rmul = 1;
        
        for (int i = 0; i < 7; i++) {
            if ((bits&(1 << i))!=0) {
                rmul *= rezs[i];
            }
        }
        
        for (int i = 0; i < 7; i++) {
            if ((bits&(1 << i))!=0) {
                rsum += rmul/rezs[i];
            }
        }
        
        return (rmul/rsum);
        
    }
    
    public SortedMap<Float, Integer> createTable() {
        SortedMap<Float, Integer> vals = new TreeMap<Float, Integer>();
        for (int i = 0; i < 128; i++) {
            vals.put(getRezistence(i), i);                    
        }
        return vals;
    }    
    
    
    
}
