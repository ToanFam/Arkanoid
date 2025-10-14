package Arkanoid.Src.powerups;

import java.awt.*;

public enum PowerUpType {
    WIDEPADDLE(Color.GREEN, "W"),
    SLOWBALL(Color.BLUE, "S"),
    MULTIBALL(Color.ORANGE, "M");

    private final Color color;
    private final String symbol;

    PowerUpType(Color color, String symbol) {
        this.color = color;
        this.symbol = symbol;
    }
    
    public Color getColor() {
        return color;
    }

    public String getSymbol() {
        return symbol;
    }
}
