package Arkanoid.Src.entities.Bricks;

import Arkanoid.Src.ImageManager.ImageManager;
import java.awt.*;
import java.awt.image.BufferedImage;

public enum BrickType {
    NORMAL(Color.CYAN, "Arkanoid/Src/assets/sounds/Brick_Sound.WAV"),
    STRONG(Color.BLUE, "Arkanoid/Src/assets/sounds/Brick_Sound.WAV"),
    POWER(Color.PINK, "Arkanoid/Src/assets/sounds/Brick_Sound.WAV"),
    EXPLOSIVE(Color.RED,"Arkanoid/Src/assets/sounds/Explosive_Brick.WAV"),
    //SPEED(Color.PINK, "Arkanoid/Src/assets/sounds/Brick_Sound.WAV"),
    UNBREAKABLE(Color.ORANGE, "Arkanoid/Src/assets/sounds/Unbreakable_Brick.WAV");
    private final Color color;
    public final String soundPath;


    BrickType(Color color, String soundPath) {
        this.color = color;
        this.soundPath = soundPath;
    }

    public Color getColor() {
        return color;
    }
}
