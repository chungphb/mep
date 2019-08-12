package general;

public class Objective {
    public static double getObjective(Point[] path) {
        double obj1 = 0;
        double obj2 = 0;
        
        for(int i = 1; i < path.length; i++) {
            double d = distance(path[i], path[i-1]);
            obj1 += d*intensity(path[i]);
            obj2 += d;
        }
        return 1000*obj1 + 0*obj2;
    }
    
    public static double intensity(Point p) {
        double I = 0;
        for(Sensor s: Model.sList)
            I += s.theta/Math.pow(distance(s.O, p), s.alpha);
        return I;
    }
    
    public static double distance(Point A, Point B) {
        return Math.sqrt((A.x - B.x)*(A.x - B.x) + (A.y - B.y)*(A.y - B.y));
    }
}
