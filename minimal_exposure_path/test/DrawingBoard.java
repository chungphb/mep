import general.Point;
import general.Edge;
import general.Model;
import general.Obstacle;
import general.Sensor;
import grid_based_algorithm.Grid;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class DrawingBoard extends JPanel {
    grid_based_algorithm.Algorithm gridAlgo = new grid_based_algorithm.Algorithm();
    genetic_algorithm.Algorithm geneAlgo = new genetic_algorithm.Algorithm();
    
    public void saveImage(int index){
        BufferedImage imagebuf = null;
        try {
            imagebuf = new Robot().createScreenCapture(this.bounds());
        } catch (AWTException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }  
        Graphics2D graphics2D = imagebuf.createGraphics();
        this.paint(graphics2D);
        try {
            ImageIO.write(imagebuf,"png", new File("C:\\Users\\96chi\\Documents\\NetBeansProjects\\minimal_exposure_path\\result" + index + ".png"));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("error");
        }
    }
    
    public DrawingBoard(grid_based_algorithm.Algorithm gridAlgo, genetic_algorithm.Algorithm geneAlgo) {
        this.geneAlgo = geneAlgo;
        this.gridAlgo = gridAlgo;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D)g;
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.drawRect(0, 0, Model.W, Model.H);
        
        for (Grid grid: Model.gList) {
            Color color = grid.color;
            grid.draw(g2D, color);
        }
        
        ArrayList<Grid> globalPath = gridAlgo.globalPath;
        for(Grid grid: globalPath)
            grid.draw(g2D, new Color(225, 225, 225));
        
        for(Obstacle obsta: Model.oList)
            obsta.draw(g2D);

        for(Sensor sensor: Model.sList) 
            sensor.draw(g2D);
   
        for(Edge edge: gridAlgo.lcPathSearch.g.eList)
            edge.draw(g2D, new Color(25, 25, 25), 1);
        
        ArrayList<Edge> path = gridAlgo.lcPathSearch.path;
        for(Edge edge: path)
            edge.draw(g2D, Color.RED, 2);
        
        Point[] fittest = geneAlgo.pop.getFittest().path;
        for(int i = 0; i < fittest.length - 1; i++) {
            Point p = fittest[i];
            Point q = fittest[i+1];
            Edge e = new Edge(p, q);
            e.draw(g2D, Color.BLUE, 2);
        }
    }
    
}

