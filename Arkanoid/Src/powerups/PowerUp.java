package Arkanoid.Src.powerups;
import java.awt.*;

import Arkanoid.Src.entities.GameObject;

abstract class PowerUp extends GameObject {
    public PowerUp(double x, double y, int width, int height) {
        super(x, y, width, height);
    }
}
