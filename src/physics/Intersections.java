package physics;

import ai.neuralnetwork.architecture.Connection;
import ai.neuralnetwork.architecture.Node;

public class Intersections {
    public static Vector2 intersectNodeConnection(Connection connection,float radius,Node collide, Node other){
        Vector2 v = getVectorConnection(connection);
        float scope=v.getScope();

        float angle=(float)Math.atan(scope);
        float x=(float)(Math.cos(angle)*radius);
        float y=(float)(Math.sin(angle)*radius);
        if(other.getX()<collide.getX()){
            x*=-1f;
            y*=-1f;
        }
        return new Vector2(collide.getX()+x,collide.getY()+y);
    }



    public static boolean pointInNode(Vector2 point,Node node,float radius){
        return new Vector2(point.x,point.y,node.getX(),node.getY()).magnitude()<radius;
    }
    public static Vector2 getVectorConnection(Connection connection){
        return new Vector2(connection.getOutNode().getX(),connection.getOutNode().getY(),connection.getInNode().getX(),connection.getInNode().getY());
    }
}

