package Arkanoid.Src.entities.Bricks;

import Arkanoid.Src.ImageManager.ImageManager;
import java.awt.*;
import java.awt.image.BufferedImage;

public enum BrickType {
    NORMAL(Color.CYAN, "Arkanoid/Src/assets/img/actors/brick_cyan.png","Arkanoid/Src/assets/sounds/Brick_Sound.WAV"),
    STRONG(Color.BLUE, "Arkanoid/Src/assets/img/actors/brick_blue.png", "Arkanoid/Src/assets/sounds/Brick_Sound.WAV"),
    POWER(Color.PINK, "Arkanoid/Src/assets/img/actors/brick_pink.png", "Arkanoid/Src/assets/sounds/Brick_Sound.WAV"),
    EXPLOSIVE(Color.RED, "Arkanoid/Src/assets/img/actors/brick_red.png", "Arkanoid/Src/assets/sounds/Explosive_Brick.WAV"),
    //SPEED(Color.PINK, "Arkanoid/Src/assets/img/actors/brick_cyan.png", "Arkanoid/Src/assets/sounds/Brick_Sound.WAV"),
    UNBREAKABLE(Color.ORANGE, "Arkanoid/Src/assets/img/actors/brick_gold.png", "Arkanoid/Src/assets/sounds/Unbreakable_Brick.WAV");
    private final Color color;
    private final String imagePath;
    private BufferedImage image;
    public final String soundPath;


    BrickType(Color color, String imagePath, String soundPath) {
        this.color = color;
        this.imagePath = imagePath;
        this.image = ImageManager.loadImage(imagePath);
        this.soundPath = soundPath;
    }

    public Color getColor() {
        return color;
    }

    public BufferedImage getImage() {
        return image;
    }

    public static void preloadImanges() {
        BrickType.values();
    }
}
