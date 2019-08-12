package genetic_algorithm;

import general.Model;
import javax.swing.JFrame;

public class Algorithm {
    public Population pop;
    public int maxStable;
    public int maxItrs;
    public double curFittest, prevFittest;
    
    public Algorithm() {
        pop = new Population(true);
        maxStable = 10;
        maxItrs = 200;
        curFittest = pop.getFittest().getFitness();
    }
    
    public void run() {
        int count = 0;
//        JFrame frame = new JFrame();
        for(int i = 1; i < maxItrs && count < maxStable; i++) {
//            Drawing panel = new Drawing(pop);
//            frame.add(panel);
//            frame.setSize(Model.W, Model.H);
//            frame.setVisible(true);
//            panel.saveImage(i);
            
            pop = Evolution.evolve(pop);
            print(i, pop.getFittest().getFitness());
            prevFittest = curFittest;
            curFittest = pop.getFittest().getFitness();
            if(prevFittest != curFittest) count = 0;           // Restart if unstable 
            else count++;
        }
    }
    
    public void print(int gen, double fittest) {
        System.out.println("HybridGenteticAlgorithm::generation = " + gen + ", fittest = " + fittest + ".");
    }
}
