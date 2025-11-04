package Arkanoid.Src.core;

import java.awt.*;

public class PauseOverlay {
    private final String[] items = {"RESUME", "RESTART", "QUIT TO MENU"};
    private int selected = 0;

    public void up()   { selected = (selected - 1 + items.length) % items.length; }
    public void down() { selected = (selected + 1) % items.length; }
    public int  getSelected() { return selected; }

    /** Vẽ lớp phủ pause lên trên khung hình gameplay hiện tại */
    public void render(Graphics2D g, int w, int h) {
        // lớp che mờ
        g.setColor(new Color(0, 0, 0, 0));
        g.fillRect(0, 0, w, h);

        // tiêu đề
        //g.setColor(Color.WHITE);
        //g.setFont(g.getFont().deriveFont(Font.BOLD, 48f));
        //String title = "PAUSED";
        //int tw = g.getFontMetrics().stringWidth(title);
        //g.drawString(title, (w - tw)/2, h/3);

        // menu
        //g.setFont(g.getFont().deriveFont(Font.PLAIN, 24f));
        //for (int i = 0; i < items.length; i++) {
        //    String t = (i == selected) ? "▶ " + items[i] + " ◀" : items[i];
        //    int iw = g.getFontMetrics().stringWidth(t);
        //    int x = (w - iw)/2;
        //    int y = h/2 + i*40;
        //    g.setColor(i == selected ? Color.YELLOW : Color.LIGHT_GRAY);
        //    g.drawString(t, x, y);
        //}
    }
}