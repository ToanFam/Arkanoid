package Arkanoid.Src.ImageManager; // Dựa trên package trong import của bạn

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL; // Cần import URL
import java.util.HashMap;
import java.util.Map;

public class ImageManager {
    private static Map<String, BufferedImage> images = new HashMap<>();

    public static BufferedImage loadImage(String path) {
        // Nếu đã tải ảnh này rồi, trả về từ cache
        if (images.containsKey(path)) {
            return images.get(path);
        }
        
        BufferedImage image = null;
        try {
            // 1. Lấy URL (đường dẫn) của tài nguyên
            URL imageUrl = ImageManager.class.getResource(path);
            
            // 2. KIỂM TRA NULL (Quan trọng nhất)
            if (imageUrl == null) {
                // Nếu không tìm thấy file, báo lỗi rõ ràng thay vì crash
                System.err.println("LỖI NGHIÊM TRỌNG: Không thể tìm thấy ảnh tại: " + path);
                System.err.println("Hãy kiểm tra lại đường dẫn (có dấu / ở đầu chưa?) và cấu trúc thư mục.");
                return null; // Trả về null một cách an toàn
            }

            // 3. Nếu không null, mới tiến hành đọc ảnh
            image = ImageIO.read(imageUrl);
            images.put(path, image); // Lưu vào cache

        } catch (IOException e) { // Lỗi khi đọc file (file hỏng, không phải file ảnh,...)
            System.err.println("Lỗi I/O khi đọc ảnh: " + path);
            e.printStackTrace();
        } catch (Exception e) { // Bắt các lỗi chung khác
            System.err.println("Lỗi không xác định khi tải ảnh: " + path);
            e.printStackTrace();
        }
        return image;
    }
}