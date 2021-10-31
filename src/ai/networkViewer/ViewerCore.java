package ai.networkViewer;

import ai.editor.View;
import ai.neuralnetwork.architecture.Connection;
import ai.neuralnetwork.architecture.NeuralNetwork;
import ai.neuralnetwork.architecture.Node;
import math.tools.MathTools;
import physics.Vector2;
import tools.ColorPalette;
import tools.FontLoader;
import tools.Log;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ViewerCore extends JPanel {
    Thread engine;
    boolean interrupted=false;

    public float HEIGHT_BOX=100;
    public float MARGIN_TOP=50;
    public float INNER_MARGIN_TOP=20;
    public float INNER_MARGIN_LEFT=5;
    public float MARGIN_LEFT=50;
    public float SPACE_BETWEEN=20;
    public float MINI_BOX_WIDTH=150;
    public float height;
    public float finalHeight=0f;
    public float previousHeight=0;
    ArrayList<NeuralNetwork> networks= new ArrayList<>();
    HashMap<JButton,Object>mapButtons = new HashMap<>();
    HashMap<JButton,NeuralNetwork>updates= new HashMap<>();
    HashMap<JButton,NeuralNetwork>show= new HashMap<>();
    public ViewerCore(){
        this.setLayout(null);
        this.setBackground(Color.white);
        engine=new Thread(new Runnable() {
            @Override
            public void run() {
                while(!interrupted){
                    repaint();
                }
            }
        });
        engine.start();

    }
    public void paint(Graphics g){
        super.paint(g);
        if(!FontLoader.alreadyLoaded) {
            FontLoader.loadFont();
        }
        Graphics2D g2= (Graphics2D)g;



        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(
                RenderingHints.KEY_FRACTIONALMETRICS,
                RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        Font f=ColorPalette.font.deriveFont(12f);
        g2.setFont(f);
        for(int i=0;i<networks.size();i++){
            drawInfoNetwork(i,networks.get(i),g2);
        }
        finalHeight=height;

        this.revalidate();
        if(finalHeight!=previousHeight){

        }
        previousHeight=finalHeight;
        height=0f;
    }
    public void checkNetworkButtons(Object o,float x,float y){

        if(!mapButtons.containsValue(o)){
            if(o instanceof Node){
                if(((Node)o).getNodeType()== Node.NodeType.NODE){
                    createButton(x,y,o);
                }
            }else{
                createButton(x,y,o);
            }


        }
        for (JButton b : mapButtons.keySet()) {
            if(mapButtons.get(b)==o) {
                b.setBounds((int) (x + MINI_BOX_WIDTH - 30), (int) (y + HEIGHT_BOX - 30f), 20, 20);
            }
        }
    }
    public void createButton(float x,float y, Object o){
        JButton b = new JButton("R");
        b.setFocusable(false);
        add(b);
        b.setMargin(new Insets(0, 0, 0, 0));
        b.setBounds((int)(x+MINI_BOX_WIDTH-30),(int)(y+HEIGHT_BOX-30f),20,20);
        b.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mousePressed(e);
                Object o= mapButtons.get(b);
                if(o instanceof Connection){
                    for (NeuralNetwork network:networks){
                        if( network.getConnections().contains(o)) {
                            Object remove=network.getConnections().get(network.getConnections().indexOf(o));
                            Connection con=(Connection)remove;
                            con.getInNode().getOutputConnections().remove(con);
                            con.getOutNode().getInputConnections().remove(con);
                            network.getConnections().remove(remove);
                            remove(b);
                            mapButtons.remove(b);

                            break;
                        }
                    }
                }
                if(o instanceof Node) {
                        Node node=    (Node)o;
                      for (NeuralNetwork network:networks) {
                          List<Connection>conToRemove2= new ArrayList<>();
                          for(Connection connection:node.getInputConnections()){

                              JButton toRemove=null;
                              for (JButton b : mapButtons.keySet()) {
                                  if (mapButtons.get(b).equals(connection)) {
                                      toRemove=b;
                                  }
                              }
                              if(toRemove!=null){
                                  mapButtons.remove(toRemove);
                                  remove(toRemove);
                              }
                              conToRemove2.add(connection);
                              network.getConnections().remove(connection);

                          }
                          for(Connection con: conToRemove2){
                              con.getInNode().getOutputConnections().remove(con);
                              con.getOutNode().getInputConnections().remove(con);
                          }
                          conToRemove2.clear();
                          List<Connection>conToRemove= new ArrayList<>();
                          for(Connection connection:node.getOutputConnections()){
                              JButton toRemove=null;

                              for (JButton b : mapButtons.keySet()) {
                                  if (mapButtons.get(b).equals(connection)) {
                                      toRemove=b;
                                  }
                              }
                              if(toRemove!=null){
                                  mapButtons.remove(toRemove);
                                  remove(toRemove);

                              }
                              conToRemove.add(connection);

                              network.getConnections().remove(connection);


                          }
                          for(Connection con: conToRemove){
                              con.getInNode().getOutputConnections().remove(con);
                              con.getOutNode().getInputConnections().remove(con);
                          }
                          conToRemove.clear();
                          network.getNodes().remove(node);
                          remove(b);
                          mapButtons.remove(b);
                          network.compile();
                    }
                }
            }
        });
        mapButtons.put(b,o);
    }
    public void drawInfoNetwork(int a,NeuralNetwork network,Graphics2D g){

        float y=height+MARGIN_TOP;
        float x=MARGIN_LEFT;
        for (JButton b : updates.keySet()) {
            if(updates.get(b)==network) {
                b.setBounds((int)(x+200),(int)(y+10f),100,25);
            }
        }
        for (JButton b : show.keySet()) {
            if(show.get(b)==network) {
                b.setBounds((int)(x+350),(int)(y+10f),100,25);
            }
        }
        if(!updates.containsValue(network)){

            JButton b = new JButton("UPDATE");
            b.setFocusable(false);
            add(b);
            updates.put(b,network);
            b.setBounds((int)(x+200),(int)(y+10f),100,25);
            b.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            NeuralNetwork.SHOW_TRAINING_PROCESS=true;
                            network.feedForward(new float[network.getInputs().size()]);
                            NeuralNetwork.SHOW_TRAINING_PROCESS=false;
                        }
                    });;
                    t.start();

                }
            });
        }
        if(!show.containsValue(network)){

            JButton b = new JButton("SHOW");
            b.setFocusable(false);
            add(b);
            show.put(b,network);
            b.setBounds((int)(x+350),(int)(y+10f),100,25);
            b.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    View view = new View(1000,600);
                    view.setVisible(true);
                    view.core.setNeuralNetwork(network);

                }
            });
        }
        g.setColor(ColorPalette.connections);
        g.setStroke(new BasicStroke(2f));
        g.setColor(Color.black);
        g.setFont(ColorPalette.font.deriveFont(12f));
        g.drawString("NEURAL NETWORK  "+networks.indexOf(network),x+20,y+20);

        java.util.List<Node> inputs=network.getInputs();
        int c=0;
        int maxBoxes=(int)((getWidth()-MARGIN_LEFT*2-INNER_MARGIN_LEFT*2)/(MINI_BOX_WIDTH+(SPACE_BETWEEN-1)));
        if(maxBoxes==0){
            maxBoxes=1;
        }
        Vector2 p=null;
        for(int i=0;i<inputs.size();i++){

             p=calcPositionMini(c,i,x,y,maxBoxes);
            drawMiniBox(p.x,p.y,inputs.get(i),g);
            checkNetworkButtons(inputs.get(i),p.x,p.y);
            c++;
        }

        java.util.List<Node> outputs=network.getOutputs();

        for(int i=0;i<outputs.size();i++){

             p=calcPositionMini(c,i,x,y,maxBoxes);
            drawMiniBox(p.x,p.y,outputs.get(i),g);
            checkNetworkButtons(outputs.get(i),p.x,p.y);
            c++;
        }

        List<Node> nodes = network.getNodes();

        for(int i=0;i<nodes.size();i++){

            p=calcPositionMini(c,i,x,y,maxBoxes);
            drawMiniBox(p.x,p.y,nodes.get(i),g);
            checkNetworkButtons(nodes.get(i),p.x,p.y);
            c++;
        }
        List<Connection> connections = network.getConnections();
        for(int i=0;i<connections.size();i++){

            p=calcPositionMini(c,i,x,y,maxBoxes);
            drawMiniBox(p.x,p.y,connections.get(i),g);
            checkNetworkButtons(connections.get(i),p.x,p.y);
            c++;
        }
        height=((int)(p.y+HEIGHT_BOX+INNER_MARGIN_TOP+MARGIN_TOP));

        g.drawRect((int)x,(int)y,(int)(getWidth()-MARGIN_LEFT*2),(int)(height-y-MARGIN_TOP));


    }
    public Vector2 calcPositionMini(float c, float i, float xBox, float yBox,int maxBoxes){

        int posX=((int)c)%maxBoxes;
        int posY=(int)c/maxBoxes;
        float xMini=xBox+INNER_MARGIN_LEFT+SPACE_BETWEEN*(posX+1)+posX*MINI_BOX_WIDTH;
        float yMini=yBox+INNER_MARGIN_TOP+SPACE_BETWEEN*(posY+1)+posY*HEIGHT_BOX;
        return new Vector2(xMini,yMini);
    }
    public void drawMiniBox(float x,float y, Node node,Graphics2D g){
        g.setColor(ColorPalette.boxes2);
        g.drawRect((int)x,(int)y,(int)MINI_BOX_WIDTH,(int)HEIGHT_BOX);
        g.setFont(ColorPalette.font.deriveFont(12f));
        g.setColor(ColorPalette.inputHover);
        g.drawString("NODE",  x+20,y+20);
        g.setFont(ColorPalette.font.deriveFont(10f));
        g.setColor(Color.black);
        g.drawString("NODE ID "+Integer.toString(node.getNodeID()),x+20,y+40);
        g.drawString(node.getNodeType().toString(),x+20,y+55);



    }
    public void drawMiniBox(float x, float y, Connection connection, Graphics2D g){
        g.setColor(ColorPalette.boxes1);
        g.drawRect((int)x,(int)y,(int)MINI_BOX_WIDTH,(int)HEIGHT_BOX);
        g.setColor(ColorPalette.outputHover);
        g.setFont(ColorPalette.font.deriveFont(12f));
        g.drawString("CONNECTION",  x+20,y+20);
        g.setFont(ColorPalette.font.deriveFont(10f));
        g.setColor(Color.black);
        g.drawString("INNOVATION "+(connection.getInnovation()),x+20,y+40);
        g.drawString("IN_NODE: "+(connection.getInNode().getNodeID()),x+20,y+55);
        g.drawString("OUT_NODE: "+(connection.getOutNode().getNodeID()),x+20,y+70);
        g.drawString("VALUE: "+ MathTools.round(connection.getValue(),2),x+20,y+85);


    }
    @Override
    public Dimension getPreferredSize() {

        return new Dimension(-1, (int)finalHeight);

    }
}
