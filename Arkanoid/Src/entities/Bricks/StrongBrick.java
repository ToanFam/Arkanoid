package Arkanoid.Src.entities.Bricks;

import Arkanoid.Src.core.GameManager;

import java.awt.*;

public class StrongBrick extends Brick {
    public StrongBrick(double x, double y, int width, int height) {
        super(x, y, width, height, 2, BrickType.STRONG);
    }

    @Override
    public void render(Graphics2D g2) {
        g2.setColor(Color.BLACK);
        g2.fillRect((int) x, (int) y, (int) (width), (int) (height));
        if (getHitPoints() == 1) {
            g2.setColor(BrickType.NORMAL.getColor());
        } else {
            g2.setColor(getType().getColor());
        }
        g2.fillRect((int) x, (int) y, (int) (width - 0.09 * width), (int) (height - 0.09 * width));
        g2.setColor(Color.WHITE);
        g2.drawRect((int) x, (int) y,
                (int) width, (int) height);
    }

    @Override
    public void takeHit() {
        super.takeHit();
        GameManager.playSound(BrickType.NORMAL.soundPath);
    }
}
