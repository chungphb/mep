package general;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class Edge {
    public Point[] vertices;
    public double exposure;
    
    public Edge(Point A, Point B) {
        vertices = new Point[] {A, B};
    }
    
    public double getExplosure() {
        if(exposure == 0)
            exposure = computeExplosure(vertices[0], vertices[1]);
        return exposure;
    }
    
    public double computeExplosure(Point A, Point B) {
        Point[] path;
        if(A.x != B.x) {
            double dx = Model.W*1.0/Model.N;
            int n = (int)(Math.abs(B.x - A.x)/dx);
            path = new Point[n + 1];
            path[0] = (A.x < B.x)? A: B;
            path[n] = (A.x < B.x)? B: A;
            for(int i = 1; i < n; i++) {
                path[i] = new Point(0, 0);
                path[i].x = Math.min(A.x, B.x) + i*dx;
                path[i].y = A.y + (path[i].x - A.x)*(B.y - A.y)/(B.x - A.x);
            }
        } else {
            double dy = Model.H*1.0/Model.N;
            int n = (int)(Math.abs(B.y - A.y)/dy);
            path = new Point[n + 1];
            path[0] = (A.y < B.y)? A: B;
            path[n] = (A.y < B.y)? B: A;
            for(int i = 1; i < n; i++) {
                path[i] = new Point(0, 0);
                path[i].y = Math.min(A.y, B.y) + i*dy;
                path[i].x = A.x + (path[i].y - A.y)*(B.x - A.x)/(B.y - A.y);
            }
        }        
        return Objective.getObjective(path);
    }
    
    public Point[] cut(Point A, Point B) {
        Point[] vSet;
        double dx = Model.W*1.0/Model.N;
        int n = (int)(Math.abs(B.x - A.x)/dx);
        vSet = new Point[n + 1];
        vSet[0] = (A.x < B.x)? A: B;
        vSet[n] = (A.x < B.x)? B: A;
        for(int i = 1; i < n; i++) {
            vSet[i] = new Point(0, 0);
            vSet[i].x = Math.min(A.x, B.x) + i*dx;
            vSet[i].y = A.y + (vSet[i].x - A.x)*(B.y - A.y)/(B.x - A.x);
        }
        return vSet;
    }
    
    public void draw(Graphics2D g, Color c, int bold) {
        g.setColor(c);
        g.setStroke(new BasicStroke(bold, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.drawLine((int)vertices[0].x, (int)vertices[0].y, (int)vertices[1].x, (int)vertices[1].y);
    }
    
    @Override
    public String toString() {
        return "Edge: (" + vertices[0].x + ";" + vertices[0].y + ") -- (" + vertices[1].x + ";" + vertices[1].y + ")";
    }
    
    @Override
    public boolean equals(Object obj) {
        Edge edge = (Edge)obj;
        return (this.vertices[0].equals(edge.vertices[0]))&&(this.vertices[1].equals(edge.vertices[1])) || 
               (this.vertices[0].equals(edge.vertices[1]))&&(this.vertices[1].equals(edge.vertices[0]));
    }
    
    @Override 
    public int hashCode() {
        return 100*(int)(this.vertices[0].x + this.vertices[0].y + this.vertices[1].x + this.vertices[1].y);
    }
}
