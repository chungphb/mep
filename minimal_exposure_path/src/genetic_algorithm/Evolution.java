package genetic_algorithm;

import general.Model;
import general.Vector;
import java.util.Random;

public class Evolution {
    static Random r = new Random();
    
    public static Population evolve(Population pop) {
        Population newPop = new Population(false);
        
        // Save the fittest individual
        if(Model.elitism) {
            Individual fittest = pop.getFittest();
            for(int i = 0; i < 1; i++)
                localSearch(fittest);
            pop.removeIndividual(fittest);
            newPop.saveIndividual(fittest);
        }
        
        // Parent selection         
        Individual parents[] = negativeAssortativeMating(pop);
        
        // Crossover
        if(r.nextDouble() < Model.pCross) {
            Individual[] child = crossover(parents[0], parents[1]);
            if(child != null) {
                pop.saveIndividual(child[0]);
                pop.saveIndividual(child[1]);
            }
        }
        
        // Mutation
        for(int i = 0; i < pop.size(); i++) {
            Individual indiv = pop.getIndividual(i);
            Individual mutatedIndiv = mutate(indiv);
            if(mutatedIndiv != null) {
                pop.removeIndividual(indiv);
                pop.saveIndividual(mutatedIndiv);
            }
        }
        
        // Survivor selection
        pop.sortIndividual();
        for(int i = 0; i < Model.popSize - 1; i++)
            newPop.saveIndividual(pop.getIndividual(i));
        
        return newPop;
    }
    
    private static Individual[] negativeAssortativeMating(Population pop){
        Individual[] mates = new Individual[2];    
        Individual firstMate = pop.getIndividual(r.nextInt(pop.size()));
        Individual bestMate = new Individual();
        double maxDist = -1;
        
        mates[0] = firstMate;
        for(int i = 1; i < 4; i++) {
            Individual secondMate = pop.getIndividual(r.nextInt(pop.size()));
            double dist = distance(firstMate, secondMate);
            if(dist > maxDist) {
                maxDist = dist;
                bestMate = secondMate;
            }
        }
        mates[1] = bestMate;
        return mates;
    }
    
    private static double distance(Individual indiv1, Individual indiv2){
        double dist = 0;
        for(int i = 0; i < indiv1.size(); i++) {
            double d = indiv1.getGene(i) - indiv2.getGene(i);
            dist += d*d;
        }
        return Math.sqrt(dist);
    }
    
    private static Individual[] crossover(Individual parent1, Individual parent2) {
        Individual child1 = new Individual();
        Individual child2 = new Individual();
        int size = parent1.size();
        int nOutTry = 50;                           // number outer trials     
        int nInTry = 50;                            // number inner trials
        for(int k = 0; k < nOutTry; k++) {
            int icross = r.nextInt(size);            
            
            for(int i = 0; i < icross - 1; i++) {
                child1.setGene(i, parent1.getGene(i));
                child2.setGene(i, parent2.getGene(i));
            }
            
            for(int i = icross + 1; i < size; i++) {
                child1.setGene(i, parent2.getGene(i));
                child2.setGene(i, parent1.getGene(i));
            }
            
            double allele1 = parent1.getGene(icross);
            double allele2 = parent2.getGene(icross);
            for(int i = 0; i < nInTry; i++) {
                double alpha = r.nextDouble();
                double cndAllele1 = alpha*allele1 + (1 - alpha)*allele2;
                double cndAllele2 = (1 - alpha)*allele1 + alpha*allele2;
                child1.setGene(icross, cndAllele1);
                child2.setGene(icross, cndAllele2);
                
                if(!child1.checkGene(icross)) {
                    Individual indiv = repairFunction(child1, icross);
                    if(indiv == null)   continue;
                    child1 = indiv;                        
                }
                
                if(!child2.checkGene(icross)) {
                    Individual indiv = repairFunction(child2, icross);
                    if(indiv == null)   continue;
                    child2 = indiv;   
                }  
                
                return new Individual[]{child1, child2};
            }
        }
        return null;
    }
    
    private static Individual mutate(Individual indiv) {
        Individual mutatedIndiv = new Individual();
        mutatedIndiv.duplicate(indiv);
        int size = indiv.size();
        for(int i = 0; i < size; i++) 
            if(r.nextDouble() < Model.pMutation){     
                double allele = r.nextDouble()*Model.H;
                mutatedIndiv.setGene(i, allele);
                if(mutatedIndiv.checkGene(i))   return mutatedIndiv;
                else {
                    Individual repairedIndiv = repairFunction(mutatedIndiv, i);
                    if(repairedIndiv == null)  return null;
                    else mutatedIndiv = repairedIndiv;
                }
            }
        return mutatedIndiv;
    }
    
    public static void localSearch(Individual indiv) {
        Individual indiv1 = new Individual();
        Individual indiv2 = new Individual();
        int size = indiv.size();
        double[] b = new double[size];
        double[] rho = new double[size];
        int succ = 0;
        int fail = 0;
        double curFit = indiv.getFitness();
        
        for(int i = 0; i < size; i++) {
            b[i] = 0;
            rho[i] = Model.initRho;
        }
        
        int maxItr = Model.maxItr;
        double deltaLS = Model.deltaLS;
        for(int i = 0; i < maxItr && Vector.isGreaterThan(rho, deltaLS); i++){
            double[] d = new double[size];
            for(int j = 0; j < size; j++) {
                d[j] = r.nextGaussian()*rho[j] + b[j];
                indiv1.setGene(j, indiv.getGene(j) - d[j]);
                indiv2.setGene(j, indiv.getGene(j) + d[j]);
            }
            
            if(indiv1.getFitness() < indiv.getFitness() && Constraint.isFeasible(indiv1)){
                indiv.duplicate(indiv1);
                succ++;
                fail = 0;
                b = Vector.add(Vector.mul(b, 0.4), Vector.mul(d, 0.2));         // b = 0.4b + 0.2d
            } else if (indiv2.getFitness() < indiv.getFitness() && Constraint.isFeasible(indiv2)){
                indiv.duplicate(indiv2);
                succ++;
                fail = 0;
                b = Vector.add(b, Vector.mul(d, -0.4));                         // b = b - 0.4*d;
            } else{
                fail++;
                succ = 0;
                b = Vector.mul(b, 0.5);                                         // b = 0.5*b
            } 
            if(succ == Model.maxSuccs){
                rho = Vector.mul(rho, 2);                                       // rho = 2*rho
                succ = 0;
            }
            if(fail == Model.maxFails){
                rho = Vector.mul(rho, 0.5);                                     // rho = 0.5*rho
                fail = 0;
            }
        }
    }
    
    public static Individual repairFunction(Individual indiv, int locus) {
        Individual indiv1 = new Individual();
        indiv1.duplicate(indiv);
        Individual indiv2 = new Individual();
        indiv2.duplicate(indiv);
        
        double dy = Model.H*1.0/(2*Model.N);
        double y = indiv.getGene(locus);
        int ntry = 20;
        for(int i = 1; i <= ntry; i++){
            if(y + i*dy <= 512) {
                indiv1.setGene(locus, y + i*dy);
                if(indiv1.checkGene(locus)) return indiv1;
            }
            if(y - i*dy >= 0) {
                indiv2.setGene(locus, y - i*dy);
                if(indiv2.checkGene(locus)) return indiv2;
            }
        }
        return null;
    }
}
