package vision.pathfinding;

import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.vecmath.Point2f;
import javax.vecmath.Vector2f;



/**
 * 
 * @author PC
 * TODO unchecked exceptions in floor canvas
 * check TODO == 0 comparations
 * 
 */
public final class PathFinding {

    /** noninstantible */
    private PathFinding() {
    }
    private static float carLength = 0.5f;
    private static float wheelSpan = 0.5f;

    public static float paintTrailsCM(BufferedImage i3, float steer) {

        if (Math.abs(steer) < 0.001) {
            return paintTrailsStraight(i3);
        }

        FloorCanvas fc = new FloorCanvas(i3);
        HeatMap hm = HeatMap.getHeatMapCM(i3);

        float distsum = 0;

        float[] rs = new float[]{-0.3f, -0.15f, 0, 0.15f, 0.3f};

        // tries each of
        for (int i = 0; i < rs.length; i++) {

            Point2f ws = new Point2f();

            float R = (carLength / (float) Math.tan(steer));

            rs[i] = Math.signum(R) * new Vector2f(R + rs[i], carLength).length();


            fc.setColor(Color.CYAN);

            Point2f ws0 = new Point2f();

            ws0 = new Point2f(rs[i] - R, 0);

            for (float dist = 0.1f; dist < 15; dist += 0.05) {

                float fi = dist / R;

                ws = new Point2f(rs[i] - R + rs[i] * (float) (1 - Math.cos(fi)), rs[i] * (float) Math.sin(fi));

                try {

//                fc.setColor(new Color(hm.getHeat(sw.x, sw.y)));


                    float heat = hm.getHeatFloor(ws.x, ws.y);

                    if (heat > 0.8) {
                        fc.setColor(Color.GREEN);
                    } else {
                        fc.setColor(Color.CYAN);

                        distsum += dist;
                        break;
                    }
                } catch (Exception ex) {
                }


                fc.drawLine(ws0, ws);
                ws0 = ws;

            }
        }

        return distsum / 5;

    }

    public static float paintTrails4(BufferedImage i3, float steer) {

        if (Math.abs(steer) < 0.001) {
            return paintTrailsStraight(i3);
        }

        FloorCanvas fc = new FloorCanvas(i3);
        HeatMap hm = HeatMap.getHeatMap2_2(i3);

        float distsum = 0;

        float[] Rs = new float[]{-0.3f, -0.15f, 0, 0.15f, 0.3f};

        // tries each of
        for (int i = 0; i < Rs.length; i++) {

            Point2f ws = new Point2f();

            float R = (carLength / (float) Math.tan(steer));

            Rs[i] = Math.signum(R) * new Vector2f(R + Rs[i], carLength).length();


            fc.setColor(Color.CYAN);

            Point2f ws0 = new Point2f();

            ws0 = new Point2f(Rs[i] - R, 0);

            for (float dist = 0.1f; dist < 5; dist += 0.05) {

                float fi = dist / R;

                ws = new Point2f(Rs[i] - R + Rs[i] * (float) (1 - Math.cos(fi)), Rs[i] * (float) Math.sin(fi));

                try {

//                fc.setColor(new Color(hm.getHeat(sw.x, sw.y)));


                    float heat = hm.getHeatFloor(ws.x, ws.y);

                    if (heat > 0.8) {
                        fc.setColor(Color.GREEN);

                    } else {
                        fc.setColor(Color.CYAN);

                        distsum += dist;
                        break;
                    }
                } catch (Exception ex) {
                }


                fc.drawLine(ws0, ws);
                ws0 = ws;

            }
        }

        return distsum / 5;

    }

