package genetic_algorithm;

import general.Edge;
import general.Model;
import general.Obstacle;
import general.Point;
import general.Sensor;
import genetic_algorithm.Population;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Drawing extends JPanel {
    Population pop;
    
    public Drawing(Population pop) {
        this.pop = pop;
    }
    
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
            ImageIO.write(imagebuf,"png", new File("C:\\Users\\96chi\\Documents\\NetBeansProjects\\wsn\\result\\" + index + ".png"));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("error");
        }
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D)g;
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.drawRect(0, 0, Model.W, Model.H);
        
        for(Obstacle obsta: Model.oList)
            obsta.draw(g2D);
        
        for(Sensor sensor: Model.sList) 
            sensor.draw(g2D);
        
        for(Individual indiv: pop.indivs) {
            Point[] path = indiv.path;
            for(int i = 0; i < indiv.size() - 1; i++) {
                Point p = path[i];
                Point q = path[i+1];
                Edge e = new Edge(p, q);
                e.draw(g2D, Color.red, 1);
            }
        }
    }
}
