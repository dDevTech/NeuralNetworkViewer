package ai.editor;

import ai.neuralnetwork.architecture.Node;
import physics.Vector2;
import tools.StringUtils;

import java.awt.*;

public class NodeView {
    //Visualization
    private float x;
    private float y;

    private float idX;
    private float idY;

    private Node node;

    public void setNode(Node node){
        this.node=node;
    }

    private boolean editedPosition=false;


    public float getX() {
        return x;
    }

    public void setX(float x,Core core) {
        this.x = x;
        this.setEditedPosition(true);
        this.setIdX(x/(float)core.getWidth());
    }

    public float getY() {
        return y;
    }

    public void setY(float y,Core core) {
        this.setEditedPosition(true);
        this.y = y;
        this.setIdY(y/(float)core.getHeight());
    }
    public void setXAbsolute(float x){
        this.x=x;
    }
    public void setYAbsolute(float y){
        this.y=y;
    }
    public float getIdX() {
        return idX;
    }

    public void setIdX(float idX) {
        this.idX = idX;
    }

    public float getIdY() {
        return idY;
    }

    public void setIdY(float idY) {
        this.idY = idY;
    }
    public boolean isEditedPosition() {
        return editedPosition;
    }

    public void setEditedPosition(boolean editedPosition) {
        this.editedPosition = editedPosition;
    }
    public void showValue(Graphics2D g,float SIZE_NEURONS){
        g.drawString(Float.toString(node.getOutValue()),x,y-20);
        if(node.getNodeType()!= Node.NodeType.INPUT_NODE) {
            g.drawString("B " + Float.toString(node.getBias()), x, y + SIZE_NEURONS / 2 + 10);
        }
        g.setColor(Color.white);

        StringUtils.drawCenteredString(g, (Integer.toString(node.getNodeID())), new Vector2(getX(), getY()), g.getFont());



    }


}
