package genetic_algorithm;

import general.Edge;
import general.Model;
import general.Obstacle;
import general.Point;
import grid_based_algorithm.FloydWarshall;
import grid_based_algorithm.Graph;
import java.util.ArrayList;
import java.util.HashSet;

public class Heuristic {
    public static FloydWarshall fw;
    public static Graph g;
    public static double[] x, y;
    public static ArrayList<Edge> path;
    
    public static void heuristic(Individual indiv, int k) {
        init(indiv, k);
        g = buildGraph(indiv, k);
        fw = new FloydWarshall(g);
        fw.search(g);
        path = fw.path;
        for(int i = 0; i < indiv.size(); i++) 
            for(Edge e: path) {
                Point[] vSet = e.cut(e.vertices[0], e.vertices[1]);
                for(int j = 0; j < vSet.length; j++) {
                    if(indiv.getGene(i)==0 && vSet[j].x == indiv.path[i+1].x)
                       indiv.setGene(i, vSet[j].y);
                }
            }
    }
    
    public static void init(Individual indiv, int k) {
        int n = Model.N/k;
        x = new double[n + 1];
        y = new double[n + 1];
        double dx = Model.W*1.0/n;
        double dy = Model.H*1.0/n;
        for(int i = 0; i < n + 1; i++) {
            if(i == 0)
                x[i] = Model.pSource.x;
            else if(i == n)
                x[i] = Model.pDestination.x;
            else {
                x[i] = i*dx;
            }
            y[i] = i*dy;
        }
    }
    
    public static Graph buildGraph(Individual indiv, int k) {
        Graph graph = new Graph();
        int n = Model.N/k;
        ArrayList<Point> preVList = new ArrayList<>();
        for(int i = 0; i < n + 1; i++) {
            ArrayList<Point> curVList = new ArrayList<>();
            if(i == 0)
                curVList.add(Model.pSource);
            else if(i == n)
                curVList.add(Model.pDestination);
            else {
                for(int j = 0; j < n + 1; j++) {
                    Point p = new Point(x[i], y[j]);
                    if(!isInsideObstacle(p))
                        curVList.add(p);
                }
            }
            for(Point p: preVList)
                for(Point q: curVList) {
                    if(Constraint.isFeasible(p, q))
                        graph.eList.add(new Edge(p, q));
                }
            preVList = curVList;
            graph.vList.addAll(curVList);
        }
        return graph;
    }
    
    public static boolean isInsideObstacle(Point p) {
        for(Obstacle obs: Model.oList) 
            if(grid_based_algorithm.ACD.isPointInsideObstacle(obs, p))
                return true;
        return false;
    }
}