    /**
     *  version between 2 and 4 :). starting to be general but in wrong way..
     */
    public static float paintTrails3(BufferedImage i3, float steer) {

        if (Math.abs(steer) < 0.001) {
            return paintTrailsStraight(i3);
        }

        FloorCanvas fc = new FloorCanvas(i3);
        HeatMap hm = HeatMap.getHeatMap2_2(i3);

        Point2f[] ws = new Point2f[5];

        for (int i = 0; i < ws.length; i++) {
            ws[i] = new Point2f();
        }

        float[] Rs = new float[]{-0.3f, -0.15f, 0, 0.15f, 0.3f};

        float R = (carLength / (float) Math.tan(steer));



        for (int i = 0; i < Rs.length; i++) {
            Rs[i] = Math.signum(R) * new Vector2f(R + Rs[i], carLength).length();
        }

        fc.setColor(Color.CYAN);

        Point2f[] ws0 = new Point2f[ws.length];

        for (int i = 0; i < ws0.length; i++) {
            ws0[i] = new Point2f(Rs[i] - R, 0);
        }

        for (float dist = 0.1f; dist < 15; dist += 0.05) {

            float fi = dist / R;

            for (int i = 0; i < Rs.length; i++) {
                ws[i] = new Point2f(Rs[i] - R + Rs[i] * (float) (1 - Math.cos(fi)), Rs[i] * (float) Math.sin(fi));
            }

            try {

//                fc.setColor(new Color(hm.getHeat(sw.x, sw.y)));

                for (int i = 0; i < Rs.length; i++) {
                    float heat = hm.getHeatFloor(ws[i].x, ws[i].y);
                    fc.setColor(Color.GREEN);
                }

//                if ((leftheat > 240)&&(rightheat > 240)&&(sheat > 240)) {
//                    fc.setColor(Color.PINK);

//                } else {
//                    fc.setColor(Color.CYAN);
//                    return dist;
//                }
//                if (hm.getHeat(sw.x, sw.y)==0)
//
//                    fc.setColor(Color.PINK);
//                else fc.setColor(Color.CYAN);
            } catch (Exception ex) {
            }

            for (int i = 0; i < Rs.length; i++) {
                fc.drawLine(ws0[i], ws[i]);
                ws0[i] = ws[i];
            }




        }

        return 1;

    }

    public static float paintTrails2(BufferedImage i3, float steer) {

        if (Math.abs(steer) < 0.001) {
            return paintTrailsStraight(i3);
        }

        FloorCanvas fc = new FloorCanvas(i3);
        HeatMap hm = HeatMap.getHeatMap2_2(i3);
        //HeatMap hm = HeatMap.getHeatMap2_2(i3);

        Point2f sw = new Point2f();
        Point2f lw = new Point2f();
        Point2f rw = new Point2f();

        float R = (carLength / (float) Math.tan(steer));
        float lR = Math.signum(R) * new Vector2f(R - 0.3f, carLength).length();
        float rR = Math.signum(R) * new Vector2f(R + 0.3f, carLength).length();

        fc.setColor(Color.CYAN);

        Point2f sw0 = new Point2f(sw.x, sw.y);
        Point2f lw0 = new Point2f(lR - R, sw.y);
        Point2f rw0 = new Point2f(rR - R, sw.y);

        for (float dist = 0.1f; dist < 15; dist += 0.05) {

            float fi = dist / R;

            sw = new Point2f(R - R * (float) Math.cos(fi), R * (float) Math.sin(fi));
            lw = new Point2f(lR - R + /*lR-*/ lR * (float) (1 - Math.cos(fi)), lR * (float) Math.sin(fi));
            rw = new Point2f(rR - R + /*rR-*/ rR * (float) (1 - Math.cos(fi)), rR * (float) Math.sin(fi));

            try {

//                fc.setColor(new Color(hm.getHeat(sw.x, sw.y)));

                float leftheat = hm.getHeatFloor(lw.x, lw.y);
                float sheat = hm.getHeatFloor(sw.x, sw.y);
                float rightheat = hm.getHeatFloor(rw.x, rw.y);

                if ((leftheat > 240) && (rightheat > 240) && (sheat > 240)) {
//                    fc.setColor(Color.PINK);
                    fc.setColor(Color.GREEN);
                } else {
                    fc.setColor(Color.CYAN);
                    return dist;
                }
//                if (hm.getHeat(sw.x, sw.y)==0)
//
//                    fc.setColor(Color.PINK);
//                else fc.setColor(Color.CYAN);
            } catch (Exception ex) {
            }

            fc.drawLine(sw0, sw);
            fc.drawLine(lw0, lw);
            fc.drawLine(rw0, rw);

            sw0 = sw;
            lw0 = lw;
            rw0 = rw;

        }

        return 1;

    }

