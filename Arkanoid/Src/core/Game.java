package Arkanoid.Src.core;
import javax.swing.*;

import Arkanoid.Src.entities.Ball;
import Arkanoid.Src.entities.Bricks.*;
import Arkanoid.Src.entities.Paddle;
import Arkanoid.Src.powerups.PowerUp;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import java.lang.reflect.*;
import java.util.Iterator;

public class Game extends JPanel implements KeyListener, ActionListener {

    private ArrayList<Ball> balls = new ArrayList<>();
    private Paddle paddle;
    private Timer timer;
    private ArrayList<Brick> bricks = new ArrayList<>();
    private Point score = new Point();
    private ArrayList<PowerUp> powerUps = new ArrayList<>();
    
    boolean leftPressed = false;
    boolean rightPressed = false;

    final int WIDTH = 680;
    final int HEIGHT = 500;
 
    public Game() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);

        // tạo bóng và paddle`
        Ball ball = new Ball(350, 250, 15);
        balls.add(ball);
        paddle = new Paddle(300, 450, 120, 18);

        int rows = 7;
        int cols = 12;
        int brickWidth = 55;
        int brickHeight = 25;
        int startX = 10, startY = 10;

        Random r = new Random();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int rnd = r.nextInt(100);
                if (rnd < 10) bricks.add(new UnbreakableBrick(startX + j * brickWidth, startY + i * brickHeight, brickWidth, brickHeight));
                else if (rnd < 30) bricks.add(new ExplosiveBrick(startX + j * brickWidth, startY + i * brickHeight, brickWidth, brickHeight));
                else if (rnd < 45) bricks.add(new StrongBrick(startX + j * brickWidth, startY + i * brickHeight, brickWidth, brickHeight));
                else if (rnd < 55) bricks.add(new PowerBrick(startX + j * brickWidth, startY + i * brickHeight, brickWidth, brickHeight));
                else bricks.add(new NormalBrick(startX + j * brickWidth, startY + i * brickHeight, brickWidth, brickHeight));
            }
        }

        setFocusable(true);
        addKeyListener(this);

        try {
        Ball warmupBall = new Ball(10, 10, 10);
        NormalBrick warmupBrick = new NormalBrick(0, 0, 10, 10);
        warmupBall.handleCollision(warmupBrick); 

        PowerUp.randomPowerUp(0, 0, 1, 1); 
        } catch (Exception ex) {
        
        }

        timer = new Timer(10, this);
        timer.start();
        }

    public void actionPerformed(ActionEvent e) {

        Iterator<Ball> ballIterator = balls.iterator();
        while (ballIterator.hasNext()) {
            Ball ball = ballIterator.next();
            if(!ball.isLaunched()) {
                ball.reset(paddle);
            } else {
                ball.move(WIDTH, HEIGHT);

                if (ball.getY() > HEIGHT) {
                    ballIterator.remove();
                    continue;
                }
            }
    

        if(ball.getBounds().intersects(paddle.getBounds())) {
            ball.bounce();
            // điều chỉnh vị trí của bóng để tránh việc bóng bị "dính" vào paddle
            ball.setY(paddle.getY() - ball.getRadius() - 5);
        }

        

        // Kiểm tra va chạm với brick và ghi điểm
        if (!bricks.isEmpty()) {
            for (Brick brick : bricks) {
                if (!brick.isDestroyed()) {
                    ball.handleCollision(brick);
                    if (brick.isDestroyed()) {
                        score.updatePoint();

                        if(Math.random() < 0.3){
                            powerUps.add(PowerUp.randomPowerUp(brick.getCenterX(), brick.getCenterY(), 20, 20));
                        }
                        break;
                }
            }
        }
    }
}

         for (int i = 0; i < powerUps.size(); i++) {
        PowerUp p = powerUps.get(i);
        p.move();

        if(p.getBounds().intersects(paddle.getBounds())) {
            activatePowerUp(p);
            powerUps.remove(i);
            i--;
            continue;
        }

        if(p.getY() > HEIGHT) {
            powerUps.remove(i);
            i--;
        }
    }

    if (balls.isEmpty()) {
        Ball newBall = new Ball(paddle.getX() + paddle.getWidth() / 2 - 10, paddle.getY() - 20, 15);
        balls.add(newBall);
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
        Graphics2D g2 = (Graphics2D) g;

        // vẽ paddle và ball
        for (Ball ball : balls) {
            ball.render(g2);
        }
        paddle.render(g2);

        for( PowerUp p : powerUps) {
            p.render(g2);
        }
        
        // vẽ gạch
        for (Brick brick : bricks) {
            if (brick.isDestroyed() && brick instanceof ExplosiveBrick) {
                brick.onDestroyed(bricks, score);
                if (((ExplosiveBrick) brick).hasExplosionEnded()) {
                    brick.render(g2);
                }
            }
            if (!brick.isDestroyed()) {
                brick.render(g2);
            }
        }

        // Hiển thị điểm
        score.render(g2);

        for (Ball ball : balls) {
            if (!ball.isLaunched()) {
            g2.setFont(new Font("Arial", Font.ITALIC, 24));
            g2.setColor(Color.orange);

            String message = "Press SPACE to start game";
            FontMetrics fm = g2.getFontMetrics();
            int textWidth = fm.stringWidth(message);

        // căn giữa theo chiều ngang, đặt ở 1/3 chiều cao màn hình
            int x = (WIDTH - textWidth) / 2;
            int y = (int)paddle.getY() + 45;

            g2.drawString(message, x, y);
            }

        }
    }

    private void activatePowerUp(PowerUp p) {
        switch (p.getType()) {
            case WIDEPADDLE:
                paddle.setWidth(paddle.getWidth() + 40);
                break;
            case SLOWBALL:
            for (Ball ball : balls) {
                ball.setdx(ball.getdx() * 0.5);
                ball.setdy(ball.getdy() * 0.5);
                break;
            }
            break;
            case MULTIBALL:
            Ball ball2 = balls.get(0);
            Ball newBall = new Ball(ball2.getX(), ball2.getY(), ball2.getWidth());
            newBall.launch();
            newBall.setdx(-ball2.getdx());
            newBall.setdy(-ball2.getdy());
            balls.add(newBall);
            break;
        }
    }
    
    

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            leftPressed = true;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
           rightPressed = true;
        }

        if(e.getKeyCode() == KeyEvent.VK_SPACE) {
            for (Ball ball : balls) {
            ball.launch();
            }
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

    