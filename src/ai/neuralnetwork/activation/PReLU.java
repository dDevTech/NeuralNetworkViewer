package ai.neuralnetwork.activation;

public class PReLU extends  Activation {
    private float alpha;

    public PReLU(float alpha){

        this.alpha = alpha;
    }
    @Override
    public float activate(float netValue) {
        return netValue > 0f ? netValue : alpha*netValue;
    }

    @Override
    public float backProp(float outValue) {
        return outValue>0f?1f:alpha;
    }
}
