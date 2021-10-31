package ai.neuralnetwork.initializer;

import ai.neuralnetwork.architecture.Connection;
import ai.neuralnetwork.architecture.Node;

public class Ones extends  Initializer {
    @Override
    public void initialize(Object n, NeuralObject object) {

        if(n instanceof Connection){
            ((Connection) n).setValue(1f);
        }else{
            ((Node) n).setBias(1f);
        }
    }
}
