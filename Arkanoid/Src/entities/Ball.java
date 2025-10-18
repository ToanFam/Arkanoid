package Arkanoid.Src.entities;
import java.awt.*;
import java.awt.geom.*;
import Arkanoid.Src.entities.Bricks.*;

public class Ball extends MovableObject {
    private double radius;
    private boolean Launched = false;

    public boolean isLaunched() {
        return Launched;
    }   

    public void launch() {
        if(!Launched) {
            dx = 5;
            dy = -5;
            Launched = true;
        }
    }

    public void reset(Paddle paddle) {
        this.x = paddle.getX() + (double) paddle.getWidth() / 2;
        this.y = paddle.getY() - this.radius * 1.5;
        Launched = false;
        dx = 0;
        dy = 0;
    }

    public Ball(double x, double y, int size) {
        super(x, y, size, size);
        this.dx = 5;
        this.dy = -5;
        this.radius = (double) size / 2;
    }
    public void move(int screenWidth, int screenHeight) {
        x += dx;
        y += dy;

        // Va chạm với tường
        if (x - radius <= 0) {
            dx *= -1;
            x = radius + 1;
        }
        if (x + radius >= screenWidth) {
            dx *= -1;
            x = screenWidth - radius - 1;
        }
        if (y - radius <= 0) {
            dy *= -1;
            y = radius + 1;
        }

    }

    public void bounce() {
        dy = -dy; // Đổi hướng khi chạm vào paddle
    }

    // lấy tọa độ điểm thuộc Rect gần Ball nhất
    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    public boolean checkCollision(Brick brick) {
        double closestX = clamp(x, brick.x, brick.x + brick.width);
        double closestY = clamp(y, brick.y, brick.y + brick.height);
        double dx = x - closestX;
        double dy = y - closestY;
        return (dx * dx + dy * dy) < (radius * radius);
    }

    public void handleCollision(Brick brick) {
        if (!checkCollision(brick)) return;

        double closestX = clamp(x, brick.x, brick.x + brick.width);
        double closestY = clamp(y, brick.y, brick.y + brick.height);

        double nx = x - closestX;
        double ny = y - closestY;
        double length = Math.sqrt(nx * nx + ny * ny);
        if (length == 0) return;

        // Phản chiếu vector vận tốc
        nx /= length;
        ny /= length;

        double dot = dx * nx + dy * ny;
        dx = dx - 2 * dot * nx;
        dy = dy - 2 * dot * ny;

        brick.takeHit();

        // Đẩy ra ngoài
        x = closestX + nx * (radius + 1);
        y = closestY + ny * (radius + 1);
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

    public double getRadius() {
        return radius;
    }

    public double getY() {
        return y;
    }

    public double getX() {
        return x;
    }

    public void render(Graphics g) {
        g.setColor(Color.RED);
        g.fillOval((int) (x - radius), (int) (y - radius), width, height);
    }
}
