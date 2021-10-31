package ai.neuralnetwork.optimizer;

public class Momentum extends Optimizer {
    float vd=0f;
    float beta=0.9f;
    @Override
    public float optimize(float d, float learningRate) {
        vd=beta*vd+(learningRate)*d;
        return vd;
    }
}
