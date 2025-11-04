package Arkanoid.Src.rendering;

import Arkanoid.Src.ImageManager.ImageManager;

import java.awt.*;
import java.awt.image.BufferedImage;

public class MenuRenderer {

    public static void render(Graphics g, int w, int h) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw background image or gradient fallback
        BufferedImage bg = ImageManager.menuBackground();
        if (bg != null) {
            g2.drawImage(bg, 0, 0, w, h, null);
        } else {
            GradientPaint gp = new GradientPaint(0, 0, new Color(30,30,40), 0, h, new Color(10,10,20));
            g2.setPaint(gp);
            g2.fillRect(0, 0, w, h);
        }

        // Title
        String title = "ARKANOID";
        g2.setFont(new Font("SansSerif", Font.BOLD, 64));
        FontMetrics fm = g2.getFontMetrics();
        int tx = (w - fm.stringWidth(title)) / 2;
        int ty = h / 3;
        g2.setColor(new Color(255,255,255));
        g2.drawString(title, tx, ty);

        // Subtitle
        String hint = "Press ENTER to Play   ·   Esc to Quit";
        g2.setFont(new Font("SansSerif", Font.PLAIN, 20));
        FontMetrics fm2 = g2.getFontMetrics();
        int hx = (w - fm2.stringWidth(hint)) / 2;
        int hy = ty + 60;
        g2.setColor(new Color(230,230,230));
        g2.drawString(hint, hx, hy);
    }
}