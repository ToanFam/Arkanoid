package Arkanoid.Src.entities.Bricks;

import Arkanoid.Src.core.GameManager;

public class PowerBrick extends Brick {
    public PowerBrick(double x, double y, int width, int height) {
        super(x, y, width, height, 1, BrickType.POWER);
    }

    @Override
    public void takeHit() {
        super.takeHit();
        GameManager.playSound(BrickType.NORMAL.soundPath);
    }
}
