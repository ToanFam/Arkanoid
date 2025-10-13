package Arkanoid.Src.entities.Bricks;

import java.awt.*;

public enum BrickType {
    NORMAL(Color.CYAN),
    STRONG(Color.BLUE),
    POWER(Color.MAGENTA),
    EXPLOSIVE(Color.RED),
    UNBREAKABLE(Color.ORANGE);

    public Color color;
    BrickType(Color c) {
        this.color = c;
    }
}
