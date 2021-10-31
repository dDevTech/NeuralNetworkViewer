package ai.neuralnetwork.initializer;

import ai.neuralnetwork.architecture.Connection;
import ai.neuralnetwork.architecture.Node;

public class Random extends Initializer {

    @Override
    public void initialize(Object n, NeuralObject object) {
        java.util.Random r = new java.util.Random();
        if(n instanceof Connection){
            ((Connection) n).setValue(r.nextFloat()*2f-1f);
        }else{
            ((Node) n).setBias(r.nextFloat()*2f-1f);
        }
    }
}

