package ui.addtrain.login;

import javax.swing.*;
import java.awt.*;
import ui.addtrain.AddTrainFrame;
import ui.BackgroundPanel;

/**
 * 登入畫面（輸入員工帳號密碼）
 */
public class EmployeeLoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public EmployeeLoginFrame() {
        setTitle("員工登入");
        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        BackgroundPanel contentPane = new BackgroundPanel();
        contentPane.setLayout(new GridBagLayout());
        setContentPane(contentPane);

        // 中央白色面板
        JPanel loginPanel = new JPanel();
        loginPanel.setPreferredSize(new Dimension(300, 250));
        loginPanel.setBackground(Color.WHITE);
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // 標題
        JLabel titleLabel = new JLabel("員工登入", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginPanel.add(titleLabel);

        loginPanel.add(Box.createVerticalStrut(20));

        // 帳號
        usernameField = new JTextField(15);
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        usernameField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        loginPanel.add(new JLabel("帳號："));
        loginPanel.add(usernameField);

        loginPanel.add(Box.createVerticalStrut(15));

        // 密碼
        passwordField = new JPasswordField(15);
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        passwordField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        loginPanel.add(new JLabel("密碼："));
        loginPanel.add(passwordField);

        loginPanel.add(Box.createVerticalStrut(20));

        // 登入按鈕
        JButton loginButton = createStyledButton("登入");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.addActionListener(e -> handleLogin());
        loginPanel.add(loginButton);

        contentPane.add(loginPanel);
        setVisible(true);
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        // 直接寫死帳密在程式裡 TODO:還是要創員工資料庫？
        if (username.equals("admin") && password.equals("1234")) {
            new AddTrainFrame(); // 登入成功後進入新增車次
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "帳號或密碼錯誤！", "錯誤", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JButton createStyledButton(String text) {
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