    public static float paintTrails(BufferedImage i3, float steer) {

        if (Math.abs(steer) < 0.001) {
            return paintTrailsStraight(i3);
        }

        FloorCanvas fc = new FloorCanvas(i3);
        HeatMap hm = HeatMap.getHeatMap(i3, HeatMap.FUNC_HM10);

        Point2f sw = new Point2f();
        Point2f lw = new Point2f();
        Point2f rw = new Point2f();

        float R = (carLength / (float) Math.tan(steer));
        float lR = Math.signum(R) * new Vector2f(R - 0.3f, carLength).length();
        float rR = Math.signum(R) * new Vector2f(R + 0.3f, carLength).length();

        fc.setColor(Color.CYAN);

        Point2f sw0 = new Point2f(sw.x, sw.y);
        Point2f lw0 = new Point2f(lR - R, sw.y);
        Point2f rw0 = new Point2f(rR - R, sw.y);

        for (float dist = 0.1f; dist < 15; dist += 0.05) {

            float fi = dist / R;

            sw = new Point2f(R - R * (float) Math.cos(fi), R * (float) Math.sin(fi));
            lw = new Point2f(lR - R + lR - lR * (float) Math.cos(fi), lR * (float) Math.sin(fi));
            rw = new Point2f(rR - R + rR - rR * (float) Math.cos(fi), rR * (float) Math.sin(fi));

            try {

                if (hm.getHeatFloor(sw.x, sw.y) < 0.5) {
                    fc.setColor(Color.PINK);
                } else {
                    fc.setColor(Color.CYAN);
                }
            } catch (Exception ex) {
            }

            fc.drawLine(sw0, sw);
            fc.drawLine(lw0, lw);
            fc.drawLine(rw0, rw);

            sw0 = sw;
            lw0 = lw;
            rw0 = rw;

        }

        return 1;

    }

    public static float paintTrailsStraight(BufferedImage i3) {

        FloorCanvas fc = new FloorCanvas(i3);

        HeatMap hm = HeatMap.getHeatMap(i3, HeatMap.FUNC_HM10);

        Point2f sw = new Point2f();
        Point2f lw = new Point2f();
        Point2f rw = new Point2f();


        Point2f sw0 = new Point2f(0, 0);
        Point2f lw0 = new Point2f(-wheelSpan / 2, carLength);
        Point2f rw0 = new Point2f(+wheelSpan / 2, carLength);

        for (float dist = 0.1f; dist < 15; dist += 0.05) {

            sw = new Point2f(0, dist);
            lw = new Point2f(-wheelSpan / 2, dist + carLength);
            rw = new Point2f(+wheelSpan / 2, dist + carLength);

            try {

                if (hm.getHeatFloor(sw.x, sw.y) == 0) {
                    fc.setColor(Color.PINK);
                } else {
                    fc.setColor(Color.CYAN);
                }
            } catch (Exception ex) {
            }

            fc.drawLine(sw0, sw);
            fc.drawLine(lw0, lw);
            fc.drawLine(rw0, rw);

            sw0 = sw;
            lw0 = lw;
            rw0 = rw;

        }

        return 1;

    }

