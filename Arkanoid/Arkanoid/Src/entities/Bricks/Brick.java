package Arkanoid.Src.entities.Bricks;

import Arkanoid.Src.core.GameManager;
import Arkanoid.Src.entities.GameObject;
import Arkanoid.Src.core.Point;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public abstract class Brick extends GameObject {
    private int hitPoints;
    protected BrickType type;
    private boolean status = false;
    protected BufferedImage brickImage;

    public Brick(double x, double y, int width, int height, int hitPoints, BrickType type) {
        super(x, y, 43, 21);
        this.hitPoints = hitPoints;
        this.type = type;
    }

    public BrickType getType() {
        return type;
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public void takeHit() {
        hitPoints--;
        playHitSound();
        if (hitPoints <= 0) {
            status = true;
        }
    }

    private void playHitSound() {
        new Thread(() -> {
            GameManager.playSound(type.soundPath);
    }   ).start();
    }

    public boolean isDestroyed() {
        return status;
    }

    public double getCenterX() { return x + width / 2; }
    public double getCenterY() { return y + height / 2; }

    public void render(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        brickImage = type.getImage();

        if (brickImage != null) {
            g2.drawImage(brickImage, (int) x, (int) y, null);
        } else {
            g2.setColor(getType().getColor());
            g2.fillRect((int) x, (int) y, (int) (width - 0.09 * width), (int) (height - 0.09 * width));
            g2.setColor(Color.WHITE);
            g2.drawRect((int) x, (int) y, (int) width, (int) height);
        }
    }

    public void onDestroyed(ArrayList<Brick> allBricks, Point score) {}

}
