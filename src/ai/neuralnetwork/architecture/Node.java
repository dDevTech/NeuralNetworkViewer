package ai.neuralnetwork.architecture;

import ai.neuralnetwork.activation.Activation;
import ai.neuralnetwork.activation.Logistic;
import ai.neuralnetwork.initializer.Initializer;
import ai.neuralnetwork.initializer.Zeros;
import ai.neuralnetwork.optimizer.Optimizer;
import ai.editor.NodeView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Node extends NodeView implements Serializable {

    private List<Connection> inputConnections=new ArrayList<>();
    private List<Connection> outputConnections=new ArrayList<>();


    private float outValue;
    private float netValue;
    private float bias;

    public enum NodeType{
        OUTPUT_NODE,INPUT_NODE,NODE
    }

    public NodeType getNodeType() {
        return nodeType;
    }

    public void setNodeType(NodeType nodeType) {
        this.nodeType = nodeType;
    }

    private NodeType nodeType;
    public int getNodeID() {
        return nodeID;
    }

    public void setNodeID(int nodeID) {
        this.nodeID = nodeID;
    }

    private int nodeID =0;

    private float errorSignal;
    public boolean alreadyFeeded=false;
    public boolean alreadyBackProp=false;

    private List<Float> historicalBias=new ArrayList<>();

    private Activation activation=new Logistic();
    private Initializer initializerBias=new Zeros();


    public Node(Activation activation,Initializer bias,NodeType type){
        this.activation = activation;
        this.nodeType=type;
        this.initializerBias = bias;
        setNode(this);
    }
    public Node(float inValue,Activation activation,NodeType type){
        this.nodeType=type;
        this.netValue = inValue;
        this.activation = activation;
        setNode(this);
    }

    public Node(Node oldNode,List<Connection>inputConnections,List<Connection>outputConnections){

        this.initializerBias = oldNode.initializerBias;
        this.activation = oldNode.activation;
        this.netValue = oldNode.netValue;
        this.outValue = oldNode.outValue;
        this.inputConnections = inputConnections;
        this.outputConnections = outputConnections;
        setNode(this);
    }
    public Node(NodeType type){
        this.nodeType=type;
        setNode(this);
    }
    public Node(){
        this.nodeType=NodeType.NODE;
        setNode(this);

    }
    public void feedForward(){
        slowProcess();
        float sum=0f;
        for(Connection connection:inputConnections){
           sum+= connection.getInNode().getOutValue()*connection.getValue();
        }
        netValue=sum +getBias();
        outValue=activation.activate(netValue);


    }


    public void initialize(){
        outValue=0f;
        netValue =0f;
        bias=0f;

        initializerBias.initialize(this, Initializer.NeuralObject.Bias);
    }

    public void backPropagation(){
        slowProcess();
        float sum=0f;
        for(Connection connection:outputConnections){
            sum+=connection.getOutNode().getErrorSignal()*connection.getValue();
        }
        sum*=activation.backProp(getOutValue());
        errorSignal=sum;
        for(Connection connection:inputConnections) {
            float gradient=errorSignal*connection.getInNode().getOutValue();
            connection.setLastGradient(gradient);
            connection.getHistoricalGradients().add(connection.getLastGradient());
        }
        historicalBias.add(errorSignal);


    }
    public void slowProcess(){
        if(NeuralNetwork.SHOW_TRAINING_PROCESS){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public void backPropagationOutput(float desired){
        slowProcess();

        errorSignal=-(desired-getOutValue())*activation.backProp(getOutValue());
        for(Connection connection:inputConnections) {
            float gradient=errorSignal*connection.getInNode().getOutValue();
            connection.setLastGradient(gradient);
            connection.getHistoricalGradients().add(connection.getLastGradient());
        }
        historicalBias.add(errorSignal);


    }
    public void adjust(float learningRate, Optimizer optimizer){
        float currentBias=0f;
        for(int i=0;i<historicalBias.size();i++) {
            currentBias += historicalBias.get(i);
        }
        float change=optimizer.optimize(currentBias,learningRate);
        bias-=change;
        historicalBias.clear();

    }
    public float getOutValue() {
        return outValue;
    }

    public void setOutValue(float outValue) {
        this.outValue = outValue;
    }

    public float getNetValue() {
        return netValue;
    }

    public void setNetValue(float netValue) {
        this.netValue = netValue;
    }

    public float getBias() {
        return bias;
    }

    public void setBias(float bias) {
        this.bias = bias;
    }

    public List<Connection> getInputConnections() {
        return inputConnections;
    }

    public void setInputConnections(List<Connection> inputConnections) {
        this.inputConnections = inputConnections;
    }

    public List<Connection> getOutputConnections() {
        return outputConnections;
    }

    public void setOutputConnections(List<Connection> outputConnections) {
        this.outputConnections = outputConnections;
    }

    public float getErrorSignal() {
        return errorSignal;
    }

    public void setErrorSignal(float errorSignal) {
        this.errorSignal = errorSignal;
    }
}
