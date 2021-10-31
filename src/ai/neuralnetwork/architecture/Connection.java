package ai.neuralnetwork.architecture;

import ai.geneticalgorithm.GeneticAlgorithm;
import ai.neuralnetwork.initializer.Initializer;
import ai.neuralnetwork.initializer.RandomNormal;
import ai.neuralnetwork.optimizer.Optimizer;
import ai.editor.ArrowLine;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Connection  implements Serializable {
    private float value=0f;
    private Node inNode;
    private Node outNode;
    private boolean disabled=false;
    private float lastGradient;
    private List<Float> historicalGradients=new ArrayList<>();
    private Initializer connectionInitializer=new RandomNormal();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    private boolean enabled=true;
    public ArrowLine arrow;
    private int innovation=-1;

    public int getInnovation() {
        return innovation;
    }

    public void setInnovation(int innovation) {
        this.innovation = innovation;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public Node getInNode() {
        return inNode;
    }

    public void setInNode(Node inNode) {
        this.inNode = inNode;
    }

    public Node getOutNode() {
        return outNode;
    }

    public void setOutNode(Node outNode) {
        this.outNode = outNode;
    }

    public void initialize(){
        connectionInitializer.initialize(this, Initializer.NeuralObject.Connections);
    }
    public Connection(Node inNode, Node outNode){
        this.inNode = inNode;
        this.outNode = outNode;
        innovation=GeneticAlgorithm.generateInnovationNumber();
    }
    public void resetArrows(){
        arrow=null;
    }
    public void setArrow(ArrowLine arrow) {
        this.arrow = arrow;
    }

    public ArrowLine getArrow() {
        return arrow;
    }
    public void renderArrows(Graphics2D g){
       if(arrow!=null) {
           arrow.renderArrow(g);
       }

    }
    public void calcPointArrows(){
        if(arrow!=null) {
            arrow.calcPointsArrow();
        }

    }
    public void adjust(float learningRate, Optimizer optimizer){
        float gradient=0f;

        for(int i=0;i<historicalGradients.size();i++) {
            gradient += historicalGradients.get(i);
        }
        float change=optimizer.optimize(gradient,learningRate);
        value-=change;
        historicalGradients.clear();

    }
    public float getLastGradient() {
        return lastGradient;
    }

    public void setLastGradient(float lastGradient) {
        this.lastGradient = lastGradient;
    }

    public List<Float> getHistoricalGradients() {
        return historicalGradients;
    }

    public void setHistoricalGradients(List<Float> historicalGradients) {
        this.historicalGradients = historicalGradients;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
}
