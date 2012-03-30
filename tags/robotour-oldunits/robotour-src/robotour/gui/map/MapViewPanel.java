package robotour.gui.map;

import robotour.gui.map.gps.MapView;
import robotour.navi.basic.LocalPoint;
import robotour.navi.basic.RobotPose;

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
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JPanel;

public class MapViewPanel extends JPanel {

    public final LocalMapView map;
    private LocalPoint mousePoint = new LocalPoint(0, 0);

    private static final int MAX_SCALE = 10000;//1024 * 1024 * 1024;
    private static final int MIN_SCALE = 1;
    /**
     * dots (pixels) per inch (2.54 cm)
     */
    static double DPI = 98;// 72;
    /**
     *  how many dots (pixels) in map with scale 1:1 is one meter in real
     */
    public static double DPM = DPI * 100.0 / 2.54;


    double mets = 0.01;



    public RobotPose eyelock = null;

    public boolean eyelockon = true;


    //    double scale = 0.0000001;
//    private double scale2 = 10000000.0;
    private double scale2;


    //    private GPSPoint eye = GPSPoint.fromDegrees(0, 0);
    protected LocalPoint eye = new LocalPoint(0, 0);
    private AffineTransform transform;


    public MapViewPanel(LocalMapView m) {

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

        setScale(20);

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
                        setScale(getScale() / 2);
                        break;
                    case KeyEvent.VK_PAGE_DOWN:
                        setScale(getScale() * 2);
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
//        LocalPoint lp = new LocalPoint(
//                eye.getX() + (scale2 * (click.x- getWidth() / 2) / DPM),
//                eye.getY() - (scale2 * (click.y - getHeight() / 2) / DPM));


        try {
            return LocalPoint.fromPoint2D(transform.inverseTransform(click, null));
        } catch (NoninvertibleTransformException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw new RuntimeException(e);
        }

//        return lp;


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

        Graphics2D g2 = (Graphics2D) bi.getGraphics();

//            g.setColor(Color.BLACK);
//            g.fillRect(0, 0, getWidth(), getHeight());

// center
        g2.translate(getWidth() / 2, getHeight() / 2);


//center to eye
        if (eyelockon) {
            if (eyelock != null) {
                this.setEye(eyelock.getPoint());
            }
        }

        g2.scale(DPM / scale2, -DPM / scale2);
        g2.translate(-eye.getX(), -eye.getY());




        transform = g2.getTransform();


        map.paint(g2);

        //                GPSPoint O = new GPSPoint();
//        g.setColor(Color.RED);
//        g.drawRect(-2, -2, 5, 5);

        g2.setColor(Color.WHITE);




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

        drawGrid(g2);

        g1.drawImage(bi, 0, 0, null);

        g1.setColor(Color.GREEN);

        g1.drawString("grid size " + (mets) +"m", getWidth() - 200, getHeight() - 40);
        g1.drawString("scale: 1:" + (int) (getScale()), getWidth() - 200, getHeight() - 20);
        g1.fillRect(getWidth() - 10 - (int) (1 + (DPM / getScale())), getHeight() - 10, (int) (1 + (DPM / getScale())), 3);

        g1.drawString("" + getEye(), 10, 20);

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
            setScale(getScale() * Math.pow(1.1, e.getWheelRotation()));


//                    Point p = e.getPoint();
//                    p.x -= getWidth()/2;
//                    p.y -= getHeight()/2;
//                    map.eye = map.convertClickToGPS(p);



        }

        @Override
        public void mousePressed(MouseEvent e) {
            requestFocus();
//            if (e.getButton() == MouseEvent.BUTTON1) {
//                eyelockon = false;
//                setEye(clickToLocal(e.getPoint()));
//            }

//            if (e.getButton() == MouseEvent.BUTTON3) {
//                eyelockon = true;
////                    eyelock++;
////                    if ((eyelock == 1) && (DeviceManager.getGps() != null)) {
////
////                        map.setScale(100);
////                    }
//////  TODO                  if ((eyelock==2)&&(DeviceManager.getBody()!=null)) {
//////
//////                        map.setScale(30);
//////                    }
//////                    if ((eyelock==3)&&(DeviceManager.getBody()!=null)) {
//////                        map.zoomTo(DeviceManager.getBody().track);
//////
//////                    }
////                    if (eyelock > 3) {
////                        eyelock = 0;
////                    }
//            }
            repaint();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
//            System.out.println("dragged ");
            LocalPoint localPoint = clickToLocal(e.getPoint());

//            System.out.println(""+localPoint+" "+mousePoint);

            eye = eye.add(localPoint.vectorTo(mousePoint));
//            eye = eye.add(mousePoint.vectorTo(localPoint));

//            mousePoint = localPoint;

            repaint();



        }
    };

    public void setEye(LocalPoint neweye) {
        this.eye = neweye;
    } //    /**

    int metersToPixels(double meters) {
        return (int) Math.round(meters * DPM / scale2);


    }

    public double getScale() {
        return scale2;


    }

    public void setScale(double scale) {
        scale = Math.max(MIN_SCALE, Math.min(scale, MAX_SCALE));


        this.scale2 = scale;


    }

    public void zoomTo(LocalPoint center, double scale) {
        setEye(center);
        setScale(
                scale);


    }

    private void drawGrid(Graphics2D g) {
        g.setColor(Color.DARK_GRAY);

//        double lon = eye.longitude().radians();//getLongitude();
//        double prec = 0.001;
//        lon = (int) Math.round(lon / prec);
//        lon = lon * prec;

        mets = 0.01;
        // pixel minimum diff


        while (metersToPixels(mets) < 30) {
            mets *= 10;


        }

        for (int i = -10; i
                < 10; i++) {

            // TODO FIXME rounding by mets! not by 1!

            LocalPoint px1 = new LocalPoint(-1000, Math.round(eye.getY()) + i * mets);
            LocalPoint px2 = new LocalPoint(1000, Math.round(eye.getY()) + i * mets);

            LocalPoint py1 = new LocalPoint(Math.round(eye.getX()) + i * mets, -1000);
            LocalPoint py2 = new LocalPoint(Math.round(eye.getX()) + i * mets, 1000);

            map.drawLine(px1, px2, 0.001);
            map.drawLine(py1, py2, 0.001);

//            map.drawLine(GPSPoint.fromDegrees(47, i), GPSPoint.fromDegrees(55, i), 1);
//            g.drawLine((int)(e.x+i), (int)(e.y-2000), (int)((e.x+i)), (int)(e.y+2000));
//            g.drawLine((int)(e.x-2000), (int)((e.y+i)), (int)(e.x+2200), (int)((e.y+i)));
//            g.drawLine((int)(zoom*i), -2000, (int)(zoom*i), 2000);
//            g.drawLine(-2000, (int)(zoom*i), 2000, (int)(zoom*i));


        }
    }

    public LocalPoint getEye() {
        return eye;


    }
}

