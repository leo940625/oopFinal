package ui.searchtrain;

import javax.swing.*;
import java.awt.*;

import ui.BackgroundPanel;
import ui.HomeFrame;
import ui.addtrain.login.EmployeeLoginFrame;
import util.ButtonUtil;

/**
 * 選擇「列車查詢」後要先選擇查詢的方法（用車次號碼查還是用起始終點車站）
 */

public class TrainSearchChoiceFrame extends JFrame{
    public TrainSearchChoiceFrame() {
        setTitle("查詢選擇");
        setSize(600, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        BackgroundPanel contentPane = new BackgroundPanel();
        contentPane.setLayout(new GridBagLayout()); // 用這個讓Panel居中
        setContentPane(contentPane);

        // 中央白色面板
        JPanel centerPanel = new JPanel();
        centerPanel.setPreferredSize(new Dimension(400, 220));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setLayout(new BorderLayout(10, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // 內距

        // 標題
        JLabel titleLabel = new JLabel("列車查詢", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        centerPanel.add(titleLabel, BorderLayout.NORTH);

        // 按鈕區（使用 BoxLayout 做水平排列）
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setOpaque(false);

        JButton employeeButton = new JButton("<html><div style='text-align: center;'>依起訖車站</div></html>");
        styleButton(employeeButton);
        employeeButton.setPreferredSize(new Dimension(180, 80));
        employeeButton.setMaximumSize(new Dimension(180, 80));
        employeeButton.setHorizontalAlignment(SwingConstants.CENTER);
        employeeButton.setVerticalAlignment(SwingConstants.CENTER);
        employeeButton.setAlignmentY(Component.CENTER_ALIGNMENT);
        employeeButton.addActionListener(e -> {
            new TrainSearchSFrame().setVisible(true); // 自動顯示畫面
            dispose();
        });

        JButton guestButton = new JButton("<html><div style='text-align: center;'>依車次號碼</div></html>");
        styleButton(guestButton);
        guestButton.setPreferredSize(new Dimension(180, 80));
        guestButton.setMaximumSize(new Dimension(180, 80));
        guestButton.setHorizontalAlignment(SwingConstants.CENTER);
        guestButton.setVerticalAlignment(SwingConstants.CENTER);
        guestButton.setAlignmentY(Component.CENTER_ALIGNMENT);
        guestButton.addActionListener(e -> {
            new TrainSearchIDFrame().setVisible(true); // 自動顯示畫面
            dispose();
        });

        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(employeeButton);
        buttonPanel.add(Box.createHorizontalStrut(30));
        buttonPanel.add(guestButton);
        buttonPanel.add(Box.createHorizontalGlue());

        centerPanel.add(buttonPanel, BorderLayout.CENTER);

        GridBagConstraints gbcCenter = new GridBagConstraints();
        gbcCenter.gridx = 0;
        gbcCenter.gridy = 0;
        gbcCenter.weighty = 0; // 不佔空間
        gbcCenter.insets = new Insets(150, 0, 0, 0); // 與底下的回首頁留距
        //gbcCenter.anchor = GridBagConstraints.CENTER;
        gbcCenter.anchor = GridBagConstraints.NORTH;
        contentPane.add(centerPanel, gbcCenter);

        // ---- 底部的「回首頁」粉紅按鈕 ----
        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JButton homeButton = ButtonUtil.createStyledButton("回首頁");
        homeButton.setPreferredSize(new Dimension(150, 45)); // 你可以再調整大小
        homeButton.addActionListener(e -> {
            dispose();
            new HomeFrame().setVisible(true);
        });
        bottomPanel.add(homeButton);

        // --- 設定靠近最下方 ---
        GridBagConstraints gbcBottom = new GridBagConstraints();
        gbcBottom.gridx = 0;
        gbcBottom.gridy = 1;
        gbcBottom.weighty = 1.0; // 向下推到底
        gbcBottom.anchor = GridBagConstraints.SOUTH;
        gbcBottom.insets = new Insets(20, 0, 30, 0); // 與底部邊界留距
        contentPane.add(bottomPanel, gbcBottom);

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