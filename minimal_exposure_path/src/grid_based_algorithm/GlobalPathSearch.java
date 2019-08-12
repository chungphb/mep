package grid_based_algorithm;

import general.Model;
import general.Point;
import general.Sensor;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;

public class GlobalPathSearch {
    HashMap<Grid, ArrayList<Grid>> graph;                   // A graph of grids represented by the neighbor lists             
    HashMap<Grid, Double> g, h, C;                          // Mapping grid with g, its heuristic value and its coverage, respectively
    ArrayList<Grid> globalPath;                             // The global path
    PriorityQueue<Grid> open;                               // Used in A* algorithm
    HashSet close;                                          // Used in A* algorithm
    Grid gS, gD;                                            // The source grid and the destination grid
    
    public GlobalPathSearch() {
        graph = new HashMap<>();
        g = new HashMap<>();
        h = new HashMap<>();
        C = new HashMap<>();
        globalPath = new ArrayList<>();
        close = new HashSet();
        open = new PriorityQueue<Grid>(30, new Comparator<Grid>() {
            @Override
            public int compare(Grid o1, Grid o2) {
                if(o1.priority < o2.priority)
                    return -1;
                else if(o1.priority > o2.priority)
                    return 1;
                else 
                    return 0;
            }
        });
    }
    
    public void init() {
        ArrayList<Grid> gList = new ArrayList<>();
        Point pS = Model.pSource;
        Point pD = Model.pDestination;
        
        // List all the white grids
        for(Grid grid: Model.gList)
            if(grid.color == Color.WHITE) 
                gList.add(grid);
        
        for(Grid grid: gList) {
            double x = grid.botleft.x;
            double y = grid.botleft.y;
            Point center = new Point(x + grid.width/2, y + grid.height/2);
            
            // Find gS and gD
            if(x == 0 && (pS.y >= y && pS.y <= y + grid.height)) 
                gS = grid;
            if(x == Model.W - grid.width && (pD.y >= y && pD.y <= y + grid.height)) 
                gD = grid;
                   
            // Calculate C where C[i] is the coverage degree of grid gI
            double cvr = 0;
            for(Sensor s: Model.sList)
                cvr += s.theta/Math.pow(Math.pow(s.O.x - center.x, 2) + Math.pow(s.O.y - center.y, 2), s.alpha/2);
            C.put(grid, new Double(cvr));
        }
        
        // Calculate h where h[i] is the heuristic estimated cost from gI to gD
        for(Grid grid: gList) {
            Double hrs = heuristic(grid);
            h.put(grid, hrs);
        }
           
        // Build graph of grids
        for(int i = 0; i < gList.size(); i++) {
            Grid g1 = gList.get(i);
            ArrayList<Grid> gNeighbors = new ArrayList<>();
            for(int j = 0; j < gList.size(); j++) {
                Grid g2 = gList.get(j);
                if((j != i) && isNeighbor(g1, g2))
                    gNeighbors.add(g2);
              
            }
            graph.put(g1, gNeighbors);
        }          
    }
    
    public void globalSearch() {
        double cost = 0;
        
        open.add(gS);
        g.put(gS, new Double(0));
        Grid gCur = gS;
        
        while(gCur != gD) {
            int id = 0;
            gCur = open.poll();
            close.add(gCur);
            for(Grid gNei: graph.get(gCur)) {
                cost = g.get(gCur) + C.get(gNei);
                if(open.contains(gNei) && cost < g.get(gNei))
                    open.remove(gNei);
                if(close.contains(gNei) && cost < g.get(gNei))
                    close.remove(gNei);
                if(!close.contains(gNei) && !open.contains(gNei)) {
                    if(g.containsKey(gNei))
                        g.replace(gNei, cost);
                    else 
                        g.put(gNei, cost);
                    gNei.priority = g.get(gNei) + h.get(gNei);
                    id++;
                    open.add(gNei);
                    gNei.parent = gCur;
                }
            }
            
        }
        
        while(gCur != null) {
            globalPath.add(gCur);
            gCur = gCur.parent;
        }
    }
    
    public boolean isNeighbor(Grid g1, Grid g2) {       
        double minX1 = g1.botleft.x;
        double maxX1 = g1.botleft.x + g1.width;
        double minX2 = g2.botleft.x;
        double maxX2 = g2.botleft.x + g2.width;
        
        double minY1 = g1.botleft.y;
        double maxY1 = g1.botleft.y + g1.height;
        double minY2 = g2.botleft.y;
        double maxY2 = g2.botleft.y + g2.height;
        
        return  (((maxY1 == minY2)||(maxY2 == minY1))&&(minX2 >= minX1 - g2.width)&&(minX2 <= maxX1))
              ||(((maxX1 == minX2)||(maxX2 == minX1))&&(minY2 >= minY1 - g2.height)&&(minY2 <= maxY1)) ;
    }
    
    public Double heuristic(Grid g) {
        double h = 0;
        Point centerGI = new Point(g.botleft.x + g.width/2, g.botleft.y + g.height/2);
        Point centerGD = new Point(gD.botleft.x + gD.width/2, gD.botleft.y + gD.height/2);
        if(ACD.isCross(g, centerGI, centerGD))
            h += C.get(g);
        return new Double(h);
    }
}
