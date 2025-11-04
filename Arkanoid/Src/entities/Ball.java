package Arkanoid.Src.entities;
import java.awt.*;
import Arkanoid.Src.entities.Bricks.*;

public class Ball extends MovableObject {
    private double radius;
    private boolean Launched = false;

    public boolean isLaunched() {
        return Launched;
    }   

    public void launch() {
        if(!Launched) {
            dx = 6.5;
            dy = -6.5;
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
        this.dx = 6.5;
        this.dy = -6.5;
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
        dy = -dy;
    }

    public void handleCollision(Brick brick) {
        Rectangle ballBounds = this.getBounds();
        Rectangle brickBounds = brick.getBounds();

        if (!ballBounds.intersects(brickBounds)) {
            return;
        }

        brick.takeHit();

        Rectangle intersection = ballBounds.intersection(brickBounds);

        if (intersection.width < intersection.height) {
            this.dx = -this.dx;

            if (this.x < brickBounds.getCenterX()) {
                this.x -= intersection.width;
            } else {
                this.x += intersection.width;
            }

        } else {
            this.dy = -this.dy;

            if(this.y < brickBounds.getCenterY()) {
                this.y -= intersection.height;
            } else {
                this.y += intersection.height;
            }
        }
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

    public double getdx() {
        return dx;
    }

    public double getdy() {
        return dy;
    }

    public void setdx(double dx) {
        this.dx = dx;
    }

    public void setdy(double dy) {
        this.dy = dy;
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int)(x - radius), (int)(y - radius), width, height);
    }

    public void bounceOnPaddle (Paddle paddle) {
        // đặt lại ngay trên paddle
        this.setY(paddle.getY() - this.getRadius() - 1);

        double paddleCenter = paddle.getX() + (double)paddle.getWidth() / 2; //tâm paddle
        double dist = this.x - paddleCenter; // tâm quả bóng đến tâm paddle

        //chuẩn hóa khoảng cách
        double normDist = dist / ((double)paddle.getWidth() / 2);

        //vân tốc mới phụ thuộc vào khoảng cách giữa 2 tâm
        double maxDx = 6.5;
        this.dx = normDist * maxDx;
        this.dy = -Math.abs(this.dy); // bóng bật lên

        // tránh bóng bay quá chậm
        if (Math.abs(this.dy) < 3) {
            this.dy = -3;
        } 
    }

    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Color baseColor = Color.RED;
        GradientPaint gp = new GradientPaint(
            (float)(x - radius), (float)(y - radius), baseColor.darker().darker(),
            (float)(x + radius), (float)(y + radius), baseColor.brighter().brighter()
        );
        g2d.setPaint(gp);
        g2d.fillOval((int)(x - radius), (int)(y - radius), width, height);
        g2d.setColor(Color.WHITE);
        g2d.fillOval((int)(x - radius*0.5), (int)(y - radius * 0.7), (int)(width * 0.4), (int)(height * 0.4));
    }
}
