package Arkanoid.Src.entities.Bricks;

import java.awt.*;
import java.util.ArrayList;
import Arkanoid.Src.core.Point;

public class ExplosiveBrick extends Brick {

    private final double explosionRadius = 60; // bán kính nổ
    private boolean hasExploded = false;

    // Biến dùng cho hiệu ứng đồ họa
    private int explosionTimer = 0;
    private int explosionDuration = 30;

    public ExplosiveBrick(double x, double y, int width, int height) {
        super(x, y, width, height, 1, BrickType.EXPLOSIVE);
    }

    public boolean getHasExploded() {
        return hasExploded;
    }

    public boolean hasExplosionEnded() {
        return explosionTimer <= explosionDuration;
    }

    @Override
    public void render(Graphics2D g2) {
        if (isDestroyed() && hasExploded && explosionTimer < explosionDuration) {
            // Hiệu ứng shockwave lan ra
            float progress = (float) explosionTimer / explosionDuration;
            int alpha = (int) (180 * (1 - progress));
            int radius = (int) (explosionRadius * progress);
            g2.setColor(new Color(255, 100, 0, Math.max(alpha, 0)));
            g2.setStroke(new BasicStroke(3));
            g2.drawOval((int) (getCenterX() - radius), (int) (getCenterY() - radius),
                    radius * 2, radius * 2);
            explosionTimer++;
            return;
        }

        if (isDestroyed()) return;

        g2.setColor(getType().color);
        g2.fillRect((int)x, (int)y, (int)(width - 0.09 * width), (int)(height - 0.09 * width));
        g2.setColor(Color.WHITE);
        g2.drawRect((int)x, (int)y, (int)width, (int)height);
    }

    @Override
    public void onDestroyed(ArrayList<Brick> allBricks, Point score) {
        if (hasExploded) return;

        hasExploded = true;
        explosionTimer = 10;

        double cx = getCenterX();
        double cy = getCenterY();

        for (Brick b : allBricks) {
            if (b == this || b.isDestroyed()) continue;

            double bx = b.getCenterX();
            double by = b.getCenterY();
            double dist = Math.hypot(cx - bx, cy - by);

            if (dist < explosionRadius) {
                // Gây sát thương giảm theo khoảng cách
                if (dist < explosionRadius / 2) {
                    b.takeHit();
                }
                b.takeHit();

                if (b.isDestroyed()) {
                    score.updatePoint();
                    // Nổ dây chuyền (nếu là ExplosiveBrick khác)
                    b.onDestroyed(allBricks, score);
                }
            }
        }
    }
}
