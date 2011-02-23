package robotour.gui;

import robotour.util.RobotSystems;
import robotour.iface.MeasureException;
import robotour.iface.Sonar;
import robotour.iface.Compass;
import robotour.util.Sonars;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import robotour.navi.gps.Azimuth;
import vision.input.VideoInput;

/**
 *
 * @author Tomas
 */
public class ViewPanel extends javax.swing.JPanel {

    public static final long serialVersionUID = 135L;
//    /** Creates new form ViewPanel */
//    private ViewPanel() {
//        throw new AssertionError();
//        //initComponents();
//    }
    private final Sonars sonars;
    private final Compass compass;
    private final VideoInput video;

    public ViewPanel(Sonars sonars, Compass compass, VideoInput video) {
        this.sonars = sonars;
        this.compass = compass;
        this.video = video;

        this.setPreferredSize(new Dimension(320, 240));
        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                repaint();
//                snap = video.snap();
//                System.out.println("snapi snap " + snap);
            }
        }, 1000, 50);

    }
    //private BufferedImage snap;

    private ViewPanel(RobotSystems systems) {
        this(systems.getSonars(), systems.getCompass(), systems.getVideo());
//        Odometry odometry = new Odometry(systems.getWheels(), systems.getCompass());
//        odometry.startOdometryThread();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        if (video != null) {
            g.drawImage(video.snap(), 0, 0, 320, 240, null);
        }
        //        g.drawString("snap: " + snap, 50, 50);

        ((Graphics2D) g).setStroke(new BasicStroke(3f));
        g.setFont(Font.decode("Arial BOLD 16"));

        g.setColor(Color.GREEN);
        g.drawRect(0, 0, 320, 240);

        try {
            Azimuth azimuth = compass.getAzimuth();
//            final int FULL = 320;
//            final double RAD = FULL / (2 * Math.PI);
//            int nx = (160 - (int) (RAD * azimuth)) % FULL;
//            int sx = (160 - (int) (RAD * (azimuth + Math.PI))) % FULL;
//            int ex = (160 - (int) (RAD * (azimuth + 0.5 * Math.PI))) % FULL;
//            int wx = (160 - (int) (RAD * (azimuth - 0.5 * Math.PI))) % FULL;

//            int nx = (int) (160 * (1 + azimuth.sin()));

//            int sx = (int) (160 * (1 + Math.sin(azimuth + Math.PI)));
//            int ex = (int) (160 * (1 + Math.sin(azimuth + 0.5 * Math.PI)));
//            int wx = (int) (160 * (1 + Math.sin(azimuth - 0.5 * Math.PI)));
//            g.drawString("N", nx, 20);
//            g.drawString("S", sx, 20);
//            g.drawString("E", ex, 20);
//            g.drawString("W", wx, 20);
            g.drawString("" + azimuth, 10, 40);
//            System.out.println("azimuth: " + azimuth);
        } catch (MeasureException ex) {
            g.drawString("compass measuring fault", 10, 20);
            Logger.getLogger(ViewPanel.class.getName()).log(Level.SEVERE, null, ex);
        }

        {
            g.setColor(Color.RED);
            int sonx = 110;
            int sonang = 105;
            for (Sonar sonar : sonars.getSonars()) {
                try {
                    g.drawOval(sonx, 230 - (int) (100 * sonar.getDistance()), 10, 10);
                    int d = (int) (500 * sonar.getDistance());
                    g.drawArc(sonx - d, 240 - d, 2 * d, 2 * d, sonang, 30);
                } catch (MeasureException ex) {
                    g.drawRect(sonx, 220, 20, 20);
                    Logger.getLogger(ViewPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
                sonang -= 30;
                sonx += 50;
            }
            System.out.println(sonars);
        }

    }

    public void openWindow() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.setSize(350, 300);
//        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) throws RemoteException, NotBoundException {
        RobotSystems systems = RobotSystems.getDefault();
        new ViewPanel(systems).openWindow();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
