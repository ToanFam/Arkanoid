package Arkanoid.Src.ImageManager;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ImageManager {
    private static final Map<String, BufferedImage> CACHE = new HashMap<>();

    /**
     * Load an image by classpath or filesystem path. Caches results.
     * Examples:
     *  - "assets/img/actors/ball.png" (classpath resource within your jar/classes)
     *  - "/absolute/path/to/ball.png" (filesystem path)
     */
    public static BufferedImage loadImage(String path) {
        if (path == null || path.isEmpty()) return null;
        if (CACHE.containsKey(path)) return CACHE.get(path);

        BufferedImage img = null;
        // Try classpath first
        try (InputStream is = ImageManager.class.getClassLoader().getResourceAsStream(path)) {
            if (is != null) {
                img = ImageIO.read(is);
            }
        } catch (IOException ignored) { }

        // Fallback to file path
        if (img == null) {
            try {
                img = ImageIO.read(new java.io.File(path));
            } catch (IOException ignored) {}
        }

        if (img != null) { CACHE.put(path, img); }
        return img;
    }

    // Convenience keys
    public static BufferedImage ball() {
        return loadImage("assets/img/actors/ball.png");
    }

    public static BufferedImage paddle() {
        return loadImage("assets/img/actors/paddle.png");
    }

    public static BufferedImage menuBackground() {
        return loadImage("assets/img/ui/menu_bg.png");
    }
}