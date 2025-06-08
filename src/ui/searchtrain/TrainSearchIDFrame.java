package ui.searchtrain;

import dao.TrainDAO;
import dao.TrainDAOImpl;
import model.Train;
import ui.GradientPanel;
import util.ButtonUtil;
import util.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.util.regex.Pattern;
/**
 * 用車次編號查詢aka某車次的詳細資訊
 * 顯示於TrainDetailFrame.java
 * TODO: 如果在TrainInformationFrame選了詳細（經過車站）是否也要連進這個畫面
 */
public class TrainSearchIDFrame extends JFrame {
    /**
     * 建構子：初始化車次查詢畫面，讓使用者輸入車次號碼並查詢。
     */
    public TrainSearchIDFrame() {
        setTitle("車次輸入");
        setSize(600, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 背景漸層面板
        JPanel contentPane = new GradientPanel();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        setContentPane(contentPane);

        // 輸入區塊
        JPanel formPanel = new JPanel();
        formPanel.setOpaque(false);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

        // 標題
        JLabel title = new JLabel("欲查詢之車次號碼", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(Box.createVerticalStrut(40)); // 上方留空
        formPanel.add(title);
        formPanel.add(Box.createVerticalStrut(30));

        // 輸入欄位
        JTextField idField = new JTextField();
        idField.setMaximumSize(new Dimension(200, 30));
        idField.setFont(new Font("SansSerif", Font.PLAIN, 18));
        idField.setHorizontalAlignment(SwingConstants.CENTER);
        idField.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(idField);
        formPanel.add(Box.createVerticalStrut(30));

        // 查詢按鈕

        JPanel buttonRow = new JPanel();
        buttonRow.setLayout(new BoxLayout(buttonRow, BoxLayout.X_AXIS));
        buttonRow.setOpaque(false);

        JButton searchButton = ButtonUtil.createStyledButton("查詢");
        searchButton.setPreferredSize(new Dimension(120, 40));
        searchButton.setMaximumSize(new Dimension(120, 40));

        formPanel.add(Box.createVerticalGlue()); // 「查詢」上方彈性空間
        formPanel.add(searchButton);
        formPanel.add(Box.createVerticalGlue()); // 底部彈性空間

        // 將 formPanel 加到畫面中
        contentPane.add(Box.createVerticalStrut(40));
        contentPane.add(formPanel);

        // 查詢邏輯
        searchButton.addActionListener(e -> {
            String input = idField.getText().trim();
            if (!Pattern.matches("\\d{4}", input)) {
                JOptionPane.showMessageDialog(this, "請輸入四位數的車次號碼", "格式錯誤", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int trainNumber = Integer.parseInt(input);

            try (Connection conn = DBConnection.getConnection()){
                TrainDAO dao = new TrainDAOImpl(conn);
                Train train = dao.getTrainByNumber(trainNumber);

                // 資料不存在提示
                if (train == null) {
                    JOptionPane.showMessageDialog(this, "查無此車次，請確認後重新輸入。", "查無資料", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                new TrainDetailFrame(train).setVisible(true);
                this.dispose();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "發生未知錯誤，請稍後再試。",
                        "錯誤", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton exitButton = ButtonUtil.createStyledButton("退出");
        exitButton.setPreferredSize(new Dimension(120, 40));
        exitButton.setMaximumSize(new Dimension(120, 40));
        exitButton.addActionListener(e -> {
            dispose();
            new TrainSearchChoiceFrame().setVisible(true);
        });

        buttonRow.add(Box.createHorizontalGlue());
        buttonRow.add(searchButton);
        buttonRow.add(Box.createRigidArea(new Dimension(30, 0)));
        buttonRow.add(exitButton);
        buttonRow.add(Box.createHorizontalGlue());

        formPanel.add(buttonRow);
        formPanel.add(Box.createVerticalGlue());

        setVisible(true); // 顯示視窗
    }

    /**
     * 套用粉紅主題按鈕樣式
     */
    private void styleButton(JButton button) {
        Color baseColor = new Color(255, 182, 193); // 淺粉紅
        Color hoverColor = new Color(255, 105, 180); // 深粉紅

        button.setFont(new Font("SansSerif", Font.BOLD, 18));
        button.setBackground(baseColor);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setContentAreaFilled(true);
        button.setOpaque(true);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(baseColor);
            }
        });
    }
}
