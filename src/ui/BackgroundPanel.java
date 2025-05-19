/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.trainscheduler;

import javax.swing.*;
import java.awt.*;

/**
 * LoginChoiceFrame背景
 * @author meredithfang
 */
class BackgroundPanel extends JPanel {
    private Image backgroundImage;
    private final String backgroundPath = "/Users/meredithfang/NetBeansProjects/resources/LoginBackground.jpg"; // ★ 這裡放你的背景圖路徑！

    public BackgroundPanel() {
        try {
            backgroundImage = new ImageIcon(backgroundPath).getImage();
        } catch (Exception e) {
            System.out.println("找不到背景圖片，使用預設。");
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
