package ui;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * LoginChoiceFrame背景
 */
public class BackgroundPanel extends JPanel {
    private Image backgroundImage;
    private final String backgroundPath = "/resources/LoginBackground.jpg";

    public BackgroundPanel() {
        try {
            URL imageUrl = getClass().getResource(backgroundPath);
            if (imageUrl != null) {
                backgroundImage = new ImageIcon(imageUrl).getImage();
            } else {
                System.out.println("找不到背景圖片：" + backgroundPath);
                backgroundImage = null;
            }
        } catch (Exception e) {
            System.out.println("載入背景圖片時發生錯誤：" + e.getMessage());
            backgroundImage = null;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            int panelWidth = getWidth();
            int panelHeight = getHeight();

            int imgWidth = backgroundImage.getWidth(this);
            int imgHeight = backgroundImage.getHeight(this);

            // 計算縮放比例（保持圖片比例）
            double scale = Math.min((double) panelWidth / imgWidth, (double) panelHeight / imgHeight);
            int drawWidth = (int) (imgWidth * scale);
            int drawHeight = (int) (imgHeight * scale);

            // 置中置頂顯示
            int x = (panelWidth - drawWidth) / 2;
            int y = 0;

            g.drawImage(backgroundImage, x, y, drawWidth, drawHeight, this);
        }
    }
}

