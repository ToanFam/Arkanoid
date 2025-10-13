package Arkanoid.Src.core;

import java.awt.*;

public class Point {
    private int totalScore = 0;

    public int getTotalScore() {
        return totalScore;
    }

    public void updatePoint() {
        totalScore += 100;
    }

    public void render(Graphics2D g2) {
        g2.setColor(Color.GREEN);
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.drawString("Score: " + totalScore, 10, 480);
    }
}