    /**
     *
     * @param i3
     * @return
     */
    public static float paintRoad(BufferedImage i3) {

        FloorCanvas fc = new FloorCanvas(i3);
        HeatMap hm = HeatMap.getHeatMap(i3, HeatMap.FUNC_HM10);

        fc.setColor(Color.GREEN);

        Point2f s0 = new Point2f(0f, 0.5f);
        Point2f s1 = new Point2f(0f, 0.5f);
        Point2f s2 = new Point2f(0f, 0.5f);
        Point2f le0 = new Point2f();
        Point2f re0 = new Point2f();

        for (float dist = 0.5f; dist < 5; dist += 0.03) {

            float lw, rw;

            for (lw = s2.x;; lw -= 0.03) {
                try {
                    if (hm.getHeatFloor(lw, dist) < 0.5) {
                        break;
                    }
                } catch (Exception ex) {
                    break;
                }
            }

            le0.y = dist;
            le0.x = lw;

            for (rw = s2.x;; rw += 0.03) {
                try {
                    if (hm.getHeatFloor(rw, dist) < 0.5) {
                        break;
                    }
                } catch (Exception ex) {
                    break;
                }
            }

            re0.y = dist;
            re0.x = rw;

            s2 = new Point2f((lw + rw) / 2, dist);

            s1.x = (s0.x + s1.x + s2.x) / 3;

            fc.drawLine(le0, re0);
            fc.drawLine(s0, s1);

            s0 = s1;
            s1 = s2;

//            fc.drawLine(rw0, rw);

//            sw0 = sw;
//            lw0 = lw;
//            rw0 = rw;

        }

        return 1;

    }

    public static float longline(BufferedImage i3) {
        Point2f lcc = new Point2f();
        Point2f rcc = new Point2f();
        Point2f ucc = new Point2f();
        Point2f dcc = new Point2f();
        lcc = findCrossroadBorder(i3, new Point2f(-0.5f, 1.5f), new Point2f(-0.5f, 5), 0);
        rcc = findCrossroadBorder(i3, new Point2f(0.5f, 1.5f), new Point2f(0.5f, 5), 1);
        ucc = findCrossroadBorder(i3, new Point2f(-3.5f, 3f), new Point2f(3.5f, 3.1f), 0);
        dcc = findCrossroadBorder(i3, new Point2f(-3.5f, 1.3f), new Point2f(3.5f, 1.35f), 1);


        FloorCanvas fc = new FloorCanvas(i3);
        fc.setColor(Color.GREEN);
        fc.drawLine(lcc, ucc);
        fc.drawLine(ucc, rcc);
        fc.drawLine(rcc, dcc);
        fc.drawLine(dcc, lcc);

        return 1;
    }

    public static Point2f findCrossroadBorder(BufferedImage i3, Point2f p1, Point2f p2, int side) {

        FloorCanvas fc = new FloorCanvas(i3);
        HeatMap hm = HeatMap.getHeatMap(i3, HeatMap.FUNC_HM10);

//     paintCrossRoad(i3);

        fc.setColor(Color.GREEN);

        fc.drawLine(p1, p2);
        float dx = p2.x - p1.x;
        float dy = p2.y - p1.y;
        float adx = Math.abs(dx);
        float ady = Math.abs(dy);

        float max = Math.max(adx, ady);
        float min = Math.min(adx, ady);

//         float k = max / min;

        //  dx /= 50f;
        //  dy /= 50f;



        Point2f maxs = new Point2f();
        Point2f maxc = new Point2f();
        Point2f s = new Point2f();
//       Point c = new Point();

        int maxDelka = 0;
        int delka = 0;
        int aktualni = 0;
        int minuly = 0;

        float dist = fc.getDistance(p1.x, p1.y, p2.x, p2.y);

        for (float i = 0; i < 1; i += 1 / dist) {

            minuly = aktualni;
            try {
                if (hm.getHeatFloor(p1.x + dx * i, p1.y + dy * i) < 0.5) {
                    aktualni = 0;
                } else {
                    aktualni = 1;
                }
            } catch (Exception ex) {
                continue;
            }

            if ((minuly == 1) && (aktualni == 1)) {
                if (delka <= 0) {
                    delka = 1;
                    s = new Point2f(p1.x + dx * i, p1.y + dy * i);
                } else {
                    delka += 1;
                }
            }

            if (((minuly == 0) && (aktualni == 0)) || (i + 1 / dist) >= 1) {

                if (delka > maxDelka) {
                    maxDelka = delka;
                    maxs = s;
                    maxc = new Point2f(p1.x + dx * i, p1.y + dy * i);
                    delka = 0;
                } else {
                    delka = 0;
                }
            }
        }

        p1 = maxs;
        p2 = maxc;



        fc.setColor(Color.red);
        fc.drawLine(p1, p2);

        Point2f cc = new Point2f();

        float vel1 = 0, vel2 = 0;
        for (int i = 0; i < 100; i++) {
            vel2 = (vel2 + vel1) / 2;
            if (i == 1) {
                vel2 = vel1;
            }
            cc = new Point2f((p1.x + p2.x) / 2, (p1.y + p2.y) / 2);

            vel1 = paintStepLine(i3, p1, p2, fc, hm, vel2, side);

            if (vel1 == -1) {
                break;
            }

        }






        return cc;


    }

