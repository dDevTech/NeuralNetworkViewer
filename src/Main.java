import ai.editor.View;
import ai.networkViewer.FrameViewer;
import ai.neuralnetwork.activation.Logistic;
import ai.neuralnetwork.architecture.Connection;
import ai.neuralnetwork.architecture.NeuralNetwork;
import ai.neuralnetwork.architecture.Node;
import ai.neuralnetwork.initializer.RandomNormal;
import ai.neuralnetwork.initializer.Zeros;
import coloringai.Frame;
import org.apache.commons.lang3.SerializationUtils;

public class Main {
    public static void main(String[]args)  {
      View view = new View(1000,600);
      view.setVisible(true);
      NeuralNetwork network = new NeuralNetwork(2,2,new Zeros(),new Logistic());
      //  NeuralNetwork network = new NeuralNetwork(2,2,new int[]{5,5,5}, new RandomNormal(),new Logistic());
        Node n1=new Node();
        Node n2=new Node();
        network.addNode(n1);
        network.addNode(n2);
        network.addConnection(new Connection(network.getInputs().get(0),n1));
        network.addConnection(new Connection(network.getInputs().get(1),n1));
        network.addConnection(new Connection(network.getInputs().get(0),n2));
        network.addConnection(new Connection(network.getInputs().get(1),n2));
        network.addConnection(new Connection(n1,network.getOutputs().get(0)));
        network.addConnection(new Connection(n2,network.getOutputs().get(0)));
      //  network.randomFill(200,100);

        view.core.setNeuralNetwork(network);
        FrameViewer viewer= new FrameViewer(1000,800);
        viewer.setVisible(true);

    //  NeuralNetwork network2 =SerializationUtils.clone(network);


      network.init();
     // network2.init();

     // View view2 = new View(1000,600);
   //   view2.setVisible(true);
     // view2.core.setNeuralNetwork(network2);

     // viewer.addNetwork(network2);
        NeuralNetwork network2= new NeuralNetwork(2,2,new Zeros(),new Logistic());
      viewer.addNetwork(network);
        viewer.addNetwork(network2);
      //  float[]ff= new float[2];
      //  ff[0]=0.55f;
      //  ff[1]=0.25f;
      //  float[]ff2= new float[2];
       // ff2[0]=0.85f;
      //  ff2[1]=0.65f;
      //  float[]ii2= new float[2];
     //   ii2[0]=0.45f;
     //   ii2[1]=0.85f;
     //   float[]ii= new float[2];
     //   ii[0]=0.15f;
     //   ii[1]=0.65f;
    ///    Dataset data= new Dataset();
     //   data.addData(ii,ff);
       // data.addData(ii2,ff2);
       // network.feedForward(ff);
        //network.train(data,0.1f,0.15f,1000);

      // Frame f = new Frame();
       // f.setVisible(true);



    }
}
