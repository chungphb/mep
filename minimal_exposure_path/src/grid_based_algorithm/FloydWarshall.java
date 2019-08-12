package grid_based_algorithm;

import general.Edge;
import general.Model;
import general.Point;
import java.util.ArrayList;

public class FloydWarshall {
    public Point[] vList;              // Used to access the vertex from vList easily  
    public int n;                      // Number of vertices in the graph g
    public double INF;                 // Infinity
    public double[][] graph, dist;     
    public int[][] pred;               
    public ArrayList<Edge> path;
    
    public FloydWarshall(Graph g) {
        vList = new Point[g.vList.size()];
        n = vList.length;
        g.vList.toArray(vList);
        INF = Double.MAX_VALUE;
        graph = new double[n][n];
        dist = new double[n][n];
        pred = new int[n][n];
        path = new ArrayList<>();
    }
    
    public void init(Graph g) {
        for(int i = 0; i < n; i++)
            for(int j = 0; j < n; j++) {
                if(i == j){
                    graph[i][j] = 0;
                    continue;
                }
                Edge e = new Edge(vList[i], vList[j]);
                if(g.eList.contains(e)) 
                    graph[i][j] = e.getExplosure();
                else
                    graph[i][j] = INF;
                pred[i][j] = -1;
                dist[i][j] = graph[i][j];
            }
    }
    
    public void search(Graph g) {
        init(g);
        for(int k = 0; k < n; k++)
            for(int i = 0; i < n; i++)
                for(int j = 0; j < n; j++)
                    if(dist[i][k] != INF && dist[k][j] != INF && dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                        pred[i][j] = k;
                    }
       
        int iS = 0, iD = 0;
        for(int i = 0; i < n; i++) {
            if(vList[i].equals(Model.pSource)) iS = i;
            if(vList[i].equals(Model.pDestination)) iD = i;
        }
        buildPath(iS, iD);
    }
    
    public void buildPath(int i, int j) {
        if(pred[i][j] == -1) {
            path.add(new Edge(vList[i], vList[j]));
        } else {
            buildPath(i, pred[i][j]);            
            buildPath(pred[i][j], j);
        }
    }
}
