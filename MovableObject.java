import java.awt.*;
public class MovableObject extends GameObject {
    protected double dx, dy;

    public MovableObject(double dx, double dy, int w, int h) {
        super(dx, dy, w, h);
    }

    public void move() {
        x += dx;
        y += dy;
    }
}
