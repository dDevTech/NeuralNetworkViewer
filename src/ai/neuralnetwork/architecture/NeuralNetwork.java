package ai.neuralnetwork.architecture;

import ai.Dataset.Dataset;
import ai.geneticalgorithm.GeneticAlgorithm;
import ai.neuralnetwork.activation.Activation;
import ai.neuralnetwork.initializer.Initializer;
import ai.neuralnetwork.optimizer.Optimizer;
import math.tools.Defaults;
import tools.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class NeuralNetwork implements Serializable {

    //Lists of the neurons of the network (inputs, outputs, hidden)
    public List<Node> inputs;
    public List<Node> outputs;
    public List<Node> nodes;

    //Node being trained currently
    private Node currentTrainingNode;

    //List of weights of the network
    public List<Connection>connections;


    //Setting to show the process of the training of the network in low motion
    public static boolean SHOW_TRAINING_PROCESS=false;


    //Only if multilayer
    private int[]architecture;

    //Graph network type or multilayer feed-forward network
    public enum NetworkType{
        MULTILAYER, NEUROEVOLUTION
    }
    public NetworkType type;

    /**
     * Create a neural network with the inputs, outputs, nodes and connections
     * Note: They must be initialized and set
     * @param inputs list of inputs of the network (input nodes)
     * @param outputs list of outputs of the network (output nodes(
     * @param nodes list of the hidden nodes
     * @param connections list of the connections of the network
     */
    public NeuralNetwork(List<Node> inputs, List<Node> outputs, List<Node> nodes, List<Connection> connections) {
        this.inputs = inputs;
        this.outputs = outputs;
        this.nodes = nodes;
        this.connections = connections;
    }

    /**
     * Create a new network with a fixed ammount of outputs and inputs. Set the  activation function and the initializer of the output layer
     * The type of network is NEUROEVOLUTION in this case
     * @param inputs number of inputs
     * @param outputs number of outputs
     * @param outputBias initializer of the biases of the output layer
     * @param outputActivation activation function of the output layer nodes
     */
    public NeuralNetwork(int inputs, int outputs, Initializer outputBias, Activation outputActivation){
        this.inputs = new ArrayList<>();
        this.outputs = new ArrayList<>();
        this.connections= new ArrayList<>();
        this.nodes = new ArrayList<>();

        for(int i=0;i<outputs;i++){
            Node node=new Node(outputActivation,outputBias,Node.NodeType.INPUT_NODE);
            node.setNodeID(GeneticAlgorithm.generateIdNumber());
            this.inputs.add(node);
        }
        for(int i=0;i<inputs;i++){
            Node node=new Node(outputActivation,outputBias,Node.NodeType.OUTPUT_NODE);
            node.setNodeID(GeneticAlgorithm.generateIdNumber());
            this.outputs.add(node);
        }
        type=NetworkType.NEUROEVOLUTION;


    }

    /**
     *  Create a MULTILAYER feed-forward neural network with a fixed amount of inputs, outputs and the architecture of the hidden layers.
     *  Also set the output activation function and initializer
     * @param inputs number of inputs
     * @param outputs number of outputs
     * @param hiddenLayers architecture of the network (hidden layers)
     * @param outputBias initializer of the biases of the output layer
     * @param outputActivation activation function of the output layer nodes
     */
    public NeuralNetwork(int inputs,int outputs,int[]hiddenLayers,Initializer outputBias, Activation outputActivation){
        //Initialize arrays
        this.inputs = new ArrayList<>();
        this.outputs = new ArrayList<>();
        this.connections= new ArrayList<>();
        this.nodes = new ArrayList<>();

        //Set some settings of the network
        architecture=hiddenLayers;
        type=NetworkType.MULTILAYER;

        //Generate inputs nodes
        //Although the input layer doesnt have an activation and bias we will also set them (they are also nodes)
        for(int i=0;i<inputs;i++){
            Node node=new Node(outputActivation,outputBias,Node.NodeType.INPUT_NODE);
            node.setNodeID(GeneticAlgorithm.generateIdNumber());
            this.inputs.add(node);
        }
        //Generate hidden nodes
        for(int i=0;i<hiddenLayers.length;i++){
           for(int j=0;j<hiddenLayers[i];j++) {
               Node node = new Node(Node.NodeType.NODE);
               addNode(node);

            }
        }
        //Generate outputs nodes
        for(int i=0;i<outputs;i++){
            Node node=new Node(outputActivation,outputBias,Node.NodeType.OUTPUT_NODE);
            node.setNodeID(GeneticAlgorithm.generateIdNumber());
            this.outputs.add(node);
        }
        //Algorithm to generate weights
        //##################
        for (int k = 0; k < inputs; k++) {
            for(int j=0;j<hiddenLayers[0];j++) {
                Node node=nodes.get(j);
                this.connections.add(new Connection(this.inputs.get(k), node));
            }
        }


        int c=0;
        for(int i=0;i<hiddenLayers.length;i++){
            for(int j=0;j<hiddenLayers[i];j++) {
                Node node=nodes.get(c);

                  if(i==hiddenLayers.length-1){
                    for(int k=0;k<outputs;k++){
                        this.connections.add(new Connection(node,this.outputs.get(k)));
                    }

                }else{
                      for(int k=0;k<hiddenLayers[i+1];k++){
                          this.connections.add(new Connection(node,this.nodes.get(c+hiddenLayers[i]-j+k)));

                      }

                }
                c++;
            }

        }
        //##################

        //Initialize the network
        init();
    }

    /**
     *  Add a node to the network. This function will also generate an innovation number for the node to store it
     * @param node The node you want to add to the network
     */
    public void addNode(Node node){
        node.setNodeID(GeneticAlgorithm.generateIdNumber());
        nodes.add(node);
    }

    /**
     *  Add a connection to the network
     * @param connection
     */
    public void addConnection(Connection connection){
        connections.add(connection);
    }

    /**
     *  This function will generate a random neural networks with random connections and nodes
     *  Note: this function won't check the infinite loops that could happen because of cycle graphs and recurrent connections
     * @param connections The number of connection you want to add
     * @param nodes The number of hidden nodes you want to have
     */
    public void randomFill(int connections,int nodes){
        for(int i=0;i<nodes;i++){
            addNode(new Node(Node.NodeType.NODE));
        }
        Random r=new Random(Defaults.seed);
        for(int i=0;i<connections;i++){
            Node in =this.nodes.get( (int)(r.nextFloat()*nodes));
            Node out =this.nodes.get( (int)(r.nextFloat()*nodes));
            boolean similar=false;
            for(int j=0;j<this.connections.size();j++){

                if((this.connections.get(j).getInNode().equals(in)&&this.connections.get(j).getOutNode().equals(out))){//Duplicates
                    similar=true;
                }
                if((this.connections.get(j).getInNode().equals(out)&&this.connections.get(j).getOutNode().equals(in))){//Recurrents
                    similar=true;
                }
            }

            if(!similar){
                addConnection(new Connection(in,out));
            }

        }

    }

    /**
     * Will initialize the neural network (nodes, outputs and connections)
     * Note: input nodes are not initialized because they will be set as the input data.
     * Hidden nodes and outputs will be  initialized their respective biases and connections their activation functions and their initial values
     *
     */
    public void init(){

        for(int i=0;i<nodes.size();i++){
            nodes.get(i).initialize();
        }
        for(int i=0;i<outputs.size();i++){
            outputs.get(i).initialize();
        }
        for(int i=0;i<connections.size();i++){
            connections.get(i).initialize();
        }

        compile();
    }

    /**
     *  This function will be in charge of updating every node inputs and outputs connections
     */
    public void compile(){
        for(int i=0;i<connections.size();i++){
            connections.get(i).getOutNode().getInputConnections().add(connections.get(i));
            connections.get(i).getInNode().getOutputConnections().add(connections.get(i));
        }
    }

    /**
     * A recursive algorithm to update the network. With the inputs. connections and nodes we will update in the forward direction the network to get the final outputs
     * @param inputData The input data we want to set to the input layer (input nodes). Must be the same size as the input layer size
     */
    public void feedForward(float[]inputData){
        for(Node node:getNodes()){
            //Check if the input connections of a node is empty.
            if(node.getInputConnections().isEmpty()){
                node.setOutValue(0f);
                node.alreadyFeeded=true;
            }
        }
        for(int i=0;i<inputData.length;i++){

            this.inputs.get(i).setOutValue(inputData[i]);
            this.inputs.get(i).setNetValue(inputData[i]);
            this.inputs.get(i).alreadyFeeded=true;
            feed(this.inputs.get(i).getOutputConnections());
        }
        for(int i=0;i<outputs.size();i++){
            outputs.get(i).alreadyFeeded=false;
        }
        for(int i=0;i<nodes.size();i++){
            nodes.get(i).alreadyFeeded=false;
        }

    }

    /**
     * Recursive function of the feed-forward
     * @param connections
     */
    private void feed(List<Connection>connections){
        for(Connection connection:connections){
            boolean feed=true;
            for(Connection input:connection.getOutNode().getInputConnections()){
                if(!input.getInNode().alreadyFeeded){
                    feed=false;
                    break;
                }
            }
            if(feed&&!connection.getOutNode().alreadyFeeded){
                connection.getOutNode().alreadyFeeded=true;
                setCurrentTrainingNode(connection.getOutNode());
                connection.getOutNode().feedForward();
                feed(connection.getOutNode().getOutputConnections());
            }

        }
    }

    /**
     * Get the output data in the outputs nodes.
     * To get the predicted values you must update the network first (feed-forward)
     * @return array with the output data of the network
     */
    public float[] getOutputData(){
        float[]out= new float[this.outputs.size()];
        for(int i=0;i<outputs.size();i++){
            out[i]=outputs.get(i).getOutValue();
        }
        return out;
    }

    /**
     * A recursive function for the network to learn with a desired data.
     * With the output values the function will calculate the error signal and propagate it in the opposite direction to the feed-forward
     * @param desiredData
     */
    public void backPropagation(float[]desiredData){
        //Pending to test
        for(Node node:getNodes()){
            if(node.getOutputConnections().isEmpty()){
                node.setErrorSignal(0f);
                node.alreadyBackProp=true;
            }
        }
        //&&
        for(int i=0;i<this.outputs.size();i++){
            setCurrentTrainingNode(this.outputs.get(i));
            this.outputs.get(i).backPropagationOutput(desiredData[i]);
            this.outputs.get(i).alreadyBackProp=true;
            back(this.outputs.get(i).getInputConnections());

        }
        for(int i=0;i<outputs.size();i++){
            outputs.get(i).alreadyBackProp=false;
        }
        for(int i=0;i<nodes.size();i++){
            nodes.get(i).alreadyBackProp=false;
        }
    }

    /**
     * Resursive function of the back-propagation
     * @param connections
     */
    private void back(List<Connection>connections){
        for(Connection connection:connections){
            boolean backProp=true;
            for(Connection output:connection.getInNode().getOutputConnections()){
                if(!output.getOutNode().alreadyBackProp){
                    backProp=false;
                    break;
                }
            }
            if(backProp&&!connection.getInNode().alreadyBackProp){
                connection.getInNode().alreadyBackProp=true;
                setCurrentTrainingNode(connection.getInNode());
                connection.getInNode().backPropagation();
                back(connection.getInNode().getInputConnections());
            }

        }

    }

    /**
     * This function will adjust all the connection of the network with their previously stored error signal.
     * Note: you must use first back-propagation to store the error signal and the gradients of every connection
     * @param learningRateConnections the decimal learning rate value of the connections from [0-1] for the network to learn. f.e 0.001
     * @param learningRateBias the  decimal learning rate value of the biases from [0-1] for the network to learn. f.e 0.005
     * @param optimizer the optimizer to use to update the connections
     */
    public void adjustNet(float learningRateConnections,float learningRateBias,Optimizer optimizer){
        for(int i=0;i<nodes.size();i++){
            nodes.get(i).adjust(learningRateBias,optimizer);
        }
        for(int i=0;i<outputs.size();i++){
            outputs.get(i).adjust(learningRateBias,optimizer);
        }
        for(int i=0;i<connections.size();i++){
            connections.get(i).adjust(learningRateConnections,optimizer);
        }

    }

    /**
     * The general function to train the network. This function will train the network for one example of a dataset
     * This function will be executed in a separate thread to avoid performance issues
     * @param inputs the input data with which you want to predict the final output
     * @param desired the desired output value you want to get. It's necessary for the supervised learning
     * @param learningRateBias The learning rate of the biases of the network [0,1]
     * @param learningRateConnections The learning rate of the connections [0-1]
     * @param optimizer The optimizer use for the training
     * @return the output data (not sure it works)
     */
    public float[] train(float[]inputs, float[]desired, float learningRateBias, float learningRateConnections, Optimizer optimizer){
        Thread training = new Thread(new Runnable() {
            @Override
            public void run() {
                int c=0;


                    feedForward(inputs);
                    float error = calcError(desired);
                    Log.log("Error: " + error + " N " + (NeuralNetwork.this.inputs.size() + NeuralNetwork.this.nodes.size() + NeuralNetwork.this.outputs.size()) + " " + c);
                    backPropagation(desired);
                    adjustNet(learningRateConnections, learningRateBias,optimizer);
                    c++;



            }
        });
        training.start();
        return getOutputData();
    }

    /**
     * General function to train the network with a full dataset
     * It supports batch gradient descent
     * It will be executed in a separate thread to avoid performance issues
     * @param dataset The dataset with which you want to train the network
     * @param learningRateBias the learning rate of the biases [0-1]
     * @param learningRateConnections the learning rate of the connections [0-1]
     * @param epochs the amount of times you want to iterate through the dataset
     * @param minibatches the amount of data iterations you want to go through to adjust the weights (connections)
     * @param optimizer the optimizer used when the connections are updated
     * @return the thread used to train the network. Check when finished
     */
    public Thread train(Dataset dataset,float learningRateBias,float learningRateConnections,int epochs,int minibatches,Optimizer optimizer){
        return initTrainProcess(dataset,learningRateBias,learningRateConnections,epochs,minibatches,optimizer);
    }
    private Thread initTrainProcess(Dataset dataset,float learningRateBias,float learningRateConnections,int epochs,int minibatches,Optimizer optimizer){
        Thread training = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.log("Training process");
                for(int i=0;i<epochs;i++){
                    Log.log("Epoch: "+i);
                    float error=0f;
                    for(int j=0;j<dataset.getData().size();j++){

                        feedForward(dataset.getData().get(j));
                        error += calcError(dataset.getDesired().get(j));
                        backPropagation(dataset.getDesired().get(j));

                        if(minibatches!=1) {
                            if ((j % minibatches == 0 && j != 0) || j == dataset.getData().size() - 1) {
                                Log.log("Error: " + error + " N " + (NeuralNetwork.this.inputs.size() + NeuralNetwork.this.nodes.size() + NeuralNetwork.this.outputs.size()));
                                adjustNet(learningRateConnections, learningRateBias,optimizer);
                                error=0f;
                            }
                        }else{
                            Log.log(" 1-Error: " + error + " N " + (NeuralNetwork.this.inputs.size() + NeuralNetwork.this.nodes.size() + NeuralNetwork.this.outputs.size()));
                            adjustNet(learningRateConnections, learningRateBias,optimizer);
                            error=0f;
                        }
                    }


                }
                evaluate(dataset);
            }
        });

        training.start();
        return training;
    }

    /**
     * Calculate the mean-squared error that has occurred to the network
     * @param desired the desired output data. Must match with the output architecture of the network
     * @return the decimal error of the network
     */
    public float calcError(float[]desired){
        float sum=0f;
        for(int i=0;i<desired.length;i++){
            float dif=desired[i]-outputs.get(i).getOutValue();
            sum+=dif*dif;
        }
        return sum/2f;
    }

    /**
     * Evaluate how the network is performing with a dataset
     * @param dataset the dataset you want to evaluate.
     */
    public void evaluate(Dataset dataset){
        Log.log("Evaluating");
        for(int i=0;i<dataset.getData().size();i++) {
            Log.log("Training set "+i);
            feedForward(dataset.getData().get(i));
            float[] outputs = getOutputData();
            for (int j = 0; j < outputs.length; j++) {
                Log.log(outputs[j] + " -> " +dataset.getDesired().get(i)[j]);
            }
        }
    }

    /**
     * Predict the result with a certain data
     */
    public void predict(){

    }


    public List<Node> getInputs() {
        return inputs;
    }

    public void setInputs(List<Node> inputs) {
        this.inputs = inputs;
    }

    public List<Node> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<Node> outputs) {
        this.outputs = outputs;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public List<Connection> getConnections() {
        return connections;
    }

    public void setConnections(List<Connection> connections) {
        this.connections = connections;
    }

    public int[] getArchitecture() {
        return architecture;
    }

    public void setArchitecture(int[] architecture) {
        this.architecture = architecture;
    }

    public Node getCurrentTrainingNode() {
        return currentTrainingNode;
    }

    public void setCurrentTrainingNode(Node currentTrainingNode) {
        this.currentTrainingNode = currentTrainingNode;
    }
}
