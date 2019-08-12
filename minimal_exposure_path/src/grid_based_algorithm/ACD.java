package grid_based_algorithm;

import general.Model;
import general.Obstacle;
import genetic_algorithm.Constraint;
import general.Point;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;

public class ACD {
    public static void acd(Grid grid, int n) {
        Color color = assignColor(grid);
        if(color == Color.GRAY) {
            if(n == 0) {
                grid.color = Color.GRAY;
                Model.gList.add(grid);
                return;
            }
            double newWidth = grid.width/2;
            double newHeight = grid.height/2;
            for(int i = 0; i < 2; i++) {
                for(int j = 0; j < 2; j++) {
                    double x = grid.botleft.x + newWidth*i;
                    double y = grid.botleft.y + newHeight*j;
                    Grid newGrid = new Grid(new Point(x, y), newWidth, newHeight);
                    acd(newGrid, n - 1);
                }
            }
        }else {
            grid.color = color;
            Model.gList.add(grid);
        }
    }
    
    public static Color assignColor(Grid grid) {
        if(isObstacleInside(grid))
            return Color.GRAY;
        
        for(Obstacle obs: Model.oList) { 
            int size = obs.size();
            for(int i = 0; i < size; i++) {
                Point A = obs.getVertex(i);
                Point B = (i == size-1)? obs.getVertex(0): obs.getVertex(i+1); 
                if(isCross(grid, A, B))
                    return Color.GRAY;
            }
            if(isGridInside(obs, grid))
                return Color.BLACK;
        }

        return Color.WHITE;
    }
    
    public static boolean isCross(Grid grid, Point A, Point B) {
        Point pBL = grid.botleft;
        Point pTL = new Point(pBL.x, pBL.y + grid.height);
        Point pTR = new Point(pBL.x + grid.width, pBL.y + grid.height);
        Point pBR = new Point(pBL.x + grid.width, pBL.y);
        
        int count = 0;
        if(Constraint.doIntersect(pBL, pTL, A, B))  count++;
        if(Constraint.doIntersect(pTL, pTR, A, B))  count++;
        if(Constraint.doIntersect(pTR, pBR, A, B))  count++;
        if(Constraint.doIntersect(pBR, pBL, A, B))  count++;
        
        switch(count) {
            case 0: 
                return false;
            case 1:
                if(isInside(grid, A) || isInside(grid, B))
                    return true;
                else 
                    return false;
            case 2:
                if(isInside(grid, A) || isInside(grid, B))
                    return true;
                if(isInBorder(grid, A) && isInBorder(grid, B))
                    return true;
                if(Constraint.orientation(A, B, pBL) == 0 || Constraint.orientation(A, B, pTR) == 0 ||
                   Constraint.orientation(A, B, pBR) == 0 || Constraint.orientation(A, B, pTL) == 0)
                    return false;
                return true;
            case 3:
                if((Constraint.orientation(A, B, pBL) == 0 && Constraint.orientation(A, B, pTL) == 0)||
                   (Constraint.orientation(A, B, pTL) == 0 && Constraint.orientation(A, B, pTR) == 0)||
                   (Constraint.orientation(A, B, pTR) == 0 && Constraint.orientation(A, B, pBR) == 0)||
                   (Constraint.orientation(A, B, pBR) == 0 && Constraint.orientation(A, B, pBL) == 0))
                    return false;
            default:
                return true;
        }
    }
    
    public static boolean isInside(Grid grid, Point P) {
        boolean b1 = P.x > grid.botleft.x;
        boolean b2 = P.x < grid.botleft.x + grid.width;
        boolean b3 = P.y > grid.botleft.y;
        boolean b4 = P.y < grid.botleft.y + grid.height;

        return b1 && b2 && b3 && b4;
    }
    
    public static boolean isInBorder(Grid grid, Point P) {
        boolean e1 = P.x == grid.botleft.x;
        boolean e2 = P.x == grid.botleft.x + grid.width;
        boolean e3 = P.y == grid.botleft.y;
        boolean e4 = P.y == grid.botleft.y + grid.height;
        boolean ge1 = P.x >= grid.botleft.x;
        boolean le1 = P.x <= grid.botleft.x + grid.width;
        boolean ge2 = P.y >= grid.botleft.y;
        boolean le2 = P.y <= grid.botleft.y + grid.height;

        return ((e1 || e2) && ge2 && le2) || ((e3 || e4) && ge1 && le1); 
    }
    
    private static boolean isObstacleInside(Grid g) {
        Point bl = g.botleft;
        Point tr = new Point(bl.x + g.width, bl.y + g.height);
                 
        for(Obstacle obs: Model.oList) {
            boolean result = true;
            for(int j = 0; j < obs.size(); j++) {
                Point p = obs.getVertex(j);
                if(p.x < bl.x || p.x > tr.x || p.y < bl.y || p.y > tr.y) {
                    result = false;
                    break;
                }
            }
            if(result) return true;
        }
        
        return false;        
    }

    private static boolean isGridInside(Obstacle obs, Grid grid) {
        Point pBL = grid.botleft;
        Point pTL = new Point(pBL.x, pBL.y + grid.height);
        Point pTR = new Point(pBL.x + grid.width, pBL.y + grid.height);
        Point pBR = new Point(pBL.x + grid.width, pBL.y);
        return isPointInside(obs, pBL) && isPointInside(obs, pTL) &&
               isPointInside(obs, pTR) && isPointInside(obs, pBR);
    }
    
    public static boolean isPointInside(Obstacle obs, Point P) {
        int size = obs.size();
        boolean result = false;    

        for(int i = 0; i < size; i++) {
            Point A = obs.getVertex(i);
            Point B = (i == size-1)? obs.getVertex(0): obs.getVertex(i+1);
            if(Constraint.onSegment(A, P, B) && Constraint.orientation(A, B, P) == 0)
                return true;
                
            if(((A.y > P.y) != (B.y > P.y))&&(P.x < A.x + (P.y - A.y)/(B.y - A.y)*(B.x - A.x)))
                result = !result;
        }
        return result;
    }
}
