import java.awt.*;
import java.awt.geom.*;

public class Ball extends MovableObject {
    double x, y;
    int size;

    public Ball(double x, double y, int size) {
        super(x, y, size, size);
    }


    public void render(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillOval((int)x, (int)y, width, height);
    }
}
