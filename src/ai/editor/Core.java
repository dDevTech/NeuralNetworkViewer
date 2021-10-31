package ai.editor;

import ai.neuralnetwork.architecture.Connection;
import ai.neuralnetwork.architecture.NeuralNetwork;
import ai.neuralnetwork.architecture.Node;
import math.tools.DataFrame;
import physics.Intersections;
import physics.Vector2;
import tools.ColorPalette;
import tools.FontLoader;
import tools.Log;
import tools.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Random;

public class Core extends JPanel {

    //Thread
    Thread engine;
    public boolean interrupted = false;

    //Variables visualization
    public float MARGIN_INPUTS = 100;
    public float MARGIN_OUTPUTS = 100;
    public float SIZE_NEURONS = 30;
    public float HORIZONTAL_MARGINS = 40;
    public float NODE_POSITIONING_CENTER_PERCENT = 0.7f;

    //Timing
    public float delta = 0f;
    private float previousFrameTime = 0f;

    //Selection hovered
    private Node selected;
    private Node hovered;

    //Create connection mode
    private boolean connectionCreating = false;
    private Node connectionCreatingNode;
    private Vector2 mousePosition;

    private Node previousNodeTrained;

    public enum SELECTION_MODE {
        NOT_INPUTS, NOT_OUTPUTS, ALL_NODES, NOT_INPUTS_NOT_OUTPUTS
    }

    //Data output
    public DataFrame data;

