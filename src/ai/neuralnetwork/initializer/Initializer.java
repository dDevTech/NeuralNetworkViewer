package ai.neuralnetwork.initializer;


import java.io.Serializable;

public abstract class Initializer implements Serializable {
    public enum NeuralObject{
        Bias,Connections
    }
    public abstract  void initialize(Object obj,NeuralObject object);

}
