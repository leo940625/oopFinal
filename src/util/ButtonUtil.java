package util;

import javax.swing.*;
import java.awt.*;

public class ButtonUtil {
    public static JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(new Color(255, 182, 193));
        button.setForeground(Color.BLACK);
        button.setFont(new Font("SansSerif", Font.BOLD, 18));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setContentAreaFilled(true);
        button.setOpaque(true);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(255, 105, 180));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(255, 182, 193));
            }
        });

        return button;
    }
}
