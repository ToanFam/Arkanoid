package Arkanoid.Src.core;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import Arkanoid.Src.core.Game;
import Arkanoid.Src.ImageManager.ImageManager;

public class StartMenuPanel extends JPanel implements ActionListener {

    private JPanel mainPanel; 
    private CardLayout cardLayout; 
    private Game gamePanel; 
    private BufferedImage bgimg;

    public StartMenuPanel(JPanel mainPanel, CardLayout cardLayout, Game gamePanel) {
        this.mainPanel = mainPanel;
        this.cardLayout = cardLayout;
        this.gamePanel = gamePanel;

        setPreferredSize(new Dimension(Game.WIDTH, Game.HEIGHT)); 
        setLayout(new GridBagLayout()); 
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(300, 0, 15, 0); // Khoảng cách giữa các nút

        String imgpath = "/Arkanoid/Src/assets/img/other/arkanoid.png";
        this.bgimg = ImageManager.loadImage(imgpath);

        Font retrofont = FontManager.getFont(20f);
        setLayout(new GridBagLayout());

        // Nút Start
        JButton startButton = new JButton("Start Game");

        startButton.setFont(retrofont);
        
        startButton.setOpaque(false);
        startButton.setContentAreaFilled(false);
        startButton.setBorderPainted(false);
        startButton.setFocusPainted(false);

        startButton.setForeground(Color.WHITE);

        startButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                // Đổi sang màu vàng hoặc cyan khi di chuột qua, giống game cũ
                startButton.setForeground(Color.CYAN); 
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                startButton.setForeground(Color.WHITE); // Trở lại màu trắng
            }
        });

        startButton.setActionCommand("START");
        startButton.addActionListener(this);
        add(startButton, gbc);

        gbc.insets = new Insets(15, 0, 10, 0);

        // Exit
        JButton exitButton = new JButton("Exit");

        exitButton.setFont(retrofont);
        exitButton.setOpaque(false);
        exitButton.setContentAreaFilled(false);
        exitButton.setBorderPainted(false);
        exitButton.setFocusPainted(false);
        
        exitButton.setForeground(Color.WHITE);

        exitButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                exitButton.setForeground(Color.CYAN);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                exitButton.setForeground(Color.WHITE);
            }
        });

        exitButton.setActionCommand("EXIT");
        exitButton.addActionListener(this);
        add(exitButton, gbc);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if("START".equals(command)) {
            cardLayout.show(mainPanel, "GAME");
        } else if("EXIT".equals(command)) {
            System.exit(0);
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (this.bgimg != null) {
            g.drawImage(this.bgimg, 0, 0, getWidth(), getHeight(), this);
        } else {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}