package src;

import java.awt.*;

public class Paddle extends MovableObject {

    public Paddle(double x, double y, int width, int height) {
        super(x, y, width, height);
    }


    public void render(Graphics g2) {
        g2.setColor(Color.BLUE);
        g2.fillRect((int)x, (int)y, width, height);
    }
}
