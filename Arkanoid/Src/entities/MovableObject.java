package Arkanoid.Src.entities;
public class MovableObject extends GameObject {
    protected double dx, dy;

    public MovableObject(double x, double y, int w, int h) {
        super(x, y, w, h);
        this.dx = 0;
        this.dy = 0;
    }

    public void move() {
        x += dx;
        y += dy;
    }
}