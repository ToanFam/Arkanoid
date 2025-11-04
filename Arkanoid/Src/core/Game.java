package Arkanoid.Src.core;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import Arkanoid.Src.entities.Ball;
import Arkanoid.Src.entities.Bricks.*;
import Arkanoid.Src.entities.Paddle;
import Arkanoid.Src.powerups.PowerUp;
import Arkanoid.Src.powerups.PowerUpType;
import Arkanoid.Src.rendering.MenuRenderer;

public class Game extends JPanel implements KeyListener, ActionListener {

    // ====== STATE & CẤU HÌNH CƠ BẢN ======
    private enum GameState { MENU, PLAYING, PAUSED, LEVEL_CLEAR, GAME_WIN, GAME_OVER }

    private final int WIDTH = 830;
    private final int HEIGHT = 950;

    private final ArrayList<Ball> balls = new ArrayList<>();
    private double ballSpeedScale = 1.0;
    private javax.swing.Timer slowBallTimer;
    private Paddle paddle;
    private ArrayList<Brick> bricks = new ArrayList<>();
    private final ArrayList<PowerUp> powerUps = new ArrayList<>();

    private Timer timer;
    private Level currentLevel;
    private int currentLevelNumber;
    private boolean levelCleared = false;
    private int levelClearCounter = 0;

    private Point score = new Point();
    private int lives;

    private GameState currentState;

    private boolean leftPressed = false;
    private boolean rightPressed = false;

    private BufferedImage backgroundImage;

    // UI renderers
    private final MenuRenderer menu;
    private final PauseOverlay pauseOverlay = new PauseOverlay(); // lớp của bạn

    // ====== ÂM THANH ======
    private Clip bgmClip;


    public Game() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        addKeyListener(this);

        // ---- load bg ----
        try {
            backgroundImage = ImageIO.read(new File("Arkanoid/Src/assets/img/ui/bg_2.png"));
        } catch (IOException e) {
            setBackground(Color.BLACK);
        }

        menu = new MenuRenderer();
        lives = 3;
        currentState = GameState.MENU;

        // ---- preload ảnh power-up để tránh lag lần đầu rơi ----
        try {
            PowerUpType.preloadImages(); // đảm bảo hàm này có trong enum/class PowerUpType
        } catch (Exception ignore) {}

        // ---- tạo bóng & paddle ban đầu ----
        Ball ball = new Ball(269, 250, 20);
        balls.add(ball);
        paddle = new Paddle(360, 890, 120, 18);

        currentLevel = new Level(GameManager.getCurrentLevel());
        currentLevelNumber = GameManager.getCurrentLevelNumber();
        bricks = currentLevel.getBricks();

        // ---- timer ----
        timer = new Timer(10, this);
        timer.start();

