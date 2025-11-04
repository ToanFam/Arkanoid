package Arkanoid.Src.powerups;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.Random;

import Arkanoid.Src.entities.GameObject;
import java.awt.image.BufferedImage;

public class PowerUp extends GameObject {
    private double dy = 2.0;
    private PowerUpType type;

    public PowerUp(double x, double y, int width, int height, PowerUpType type) {
        super(x, y, width, height);
        this.type = type;
    }

    public PowerUpType getType() {
        return type;
    }

    public void move() {
        y += dy;
    }

    public void render(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        BufferedImage powerUpImage = type.getImage();

        if (powerUpImage != null) {
            g2.drawImage(powerUpImage, (int)x, (int)y, width, height, null);
        } else {
            Color baseColor = type.getColor();
            int arc = width / 2;
            RoundRectangle2D.Double capsule = new RoundRectangle2D.Double(x, y, width, height, arc, arc);
            GradientPaint gp = new GradientPaint(
                (int)x, (int)y, baseColor.brighter().brighter(),
                (int)x, (int)y + height, baseColor.darker()
            );
            g2.setPaint(gp);
            g2.fill(capsule);
            g2.setColor(baseColor.darker().darker());
            g2.setStroke(new BasicStroke(2));
            g2.draw(capsule);
            
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, (int)(width * 0.5))); // Cỡ chữ nhỏ hơn một chút
            FontMetrics fm = g2.getFontMetrics();
            String fallbackSymbol = type.name().substring(0, 1); // Lấy chữ cái đầu của tên ENUM
            int textWidth = fm.stringWidth(fallbackSymbol);
            int textHeight = fm.getAscent();
            g2.drawString(fallbackSymbol, (int)(x + (width - textWidth) / 2), (int)(y + (height - textHeight) / 2 + textHeight));
        }
    }

       

    public static PowerUp randomPowerUp(double x, double y, int width, int height) {
        PowerUpType[] types = PowerUpType.values();
        Random r= new Random();
        PowerUpType randomType = types[r.nextInt(types.length)];
        return new PowerUp(x, y, width, height, randomType);
    }

    public int getY() {
        return (int) y;
    }
}
