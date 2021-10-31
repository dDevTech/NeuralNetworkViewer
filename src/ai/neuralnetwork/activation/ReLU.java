package ai.neuralnetwork.activation;

public class ReLU extends  Activation {
    @Override
    public float backProp(float outValue) {
        return outValue>0f?1f:0f;
    }

    @Override
    public float activate(float netValue) {
        return netValue > 0f ? netValue : 0;

    }
}

