package Arkanoid.Src.entities.Bricks;

import Arkanoid.Src.core.GameManager;

public class NormalBrick extends Brick{
    public NormalBrick(double x, double y, int width, int height) {
        super(x, y, width, height, 1, BrickType.NORMAL);
    }

    @Override
    public void takeHit() {
        super.takeHit();
        GameManager.playSound(BrickType.NORMAL.soundPath);
    }
}
