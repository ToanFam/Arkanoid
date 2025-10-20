package Arkanoid.Src.powerups;
import java.awt.*;
import java.util.Random;

import Arkanoid.Src.entities.GameObject;

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
        g2.setColor(type.getColor());
        g2.fillOval((int)x, (int)y, (int) width,(int) height);
        g2.setColor(Color.WHITE);
        g2.drawOval((int)x, (int)y, (int) width,(int) height);
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        g2.drawString(type.getSymbol(), (int)x +6, (int)y + 14);
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
