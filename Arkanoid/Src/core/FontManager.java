package Arkanoid.Src.core; // Bạn có thể đặt nó chung package với ImageManager

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.io.InputStream;

import Arkanoid.Src.assets.font.*;;

public class FontManager {

    private static Font pressStartFont;
    static {
        try {
            // Đường dẫn đến file font trong thư mục resources
            String fontPath = "/Arkanoid/Src/assets/font/PressStart2P-Regular.ttf"; 
            
            // Dùng getResourceAsStream để đọc file từ bên trong JAR
            InputStream is = FontManager.class.getResourceAsStream(fontPath);
            
            if (is == null) {
                System.err.println("LỖI NGHIÊM TRỌNG: Không thể tìm thấy font: " + fontPath);
            } else {
                // Tạo font từ file
                pressStartFont = Font.createFont(Font.TRUETYPE_FONT, is);
                
                // Đăng ký font này với hệ thống
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(pressStartFont);
            }

        } catch (IOException | FontFormatException e) {
            System.err.println("Lỗi khi tải font. Sử dụng font mặc định.");
            e.printStackTrace();
            pressStartFont = null;
        }
    }

    /**
     * Lấy font "Press Start 2P" với kích thước mong muốn.
     * @param size Kích thước font
     * @return Font đã được tải, hoặc font "Arial" mặc định nếu có lỗi.
     */
    public static Font getFont(float size) {
        if (pressStartFont != null) {
            // Trả về một phiên bản của font với kích thước mới
            return pressStartFont.deriveFont(size);
        } else {
            // Nếu tải font thất bại, trả về font dự phòng an toàn
            return new Font("Arial", Font.PLAIN, (int) size);
        }
    }
}