    public Core() {
        this.setBackground(Color.WHITE);
        if (!FontLoader.alreadyLoaded) {
            FontLoader.loadFont();
        }
        addListeners();
        engine = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!interrupted) {
                    repaint();
                }
            }
        });
        data = new DataFrame();
        startEngine();

    }


    public void addListeners() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                updateNetwork();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                //if mouse clicked in a node when creating connection , the connection will be established
                if (connectionCreating) {
                    //The final node cant be an input node
                    Node finalNode = checkSelection(e.getX(), e.getY(), SELECTION_MODE.NOT_INPUTS);
                    if (finalNode != null && connectionCreatingNode != finalNode) {
                        //Create the connection
                        Connection connection = new Connection(connectionCreatingNode, finalNode);
                        network.addConnection(connection);
                        connection.initialize();
                        //Set neighbors of node
                        finalNode.getInputConnections().add(connection);
                        connectionCreatingNode.getOutputConnections().add(connection);
                        //Turn of the create connection mode
                        connectionCreating = false;
                    }
                }
                //Update the position of a selected node
                if (e.getButton() == MouseEvent.BUTTON1) {
                    //Only for normal nodes
                    selected = checkSelection(e.getX(), e.getY(), SELECTION_MODE.NOT_INPUTS_NOT_OUTPUTS);
                    if (selected != null) {
                        setCursor(new Cursor(Cursor.MOVE_CURSOR));
                        //Update position
                        selected.setEditedPosition(true);
                        selected.setIdX(selected.getX() / (float) getWidth());
                        selected.setIdY(selected.getY() / (float) getHeight());
                    }
                    //Create a new connection
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    //Reset the connection if right clicked
                    if (connectionCreating) {
                        connectionCreating = false;
                    } else {
                        //Get node if available to start the connection. It cant be an output node
                        connectionCreatingNode = checkSelection(e.getX(), e.getY(), SELECTION_MODE.NOT_OUTPUTS);
                        if (connectionCreatingNode != null) {
                            connectionCreating = true;
                            mousePosition = new Vector2(e.getX(), e.getY());
                        }
                    }
                }
                //Create new vertex (node) in the position of mouse when double-clicked main mouse button
                if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
                    //Create the node
                    Node node = new Node(Node.NodeType.NODE);
                    network.addNode(node);
                    node.initialize();
                    node.setEditedPosition(true);
                    node.setX(e.getX(), Core.this);
                    node.setY(e.getY(), Core.this);
                    node.initialize();

                }

            }

        });
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                //Update position of a node
                super.mouseDragged(e);
                if (SwingUtilities.isLeftMouseButton(e)) {
                    if (selected != null) {
                        selected.setX(e.getX(), Core.this);
                        selected.setY(e.getY(), Core.this);

                        updateNetwork();
                    }
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                data.cursorPoint = new Vector2(e.getX(), e.getY());
                showConnectionsOfNode(e.getX(), e.getY());
                if (connectionCreating) {
                    mousePosition = new Vector2(e.getX(), e.getY());
                }

            }
        });

    }
    //Start render of network
    public void startEngine() {
        engine.start();
        interrupted = false;
    }

    //Stop render of network
    public void interruptEngine() {
        engine.interrupt();
        interrupted = true;
    }

    NeuralNetwork network;

    public void paint(Graphics g) {
        //Calculate deltatime
        delta = System.currentTimeMillis() - previousFrameTime;
        previousFrameTime = System.currentTimeMillis();

        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;

        //Set smooth graphics
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(
                RenderingHints.KEY_FRACTIONALMETRICS,
                RenderingHints.VALUE_FRACTIONALMETRICS_ON);

        //Update graphics of network
        if (network != null) {
            viewNetwork(g2);
            renderEditMode(g2);
            data.renderDataFrame(g2, this, network);
        }


    }

    //When creating a connection show the connection line animation
    public void renderEditMode(Graphics g) {

        if (connectionCreating) {
            g.setColor(ColorPalette.connections);
            g.drawLine((int) connectionCreatingNode.getX(), (int) connectionCreatingNode.getY(), (int) mousePosition.x, (int) mousePosition.y);
        }
    }

    public void showConnectionsOfNode(int x, int y) {
        if (network != null) {
            Node hoveredNew = checkSelection(x, y, SELECTION_MODE.ALL_NODES);

            if (hoveredNew != hovered) {
                if (hovered != null) {
                    visualizeConnections(hovered, ColorPalette.connections, ColorPalette.connections);
                }
                hovered = hoveredNew;
            }

            if (hovered != null) {
                visualizeConnections(hovered, ColorPalette.inputHover, ColorPalette.outputHover);
            }
        }

    }

    public void visualizeConnections(Node node, Color inputs, Color outputs) {
        for (Connection connection : node.getInputConnections()) {
            connection.getArrow().setColor(inputs);
        }
        for (Connection connection : node.getOutputConnections()) {
            connection.getArrow().setColor(outputs);
        }
    }

    public Node checkSelection(int x, int y, SELECTION_MODE mode) {
        List<Node> nodes = network.getNodes();
        //Calculate width of network window
        float available_width = (getWidth() - MARGIN_INPUTS - MARGIN_OUTPUTS);
        float possible_width = available_width * NODE_POSITIONING_CENTER_PERCENT;

        //Check selection of normal nodes
        for (int i = 0; i < nodes.size(); i++) {
            boolean intersect = Intersections.pointInNode(new Vector2(x, y), nodes.get(i), SIZE_NEURONS / 2f);
            if (intersect) {
                return nodes.get(i);
            }
        }
        //Check if an output node is selected
        if (mode == SELECTION_MODE.NOT_INPUTS || mode == SELECTION_MODE.ALL_NODES) {
            for (int i = 0; i < network.getOutputs().size(); i++) {
                boolean intersect = Intersections.pointInNode(new Vector2(x, y), network.getOutputs().get(i), SIZE_NEURONS / 2f);
                if (intersect) {
                    return network.getOutputs().get(i);
                }
            }
        }
        //Check if an input node is selected
        if (mode == SELECTION_MODE.NOT_OUTPUTS || mode == SELECTION_MODE.ALL_NODES) {
            for (int i = 0; i < network.getInputs().size(); i++) {
                boolean intersect = Intersections.pointInNode(new Vector2(x, y), network.getInputs().get(i), SIZE_NEURONS / 2f);
                if (intersect) {
                    return network.getInputs().get(i);
                }
            }
        }

        //If not node is selected or cant be selected return null
        return null;
    }

    public void viewNetwork(Graphics2D g) {

        //Titles
        Font font = ColorPalette.font.deriveFont(10f);
        g.setColor(Color.black);
        StringUtils.drawCenteredString(g, "INPUT NODES", new Vector2(MARGIN_INPUTS, 10), font);
        StringUtils.drawCenteredString(g, "OUTPUT NODES", new Vector2(getWidth() - MARGIN_OUTPUTS, 10), font);

        //Render connections
        List<Connection> connections = network.getConnections();
        for (int i = 0; i < connections.size(); i++) {
            connections.get(i).renderArrows(g);
        }

        //Render input nodes
        List<Node> inputs = network.getInputs();

        for (int i = 0; i < inputs.size(); i++) {
            g.setColor(new Color(0, 200, 157, 200));
            g.fillOval((int) (inputs.get(i).getX() - SIZE_NEURONS / 2f), (int) (inputs.get(i).getY() - SIZE_NEURONS / 2f), (int) SIZE_NEURONS, (int) SIZE_NEURONS);
            inputs.get(i).showValue(g, SIZE_NEURONS);
        }

        //Render output nodes
        List<Node> outputs = network.getOutputs();

        for (int i = 0; i < outputs.size(); i++) {
            g.setColor(new Color(0, 169, 243, 200));
            g.fillOval((int) (outputs.get(i).getX() - SIZE_NEURONS / 2f), (int) (outputs.get(i).getY() - SIZE_NEURONS / 2f), (int) SIZE_NEURONS, (int) SIZE_NEURONS);
            outputs.get(i).showValue(g, SIZE_NEURONS);
        }

        //Render rest of nodes
        List<Node> nodes = network.getNodes();

        for (int i = 0; i < nodes.size(); i++) {
            g.setColor(new Color(182, 19, 0, 200));
            g.fillOval((int) (nodes.get(i).getX() - SIZE_NEURONS / 2f), (int) (nodes.get(i).getY() - SIZE_NEURONS / 2f), (int) SIZE_NEURONS, (int) SIZE_NEURONS);
            nodes.get(i).showValue(g, SIZE_NEURONS);
        }

        //Show training process
        if (NeuralNetwork.SHOW_TRAINING_PROCESS) {
            Node n = network.getCurrentTrainingNode();
            if (previousNodeTrained == null) {
                previousNodeTrained = n;
            }
            if (n != previousNodeTrained && previousNodeTrained != null) {

                visualizeConnections(previousNodeTrained, ColorPalette.connections, ColorPalette.connections);
                previousNodeTrained = n;
            }

            if (n != null) {
                g.setColor(Color.black);
                g.drawOval((int) (n.getX() - SIZE_NEURONS / 2f), (int) (n.getY() - SIZE_NEURONS / 2f), (int) SIZE_NEURONS, (int) SIZE_NEURONS);
                visualizeConnections(n, ColorPalette.inputHover, ColorPalette.outputHover);
            }
        }
    }

    public void setNeuralNetwork(NeuralNetwork network) {
        //Set neural network to render
        this.network = network;
        initializeNodesPositioning();
        updateNetwork();
        if (network.type == NeuralNetwork.NetworkType.MULTILAYER) {
            checkTopology();
        }
    }

    public void initializeNodesPositioning() {
        Random rand = new Random();
        List<Node> nodes = network.getNodes();
        for (int i = 0; i < nodes.size(); i++) {
            nodes.get(i).setIdX(rand.nextFloat());
            nodes.get(i).setIdY(rand.nextFloat());
        }
    }

    public void updateNetwork() {

        if (network != null) {

            float availableHeightSpace = getHeight() - 2 * HORIZONTAL_MARGINS;

            List<Node> inputs = network.getInputs();
            float h_space_inputs = (availableHeightSpace) / ((float) inputs.size() + 1f);
            for (int i = 0; i < inputs.size(); i++) {
                float y = HORIZONTAL_MARGINS + (i + 1) * h_space_inputs;
                inputs.get(i).setXAbsolute(MARGIN_INPUTS);
                inputs.get(i).setYAbsolute(y);
            }

            List<Node> outputs = network.getOutputs();
            float h_space_outputs = (availableHeightSpace) / ((float) outputs.size() + 1f);
            for (int i = 0; i < outputs.size(); i++) {
                float y = HORIZONTAL_MARGINS + (i + 1) * h_space_outputs;
                outputs.get(i).setXAbsolute((getWidth() - MARGIN_OUTPUTS));
                outputs.get(i).setYAbsolute(y);
            }

            List<Node> nodes = network.getNodes();
            float available_width = (getWidth() - MARGIN_INPUTS - MARGIN_OUTPUTS);
            float possible_width = available_width * NODE_POSITIONING_CENTER_PERCENT;
            for (int i = 0; i < nodes.size(); i++) {
                if (!nodes.get(i).isEditedPosition()) {
                    nodes.get(i).setXAbsolute((MARGIN_INPUTS + (available_width - possible_width) / 2f + nodes.get(i).getIdX() * possible_width));
                    nodes.get(i).setYAbsolute((HORIZONTAL_MARGINS + nodes.get(i).getIdY() * availableHeightSpace));
                } else {
                    nodes.get(i).setXAbsolute(nodes.get(i).getIdX() * (float) getWidth());
                    nodes.get(i).setYAbsolute(nodes.get(i).getIdY() * (float) getHeight());
                }
            }

            List<Connection> connections = network.getConnections();
            for (int i = 0; i < connections.size(); i++) {
                Vector2 inter = Intersections.intersectNodeConnection(connections.get(i), SIZE_NEURONS / 2f, connections.get(i).getOutNode(), connections.get(i).getInNode());
                Vector2 inter2 = Intersections.intersectNodeConnection(connections.get(i), SIZE_NEURONS / 2f, connections.get(i).getInNode(), connections.get(i).getOutNode());
                connections.get(i).setArrow(new ArrowLine(inter, Intersections.getVectorConnection(connections.get(i)), inter2, connections.get(i)));
                connections.get(i).calcPointArrows();
            }
        }
    }

    public void checkTopology() {
        int c = 0;

        float availableHeightSpace = getHeight() - 2 * HORIZONTAL_MARGINS;
        float w_space_hidden = (getWidth() - MARGIN_INPUTS - MARGIN_OUTPUTS) / (float) (network.getArchitecture().length + 1);
        for (int i = 0; i < network.getArchitecture().length; i++) {
            for (int j = 0; j < network.getArchitecture()[i]; j++) {
                Node node = network.getNodes().get(c);
                float h_space_hidden = (availableHeightSpace) / ((float) network.getArchitecture()[i] + 1f);
                float y = HORIZONTAL_MARGINS + (j + 1) * h_space_hidden;
                float x = MARGIN_INPUTS + (i + 1) * w_space_hidden;

                node.setY(y, this);
                node.setX(x, this);
                c++;
            }
        }

    }
}
