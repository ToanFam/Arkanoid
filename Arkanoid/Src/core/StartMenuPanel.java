package Arkanoid.Src.core;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import Arkanoid.Src.ImageManager.ImageManager;

public class StartMenuPanel extends JPanel implements ActionListener, MouseListener, MouseMotionListener, ComponentListener, KeyListener {

    private JPanel mainPanel;
    private CardLayout cardLayout;
    private Game gamePanel;
    private BufferedImage bgimg;

    private Timer animationTimer;
    private boolean showBlinkingText = true;

    private Font retroFontBig; 
    private Font retroFontMedium;

    private Color highlightColor = new Color(0, 255, 255); 
    private Color defaultColor = Color.WHITE;
   
    private Rectangle startButtonBounds;
    private Rectangle exitButtonBounds;

    private boolean isStartHovered = false;
    private boolean isExitHovered = false;
    private boolean isStartPressed = false;
    private boolean isExitPressed = false;

    private int selectedOption;
    private static final int START_GAME = 0;
    private static final int EXIT = 1;

    private Color gridColor = new Color(50, 20, 100, 100);
    private int gridSize = 40; 

    public StartMenuPanel(JPanel mainPanel, CardLayout cardLayout, Game gamePanel) {
        this.mainPanel = mainPanel;
        this.cardLayout = cardLayout;
        this.gamePanel = gamePanel;

        setLayout(null);
        setOpaque(true);
        setBackground(new Color(15, 5, 30));

        setPreferredSize(new Dimension(Game.WIDTH, Game.HEIGHT));

        try {
            retroFontBig = new Font("Monospaced", Font.BOLD, 32); 
            retroFontMedium = new Font("Monospaced", Font.BOLD, 24); 
        } catch (Exception e) {
            e.printStackTrace();
            retroFontBig = new Font(Font.MONOSPACED, Font.BOLD, 32);
            retroFontMedium = new Font(Font.MONOSPACED, Font.BOLD, 24);
        }

        String imgpath = "/Arkanoid/Src/assets/img/other/arkanoid.png";
        this.bgimg = ImageManager.loadImage(imgpath);
        
        addMouseListener(this);
        addMouseMotionListener(this);
        addComponentListener(this);
        addKeyListener(this);

        animationTimer = new Timer(500, this);
        animationTimer.setActionCommand("ANIMATE");

        int buttonWidth = 280;
        int buttonHeight = 50; 
        int startY = Game.HEIGHT / 2 + 50;
        int exitY = startY + buttonHeight + 25; 

        startButtonBounds = new Rectangle((Game.WIDTH - buttonWidth) / 2, startY, buttonWidth, buttonHeight);
        exitButtonBounds = new Rectangle((Game.WIDTH - buttonWidth) / 2, exitY, buttonWidth, buttonHeight);

        selectedOption = START_GAME;
        updateHoverStates();
    }

    private void updateHoverStates() {
        isStartHovered = (selectedOption == START_GAME);
        isExitHovered = (selectedOption == EXIT);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("ANIMATE")) {
            showBlinkingText = !showBlinkingText;
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        drawBackgroundGrid(g2);

        if (this.bgimg != null) {
            g2.drawImage(this.bgimg, 0, 0, getWidth(), getHeight(), this);
        } else {
            g2.setColor(new Color(15, 5, 30));
            g2.fillRect(0, 0, getWidth(), getHeight());
        }

        drawButton(g2, startButtonBounds, "START GAME", isStartHovered, isStartPressed);
        drawButton(g2, exitButtonBounds, "EXIT", isExitHovered, isExitPressed);

    }

    private void drawBackgroundGrid(Graphics2D g2d) {
        g2d.setColor(gridColor);
        for (int y = 0; y < getHeight(); y += gridSize) {
            g2d.drawLine(0, y, getWidth(), y);
        }
    
        for (int x = 0; x < getWidth(); x += gridSize) {
            g2d.drawLine(x, 0, x, getHeight());
        }
    }

