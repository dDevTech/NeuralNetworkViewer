package ai.networkViewer;

import ai.neuralnetwork.architecture.NeuralNetwork;

import javax.swing.*;


public class FrameViewer extends JFrame {
    public ViewerCore core;
    JScrollPane scroll;
    public FrameViewer(int width,int height){
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        this.setSize(width,height);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Network Viewer");
        core=new ViewerCore();

        scroll= new JScrollPane(core);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setBorder(null);
        this.setContentPane(scroll);
        this.setLocationRelativeTo(null);
    }
    public void addNetwork(NeuralNetwork network){
        core.networks.add(network);
    }
}
