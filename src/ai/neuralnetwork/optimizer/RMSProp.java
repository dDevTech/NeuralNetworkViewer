package ai.neuralnetwork.optimizer;

import java.util.ArrayList;

public class RMSProp extends Optimizer{

    float vd=0f;
    float beta=0.9f;
    float e=0.000001f;
    @Override
    public float optimize(float d, float learningRate) {
        vd=beta*vd+(1-beta)*d*d;
        return (float) (learningRate*(d/(Math.sqrt(vd)+e)));
    }

    public void setBeta(float beta) {
        this.beta = beta;
    }

    public void setE(float e) {
        this.e = e;
    }
}
