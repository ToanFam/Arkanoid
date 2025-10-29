package Arkanoid.Src.core;
import javax.swing.*;

import Arkanoid.Src.entities.Ball;
import Arkanoid.Src.entities.Bricks.*;
import Arkanoid.Src.powerups.PowerUp;
import Arkanoid.Src.entities.Paddle;
import Arkanoid.Src.powerups.PowerUpType;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class Game extends JPanel implements KeyListener, ActionListener, ComponentListener {

    private ArrayList<Ball> balls = new ArrayList<>();
    private Paddle paddle;
    private Timer timer;
    private Level currentLevel;
    private boolean levelCleared = false;
    private int levelClearCounter = 0;
    private ArrayList<Brick> bricks = new ArrayList<>();
    private Point score = new Point();
    private ArrayList<PowerUp> powerUps = new ArrayList<>();
    
    boolean leftPressed = false;
    boolean rightPressed = false;

    public static final int WIDTH = 680;
    public static final int HEIGHT = 500;
 
    public Game() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);

        //load hình ảnh cho power-up để không bi lag khi lần đầu xuất hiện
        PowerUpType.preloadImages();

        // tạo bóng và paddle`
        Ball ball = new Ball(350, 250, 20);
        balls.add(ball);
        paddle = new Paddle(300, 450, 120, 18);

        currentLevel = new Level(GameManager.getCurrentLevel());
        bricks = currentLevel.getBricks();

        timer = new Timer(10, this);

        addComponentListener(this);
    }

    public void componentShown(ComponentEvent e) {
        setFocusable(true);
        removeKeyListener(this);
        addKeyListener(this);
        requestFocusInWindow();
        timer.start();
    }

    public void componentHidden(ComponentEvent e) {
        timer.stop();
        removeKeyListener(this);
    }

    public void componentMoved(ComponentEvent e) {}
    public void componentResized(ComponentEvent e) {}



    private void loadLevel(String path) {
        bricks.clear();
        currentLevel = new Level(path);
        bricks = currentLevel.getBricks();
    }

    private void checkLevelComplete() {
        // Chuyển level
        if (bricks.isEmpty() || levelCleared) {
            levelClearCounter++;
            if (levelClearCounter >= 20) {
                levelCleared = false;
                levelClearCounter = 0;
                String next = GameManager.getNextLevel();
                if (next != null) {
                    JOptionPane.showMessageDialog(this, "Level Completed! Loading next...");
                    loadLevel(next);
                } else {
                    JOptionPane.showMessageDialog(this, "🎉 You Win! All levels cleared!");
                    GameManager.reset();
                    loadLevel(GameManager.getCurrentLevel());
                }
                balls.clear();
                balls.add(new Ball(350, 250, 20));
                paddle = new Paddle(300, 450, 120, 18);
            }
        }
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
                ball.bounceOnPaddle(paddle);
            }

            // Kiểm tra va chạm với brick và ghi điểm
            if (!bricks.isEmpty()) {
                for (Brick brick : bricks) {
                    if (!brick.isDestroyed()) {
                        ball.handleCollision(brick);
                        if (brick.isDestroyed()) {
                            score.updatePoint();

                            if(Math.random() < 0.3){
                                powerUps.add(PowerUp.randomPowerUp(brick.getCenterX(), brick.getCenterY(), 32, 32));
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
            Ball newBall = new Ball(paddle.getX() + paddle.getWidth() / 2 - 10, paddle.getY() - 20, 20);
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

        levelCleared = true;
        for (Brick brick : bricks) {
            if (!brick.isDestroyed() && !(brick instanceof UnbreakableBrick)) {
                levelCleared = false;
            }
        }

        checkLevelComplete();

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
            case LASER:
                break;
            case SHIELD:
                break;
            case STICKYPADDLE:
                break;
            case LIFELOSS:
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
        if(e.getKeyCode() == KeyEvent.VK_N) {
            bricks.clear();
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

}

    