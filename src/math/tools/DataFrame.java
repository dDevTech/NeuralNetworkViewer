package math.tools;

import ai.editor.Core;
import ai.neuralnetwork.architecture.NeuralNetwork;
import ai.neuralnetwork.architecture.Node;
import physics.Vector2;

import java.awt.*;

public class DataFrame {
    public  String VERSION="1.0.0";
    public  String APP_NAME="Neural Net Visualizer";
    public  Vector2 cursorPoint= new Vector2(0,0);
    public  Node nodeSelected;

    public  void renderDataFrame(Graphics2D g, Core core, NeuralNetwork network){
        g.setColor(Color.black);
        g.drawString(""+(int)cursorPoint.x+" "+(int)cursorPoint.y,core.getWidth()-70,core.getHeight()-20);
        g.drawString(APP_NAME.toUpperCase()+" "+VERSION,20,core.getHeight()-20);
        g.drawString("Connections: "+network.connections.size(),300,core.getHeight()-20);
    }
}
