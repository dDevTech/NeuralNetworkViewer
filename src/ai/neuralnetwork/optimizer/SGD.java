package ai.neuralnetwork.optimizer;

public class SGD extends Optimizer {

    @Override
    public float optimize(float dw ,float learningRate) {
        return (learningRate * dw);
    }
}
