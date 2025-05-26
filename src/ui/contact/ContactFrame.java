package ui.contact;

import javax.swing.*;
import java.awt.*;
import ui.HomeFrame;
import ui.GradientPanel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
/**
 * 聯絡資訊畫面
 */
public class ContactFrame extends JFrame {

    public ContactFrame() {
        setTitle("聯絡資訊");
        setSize(600, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel contentPane = new GradientPanel();
        contentPane.setLayout(new BorderLayout());
        setContentPane(contentPane);

        // 標題
        JLabel title = new JLabel("聯絡資訊");
        title.setFont(new Font("SansSerif", Font.BOLD, 25));
        title.setForeground(Color.BLACK);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));
        contentPane.add(title, BorderLayout.NORTH);

        // 主要內容
        JPanel infoPanel = new JPanel();
        infoPanel.setOpaque(false);
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        addContactInfo(infoPanel, "公司地址", "  台北市大安區羅斯福路四段1號");
        addContactInfo(infoPanel, "客服電話", "  0800-114-514（市話）； (02) 3366-5792");
        addContactInfo(infoPanel, "客服信箱", "  trainscheduler2025@gmail.com");
        addContactInfo(infoPanel, "客服 LINE", "@trainscheduler2025");

        contentPane.add(infoPanel, BorderLayout.CENTER);

        // 回首頁按鈕
        JButton homeButton = new JButton("回首頁");
        homeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        homeButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        homeButton.setForeground(Color.BLACK);
        homeButton.setBackground(new Color(255, 192, 203)); // 淺粉色
        homeButton.setFocusPainted(false);
        homeButton.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        homeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        // ✨ 加這兩行讓 hover 背景生效！
        homeButton.setContentAreaFilled(true);
        homeButton.setOpaque(true);


        // hover 效果 + 回首頁功能
        homeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                homeButton.setBackground(new Color(255, 160, 180));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                homeButton.setBackground(new Color(255, 192, 203));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                dispose(); // 關閉目前畫面
                new HomeFrame().setVisible(true); // 開啟首頁
            }
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 0));
        bottomPanel.add(homeButton);

        contentPane.add(bottomPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    private void addContactInfo(JPanel panel, String label, String detail) {
        JPanel row = new JPanel();
        row.setLayout(new FlowLayout(FlowLayout.LEFT));
        row.setOpaque(false);

        JLabel titleLabel = new JLabel(label);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));

        JLabel detailLabel = new JLabel(detail);
        detailLabel.setFont(new Font("SansSerif", Font.PLAIN, 15));
        detailLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        row.add(titleLabel);
        row.add(detailLabel);
        panel.add(row);
        panel.add(Box.createVerticalStrut(15));
    }
}
