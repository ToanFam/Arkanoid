package Arkanoid.Src.powerups;

import java.awt.*;
import Arkanoid.Src.ImageManager.ImageManager;
import java.awt.image.BufferedImage;

public enum PowerUpType {
    WIDEPADDLE(Color.GREEN, "Arkanoid/Src/assets/img/powerup/widepaddle.png"),
    SLOWBALL(Color.BLUE, "Arkanoid/Src/assets/img/powerup/slowball.png"),
    MULTIBALL(Color.ORANGE, "Arkanoid/Src/assets/img/powerup/multipleball.png"),
    LASER(Color.MAGENTA, "Arkanoid/Src/assets/img/powerup/laser.png"),
    SHIELD(Color.CYAN, "Arkanoid/Src/assets/img/powerup/shield.png"),
    STICKYPADDLE(Color.YELLOW, "Arkanoid/Src/assets/img/powerup/stickyball.png"),
    LIFELOSS(Color.RED.darker(), "Arkanoid/Src/assets/img/powerup/lifeloss.png");
    
    private final Color color;
    private final String imagePath;
    private BufferedImage image;

    PowerUpType(Color color, String imagePath) {
        this.color = color;
        this.imagePath = imagePath;
        this.image = ImageManager.loadImage(imagePath);
    }
    
    public Color getColor() {
        return color;
    }

    public BufferedImage getImage() {
        return image;
    }

    public static void preloadImages() {
        PowerUpType.values();
    }
}