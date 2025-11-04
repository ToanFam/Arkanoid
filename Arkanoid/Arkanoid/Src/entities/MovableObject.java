package Arkanoid.Src.entities;

import java.awt.*;

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

    @Override
    public double getX() {
        return super.getX();
    }

    @Override
    public void setX(double x) {
        super.setX(x);
    }

    @Override
    public double getY() {
        return super.getY();
    }

    @Override
    public void setY(double y) {
        super.setY(y);
    }

    @Override
    public int getWidth() {
        return super.getWidth();
    }

    @Override
    public void setWidth(int width) {
        super.setWidth(width);
    }

    @Override
    public int getHeight() {
        return super.getHeight();
    }

    @Override
    public void setHeight(int height) {
        super.setHeight(height);
    }

    @Override
    public Rectangle getBounds() {
        return super.getBounds();
    }

    @Override
    public void update() {
        super.update();
    }
}
