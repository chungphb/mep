package grid_based_algorithm;

import general.Edge;
import general.Model;
import general.Point;
import java.util.ArrayList;

public class Algorithm {
    public GlobalPathSearch glPathSearch;
    public LocalPathSearch lcPathSearch;
    public ArrayList<Grid> globalPath;
    public ArrayList<Edge> localPath;
    public double obj;
    
    public void run() {
        Grid orgGrid = new Grid(new Point(0, 0), Model.W, Model.H);
        ACD.acd(orgGrid, Model.n);
        
        glPathSearch = new GlobalPathSearch();
        glPathSearch.init();
        glPathSearch.globalSearch();
        globalPath = glPathSearch.globalPath;
        
        lcPathSearch = new LocalPathSearch(globalPath);
        lcPathSearch.localSearch();
        localPath = lcPathSearch.path;
        
        obj = lcPathSearch.getObjective();
    }
}
