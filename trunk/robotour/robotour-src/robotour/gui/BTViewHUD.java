package robotour.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;
import robotour.jbrain.Bluetooth;

/**
 *
 * @author Kotuc
 */
public class BTViewHUD extends JPanel {

    final Bluetooth bt;

    public BTViewHUD() throws Exception {
        this.bt = Bluetooth.getBluetooth("COM10");
    }
    float azimuth;
    int[] ranges = new int[3];

    @Override
    public void paint(Graphics g) {
        paintCompass(g);
        paintSonars(g);
    }

    public void paintCompass(Graphics g) {
        ((Graphics2D) g).setStroke(new BasicStroke(3f));
        g.setFont(Font.decode("Arial BOLD 16"));

        g.drawString("" + azimuth, 10, 40);
    }

    public void paintSonars(Graphics g) {

        g.setColor(Color.RED);
        int sonx = 110;
        int sonang = 105;
        for (int range : ranges) {
            g.drawOval(sonx, 230 - range, 10, 10);
            int d = (int) (5 * range);
            g.drawArc(sonx - d, 240 - d, 2 * d, 2 * d, sonang, 30);
            sonang -= 30;
            sonx += 50;
        }
//        System.out.println(sonars);
    }

    public static void main(String[] args) throws Exception {

        BTViewHUD viewHUD = new BTViewHUD();
        JFrame jFrame = new JFrame("BTView");
        jFrame.add(viewHUD);
        jFrame.pack();
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        new Thread() {

            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(BTViewHUD.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }.start();
    }
    // Variables declaration - do not modify
    // End of variables declaration
}