    public static float paintStepLine(BufferedImage i3, Point2f p1, Point2f p2, FloorCanvas fc, HeatMap hm, float prev, int side) {
        Point2f c = new Point2f();
        c = showStepLineCentre(i3, p1, p2, side);


        float dx = p2.x - c.x;
        float dy = p2.y - c.y;

        int aktualni = 0;
        int minuly = 0;

        float dist = fc.getDistance(c.x, c.y, p2.x, p2.y);



        for (float i = 0; i < 8; i += 1 / dist) {


            minuly = aktualni;
            try {
                if (hm.getHeatFloor(c.x + dx * i, c.y + dy * i) < 0.5) {
                    aktualni = 0;
                } else {
                    aktualni = 1;
                }
            } catch (Exception ex) {
                continue;
            }

            if (((minuly == 1) && (aktualni == 1)) || (i + (1 / dist) > 8)) {
                p2.x = c.x + dx * i;
                p2.y = c.y + dy * i;
            }

            if ((minuly == 0) && (aktualni == 0)) {
                break;
            }

        }

        dx = p1.x - c.x;
        dy = p1.y - c.y;

        aktualni = 0;
        minuly = 0;

        dist = fc.getDistance(p1.x, p1.y, c.x, c.y);



        for (float i = 0; i < 8; i += 1 / dist) {


            minuly = aktualni;
            try {
                if (hm.getHeatFloor(c.x + dx * i, c.y + dy * i) < 0.5) {
                    aktualni = 0;
                } else {
                    aktualni = 1;
                }
            } catch (Exception ex) {
                continue;
            }

            if ((minuly == 1) && (aktualni == 1)) {
                p1.x = c.x + dx * i;
                p1.y = c.y + dy * i;
            }

            if (((minuly == 0) && (aktualni == 0)) || (i + (1 / dist) > 8)) {
                break;
            }

        }

        dist = fc.getDistance(p1.x, p1.y, p2.x, p2.y);
        fc.setColor(Color.orange);
        if ((dist / prev > 1.3f) && (prev > 0.1f)) {
            fc.setColor(Color.blue);
            dist = -1;
        }
        fc.drawLine(p1, p2);
        return dist;
    }

    public static Point2f showStepLineCentre(BufferedImage i3, Point2f p1, Point2f p2, int side) {
        Point2f vec = new Point2f(p2.x - p1.x, p2.y - p1.y);
        vec = normalizovany(i3, p1, p2, 0.02f);
        vec = kolmy(vec);
        if (side == 1) {
            vec.x = -vec.x;
            vec.y = -vec.y;
        }
        p1.x += vec.x;
        p1.y += vec.y;
        p2.x += vec.x;
        p2.y += vec.y;

        vec.x = ((p1.x + p2.x) / 2f);
        vec.y = ((p1.y + p2.y) / 2f);

        return vec;



    }

