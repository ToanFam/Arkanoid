package Arkanoid.Src.core;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameManager {
    private static List<String> levelFiles = new ArrayList<>();
    private static int currentLevelIndex = 0;

    static {
        // Danh sách file JSON level
        levelFiles.add("Arkanoid/Src/assets/levels/level1.json");
        levelFiles.add("Arkanoid/Src/assets/levels/level2.json");
        levelFiles.add("Arkanoid/Src/assets/levels/level3.json");
        levelFiles.add("Arkanoid/Src/assets/levels/level4.json");
        levelFiles.add("Arkanoid/Src/assets/levels/level5.json");
        levelFiles.add("Arkanoid/Src/assets/levels/level6.json");
        levelFiles.add("Arkanoid/Src/assets/levels/level7.json");
        levelFiles.add("Arkanoid/Src/assets/levels/level8.json");
        levelFiles.add("Arkanoid/Src/assets/levels/level9.json");
        levelFiles.add("Arkanoid/Src/assets/levels/level10.json");
        /*levelFiles.add("Arkanoid/Src/assets/levels/level11.json");
        /*levelFiles.add("Arkanoid/Src/assets/levels/level12.json");
        /*levelFiles.add("Arkanoid/Src/assets/levels/level13.json");
        /*levelFiles.add("Arkanoid/Src/assets/levels/level14.json");
        /*levelFiles.add("Arkanoid/Src/assets/levels/level15.json");
        /*levelFiles.add("Arkanoid/Src/assets/levels/level16.json");
        /*levelFiles.add("Arkanoid/Src/assets/levels/level17.json");
        /*levelFiles.add("Arkanoid/Src/assets/levels/level18.json");
        /*levelFiles.add("Arkanoid/Src/assets/levels/level19.json");
        /*levelFiles.add("Arkanoid/Src/assets/levels/level20.json");*/
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

    public static int getCurrentLevelNumber() {
        return currentLevelIndex + 1;
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