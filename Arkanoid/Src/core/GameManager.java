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
