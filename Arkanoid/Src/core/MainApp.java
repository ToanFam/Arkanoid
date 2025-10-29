package Arkanoid.Src.core; // Đặt cùng package với Game.java

import javax.swing.*;
import java.awt.*;

public class MainApp {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Arkanoid");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        CardLayout cardLayout = new CardLayout();
        JPanel mainPanel = new JPanel(cardLayout);

        Game gamePanel = new Game();
        StartMenuPanel menuPanel = new StartMenuPanel(mainPanel, cardLayout, gamePanel);

        mainPanel.add(menuPanel, "MENU");
        mainPanel.add(gamePanel, "GAME");

        frame.add(mainPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);

        cardLayout.show(mainPanel, "MENU");

        frame.setVisible(true);
    }
}