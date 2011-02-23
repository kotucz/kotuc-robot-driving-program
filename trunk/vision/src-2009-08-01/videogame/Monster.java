/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package videogame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import vision.objects.ImageObject;

/**
 *
 * @author Kotuc
 */
public class Monster {

    int x = 250;
    int y = 250;
    int vx;
    int vy;
    int r = 50;
    int health = 100;
    Board board;
    Image image;
    private Image[] images = new Image[]{
        Toolkit.getDefaultToolkit().createImage(getClass().getResource("/monsters/1.png")),
        Toolkit.getDefaultToolkit().createImage(getClass().getResource("/monsters/2.png")),
        Toolkit.getDefaultToolkit().createImage(getClass().getResource("/monsters/3.png")),
        Toolkit.getDefaultToolkit().createImage(getClass().getResource("/monsters/4.png"))
    };

    public Monster(Board board) {
        this.board = board;
        image = images[(int) ((images.length) * Math.random())];
    }

    public boolean isAlive() {
        return health > 0;
    }

    public boolean isOut() {
        return (y > board.height + 100) || (y < -100);
    }

    public void paint(Graphics g) {
        if (isAlive()) {
            g.setColor(Color.YELLOW);

        } else {
            g.setColor(Color.RED);
        }
        g.fillOval(x - r, y - r, 2 * r, 2 * r);
        if (isAlive()) {
            g.drawImage(image, x - r, y - r, 2 * r, 2 * r, null);
        }
        if (isAlive()) {
            g.setColor(Color.RED);
        } else {
            g.setColor(Color.BLACK);
        }
        g.drawString("" + health + "%", x, y);
    }

    public void move() {
        if (isAlive()) {
            vx -= (x - board.width / 2) / 50;
//            vy -= (y - board.height/2)/50;
            vy += 10 * (Math.random() - 0.5);
        } else {
            vy += 3;
            vx = vx * 9 / 10;
        }
        x += vx;
        y += vy;
    }

    public void attack(int x, int y) {

        if (Math.abs(this.x - x) < r && Math.abs(this.y - y) < r) {
            health -= 5;
        }
    }

    public void attack(ImageObject io) {
        System.out.println("attack");
        int xa = Math.max(0, Math.min(io.getMaxX(), x + r) - Math.max(x - r, io.getMinX()));
        int ya = Math.max(0, Math.min(io.getMaxY(), y + r) - Math.max(y - r, io.getMinY()));
        health -= Math.min(xa * ya / 100, 20);
    }
}
