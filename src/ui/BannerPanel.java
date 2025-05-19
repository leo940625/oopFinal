package ui;

import javax.swing.*;
import java.awt.*;

/**
 * 首頁上方banner
 * @author meredithfang
 */
public class BannerPanel extends JPanel {
    private Image bannerImage;
    private final String bannerPath = "/Users/meredithfang/NetBeansProjects/resources/train.jpg";

    public BannerPanel() {
        try {
            bannerImage = new ImageIcon(bannerPath).getImage();
        } catch (Exception e) {
            System.out.println("找不到圖片，使用預設背景。");
            bannerImage = null;
        }
        setPreferredSize(new Dimension(600, 250)); // 預設高度
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (bannerImage != null) {
            g.drawImage(bannerImage, 0, 0, getWidth(), getHeight(), this);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("SansSerif", Font.BOLD, 40));
        FontMetrics fm = g.getFontMetrics();
        String text = "Train Scheduler";
        int x = (getWidth() - fm.stringWidth(text)) / 2;
        int y = getHeight(); // 貼到底部
        g.drawString(text, x, y);
    }
}
