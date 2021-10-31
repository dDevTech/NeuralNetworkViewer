package ai.editor;

import ai.neuralnetwork.architecture.Connection;
import physics.Vector2;
import tools.ColorPalette;

import java.awt.*;
import java.io.Serializable;

public class ArrowLine implements Serializable {
    private Vector2 position;
    private Vector2 direction;
    private Vector2 initialPoint;
    private Connection connection;
    private int[] pointsX;
    private int[]pointsY;
    private final int nPoints=3;

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color color= ColorPalette.connections;



    public ArrowLine(Vector2 position, Vector2 direction, Vector2 initialPoint,Connection connection){

        this.position = position;
        this.direction = direction;
        this.initialPoint = initialPoint;
        this.connection = connection;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getDirection() {
        return direction;
    }
    public void calcPointsArrow(){
        pointsX= new int[3];
        pointsY = new int[3];
        float d=7.5f;
        float h=2.5f;
        float dx = position.x - initialPoint.x, dy = position.y - initialPoint.y;
        double D = Math.sqrt(dx*dx + dy*dy);
        double xm = D - d, xn = xm, ym = h, yn = -h, x;
        double sin = dy / D, cos = dx / D;

        x = xm*cos - ym*sin + initialPoint.x;
        ym = xm*sin + ym*cos + initialPoint.y;
        xm = x;

        x = xn*cos - yn*sin + initialPoint.x;
        yn = xn*sin + yn*cos + initialPoint.y;
        xn = x;

        pointsX = new int[]{(int) position.x, (int) xm, (int) xn};
        pointsY  = new int[]{(int)position.y, (int) ym, (int) yn};
    }
    public void renderArrow(Graphics2D g){
        g.setColor(color);
        g.drawString(Float.toString(connection.getValue()),(position.x+initialPoint.x)/2f,(position.y+initialPoint.y)/2f);
        g.fillPolygon(pointsX,pointsY,nPoints);
        g.drawLine((int)initialPoint.x,(int)initialPoint.y,(int)position.x,(int)position.y);
    }

}
