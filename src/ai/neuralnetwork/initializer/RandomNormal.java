package ai.neuralnetwork.initializer;


import ai.neuralnetwork.architecture.Connection;
import ai.neuralnetwork.architecture.Node;


import java.util.Random;

public class RandomNormal extends Initializer {

    @Override
    public void initialize(Object n, NeuralObject object) {
        Random r = new Random();
        if(n instanceof Connection){
            ((Connection) n).setValue((float)r.nextGaussian());
        }else{
            ((Node) n).setBias((float)(r.nextGaussian()));
        }
    }
}
