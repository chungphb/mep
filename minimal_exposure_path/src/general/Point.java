package general;

public class Point {
    public double x;
    public double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    @Override
    public String toString() {
        return "Point: (" + x + ", " + y + ")";
    }
    
    @Override
    public boolean equals(Object o) {
        Point p = (Point)o;
        return ((this.x == p.x)&&(this.y == p.y));
    }
    
    @Override 
    public int hashCode() {
        return 100*(int)(x + y);
    }
}