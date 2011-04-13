package robotour.gui.map;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JPanel;

public class MapViewPanel extends JPanel {

    public final MapView map;
    private LocalPoint mousePoint = new LocalPoint(0, 0);


    public MapViewPanel(MapView m) {

        if (m == null) {
            throw new NullPointerException("map");
        }

//        setPreferredSize(new Dimension(400, 400));
        this.map = m;

        Timer t = new java.util.Timer();
        t.schedule(new TimerTask() {

            public void run() {
                repaint();
            }
        }, 50, 50);



        addMouseListener(mouseTool);
        addMouseMotionListener(mouseTool);
        addMouseWheelListener(mouseTool);

        addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_S:

//                        GPXIO.showSaveTrackDialog(DeviceManager.getGps().getActiveLog(), (Component) e.getSource());

                        break;
                    case KeyEvent.VK_PAGE_UP:
                        map.setScale(map.getScale() / 2);
                        break;
                    case KeyEvent.VK_PAGE_DOWN:
                        map.setScale(map.getScale() * 2);
                        break;

//                    case KeyEvent.VK_UP:
//                        map.setEye(map.getEye().move(Azimuth.NORTH, map.getScale() / 64));
//                        break;
//                    case KeyEvent.VK_DOWN:
//                        map.setEye(map.getEye().move(Azimuth.SOUTH, map.getScale() / 64));
//                        break;
//                    case KeyEvent.VK_LEFT:
//                        map.setEye(map.getEye().move(Azimuth.WEST, map.getScale() / 64));
//                        break;
//                    case KeyEvent.VK_RIGHT:
//                        map.setEye(map.getEye().move(Azimuth.EAST, map.getScale() / 64));
//                        break;
//                    case KeyEvent.VK_
                }
                repaint();
            }
        });



        setPreferredSize(new Dimension(600, 600));

    }

    @Override
    public boolean isFocusable() {
        return true;
    }

    LocalPoint clickToLocal(Point click) {
        click.x -= getWidth() / 2;
        click.y -= getHeight() / 2;
        return map.clickToLocal(click);
    }

    @Override
    public void paint(Graphics g1) {

        

//            if ((eyelock == 1) && (DeviceManager.getGps() != null)) {
//                map.setEye(DeviceManager.getGps().getPosition());
//
//            }
//            if ((eyelock==2)&&(DeviceManager.getBody()!=null)) {
//                map.setEye(DeviceManager.getBody().getPos());
//            }


        BufferedImage bi = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);

        Graphics2D g = (Graphics2D) bi.getGraphics();

//            g.setColor(Color.BLACK);
//            g.fillRect(0, 0, getWidth(), getHeight());

        g.translate(getWidth() / 2, getHeight() / 2);

        map.paint(g);

        //                GPSPoint O = new GPSPoint();
//        g.setColor(Color.RED);
//        g.drawRect(-2, -2, 5, 5);

        g.setColor(Color.WHITE);


//            Device.gps.getAzimuth();


//            g.setColor(Color.GREEN);

        //                g.fillRect(10, 10, (int)(1+(map.dpm*map.scale)), 3);

//            if (rover != null) {
//                /*
//                g.translate(map.zoom*rover.pos.x, map.zoom*rover.pos.y);
//                g.rotate(-rover.wang+Math.PI);
//
//                g.drawImage(roverIcon, -8, -8, null);
//
//                g.rotate(+rover.wang+Math.PI);
//                g.translate(-map.zoom*rover.pos.x, -map.zoom*rover.pos.y);
//
//                g.setColor(Color.RED);
//
//                Point p0 = new Point((int)(map.zoom*rover.pos.x), (int)(map.zoom*rover.pos.y));
//                for (Map.Waypoint p1 : rover.shortPath) {
//                    g.drawLine(p0.x, p0.y, p0.x=map.getX(p1), p0.y=map.getY(p1));
//
//                }
//                 */
//            }

//                g.drawRect(-200, -200, 400, 200);

        g1.drawImage(bi, 0, 0, null);

        g1.setColor(Color.GREEN);

        g1.drawString("grid size " + (map.mets) +"m", getWidth() - 200, getHeight() - 40);
        g1.drawString("scale: 1:" + (int) (map.getScale()), getWidth() - 200, getHeight() - 20);
        g1.fillRect(getWidth() - 10 - (int) (1 + (MapView.DPM / map.getScale())), getHeight() - 10, (int) (1 + (MapView.DPM / map.getScale())), 3);

        g1.drawString("" + map.getEye(), 10, 20);

        g1.drawString("" + mousePoint, 10, 40);

    }

    public static void main(String[] args) {
        new MapView().showInFrame();
    }
    final MouseAdapter mouseTool = new MouseAdapter() {

        @Override
        public void mouseMoved(MouseEvent e) {
//            System.out.println("moved");
            mousePoint = clickToLocal(e.getPoint());
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {

//                map.setScale(map.getScale() * Math.pow(0.5, e.getWheelRotation()));
            map.setScale(map.getScale() * Math.pow(1.1, e.getWheelRotation()));


//                    Point p = e.getPoint();
//                    p.x -= getWidth()/2;
//                    p.y -= getHeight()/2;
//                    map.eye = map.convertClickToGPS(p);



        }

        @Override
        public void mousePressed(MouseEvent e) {
            requestFocus();
            if (e.getButton() == MouseEvent.BUTTON1) {
                map.eyelockon = false;
                map.setEye(clickToLocal(e.getPoint()));
            }

            if (e.getButton() == MouseEvent.BUTTON3) {
                map.eyelockon = true;
//                    eyelock++;
//                    if ((eyelock == 1) && (DeviceManager.getGps() != null)) {
//
//                        map.setScale(100);
//                    }
////  TODO                  if ((eyelock==2)&&(DeviceManager.getBody()!=null)) {
////
////                        map.setScale(30);
////                    }
////                    if ((eyelock==3)&&(DeviceManager.getBody()!=null)) {
////                        map.zoomTo(DeviceManager.getBody().track);
////
////                    }
//                    if (eyelock > 3) {
//                        eyelock = 0;
//                    }
            }
            repaint();
        }
    };
}
