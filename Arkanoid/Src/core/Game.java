package Arkanoid.Src.core;
import javax.swing.*;

import Arkanoid.Src.entities.Ball;
import Arkanoid.Src.entities.Bricks.*;
import Arkanoid.Src.powerups.PowerUp;
import Arkanoid.Src.entities.Paddle;
import Arkanoid.Src.powerups.PowerUpType;
import Arkanoid.Src.ImageManager.ImageManager;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.event.*;

public class Game extends JPanel implements KeyListener, ActionListener, ComponentListener {

    private ArrayList<Ball> balls = new ArrayList<>();
    private Paddle paddle;
    private ArrayList<Brick> bricks = new ArrayList<>();

    private Timer timer;
    private Level currentLevel;
    private boolean levelCleared = false;
    private int levelClearCounter = 0;
    private int currentLevelNumber;

    private Point score = new Point();
    private ArrayList<PowerUp> powerUps = new ArrayList<>();
    private int lives;

    private enum GameState { PLAYING, PAUSED, LEVEL_CLEAR, GAME_WIN, GAME_OVER }
    private GameState currentState;

    private BufferedImage bgimg;
    
    boolean leftPressed = false;
    boolean rightPressed = false;

    public static final int WIDTH = 680;
    public static final int HEIGHT = 500;
 
    public Game() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        String imgpath ="/Arkanoid/Src/assets/img/other/bg.png";
        this.bgimg = ImageManager.loadImage(imgpath);
        
        setBackground(Color.BLACK);

        lives = 3;
        currentState = GameState.PLAYING;


        //load hình ảnh cho power-up để không bi lag khi lần đầu xuất hiện
        PowerUpType.preloadImages();

        // tạo bóng và paddle`
        Ball ball = new Ball(350, 250, 20);
        balls.add(ball);
        paddle = new Paddle(300, 450, 120, 18);

        currentLevel = new Level(GameManager.getCurrentLevel());
        currentLevelNumber = GameManager.getCurrentLevelNumber();
        bricks = currentLevel.getBricks();

        timer = new Timer(10, this);

