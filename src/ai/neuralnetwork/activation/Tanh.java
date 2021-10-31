package ai.neuralnetwork.activation;

import ai.neuralnetwork.optimizer.Optimizer;

public class Tanh extends Activation {

    @Override
    public float activate(float netValue) {

        return (float)(1f/(1f+Math.pow(Math.E,-2*netValue))-1f);
    }

    @Override
    public float backProp(float outValue) {
        return 1-outValue*outValue;
    }
}