    static Point2f kolmy(Point2f a) {
        a.x = -a.x;
        float pom = a.x;
        a.x = a.y;
        a.y = pom;
        return a;
    }

    static float velikost(Point2f a) {
        return ((float) Math.sqrt(a.x * a.x + a.y * a.y));
    }

    static Point2f normalizovany(BufferedImage i3, Point2f p1, Point2f p2, float n) {
//        FloorCanvas fc = new FloorCanvas(i3);
        Point2f a = new Point2f(p2.x - p1.x, p2.y - p1.y);
        float vel = velikost(a);

        float k = n / vel;

        a.x *= k;
        a.y *= k;



        return a;

    }

    static float explosivePF(BufferedImage i3) {

        FloorCanvas fc = new FloorCanvas(i3);
        HeatMap hm = HeatMap.getHeatMap(i3, HeatMap.FUNC_HM10);

        fc.setColor(Color.GREEN);

        Point2f s0 = new Point2f(0f, 0.5f);
        Point2f s1 = new Point2f(0f, 0.5f);
        Point2f s2 = new Point2f(0f, 0.5f);
        Point2f le0 = new Point2f();
        Point2f re0 = new Point2f();

        float dx = 0;
        int c = 0;

        for (float dist = 0.8f; dist < 2.0; dist += 0.03) {
//            for (lw=s2.x; ; lw-=0.03) {
//                try {
//                    if (hm.getHeat(lw, dist)<0.5) break;
//                } catch (Exception ex) {
//                    break;
//                }
//            }
//
//            le0.y = dist;
//            le0.x = lw;
//
//            for (rw=s2.x; ; rw+=0.03) {
//                try {
//                    if (hm.getHeat(rw, dist)<0.5) break;
//                } catch (Exception ex) {
//                    break;
//                }
//            }
//
//            re0.y = dist;
//            re0.x = rw;
//
//            s2 = new Point2f((lw+rw)/2, dist);
//
//
//            dx = (dx*c + s2.x)/(c+1);
//            c++;
//
//            s1.x = (s0.x+s1.x+s2.x)/3;
//
//            if ((rw-lw)>1) fc.setColor(Color.RED);
//            else fc.setColor(Color.GREEN);
//
//            fc.drawLine(le0, re0);
//            fc.drawLine(s0, s1);
//
//
//
//            s0 = s1;
//            s1 = s2;
//
//
//            fc.drawLine(rw0, rw);
//            sw0 = sw;
//            lw0 = lw;
//            rw0 = rw;
        }

        fc.setColor(Color.ORANGE);

        fc.drawLine(new Point2f(), s0);

        return s0.x;

    }

    static float paintCrossRoad(BufferedImage i3) {

        FloorCanvas fc = new FloorCanvas(i3);
        HeatMap hm = HeatMap.getHeatMap(i3, HeatMap.FUNC_HM10);

        fc.setColor(Color.GREEN);

        Point2f s0 = new Point2f(0f, 0.5f);
        Point2f s1 = new Point2f(0f, 0.5f);
        Point2f s2 = new Point2f(0f, 0.5f);
        Point2f le0 = new Point2f();
        Point2f re0 = new Point2f();

        float dx = 0;
        int c = 0;

        for (float dist = 0.8f; dist < 3.0; dist += 0.03) {



            float lw, rw;

            for (lw = s2.x;; lw -= 0.03) {
                try {
                    if (hm.getHeatFloor(lw, dist) < 0.5) {
                        break;
                    }
                } catch (Exception ex) {
                    break;
                }
            }

            le0.y = dist;
            le0.x = lw;

            for (rw = s2.x;; rw += 0.03) {
                try {
                    if (hm.getHeatFloor(rw, dist) < 0.5) {
                        break;
                    }
                } catch (Exception ex) {
                    break;
                }
            }

            re0.y = dist;
            re0.x = rw;

            s2 = new Point2f((lw + rw) / 2, dist);


            dx = (dx * c + s2.x) / (c + 1);
            c++;

            s1.x = (s0.x + s1.x + s2.x) / 3;

            if ((rw - lw) > 1) {
                fc.setColor(Color.RED);
            } else {
                fc.setColor(Color.GREEN);
            }

            fc.drawLine(le0, re0);
            fc.drawLine(s0, s1);



            s0 = s1;
            s1 = s2;



//            fc.drawLine(rw0, rw);

//            sw0 = sw;
//            lw0 = lw;
//            rw0 = rw;

        }

        fc.setColor(Color.ORANGE);

        fc.drawLine(new Point2f(), s0);

        return s0.x / 2;

    }
    static private boolean crossroad = false;

