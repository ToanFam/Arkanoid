package Arkanoid.Src.entities.Bricks;

import Arkanoid.Src.core.GameManager;

public class UnbreakableBrick extends Brick {
    public UnbreakableBrick(double x, double y, int width, int height) {
        super(x, y, width, height, 10, BrickType.UNBREAKABLE);
    }

    @Override
    public void takeHit() {
        GameManager.playSound(BrickType.UNBREAKABLE.soundPath);
    }
}
