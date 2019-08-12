package general;

import java.awt.Color;
import java.awt.Graphics2D;

public class Sensor {
    public Point O;
    public double r;
    public double alpha;
    public double theta;
    
    public Sensor(Point O, double r, double alpha, double theta) {
        this.O = O;
        this.r = r;
        this.alpha = alpha;
        this.theta = theta;
    }
    
    public void draw(Graphics2D g) {
        int n = 10;
        g.setColor(new Color(62, 180, 137));
        g.fillOval((int)(O.x - r), (int)(O.y - r), (int)(2*r), (int)(2*r));
    }
    
    @Override
    public String toString() {
        return "Sensor: O(" + O.x + ";" + O.y + "); r = " + r + "; alpha = " + alpha + "; theta = " + theta;
    }
}
