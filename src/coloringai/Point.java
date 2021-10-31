package coloringai;

public class Point {
    private  int x;
    private  int y;
    private  float color;

    public Point(int x, int y, float Color){

        this.x = x;
        this.y = y;
        color = Color;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public float getColor() {
        return color;
    }

    public void setColor(float color) {
        this.color = color;
    }
}
