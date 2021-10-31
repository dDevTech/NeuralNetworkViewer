package ai.geneticalgorithm;

import ai.neuralnetwork.architecture.Connection;
import ai.neuralnetwork.architecture.NeuralNetwork;

import java.util.ArrayList;
import java.util.List;

public class GeneticClient {
    public static void mutate(NeuralNetwork nn,double probWeights,double probConnection){
        List<Connection> dna=nn.connections;
        for(Connection con:dna){
            if(probConnection>Math.random()){

            }
        }
    }
}
