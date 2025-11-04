package Arkanoid.Src.entities;
import java.awt.image.BufferedImage;
import Arkanoid.Src.ImageManager.ImageManager;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Paddle extends MovableObject {
    private BufferedImage sprite;
    public int speed = 15;
    private final int borderX = 25;

    public Paddle(double x, double y, int width, int height) {
        super(x, y, 120, 18);
        loadSprite();
    }
    public void loadSprite() {
        try {
            sprite = ImageIO.read(new File("Arkanoid/Src/assets/img/actors/paddle.png"));
            if (sprite != null) {
                width = sprite.getWidth();
                height = sprite.getHeight();
            }
        } catch (IOException e) {
            sprite = null;
        }
    }

    public int getSpeed() {
        return speed;
    }

    public void moveLeft() {
        x -= speed;
    }

    public void moveRight() {
        x += speed;
    }

    public void update(int screenWidth) {
        if (x < borderX) {
            x = borderX;
        } else if (x + width > screenWidth - 5) {
            x = screenWidth - 5 - width;
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

    @Override
    public void setX(double x) {
        super.setX(x);
    }

    @Override
    public void setY(double y) {
        super.setY(y);
    }

    @Override
    public void setHeight(int height) {
        super.setHeight(height);
    }

    public void render(Graphics g2) {
        if (sprite != null) {
            int drawX = (int) Math.round(x);
            int drawY = (int) Math.round(y);
            // Draw the image at coordinates (0, 0)
            g2.drawImage(sprite, drawX, drawY, width, height, null);
        } else {
            Graphics2D g2d = (Graphics2D) g2;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color baseColor = Color.BLUE;
            GradientPaint gp = new GradientPaint(
                    (int) x, (int) y, baseColor.brighter(),
                    (int) x, (int) (y + height), baseColor.darker()
            );
            g2d.setPaint(gp);

            int arcSize = 15;
            g2d.fillRoundRect((int) x, (int) y, width, height, arcSize, arcSize);

            g2d.setColor(baseColor.darker().darker());
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRoundRect((int) x, (int) y, width, height, arcSize, arcSize);
        }
    }
}