        addComponentListener(this);
    }

    public void componentShown(ComponentEvent e) {
        setFocusable(true);
        removeKeyListener(this);
        addKeyListener(this);
        requestFocusInWindow();

        if(currentState == GameState.PLAYING) {
            timer.start();
        }
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
        currentLevelNumber = GameManager.getCurrentLevelNumber();
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
                    currentState = GameState.LEVEL_CLEAR;
                    loadLevel(next);
                } else {
                    currentState = GameState.GAME_WIN;
                    GameManager.reset();
                    loadLevel(GameManager.getCurrentLevel());
                }
            }
        }
    }

    public void actionPerformed(ActionEvent e) {

        if(currentState == GameState.PLAYING) {
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
            loseLife();
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
    }

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        if (this.bgimg != null) {
            g2.drawImage(this.bgimg, 0, 0, getWidth(), getHeight(), this);
        }

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

        if(currentState != GameState.PLAYING && currentState != null) {
            g2.setColor(new Color(0, 0, 0, 150));
            g2.fillRect(0, 0, WIDTH, HEIGHT);

            String title = "";
            String subtitle = "Press ENTER to continue"; // Mặc định
            FontMetrics fm; // Khai báo ở đây

            if (currentState == GameState.LEVEL_CLEAR) {
                title = "LEVEL COMPLETE!";
                g2.setColor(Color.GREEN);
            } else if (currentState == GameState.GAME_WIN) {
                title = "YOU WIN!";
                g2.setColor(Color.YELLOW);
            } else if (currentState == GameState.GAME_OVER) {
                title = "GAME OVER";
                g2.setColor(Color.RED);
            } else if (currentState == GameState.PAUSED) { 
                title = "PAUSED";
                subtitle = "Press P to resume"; 
                g2.setColor(Color.CYAN); 
            }

            g2.setFont(new Font("Arial", Font.BOLD, 40));
            fm = g2.getFontMetrics();
            int titleWidth = fm.stringWidth(title);
            g2.drawString(title, (WIDTH - titleWidth) / 2, HEIGHT / 2 - 20);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.ITALIC, 20));
            fm = g2.getFontMetrics();
            int subtitleWidth = fm.stringWidth(subtitle);
            g2.drawString(subtitle, (WIDTH - subtitleWidth) / 2, HEIGHT / 2 + 30);
        }

        //hiển thị số mạng người chơi
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        g2.setColor(Color.RED);
        g2.drawString("Lives: " + lives, WIDTH - 100, 30);

        //Vẽ chữ nhấn SPACE để bắt đầu game
        for (Ball ball : balls) {
            if (!ball.isLaunched()) {
            g2.setFont(new Font("Arial", Font.ITALIC, 24));
            g2.setColor(Color.orange);

            String message = "Press SPACE to start game";
            FontMetrics fm = g2.getFontMetrics();
            int textWidth = fm.stringWidth(message);

            int x = (WIDTH - textWidth) / 2;
            int y = (int)paddle.getY() + 45;

            g2.drawString(message, x, y);
            }
        }

        // Vẽ level hiện tại
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        g2.setColor(Color.WHITE);
        String levelText = "Level: " + currentLevelNumber;
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(levelText);
        g2.drawString(levelText, (WIDTH - textWidth) / 2, 30);


        // VẼ lại JOptionPane thông báo
        if(currentState != GameState.PLAYING && currentState != null) {
            g2.setColor(new Color(0, 0, 0, 150));
            g2.fillRect(0, 0, WIDTH, HEIGHT);

            String title = "";
            String subtitle = "Press ENTER to continue";

            if (currentState == GameState.LEVEL_CLEAR) {
                title = "LEVEL COMPLETE!";
                g2.setColor(Color.GREEN);
            } else if (currentState == GameState.GAME_WIN) {
                title = "YOU WIN!";
                g2.setColor(Color.YELLOW);
            } else if (currentState == GameState.GAME_OVER) {
                title = "GAME OVER";
                g2.setColor(Color.RED);
            }

            g2.setFont(new Font("Arial", Font.BOLD, 40));
            fm = g2.getFontMetrics();
            int titleWidth = fm.stringWidth(title);
            g2.drawString(title, (WIDTH - titleWidth) / 2, HEIGHT / 2 - 20);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.ITALIC, 20));
            fm = g2.getFontMetrics();
            int subtitleWidth = fm.stringWidth(subtitle);
            g2.drawString(subtitle, (WIDTH - subtitleWidth) / 2, HEIGHT / 2 + 30);
        }
    }

    private void loseLife() {
        lives --;
        if (lives <=0) {
            timer.stop();
            currentState = GameState.GAME_OVER;
        } else {
            paddle = new Paddle(300, 450, 120, 18); // Reset vị trí paddle về giữa
            Ball newBall = new Ball(paddle.getX() + paddle.getWidth() / 2 - 10, paddle.getY() - 20, 20);
            balls.add(newBall);
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
                if (balls.isEmpty()) break;

                Ball ball2 = balls.get(0);
                Ball newBall = new Ball(ball2.getX(), ball2.getY(), ball2.getWidth());
                newBall.launch();
                
                //sửa lỗi ăn powerup khi bóng vẫn đang trên paddle
                if (ball2.isLaunched()) {
                    newBall.setdx(-ball2.getdx());
                    newBall.setdy(-ball2.getdy());
                } else {
                    ball2.launch();
                    newBall.setdx(-newBall.getdx());
                }
                balls.add(newBall);
                break;
            case LASER:
                break;
            case SHIELD:
                break;
            case STICKYPADDLE:
                break;
            case LIFELOSS:
                lives --;
                if(lives <= 0) {
                    timer.stop();
                    currentState = GameState.GAME_OVER;
                }
                break;
        }
    }
    
    

    public void keyPressed(KeyEvent e) {
        if(currentState == GameState.PLAYING) {
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                leftPressed = true;
            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            rightPressed = true;
            }
        }

        if(e.getKeyCode() == KeyEvent.VK_P) {
            if (currentState == GameState.PLAYING) {
                currentState = GameState.PAUSED;
                timer.stop(); 
                repaint(); 
            } else if (currentState == GameState.PAUSED) {
                currentState = GameState.PLAYING;
                timer.start(); 
        }
    }

        if(e.getKeyCode() == KeyEvent.VK_SPACE) {
            for (Ball ball : balls) {
            ball.launch();
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_N) {
            bricks.clear();
        }

        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (currentState == GameState.GAME_OVER || currentState == GameState.GAME_WIN) {
                lives = 3;
                score = new Point();

                if (currentState == GameState.GAME_OVER) {
                    loadLevel(GameManager.getCurrentLevel());
                }
                balls.clear();
                balls.add(new Ball(350, 250, 20));
                paddle = new Paddle(300, 450, 120, 18);

                currentState = GameState.PLAYING;
                timer.start();
            } else if (currentState == GameState.LEVEL_CLEAR) {
               
                balls.clear();
                balls.add(new Ball(350, 250, 20));
                paddle = new Paddle(300, 450, 120, 18);
                
                currentState = GameState.PLAYING;
                timer.start();
            }
        }
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

    