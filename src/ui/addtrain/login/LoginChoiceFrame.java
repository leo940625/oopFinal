/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui.addtrain.login;

import javax.swing.*;
import java.awt.*;

/**
 * 新增班次時要先確認是否是員工
 * @author meredithfang
 */
class LoginChoiceFrame extends JFrame {
    public LoginChoiceFrame() {
        setTitle("登入選擇");
        setSize(600, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        BackgroundPanel contentPane = new BackgroundPanel();
        contentPane.setLayout(new GridBagLayout()); // 用這個讓Panel居中
        setContentPane(contentPane);

        // 中央白色面板
        JPanel centerPanel = new JPanel();
        centerPanel.setPreferredSize(new Dimension(300, 250));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setLayout(new BorderLayout(10, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // 內距

        // 標題
        JLabel titleLabel = new JLabel("員工職權", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        centerPanel.add(titleLabel, BorderLayout.NORTH);

        // 按鈕區
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 20, 0)); // 左右兩個按鈕
        buttonPanel.setOpaque(false);

        JButton employeeButton = new JButton("<html>我是員工<br>(登入)</html>");
        styleButton(employeeButton);
        employeeButton.addActionListener(e -> {
            new EmployeeLoginFrame(); // 跳到員工登入頁
            dispose();
        });

        JButton guestButton = new JButton("<html>我是訪客<br>(退出)</html>");
        styleButton(guestButton);
        guestButton.addActionListener(e -> {
            new HomeFrame(); // 回首頁
            dispose();
        });

        buttonPanel.add(employeeButton);
        buttonPanel.add(guestButton);
        centerPanel.add(buttonPanel, BorderLayout.CENTER);

        contentPane.add(centerPanel); // 加到最中間
        setVisible(true);
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("SansSerif", Font.BOLD, 18));
        button.setBackground(new Color(255, 182, 193));
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
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
    }
}
