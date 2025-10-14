package Arkanoid.Src.entities;
import java.awt.*;

public class Paddle extends MovableObject {
    public int speed = 15;

    public Paddle(double x, double y, int width, int height) {
        super(x, y, width, height);
    }

    public void moveLeft() {
            x -= speed;
        }

    public void moveRight() {
            x += speed;
    }

    public void update(int screenWidth) {
        if (x < 0) {
            x = 0;
        } else if (x + width > screenWidth) { 
            x = screenWidth - width;
        }
    }

    public double getX() {
        return x;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public double getY() {
        return y;
    }

    public void setWidth(int width) {
        this.width = width;
    }


    public void render(Graphics g2) {
        g2.setColor(Color.BLUE);
        g2.fillRect((int)x, (int)y, width, height);
    }
}
