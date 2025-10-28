package Arkanoid.Src.core;
import javax.sound.sampled.*;
import java.awt.event.KeyEvent;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameManager {
    private static List<String> levelFiles = new ArrayList<>();
    private static int currentLevelIndex = 0;

    static {
        // Danh sách file JSON level
        levelFiles.add("Arkanoid/Src/assets/levels/level1.json");
        levelFiles.add("Arkanoid/Src/assets/levels/level2.json");
        levelFiles.add("Arkanoid/Src/assets/levels/level3.json");
    }

    public static String getCurrentLevel() {
        return levelFiles.get(currentLevelIndex);
    }

    public static String getNextLevel() {
        currentLevelIndex++;
        if (currentLevelIndex < levelFiles.size()) {
            return levelFiles.get(currentLevelIndex);
        }
        return null; // Không còn level nào
    }

    public static void reset() {
        currentLevelIndex = 0;
    }

    // âm thanh
    public static void playSound(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                System.err.println("Audio file not found!" + filePath);
                return;
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println(e.getMessage());
        }
    }


}