    public static float paintCrossRoad2(BufferedImage i3, int side) {

        FloorCanvas fc = new FloorCanvas(i3);
        HeatMap hm = HeatMap.getHeatMap(i3, HeatMap.FUNC_HM10);

        fc.setColor(Color.GREEN);

        Point2f s0 = new Point2f(0f, 0.5f);
        Point2f s1 = new Point2f(0f, 0.5f);
        Point2f s2 = new Point2f(0f, 0.5f);
        Point2f le0 = new Point2f();
        Point2f re0 = new Point2f();

//        extreme width
        int exw = 0;

        for (float dist = 0.8f; dist < 4.0; dist += 0.05) {

            float lw, rw;

            for (lw = s2.x;; lw -= 0.05) {
                try {
                    if ((hm.getHeatFloor(lw, dist) < 0.5) && (hm.getHeatFloor(lw - 0.05f, dist) < 0.5)) {
                        break;
                    }
                } catch (Exception ex) {
                    break;
                }
            }

            le0.y = dist;
            le0.x = lw;

            for (rw = s2.x;; rw += 0.05) {
                try {
                    if ((hm.getHeatFloor(rw, dist) < 0.5) && (hm.getHeatFloor(rw + 0.05f, dist) < 0.5)) {
                        break;
                    }
                } catch (Exception ex) {
                    break;
                }
            }

            re0.y = dist;
            re0.x = rw;

            s2 = new Point2f((rw + lw) / 2f, dist);

            s1.x = (s0.x + s1.x + s2.x) / 3;

            if ((rw - lw) > 2.75) {
                fc.setColor(Color.RED);
                exw++;
                if (exw > 6) {
                    crossroad = true;
                    System.out.println("krizovatka jak svine");
                }
            } else {
                fc.setColor(Color.GREEN);
                exw = 0;
            }

            fc.drawLine(le0, re0);
            fc.drawLine(s0, s1);



            s0 = s1;
            s1 = s2;



//            fc.drawLine(rw0, rw);

//            sw0 = sw;
//            lw0 = lw;
//            rw0 = rw;

        }



        le0.x = Math.min(s0.x, le0.x + 1f);
        re0.x = Math.max(s0.x, re0.x - 1f);
        fc.setColor(Color.BLUE);
        fc.drawLine(new Point2f(), le0);
        fc.setColor(Color.RED);
        fc.drawLine(new Point2f(), re0);
        fc.setColor(Color.ORANGE);
        fc.drawLine(new Point2f(), s0);

        switch (side) {
            case -1:
                return le0.x;
            case 1:
                return re0.x;
            case 0:
                return s0.x;
            default:
                return 0;
        }


    }

