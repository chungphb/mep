package genetic_algorithm;

import general.Model;
import general.Point;
import general.Objective;
import general.Obstacle;
import general.Sensor;
import java.util.ArrayList;
import java.util.HashMap;

public class Individual {
    private int lchrom; 
    public double[] chrom;
    public Point[] path;
    private double fitness;
    
    public Individual() {
        lchrom = Model.N - 1;
        chrom = new double[lchrom];
        fitness = 0;
        
        path = new Point[lchrom + 2];
        for(int i = 0; i < path.length; i++) {
            if(i == 0)              path[i] = Model.pSource;
            else if(i == Model.N)   path[i] = Model.pDestination;
            else {
                path[i] = new Point(0, 0);
                path[i].x = i*Model.W*1.0/Model.N;
            }
        }
    }
    
    public void heuristicInit(int k) {
        Heuristic.heuristic(this, k);
    }
    
    public void randomInit() {
        int restart = 0;
        for(int i = 0; i < size(); i++) {
            double allele = Math.random()*Model.H;
            setGene(i, allele);
            
            Point p = new Point(path[i].x, path[i].y);
            Point q = new Point(path[i + 1].x, path[i + 1].y);
            
            double dy = Model.H*1.0/Model.N;
            double cndAllele1 = allele;                                         // First candidate
            double cndAllele2 = allele;                                         // Second candidate
            while(!Constraint.isFeasible(p, q) && (cndAllele1 >= 0 || cndAllele2 <= Model.H)) {
                cndAllele1 -= dy;                                               
                cndAllele2 += dy;                                               
                if(cndAllele1 > 0) {
                    setGene(i, cndAllele1);
                    q = new Point(path[i + 1].x, path[i + 1].y);
                    if(Constraint.isFeasible(p, q)) break;
                }
                
                if(cndAllele2 < Model.H) {
                    setGene(i, cndAllele2);
                    q = new Point(path[i + 1].x, path[i + 1].y);
                    if(Constraint.isFeasible(p, q)) break;
                }
            }
            
            if(cndAllele1 < 0 && cndAllele2 > Model.H) {
                i -= 2;                                                         // Reassign the previous gene
                restart++;                                                      
                if(restart == 512) {                                            // Restart the entire chromosome 
                    restart = 0;
                    i = -1;
                }
            }
        }
    }
    
    public double getGene(int index) {
        return chrom[index];
    }
    
    public void setGene(int index, double value) {
        chrom[index] = value;
        path[index + 1].y = value;
        fitness = 0; 
    }

    public boolean checkGene(int index) {
        Point p = path[index];
        Point q = path[index + 1];
        Point r = path[index + 2];
        return Constraint.isFeasible(p, q) && Constraint.isFeasible(q, r);
    }
       
    public double getFitness() {
        if(fitness == 0)
            fitness = Objective.getObjective(this.path);
        return fitness;
    }
     
    public int size() {
        return lchrom;
    }
    
    public void duplicate(Individual indiv) {
        for(int i = 0; i < indiv.size(); i++)
            setGene(i, indiv.getGene(i));
    }
    
    @Override
    public String toString() {
        String s = "Individual: ";
        for(int i = 0; i < size() + 2; i++) 
            s += "\n\t" + path[i];
        return s;
    }
}
