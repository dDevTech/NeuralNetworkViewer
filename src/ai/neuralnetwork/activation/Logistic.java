package ai.neuralnetwork.activation;

public class Logistic extends  Activation {
    @Override
    public float backProp(float outValue) {
        return outValue*(1f-outValue);
    }

    @Override
    public float activate(float netValue) {

        return (float)(1f/(1f+Math.pow(Math.E,-netValue)));
    }


}
