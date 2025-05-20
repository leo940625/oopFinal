package ui;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import javax.swing.ImageIcon;

/**
 * 首頁上方banner
 */
public class BannerPanel extends JPanel {
    private Image bannerImage;

    public BannerPanel() {
        URL url = getClass().getResource("/resources/train.jpg");
        if (url != null) {
            bannerImage = new ImageIcon(url).getImage();
        } else {
            System.out.println("圖片載入失敗");
            bannerImage = null;
        }

        setPreferredSize(new Dimension(600, 250));
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
        int y = getHeight();
        g.drawString(text, x, y);
    }
}


