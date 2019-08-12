package grid_based_algorithm;

import general.Edge;
import general.Model;
import general.Point;
import general.Sensor;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.PriorityQueue;

public class LocalPathSearch {
    public Graph g;                    // The graph is constructed to find the local path 
    public FloydWarshall fw;    
    public ArrayList<Edge> path;       
    
    public LocalPathSearch(ArrayList<Grid> globalPath) {
        g = buildGraph(globalPath);
        fw = new FloydWarshall(g);
        path = new ArrayList<>();
    }
    
    public Graph buildGraph(ArrayList<Grid> globalPath) {
        Graph graph = new Graph();
        
        // Find the minimum grid
        double lu = Model.W;
        for(Grid grid: globalPath)
            if(grid.width < lu)
                lu = grid.width;
        
        // Adding source point and destination point to graph
        graph.vList.add(Model.pSource);
        graph.vList.add(Model.pDestination);
       
        for(int i = 0; i < globalPath.size(); i++) {
            Grid gCur = globalPath.get(i);                          // The current grid
            Grid gPrev = (i > 0)? globalPath.get(i - 1): null;      // The previous grid
            double x = gCur.botleft.x;                              // x-coordinate of the current grid
            double y = gCur.botleft.y;                              // y-coordinate of the current gri
            double l = gCur.width;                                  // Width of the coordinate
            int M = (int)(Math.log(l/lu)/Math.log(2)) + 1;          // Number of interval in each edge
            double h = l/M;                                         // Length of the segment in each edge
             
            // Constructing vertices and edges for the new graph
            for(int j = 0; j < M + 1; j++) {
                gCur.vList.add(new Point(x, y + j*h));
                gCur.vList.add(new Point(x + l, y + j*h));
                gCur.vList.add(new Point(x + j*h, y));
                gCur.vList.add(new Point(x + j*h, y + l));
            }
            graph.vList.addAll(gCur.vList);
            
            Iterator it = gCur.vList.iterator();
            while(it.hasNext()) {
                Point P = (Point)it.next();
                Iterator jt = gCur.vList.iterator();
                while(jt.hasNext()) {
                    Point Q = (Point)jt.next();
                    if((P.x != Q.x) && (P.y != Q.y))    
                        graph.eList.add(new Edge(P, Q));
                }
            }
                       
            // Considering special cases. Note: We travel backward from the destination grid to the source grid
            if(i == 0) continue;                                            // If the considered grid is the first grid
            if(i == 1) gPrev.vList.add(Model.pDestination);                 // If the considered grid is the second grid
            if(i == globalPath.size() - 1) gCur.vList.add(Model.pSource);   // If the considered grid is the last grid    
            
            // Adding special edges in case two grids are neighbors
            HashSet<Point> vList = new HashSet<>();
            vList.addAll(gCur.vList);
            vList.addAll(gPrev.vList);
            
            it = vList.iterator();
            while(it.hasNext()) {
                Point P = (Point)it.next();
                
                PriorityQueue<Point> xList = new PriorityQueue<>(10, new Comparator<Point>(){
                    @Override
                    public int compare(Point o1, Point o2) {
                        if(o1.y < o2.y)         return  -1;
                        else if(o1.y > o2.y)    return 1;
                        else                    return 0;
                    }
                });
                PriorityQueue<Point> yList = new PriorityQueue<>(10, new Comparator<Point>(){
                    @Override
                    public int compare(Point o1, Point o2) {
                        if(o1.x < o2.x)         return  -1;
                        else if(o1.x > o2.x)    return 1;
                        else                    return 0;
                    }
                });
                xList.add(P);
                yList.add(P);

                Iterator jt = vList.iterator();
                while(jt.hasNext()) {
                    Point Q = (Point)jt.next();
                    if(!P.equals(Q))
                        if(P.x == Q.x)          xList.add(Q);
                        else if (P.y == Q.y)    yList.add(Q);
                }
                
                int size = xList.size();
                for(int k = 0; k < size - 1; k++) {
                    Point A = xList.poll();
                    Point B = xList.peek();
                    graph.eList.add(new Edge(A, B));
                }
                
                size = yList.size();
                for(int k = 0; k < size - 1; k++) {
                    Point A = yList.poll();
                    Point B = yList.peek();
                    graph.eList.add(new Edge(A, B));
                }
            }
        }
        System.out.println();
        return graph;
    }    
    
    public void localSearch() {
        fw.search(g);
        path = fw.path;
    }
    
    public double getObjective() {
        double obj = 0;
        for(Edge e: path)
            obj += e.getExplosure();
        return obj;
    }
}
