package eurobot.plot;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.vecmath.Color3b;

/**
 *
 * @author Kotuc
 */
public class Graph {

    Color[] cols = new Color[]{Color.black, Color.red, Color.green, Color.blue, Color.cyan, Color.magenta, Color.yellow};
//    Variable time = new Variable(Color.black, "Time");
    List<Variable> vars = new ArrayList<Variable>();

    public Graph() {
//            vars.add(new Variable(Color.black, "Time"));
        vars.add(new Variable(Color.red, "curCnt"));
        vars.add(new Variable(Color.green, "encoder"));
        vars.add(new Variable(Color.yellow, "power"));
        vars.add(new Variable(Color.blue, "curr"));

        vars.add(new Variable(Color.red, "curCnt"));
        vars.add(new Variable(Color.green, "encoder"));
        vars.add(new Variable(Color.yellow, "power"));
        vars.add(new Variable(Color.blue, "curr"));
    }

    void newData(double... vals) {
//        time.update(t);
        if (vals.length != vars.size()) {
            throw new IllegalArgumentException("Length mismatch " + vals.length + " " + vars.size());
        }
        for (int i = 0; i < vals.length; i++) {
            vars.get(i).update(vals[i]);
        }
    }

    class Variable {

        final Color color;
        final String name;
        final List<Double> vals = new ArrayList<Double>();

        public Variable(Color color, String name) {
            this.color = color;
            this.name = name;
        }

        void update(double newval) {
            vals.add(newval);
        }

        public void paint(Graphics2D g) {
            g.setColor(color);
            for (int i = 1; i < vals.size(); i++) {
                g.draw(new Line2D.Double((i - 1), vals.get(i - 1), (i), vals.get(i)));
//                g.draw(new Line2D.Double(time.vals.get(i - 1), vals.get(i - 1), time.vals.get(i - 1), vals.get(i)));
            }
        }
    }

    public void paint(Graphics2D g) {

        for (Variable var : vars) {
            var.paint(g);
        }
    }

    void readCSV(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = reader.readLine();
        // first line with names
        String[] split = line.split(",");
        for (String string : split) {
            vars.add(new Variable(cols[vars.size()], string));
        }
        while ((line = reader.readLine()) != null) {
            parseLine(line);
        }
        reader.close();
    }

    public void parseLine(String line) {
        String[] split = line.split(",");
        for (int i = 0; i < split.length; i++) {
            try {
                vars.get(i).update(Double.valueOf(split[i]));
            } catch (Exception exception) {
                try {
                    vars.get(i).update(0);
                } catch (Exception excefption) {
                }
            }

        }
    }

    void writeCSV(File file) throws IOException {
        CVSWriter writer = new CVSWriter(new FileWriter(file));
        for (Variable variable : vars) {
            writer.writeField(variable.name);
        }
        writer.newLine();
        for (int i = 0; i < vars.get(0).vals.size(); i++) {
            for (Variable variable : vars) {
                writer.writeField(String.valueOf(variable.vals.get(i)));
            }
            writer.newLine();
        }
        writer.close();
    }
}
