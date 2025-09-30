import javax.swing.*;
import java.awt.*;

public class Game extends JPanel {

    private Ball ball;
    private Paddle paddle;

    final int WIDTH = 700;
    final int HEIGHT = 500;

    public Game() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.WHITE);

        // tạo bóng và paddle
        ball = new Ball(350, 250, 14);
        paddle = new Paddle(300, 450, 120, 18);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);



        // vẽ paddle và ball
        ball.render(g);
        paddle.render(g);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Arkanoid");
        Game game = new Game();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(game);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
