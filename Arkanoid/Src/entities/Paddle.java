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

    public void setWidth(int newWidth) {
        this.width = newWidth;
    }

    public void render(Graphics g2) {
        Graphics2D g2d = (Graphics2D) g2;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Color baseColor = Color.BLUE;
        GradientPaint gp = new GradientPaint(
            (int)x, (int)y, baseColor.brighter(), 
            (int)x, (int)(y + height), baseColor.darker()
        );
        g2d.setPaint(gp);

        int arcSize = 15;
        g2d.fillRoundRect((int)x, (int)y, width, height, arcSize, arcSize);

        g2d.setColor(baseColor.darker().darker());
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect((int)x, (int)y, width, height, arcSize, arcSize);
    }
}