    public boolean crossroadDetected() {
        return crossroad;
    }
//        public float paintTrails(BufferedImage i3, float steer) {
//
//        if (Math.abs(steer)<0.001) {
//            return paintTrailsStraight(i3);
//        }
//
//        Graphics2D g2 = (Graphics2D)i3.getGraphics();
//        g2.setColor(Color.CYAN);
//
//        int iw = i3.getWidth(), ih = i3.getHeight();
//
//        FloorProjection fp = new FloorProjection();
//
//
//        Point2f sw = new Point2f();
//        Point2f lw = new Point2f();
//        Point2f rw = new Point2f();
//        Point2f sw0 = new Point2f();
//
//        float R = (carLength/(float)Math.tan(steer));
//        float lR = new Vector2f(R+0.3f, carLength).length();
//        float rR = new Vector2f(R-0.3f, carLength).length();
//
//        g2.setColor(Color.CYAN);
//
//        Point sp1 = fp.getPoint(sw0.x, sw0.y, iw, ih);
//        Point lp1 = fp.getPoint(lR-R, sw0.y, iw, ih);
//        Point rp1 = fp.getPoint(rR-R, sw0.y, iw, ih);
//
//        for (float dist = 0.1f; dist<15; dist+=0.05) {
//
//            float fi = dist/R;
//
//            sw.x=R-R*(float)Math.cos(fi);
//            sw.y=R*(float)Math.sin(fi);
//
//            Point sp2 = fp.getPoint(sw.x, sw.y, iw, ih);
//            Point lp2 = fp.getPoint(lR-R+lR-lR*(float)Math.cos(fi), lR*(float)Math.sin(fi), iw, ih);
//            Point rp2 = fp.getPoint(rR-R+rR-rR*(float)Math.cos(fi), rR*(float)Math.sin(fi), iw, ih);
//
//            try {
//                int h = 0xFFFFFF&i3.getRGB(sp2.x, sp2.y);
//                if (h==0) g2.setColor(Color.PINK);
//
//            } catch (Exception ex) {}
//
//            g2.drawLine(sp1.x, sp1.y, sp2.x, sp2.y);
//            g2.drawLine(lp1.x, lp1.y, lp2.x, lp2.y);
//            g2.drawLine(rp1.x, rp1.y, rp2.x, rp2.y);
//
//            sw0.x = sw.x;
//            sw0.y = sw.y;
//
//            sp1 = sp2;
//            lp1 = lp2;
//            rp1 = rp2;
//
//        }
//
//        return 1;
//
//    }
//    public void findTrails(BufferedImage hm) {
//
//        Graphics2D g2 = (Graphics2D)hm.getGraphics();
//        g2.setColor(Color.WHITE);
//
//        int x = 150;
//        int y = 200;
//
//        while ((y>0)&&(getHeat(x, --y)>100)) {
//
//            g2.drawLine(x, y, x, y-=20);
//
//        }
//
//
//
//    }
//    public void paintTrails(BufferedImage i3) {
//
//        Graphics2D g2 = (Graphics2D)i3.getGraphics();
//        g2.setColor(Color.CYAN);
//
//        int iw = i3.getWidth(), ih = i3.getHeight();
//
//        float fx1 = 0;
//        float fy = 0;
//
//        FloorProjection fp = new FloorProjection();
//
//        for (float steer = -0.025f; steer<0.02f; steer+=0.01) {
////        for (float steer = -0.205f; steer<0.2f; steer+=0.1) {
//
//        float r = /*wheelSpan/2 + */(carLength/(float)Math.tan(steer));
//// if R==0 TODO straight ride
//
//        g2.setColor(Color.CYAN);
//
//        Point p1 = fp.getPoint(fx1, fy, iw, ih);
//        for (int wheel = -1; wheel<2; wheel+=2) {
//        float R = r+(wheel*wheelSpan/2);
//        float fx = fx1+(wheel*wheelSpan/2);
//        for (float dist = 0; dist<15; dist+=0.05) {
//
//
//               float fi = dist/R;
//
////                fx+=R-R*(float)Math.cos(fi);
////                fy+=R*(float)Math.sin(fi);
//
//                Point p2 = fp.getPoint(fx+R-R*(float)Math.cos(fi), fy+R*(float)Math.sin(fi), iw, ih);
//
//                try {
//                    int h = 0xFF0000&i3.getRGB(p2.x, p2.y);
//                    if (h==0) g2.setColor(Color.PINK);
//                } catch (Exception ex) {
//
//                }
//
//                g2.drawLine(p1.x, p1.y, p2.x, p2.y);
//
//                p1 = p2;
//
//            }
//        }
//        }
//
//    }
}
