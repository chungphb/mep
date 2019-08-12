package general;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class Obstacle {
    ArrayList<Point> vertices;

    public Obstacle(ArrayList<Point> vertices) {
        this.vertices = vertices;
    }
    
    public Point getVertex(int index) {
        return vertices.get(index);
    }
    
    public int size() {
        return vertices.size();
    }
    
    public void draw(Graphics2D g) {
        int x[] = new int[size()];
        int y[] = new int[size()];
        for(int i = 0; i < size(); i++) {
            x[i] = (int) vertices.get(i).x;
            y[i] = (int) vertices.get(i).y;
        }
//        g.setColor(new Color(56, 56, 56));
//        g.fillPolygon(x, y, size());
        g.setColor(new Color(32, 32, 32));
        g.drawPolygon(x, y, size());
    }
    
    @Override
    public String toString() {
        String s = "Obstacle: ";
        for(int i = 0; i < size(); i++) {
            s += "(" + vertices.get(i).x + ", " + vertices.get(i).y + ")";
            if(i != size() - 1) s += "; ";
        }
        return s;
    }
}