package ai.neuralnetwork.optimizer;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Optimizer implements Serializable {

    public abstract float optimize(float d, float learningRate);


}
