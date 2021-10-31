package ai.neuralnetwork.activation;

import java.io.Serializable;

public abstract class Activation implements Serializable {
    public abstract float activate(float netValue);
    public abstract float backProp(float outValue);
}
