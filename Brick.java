import java.awt.*;

public class Brick extends GameObject {
    public Brick(double x, double y, int width, int height) {
        super(x, y, width, height);
    }

    public void render(Graphics g2) {
        g2.setColor(Color.GREEN);
        g2.fillRect((int)x, (int)y, width, height);
        g2.setColor(Color.BLACK);
        g2.drawRect((int)x, (int)y, width, height);
    }
}
