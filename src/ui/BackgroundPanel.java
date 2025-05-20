package ui;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * LoginChoiceFrame背景
 * 還沒帶進去不知道有沒有成功
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
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}

