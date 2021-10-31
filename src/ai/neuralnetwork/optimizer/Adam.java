package ai.neuralnetwork.optimizer;


public class Adam extends Optimizer{

    float vd=0f;
    float sd=0f;
    float beta=0.9f;
    float beta2=0.99f;
    float e=1e-10f;
    @Override
    public float optimize(float d, float learningRate) {
        vd=beta*vd+(1-beta)*d;
        sd=beta2*sd+(1-beta2)*d*d;
        return (float) (learningRate*((vd*d)/(Math.sqrt(sd)+e)));
    }

    public void setBeta(float beta) {
        this.beta = beta;
    }

    public void setE(float e) {
        this.e = e;
    }

    public void setBeta2(float beta2) {
        this.beta2 = beta2;
    }
}
