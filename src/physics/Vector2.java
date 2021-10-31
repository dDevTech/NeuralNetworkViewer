package physics;

import java.io.Serializable;

public class Vector2 implements Serializable {
    public  float x;
    public  float y;

    public Vector2(float x, float y){

        this.x = x;
        this.y = y;
    }
    public Vector2(float x1,float y1,float x2,float y2){
        x=x2-x1;
        y=y2-y1;
    }
    public Vector2 normalize(){
        float magnitude= magnitude();
        return new Vector2(x/magnitude,y/magnitude);
    }
    public float getScope(){
        return y/x;
    }
    public float magnitude(){
        return (float)(Math.sqrt(x*x+y*y));
    }
    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
