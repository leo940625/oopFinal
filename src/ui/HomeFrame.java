package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import ui.addtrain.AddTrainFrame;
import ui.addtrain.login.*;
import ui.searchtrain.TrainSearchChoiceFrame;
import ui.ticketdownload.*;
/**
 * 首頁畫面 含四個選項
 */
public class HomeFrame extends JFrame {
    public HomeFrame() {
        setTitle("Train Scheduler - CSLD Railway");
        setSize(600, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());

        // 上半部: 放圖片＋標題
        BannerPanel banner = new BannerPanel();
        contentPane.add(banner, BorderLayout.NORTH);

        // 下半部: 四個按鈕
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new GridLayout(4, 1, 15, 15));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 80, 20, 80));

        String[] buttonNames = {"新增車次", "列車查詢", "車票下載", "聯絡客服"};

        for (String name : buttonNames) {
            JButton btn = createStyledButton(name);

            if (name.equals("新增車次")) {
                btn.addActionListener(e -> {
                    new LoginChoiceFrame();
                    dispose();
                });
            } else if (name.equals("車票下載")) {
                btn.addActionListener(e -> {
                    new TicketInputFrame(); // 改成先跳輸入畫面
                    dispose();
                });
            } else if (name.equals("列車查詢")) {
                btn.addActionListener(e -> {
                    new TrainSearchChoiceFrame();
                    dispose();
                });
            } else {
                btn.addActionListener(e -> {
                    JOptionPane.showMessageDialog(this, "此功能尚未開放", "提示", JOptionPane.INFORMATION_MESSAGE);
                });
            }
            buttonPanel.add(btn);
        }

        contentPane.add(buttonPanel, BorderLayout.CENTER);
        setContentPane(contentPane);
        setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(new Color(255, 182, 193)); // 粉紅色
        button.setForeground(Color.WHITE);
        button.setFont(new Font("SansSerif", Font.BOLD, 20));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setContentAreaFilled(true);
        button.setOpaque(true);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(255, 105, 180)); // hover深色
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(255, 182, 193)); // 恢復正常色
            }
        });

        return button;
    }
}
