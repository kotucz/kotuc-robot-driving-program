package videogame;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import vision.objects.ImageObject;

/**
 *
 * @author Kotuc
 */
public class Board {    

    private List<Monster> monsters = new ArrayList<Monster>();
    int score = 0;
        
    public Board() {
        monsters.add(new Monster(this));
    }
    
    int width;
    int height;
    long startTime;
    long endTime;

    public void moves() {
        for (Iterator<Monster> iter = monsters.iterator(); iter.hasNext();) {
            Monster monster = iter.next();
            if (monster.isOut()) {
                iter.remove();
                score += 100;
                System.out.println("score: "+score);
                endTime += 3000;

                if (Math.random()>0.9) {
//                    diff++;
                }

                diff = 1+(int)Math.log(1+(score/100));  
//                diff = 1+(score/1000);                
                
            } else {
                monster.move();
            }
        }
        
        if (monsters.size()<diff) {
            if (Math.random()<0.9) {
                addEnemy();
            }
        }
        
//        
//        if (Math.random()>(100.0-(score/100.0))) {
////        if (Math.random()>((100.0-Math.log(score/100.0))/100.0)) {
//            addEnemy();
//        }
        
        
    }

    public long getRunTime() {
        return System.currentTimeMillis()-startTime;
    }
    
    int diff = 1;
       
    public void addEnemy() {
        Monster newMonst = new Monster(this);
        newMonst.x = (int) (Math.round(Math.random()) * width);
        newMonst.y = (int) (Math.random() * height);
        monsters.add(newMonst);

    }

    public void paintMonsters(Graphics g) {
        for (Monster monster : monsters) {
            monster.paint(g);
        }
    }

    public void attack(int x, int y) {
        for (Monster monster : monsters) {
            monster.attack(x, y);
        }
    }

    public void attack(ImageObject io) {

        for (Monster monster : monsters) {

            monster.attack(io);
        }
    }
}
