package Arkanoid.Src.entities.Bricks;

import java.awt.*;

public enum BrickType {
    NORMAL(Color.CYAN, "Arkanoid/Src/assets/sounds/Brick_Sound.WAV"),
    STRONG(Color.BLUE, "Arkanoid/Src/assets/sounds/Brick_Sound.WAV"),
    POWER(Color.MAGENTA, "Arkanoid/Src/assets/sounds/Brick_Sound.WAV"),
    EXPLOSIVE(Color.RED, "Arkanoid/Src/assets/sounds/Explosive_Brick.WAV"),
    SPEED(Color.PINK, "Arkanoid/Src/assets/sounds/Brick_Sound.WAV"),
    UNBREAKABLE(Color.ORANGE, "Arkanoid/Src/assets/sounds/Unbreakable_Brick.WAV");
    public final Color color;
    public final String soundPath;

    BrickType(Color color, String soundPath) {
        this.color = color;
        this.soundPath = soundPath;
    }
}