        // ---- âm thanh ----
        initAudio();
    }

    // ====== ÂM THANH ======
    private void initAudio() {
        bgmClip = loadClip("Arkanoid/Src/assets/sounds/music.wav");
    }

    private Clip loadClip(String path) {
        try (AudioInputStream ais = AudioSystem.getAudioInputStream(new File(path))) {
            Clip c = AudioSystem.getClip();
            c.open(ais);
            return c;
        } catch (Exception e) {
            System.err.println("Cannot load sound: " + path + " -> " + e.getMessage());
            return null;
        }
    }

    private void playLoop(Clip c) {
        if (c == null) return;
        if (c.isRunning()) c.stop();
        c.setFramePosition(0);
        c.loop(Clip.LOOP_CONTINUOUSLY);
    }

    private void stop(Clip c) {
        if (c != null && c.isRunning()) c.stop();
    }

    private void playOnce(Clip c) {
        if (c == null) return;
        if (c.isRunning()) c.stop();
        c.setFramePosition(0);
        c.start();
    }

    // ====== LEVEL ======
    private void loadLevel(String path) {
        bricks.clear();
        currentLevel = new Level(path);
        bricks = currentLevel.getBricks();
        currentLevelNumber = GameManager.getCurrentLevelNumber();
    }

    private void checkLevelComplete() {
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

    // ====== GAME LOOP ======
    @Override
    public void actionPerformed(ActionEvent e) {
        if (currentState == GameState.PLAYING) {
            Iterator<Ball> ballIterator = balls.iterator();
            while (ballIterator.hasNext()) {
                Ball ball = ballIterator.next();

                if (!ball.isLaunched()) {
                    ball.reset(paddle);
                } else {
                    ball.move(WIDTH, HEIGHT);

                    if (ball.getY() > HEIGHT) {
                        ballIterator.remove();
                        continue;
                    }
                }

                if (ball.getBounds().intersects(paddle.getBounds())) {
                    ball.bounceOnPaddle(paddle);
                    //playOnce(sfxPaddleClip);
                }

                if (!bricks.isEmpty()) {
                    for (Brick brick : bricks) {
                        if (!brick.isDestroyed()) {
                            ball.handleCollision(brick);
                            if (brick.isDestroyed()) {
                                score.updatePoint();
                                //playOnce(sfxBrickClip);

                                if (Math.random() < 0.3) {
                                    powerUps.add(PowerUp.randomPowerUp(
                                            brick.getCenterX(), brick.getCenterY(), 32, 32));
                                }
                                break;
                            }
                        }
                    }
                }
            }

            // power-ups
            for (int i = 0; i < powerUps.size(); i++) {
                PowerUp p = powerUps.get(i);
                p.move();

                if (p.getBounds().intersects(paddle.getBounds())) {
                    activatePowerUp(p);
                    powerUps.remove(i);
                    i--;
                    continue;
                }

                if (p.getY() > HEIGHT) {
                    powerUps.remove(i);
                    i--;
                }
            }

            // dead?
            if (balls.isEmpty()) {
                loseLife();
            }

            // paddle move
            if (leftPressed) {
                paddle.moveLeft();
                paddle.update(WIDTH - 20);
            }
            if (rightPressed) {
                paddle.moveRight();
                paddle.update(WIDTH - 20);
            }

            // level cleared?
            levelCleared = true;
            for (Brick brick : bricks) {
                if (!brick.isDestroyed() && !(brick instanceof UnbreakableBrick)) {
                    levelCleared = false;
                    break;
                }
            }
            checkLevelComplete();
        }

        repaint();
    }

    // ====== RENDER ======
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        switch (currentState) {
            case MENU: {
                // nền
                if (backgroundImage != null) {
                    g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
                // menu
                menu.render(g2, getWidth(), getHeight());
                break;
            }

            case PLAYING:
            case PAUSED:
            case LEVEL_CLEAR:
            case GAME_WIN:
            case GAME_OVER: {
                // background
                if (backgroundImage != null) {
                    g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }

                // balls + paddle
                for (Ball ball : balls) ball.render(g2);
                paddle.render(g2);

                // powerups
                for (PowerUp p : powerUps) p.render(g2);

                // bricks
                for (Brick brick : bricks) {
                    if (brick.isDestroyed() && brick instanceof ExplosiveBrick) {
                        brick.onDestroyed(bricks, score);
                        if (((ExplosiveBrick) brick).hasExplosionEnded()) {
                            brick.render(g2);
                        }
                    }
                    if (!brick.isDestroyed()) brick.render(g2);
                }

                // score
                score.render(g2);

                // lives
                g2.setFont(new Font("Arial", Font.BOLD, 20));
                g2.setColor(Color.RED);
                g2.drawString("Lives: " + lives, WIDTH - 100, 30);

                // hint press SPACE
                for (Ball ball : balls) {
                    if (!ball.isLaunched()) {
                        g2.setFont(new Font("Arial", Font.ITALIC, 24));
                        g2.setColor(Color.ORANGE);
                        String msg = "Press SPACE to start";
                        FontMetrics fm = g2.getFontMetrics();
                        int tw = fm.stringWidth(msg);
                        int x = (WIDTH - tw) / 2;
                        int y = (int) paddle.getY() - 45;
                        g2.drawString(msg, x, y);
                        break;
                    }
                }

                // level text
                g2.setFont(new Font("Arial", Font.BOLD, 20));
                g2.setColor(Color.WHITE);
                String levelText = "Level: " + currentLevelNumber;
                FontMetrics fm = g2.getFontMetrics();
                int textWidth = fm.stringWidth(levelText);
                g2.drawString(levelText, (WIDTH - textWidth) / 2, 160);

                // OVERLAY CHO LEVEL/GAME OVER/WIN
                if (currentState == GameState.LEVEL_CLEAR
                        || currentState == GameState.GAME_WIN
                        || currentState == GameState.GAME_OVER) {

                    g2.setColor(new Color(0, 0, 0, 168));
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

                // OVERLAY CHO PAUSE (menu nhỏ)
                if (currentState == GameState.PAUSED) {
                    pauseOverlay.render(g2, getWidth(), getHeight());
                    drawPauseMiniMenu(g2);
                }
                break;
            }
        }
    }

    // mini pause menu (Resume / Restart / Quit)
    private void drawPauseMiniMenu(Graphics2D g2) {
        int w = 280, h = 180;
        int x = (WIDTH - w) / 2;
        int y = (HEIGHT - h) / 2;

        g2.setColor(new Color(0, 0, 0, 170));
        g2.fillRoundRect(x, y, w, h, 16, 16);
        g2.setColor(Color.WHITE);
        g2.drawRoundRect(x, y, w, h, 16, 16);

        g2.setFont(new Font("Arial", Font.BOLD, 20));
        g2.drawString("Paused", x + 20, y + 35);

        g2.setFont(new Font("Arial", Font.PLAIN, 16));
        int line = 0;
        g2.drawString("[ENTER] Resume", x + 20, y + 70 + 28 * line++);
        g2.drawString("[R] Restart level", x + 20, y + 70 + 28 * line++);
        g2.drawString("[Q] Quit to Menu", x + 20, y + 70 + 28 * line);
    }

    // ====== GAME FLOW ======
    private void loseLife() {
        lives--;
        if (lives <= 0) {
            currentState = GameState.GAME_OVER;
            stop(bgmClip);
        } else {
            paddle = new Paddle(360, 890, 120, 18);
            Ball newBall = new Ball(
                    (int) (paddle.getX() + paddle.getWidth() / 2 - 10),
                    (int) (paddle.getY() - 20), 20);
            balls.add(newBall);
        }
    }

    private void activatePowerUp(PowerUp p) {
        switch (p.getType()) {
            case WIDEPADDLE: {
                int oldW = paddle.getWidth();
                int newW = oldW + 40;
                double cx = paddle.getX() + oldW / 2.0;
                paddle.setWidth(newW);
                paddle.setX((int) Math.round(cx - newW / 2.0));

                // clamp trong biên
                int ww = WIDTH - 15;
                if (paddle.getX() < 0) paddle.setX(0);
                if (paddle.getX() + paddle.getWidth() > ww) {
                    paddle.setX(ww - paddle.getWidth());
                }
            }
            break;

            case SLOWBALL: {
                applySlowAllBalls(0.5, 8000);
                break;
            }

            case MULTIBALL:
                if (balls.isEmpty()) break;
                Ball base = balls.get(0);
                Ball nb = new Ball(base.getX(), base.getY(), base.getWidth());
                nb.launch();

                if (base.isLaunched()) {
                    nb.setdx(-base.getdx());
                    nb.setdy(-base.getdy());
                } else {
                    base.launch();
                    nb.setdx(-nb.getdx());
                }
                balls.add(nb);
                break;

            case LASER:
                // TODO: triển khai laser ở Paddle
                break;
            case SHIELD:
                // TODO: thêm thanh chắn cuối màn
                break;
            case STICKYPADDLE:
                // TODO: bật flag sticky trong Paddle
                break;
            case LIFELOSS:
                lives--;
                if (lives <= 0) {
                    currentState = GameState.GAME_OVER;
                    stop(bgmClip);
                }
                break;
        }
    }

    // ====== INPUT ======
    @Override
    public void keyPressed(KeyEvent e) {
        switch (currentState) {
            case MENU: {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    currentState = GameState.PLAYING;
                    playLoop(bgmClip);
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    System.exit(0);
                }
                break;
            }

            case PLAYING: {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) leftPressed = true;
                else if (e.getKeyCode() == KeyEvent.VK_RIGHT) rightPressed = true;

                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    for (Ball ball : balls) ball.launch();
                }
                if (e.getKeyCode() == KeyEvent.VK_N) {
                    bricks.clear();
                }
                if (e.getKeyCode() == KeyEvent.VK_P || e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    currentState = GameState.PAUSED;
                    // giữ nhạc chạy khi pause? nếu muốn tắt:
                    // stop(bgmClip);
                }
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // không dùng ở PLAYING (tránh đè hành vi overlay)
                }
                break;
            }

            case PAUSED: {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    currentState = GameState.PLAYING;
                    // playLoop(bgmClip); // nếu đã stop khi pause
                } else if (e.getKeyCode() == KeyEvent.VK_R) {
                    restartLevel();
                } else if (e.getKeyCode() == KeyEvent.VK_Q || e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    goToMenu();
                }
                break;
            }

            case LEVEL_CLEAR:
            case GAME_WIN:
            case GAME_OVER: {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (currentState == GameState.GAME_OVER) {
                        lives = 3;
                        score = new Point();
                        loadLevel(GameManager.getCurrentLevel());
                    }
                    balls.clear();
                    balls.add(new Ball(350, 250, 20));
                    paddle = new Paddle(360, 890, 120, 18);

                    currentState = GameState.PLAYING;
                    playLoop(bgmClip);
                }
                break;
            }
        }
    }

    private void restartLevel() {
        // reload stage hiện tại
        String current = GameManager.getCurrentLevel();
        loadLevel(current);
        balls.clear();
        balls.add(new Ball(350, 250, 20));
        paddle = new Paddle(360, 890, 120, 18);
        currentState = GameState.PLAYING;
        playLoop(bgmClip);
    }

    private void goToMenu() {
        balls.clear();
        powerUps.clear();
        score = new Point();
        lives = 3;
        GameManager.reset();
        loadLevel(GameManager.getCurrentLevel());
        currentState = GameState.MENU;
        stop(bgmClip);
    }

    private void applySlowAllBalls(double factor, int durationMs) {
        if (factor <= 0) return;
        ballSpeedScale = factor;

        for (Ball b : balls) {
            b.setdx(b.getdx() * factor);
            b.setdy(b.getdy() * factor);
        }

        if (slowBallTimer != null && slowBallTimer.isRunning()) {
            slowBallTimer.stop();
        }
        slowBallTimer = new javax.swing.Timer(durationMs, e -> {
            for (Ball b : balls) {
                b.setdx(b.getdx() / factor);
                b.setdy(b.getdy() / factor);
            }
            ballSpeedScale = 1.0;
            ((javax.swing.Timer) e.getSource()).stop();
        });
        slowBallTimer.setRepeats(false);
        slowBallTimer.start();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) leftPressed = false;
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT) rightPressed = false;
    }

    @Override public void keyTyped(KeyEvent e) { }

    // ====== MAIN ======
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
