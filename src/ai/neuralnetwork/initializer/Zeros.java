package ai.neuralnetwork.initializer;

import ai.neuralnetwork.architecture.Connection;
import ai.neuralnetwork.architecture.Node;

public class Zeros extends Initializer {

    @Override
    public void initialize(Object n, NeuralObject object) {
        if(n instanceof Connection){
            ((Connection) n).setValue(0f);
        }else{
            ((Node) n).setBias(0f);
        }
    }
}