    private void drawButton(Graphics2D g2, Rectangle bounds, String text, boolean isHovered, boolean isPressed) {
        Color topColor = new Color(50, 50, 50, 200); 
        Color bottomColor = new Color(20, 20, 20, 200); 

        if (isHovered) {
            topColor = highlightColor.brighter();
            bottomColor = highlightColor.darker();
        }
        if (isPressed) {
            topColor = new Color(0, 100, 100); 
            bottomColor = new Color(0, 50, 50);
        }
        
        GradientPaint gp = new GradientPaint(bounds.x, bounds.y, topColor, bounds.x, bounds.y + bounds.height, bottomColor);
        g2.setPaint(gp);
        g2.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);

        g2.setColor(isHovered || isPressed ? highlightColor.brighter() : defaultColor.darker());
        g2.setStroke(new BasicStroke(3));
        g2.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);

        g2.setColor(isHovered || isPressed ? highlightColor.darker() : defaultColor.darker().darker());
        g2.setStroke(new BasicStroke(1));
        g2.drawRect(bounds.x + 3, bounds.y + 3, bounds.width - 6, bounds.height - 6);

        g2.setFont(retroFontBig);
        
        Color shadowColor = isHovered || isPressed ? highlightColor.darker().darker().darker() : Color.BLACK;
        g2.setColor(shadowColor);
        String shadowText = text;
        if (isHovered && !isPressed) {
            shadowText = "> " + text + " <";
        }
        
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(shadowText);
        int textHeight = fm.getHeight();
        
        int textX = bounds.x + (bounds.width - textWidth) / 2;
        int textY = bounds.y + (bounds.height - textHeight) / 2 + fm.getAscent();

        g2.drawString(shadowText, textX + 2, textY + 2); 
    
        g2.setColor(isHovered || isPressed ? Color.YELLOW : defaultColor); 
        String displayText = text;
        if (isHovered && !isPressed) {
            displayText = "> " + text + " <";
        } else if (isHovered && isPressed) {
             
            displayText = "> " + text + " <";
            g2.drawString(displayText, textX + 2, textY + 2); 
            return; 
        }

        g2.drawString(displayText, textX, textY);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        java.awt.Point p = e.getPoint();

        boolean oldStartHover = isStartHovered;
        boolean oldExitHover = isExitHovered;

        isStartHovered = startButtonBounds.contains(p);
        isExitHovered = exitButtonBounds.contains(p);

        if (isStartHovered) {
            selectedOption = START_GAME;
        } else if (isExitHovered) {
            selectedOption = EXIT;
        } 

        if (oldStartHover != isStartHovered || oldExitHover != isExitHovered) {
            repaint();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        java.awt.Point p = e.getPoint();
        if (startButtonBounds.contains(p)) {
            isStartPressed = true;
        } else if (exitButtonBounds.contains(p)) {
            isExitPressed = true;
        }
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (isStartPressed && startButtonBounds.contains(e.getPoint())) {
            cardLayout.show(mainPanel, "GAME");
        } else if (isExitPressed && exitButtonBounds.contains(e.getPoint())) {
            System.exit(0);
        }
        isStartPressed = false;
        isExitPressed = false;
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {
        isStartHovered = false;
        isExitHovered = false;
        repaint();
    }
    @Override
    public void mouseDragged(MouseEvent e) {}

    @Override
    public void componentShown(ComponentEvent e) {
        animationTimer.start(); 
        setFocusable(true);
        removeKeyListener(this); 
        addKeyListener(this);    
        requestFocusInWindow();  
    }

    @Override
    public void componentHidden(ComponentEvent e) {
        animationTimer.stop(); 
        removeKeyListener(this);
    }

    @Override
    public void componentMoved(ComponentEvent e) {}
    @Override
    public void componentResized(ComponentEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_UP) {
            selectedOption--;
            if (selectedOption < START_GAME) {
                selectedOption = EXIT;
            }
            updateHoverStates();
            repaint();
        } else if (key == KeyEvent.VK_DOWN) {
            selectedOption++;
            if (selectedOption > EXIT) {
                selectedOption = START_GAME;
            }
            updateHoverStates();
            repaint();
        } else if (key == KeyEvent.VK_ENTER) {
            if (selectedOption == START_GAME) {
                isStartPressed = true;
                repaint();
                Timer t = new Timer(100, (ae) -> {
                    cardLayout.show(mainPanel, "GAME");
                    isStartPressed = false; 
                });
                t.setRepeats(false);
                t.start();
                
            } else if (selectedOption == EXIT) {
                System.exit(0);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}