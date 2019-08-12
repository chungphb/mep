package general;

import grid_based_algorithm.Grid;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class Model {
    /* This class included the input of the problem */
    public static int W;                                // W = width of ROI
    public static int H;                                // H = height of ROI
    public static int N;                                // N = number of segment
    public static Point pSource;                        // pSource = source point
    public static Point pDestination;                   // pDestination = destination point
 
    public static ArrayList<Obstacle> oList;            // Obstacle list
    public static ArrayList<Sensor> sList;              // Sensor list

    /* Genetic Algorithm parameters */
    public static int popSize = 100;                    // Population size
    public static double pCross = 0.8;                  // Crossover rate
    public static double pMutation = 0.05;              // Mutation rate
    public static boolean elitism = true;               // True if the fittest individual is saved
    
    /* Local Search parameters */
    public static int maxItr = 100;                     // Number of LS iteration 
    public static double deltaLS = Math.pow(10, -15);   // Unknown
    public static double initRho = 0.2;                 // Initialize rho
    public static double maxSuccs = 5;                  // Maximum number of successful update
    public static double maxFails = 3;                  // Maximum number of failed update 
    
    /* Grid-based Algorithm parameters */
    public static int n;
    public static ArrayList<Grid> gList;
    
    /* Some parameters*/
    
    public Model(int W, int H, int n, Point pSource, Point pDestination) {
        Model.W = W;
        Model.H = H;
        Model.N = (int) Math.pow(2, n);
        Model.pSource = pSource;
        Model.pDestination = pDestination;
        Model.oList = new ArrayList<>();
        Model.sList = new ArrayList<>();
        Model.n = n;
        Model.gList = new ArrayList<>();
    }
}