/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eurobot.plot;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

/**
 *
 * @author Kotuc
 */
public class CVSWriter extends BufferedWriter {

    
    private boolean first = true;

    public CVSWriter(Writer writer) {
        super(writer);
    }    
    
    public void writeField(String string) throws IOException {
        if (first) {
            first = false;
        } else {
            append(",");
        }
        append(string);
    }
       
    @Override
    public void newLine() throws IOException {
        first = true;
        super.newLine();
    }    
    
    
}
