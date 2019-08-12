package genetic_algorithm;

import general.Model;
import java.util.ArrayList;

public class Population {
    ArrayList<Individual> indivs = new ArrayList<>();;
    
    public Population(boolean initialise) {
        double p = 70.0/100*Model.popSize;
        if(initialise) {
            for(int i = 0; i < (int)p; i++) {
                Individual indiv = new Individual();
                indiv.heuristicInit(Model.N/16);
                indivs.add(indiv);
            }
            for(int i = (int)p; i < Model.popSize; i++) {
                Individual indiv = new Individual();
                indiv.randomInit();
                indivs.add(indiv);
            }
        }
    }
    
    public int size(){
        return indivs.size();
    } 
        
    public Individual getIndividual(int id) {
        return indivs.get(id);
    }
    
    public void setIndividual(int index, Individual indiv) {
        indivs.set(index, indiv);
    }
    
    public void saveIndividual(Individual indiv) {
        indivs.add(indiv);
    }
    
    public void removeIndividual(Individual indiv) {
        indivs.remove(indiv);
    }
    
    public void duplicate(Population pop) {
        for(int i = 0; i < pop.size(); i++){
            Individual indiv = new Individual();
            indiv.duplicate(pop.getIndividual(i));
            indivs.add(indiv);
        }
    }
    
    public void sortIndividual() {
        quickSort(0, size() - 1);
    }
    
    public void quickSort(int L, int R) {
        if(L < R){
            int M = partition(L, R);
            quickSort(L, M - 1);
            quickSort(M + 1, R);
        }        
    }
    
    public int partition(int L, int R) {
        double pivot = getIndividual(R).getFitness();
        int i = L - 1;
        for(int j = L; j < R; j++) {
            if(getIndividual(j).getFitness() <= pivot) {
                i++;
                Individual temp = getIndividual(i);
                setIndividual(i, getIndividual(j));
                setIndividual(j, temp);
            }
        }
        Individual temp = getIndividual(i + 1);
        setIndividual(i + 1, getIndividual(R));
        setIndividual(R, temp);
        return i + 1;
    }
    
    public Individual getFittest() {
        Individual fittest = getIndividual(0);
        for(int i = 0; i < indivs.size(); i++) {
            Individual indiv = getIndividual(i);
            if(indiv.getFitness() <= fittest.getFitness())
                fittest = indiv;
        }
        return fittest;
    }
    
    public double[] getSumMinMaxFitness(){
        double sum = 0;
        double min = 100000;
        double max = 0;
        for(int i = 0; i < size(); i++) {
            double fitness = getIndividual(i).getFitness();
            sum += fitness;
            if(fitness < min)
                min = fitness;
            if(fitness > max)
                max = fitness;
        }
        return new double[]{sum, min, max};
    }
    
    @Override
    public String toString() {
        String s = "Population: ";
        for(int i = 0; i < size(); i++)
            s += "\n" + getIndividual(i);
        return s;
    }
}
