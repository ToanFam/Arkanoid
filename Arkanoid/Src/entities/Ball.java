package Arkanoid.Src.entities;
import java.awt.*;
import java.awt.geom.*;

public class Ball extends MovableObject {

    private boolean Launched = false;

    public boolean isLaunched() {
        return Launched;
    }   

    public void launch() {
        if(!Launched) {
            dx = 3;
            dy = -3;
            Launched = true;
        }
    }

    public void reset(Paddle paddle) {
        this.x = paddle.getX() + paddle.getWidth() / 2 - this.width / 2;
        this.y = paddle.getY() - this.height;
        Launched = false;
        dx = 0;
        dy = 0;
    }

    public Ball(double x, double y, int size) {
        super(x, y, size, size);
        this.dx = 3;
        this.dy = -3;
    }
    public void move(int screenWidth, int screenHeight) {
        x += dx;
        y += dy;

        if (x < 0 || x + width > screenWidth) {
            dx = -dx; // Đổi hướng khi chạm vào cạnh trái hoặc phải
        }

        if (y < 0) {
            dy = -dy; // Đổi hướng khi chạm vào cạnh trên
        }

    }

    public void bounce() {
        dy = -dy; // Đổi hướng khi chạm vào paddle
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
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

    public double getX() {
        return x;
    }

    public void setdx(double dx) {
        this.dx = dx;
    }

    public void setdy(double dy) {
        this.dy = dy;
    }

    public int getdy() {
        return (int)dy;
    }

    public int getdx() {
        return (int)dx;
    }
    


    public void render(Graphics g) {
        g.setColor(Color.RED);
        g.fillOval((int)x, (int)y, width, height);
    }
}
