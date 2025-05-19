/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.trainscheduler;

import javax.swing.*;
import java.awt.*;

/**
 * 粉紅漸層背景
 * @author meredithfang
 */
class GradientPanel extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        Color color1 = new Color(255, 228, 225); // 上方 淺粉紅
        Color color2 = new Color(255, 192, 203); // 下方 粉紅
        
        int width = getWidth();
        int height = getHeight();
        
        GradientPaint gp = new GradientPaint(0, 0, color1, 0, height, color2);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, width, height);
    }
}
