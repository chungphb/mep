package genetic_algorithm;

import general.Model;
import general.Point;
import general.Obstacle;
import java.util.ArrayList;

public class Constraint {
    public static boolean isFeasible(Individual indiv){
        int size = indiv.path.length;
        
        // Check if any segment crosses the obstacles
        for(int i = 0; i < size - 1; i++)
            if(!isFeasible(indiv.path[i], indiv.path[i+1]))  
                return false;
        
        for(int i = 0; i < indiv.size(); i++)
            if(indiv.getGene(i) < 0 || indiv.getGene(i) > 512) 
                return false;
        
        return true;
    }
    
    public static boolean isFeasible(Point p1, Point q1){
        for(Obstacle obs: Model.oList){
            for(int i = 0; i < obs.size(); i++){
                Point p2 = obs.getVertex(i);
                Point q2 = (i == obs.size() - 1)? obs.getVertex(0): obs.getVertex(i + 1);
                if(doIntersect(p2, q2, p1, q1)) return false;
            }
        }
        return true;
    }
    
    public static boolean doIntersect(Point p1, Point q1, Point p2, Point q2){
        int o1 = orientation(p1, q1, p2);
        int o2 = orientation(p1, q1, q2);
        if(o1 == o2 && o1 != 0) return false;
        
        int o3 = orientation(p2, q2, p1);
        int o4 = orientation(p2, q2, q1);
        if(o3 == o4 && o3 != 0) return false;
        
        if((o1 != o2) && (o3 != o4))            return true;
        if((o1 == 0) && onSegment(p1, p2, q1))  return true;
        if((o2 == 0) && onSegment(p1, q2, q1))  return true;
        if((o3 == 0) && onSegment(p2, p1, q2))  return true;
        if((o4 == 0) && onSegment(p2, q1, q2))  return true;
        
        return false;
    }
    
    public static boolean onSegment(Point p, Point q, Point r){
        return      (q.x <= Math.max(p.x, r.x) && q.x >= Math.min(p.x, r.x)) 
                &&  (q.y <= Math.max(p.y, r.y) && q.y >= Math.min(p.y, r.y));
    }
    
    public static int orientation(Point p, Point q, Point r){
        double val = (q.y - p.y)*(r.x - q.x) - (q.x - p.x)*(r.y - q.y);
        if(val == 0)    return 0;
        else            return (val > 0)? 1: 2;
    }
}
