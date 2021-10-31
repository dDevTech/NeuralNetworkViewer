package ai.geneticalgorithm;


public class GeneticAlgorithm {
    public static int count=0;
    public static int innovationCount=0;
    public static int generateIdNumber(){
        count++;
        return count-1;
    }
    public static int generateInnovationNumber(){
        innovationCount++;
        return innovationCount-1;
    }

}
