import general.Model;
import general.Obstacle;
import general.Point;
import general.Sensor;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JFrame;

enum Algorithm {
    GRID_BASED_ALGORITHM,
    GENETIC_ALGORITHM;
}

public class Test {
    public static Model model;
    public static grid_based_algorithm.Algorithm grid;
    public static genetic_algorithm.Algorithm ga;
    
    public static void main(String args[]) {
        Test test = new Test();
        test.readData("C:\\Users\\96chi\\Documents\\NetBeansProjects\\minimal_exposure_path\\test\\test.txt");
        test.init(Algorithm.GRID_BASED_ALGORITHM);
        grid = new grid_based_algorithm.Algorithm();
        test.run(Algorithm.GRID_BASED_ALGORITHM);
        test.init(Algorithm.GENETIC_ALGORITHM);
        ga = new genetic_algorithm.Algorithm();
        test.run(Algorithm.GENETIC_ALGORITHM);
        test.paint();
    }
    
    public void run(Algorithm algo) {
        if(algo == Algorithm.GRID_BASED_ALGORITHM)
            grid.run();
        else if(algo == Algorithm.GENETIC_ALGORITHM) 
            ga.run();
        print(algo);
    }
    
    public void readData(String fn) {
        try {
            Scanner in = new Scanner(new File(fn));          
            int W = in.nextInt();
            int H = in.nextInt();
            int n = in.nextInt();
            Point pS = new Point(in.nextInt(), in.nextInt());
            Point pD = new Point(in.nextInt(), in.nextInt());
            model = new Model(W, H, n, pS, pD);

            int nObs = in.nextInt();
            for(int i = 0; i < nObs; i++) {
                int nVer = in.nextInt();
                ArrayList<Point> vertices = new ArrayList<>();
                for(int j = 0; j < nVer; j++) {
                    double x = in.nextDouble();
                    double y = in.nextDouble();
                    vertices.add(new Point(x, y));
                }
                Model.oList.add(new Obstacle(vertices));
            }

            int nSensor = in.nextInt();
            for(int i = 0; i < nSensor; i++) {
                Point O = new Point(in.nextInt(), in.nextInt());
                double r = in.nextInt();
                double theta = in.nextInt();
                double alpha = in.nextInt();
                Model.sList.add(new Sensor(O, r, theta, alpha));
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public void init(Algorithm algo) {
        if(algo == Algorithm.GRID_BASED_ALGORITHM) {
            
        } else if(algo == Algorithm.GENETIC_ALGORITHM) {
            Model.popSize = 50;
            Model.pCross = 0.8;
            Model.pMutation = 1.0/(Model.N - 1);
            Model.elitism = true;
            Model.maxItr = 1000;
            Model.deltaLS = Math.pow(10, -10);
            Model.initRho = 0.2;
            Model.maxSuccs = 5;
            Model.maxFails = 3;
        }
    }
    
    public void print(Algorithm algo) {
        if(algo == Algorithm.GRID_BASED_ALGORITHM) {
            System.out.println("GRID-BASED ALGORITHM");
            System.out.println("Objective function value of solution:" + grid.obj);
        } else if(algo == Algorithm.GENETIC_ALGORITHM) {
            System.out.println("HYBRID GENETIC ALGORITHM");
            System.out.println("Objective function value of solution:" + ga.pop.getFittest().getFitness());
        }
    }
    
    public void paint() {
        JFrame frame = new JFrame();
        DrawingBoard panel = new DrawingBoard(grid, ga);
        frame.add(panel);
        frame.setSize(600, 600);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    }
}
