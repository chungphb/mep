package grid_based_algorithm;

import general.Point;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashSet;

public class Grid {
    public Point botleft;                              // The bottom left point of the grid
    public double width, height;                       // Width and height of the grid
    public HashSet<Point> vList;                       // Set of points are constructed in grid's borders
    public double priority;                            // Unknown
    public Color color;                                // The color of the grid
    public Grid parent;                                // The grid is right before the current grid in the final path
    
    public Grid(Point p, double w, double h) {
        botleft = p;
        width = w;
        height = h;
        vList = new HashSet<>();
    }
    
    public void draw(Graphics2D g, Color c) {
        g.setColor(c);
        g.fillRect((int)botleft.x, (int)botleft.y, (int)width, (int)height);
        g.setColor(Color.BLACK);
        g.drawRect((int)botleft.x, (int)botleft.y, (int)width, (int)height);
    }
}
