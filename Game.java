import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Game extends JPanel implements KeyListener, ActionListener {

    private Ball ball;
    private Paddle paddle;
    private Timer timer;
    private ArrayList<Brick> bricks;
    private int score = 0;
    
    boolean leftPressed = false;
    boolean rightPressed = false;

    final int WIDTH = 700;
    final int HEIGHT = 500;
 
    public Game() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.WHITE);

        // tạo bóng và paddle
        ball = new Ball(350, 250, 20);
        paddle = new Paddle(300, 450, 120, 18);

        bricks = new ArrayList<>();
        int rows = 5;
        int cols =10;
        int brickWidth = 60;
        int brickHeight = 20;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int x = col * brickWidth + 30;
                int y = row * brickHeight + 50;
                bricks.add(new Brick(x, y, brickWidth, brickHeight));
            }
        }

        setFocusable(true);
        addKeyListener(this);

        timer = new Timer(10, this);
        timer.start();
        }

    public void actionPerformed(ActionEvent e) {

        if(!ball.isLaunched()) {
            ball.reset(paddle);
        }
        else {
            ball.move(WIDTH, HEIGHT);

            if (ball.getY() > HEIGHT) {
                ball.reset(paddle);
            }
        }

        if(ball.getY() + ball.getHeight() >= paddle.getY() &&
           ball.getX() + ball.getWidth() >= paddle.getX() &&
           ball.getX() <= paddle.getX() + paddle.getWidth()) {
            ball.bounce();
        }


        for (int i = 0; i < bricks.size(); i++) {
            Brick brick = bricks.get(i);
            if (ball.getBounds().intersects(brick.getBounds())) {
                ball.bounce();
                bricks.remove(i);
                score += 100;
                break;
            }
        }

        if (leftPressed) {
            paddle.moveLeft();
            paddle.update(WIDTH);
        }

        if (rightPressed) {
            paddle.moveRight();
            paddle.update(WIDTH);
        }

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // vẽ paddle và ball
        ball.render(g);
        paddle.render(g);
        for (Brick brick : bricks) {
            brick.render(g);
        }


    }


    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            leftPressed = true;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
           rightPressed = true;
        }

        if(e.getKeyCode() == KeyEvent.VK_SPACE) {
            ball.launch();
        }
    repaint();
}

    @Override 
    public void keyReleased(KeyEvent e) {
    if (e.getKeyCode() == KeyEvent.VK_LEFT) {
        leftPressed = false;
    } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
        rightPressed = false;
    }
}
    @Override 
    public void keyTyped(KeyEvent e) { }

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

    