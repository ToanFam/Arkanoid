import javax.swing.*;
import java.awt.*;

public class Game extends JPanel {

    private Ball ball;
    private Paddle paddle;

    public Game() {
        setPreferredSize(new Dimension(700, 500));
        setBackground(Color.WHITE);

        // tạo bóng và paddle
        ball = new Ball(350, 250, 20);
        paddle = new Paddle(300, 450, 100, 15);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // vẽ paddle và ball
        ball.draw(g);
        paddle.draw(g);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Arkanoid Demo");
        Game game = new Game();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(game);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
