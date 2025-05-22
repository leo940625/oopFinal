package ui.searchtrain;

import javax.swing.*;
import java.awt.*;
import ui.HomeFrame;
import dao.*;
import model.*;
import util.DBConnection;
import java.sql.*;
import java.time.LocalTime;
import java.util.*;
import java.util.List;
import java.util.Arrays;
import ui.searchtrain.TrainInformationFrame;

/**
 * 查詢列車：起訖站選擇畫面
 */
public class TrainSearchSFrame extends JFrame {

    private JComboBox<String> startStationBox;
    private JComboBox<String> endStationBox;

    public TrainSearchSFrame() {
        setTitle("起訖站查詢");
        setSize(600, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel contentPane = new GradientPanel();
        contentPane.setLayout(new BorderLayout());
        setContentPane(contentPane);

        JPanel formPanel = new JPanel();
        formPanel.setOpaque(false);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("起訖站選擇");
        title.setFont(new Font("SansSerif", Font.BOLD, 25));
        title.setForeground(Color.BLACK);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(Box.createVerticalStrut(30));
        // formPanel.add(Box.createVerticalGlue());  // 這行可以將空間頂部推開 根據空間考慮要俵
        formPanel.add(title);
        formPanel.add(Box.createVerticalStrut(30));

        // TODO: 假資料，可替換為 StationDAO 取得的資料
        List<String> stationNames = Arrays.asList("南港", "台北", "板橋", "桃園", "新竹", "苗栗",
                "台中", "彰化", "雲林", "嘉義", "台南", "左營");
        String[] stationArray = stationNames.toArray(new String[0]);

        startStationBox = new JComboBox<>(stationArray);
        endStationBox = new JComboBox<>(stationArray);
        styleComboBox(startStationBox);
        styleComboBox(endStationBox);

        formPanel.add(Box.createVerticalStrut(40));
        formPanel.add(labeledRow("起始站", startStationBox));
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(labeledRow("抵達站", endStationBox));
        formPanel.add(Box.createVerticalStrut(30));

        JButton searchButton = new JButton("查詢");
        styleButton(searchButton);
        searchButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        searchButton.addActionListener(e -> {
            String from = (String) startStationBox.getSelectedItem();
            String to = (String) endStationBox.getSelectedItem();

            if (from.equals(to)) {
                JOptionPane.showMessageDialog(this,
                        "起始站與抵達站不能相同！",
                        "錯誤", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (Connection conn = DBConnection.getConnection()) {
                TrainDAO trainDAO = new TrainDAOImpl(conn);
                // TODO: 這邊要寫一下findTrainsBetween的功能是找起訖站對應的車次們
                //  result 就是符合我起訖站的車次們
                // TODO: 我的理解是車次帶著其他資訊 應該沒有理解錯誤吧？
                List<Train> result = trainDAO.findTrainsBetween(from, to);

                if (result.isEmpty()) {
                    Object[] options = {"重新查詢", "回首頁"};
                    int choice = JOptionPane.showOptionDialog(
                            this,
                            "查無符合條件的列車。",
                            "查無資料",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.INFORMATION_MESSAGE,
                            null,
                            options,
                            options[0]
                    );

                    if (choice == JOptionPane.NO_OPTION) {
                        this.dispose();
                        new HomeFrame().setVisible(true);
                    }
                    // YES_OPTION：什麼都不做，讓使用者繼續查詢
                    return;
                }

                // 切換到顯示查詢結果的新視窗
                new TrainInformationFrame(result).setVisible(true);
                this.dispose(); // 關掉目前畫面

            } catch (SQLException ex) {
                ex.printStackTrace();

                String message = "查詢失敗，請稍後再試。";
                String title = "錯誤";

                Object[] options = {"重新查詢", "回首頁"};
                int choice = JOptionPane.showOptionDialog(
                        this,
                        message,
                        title,
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.ERROR_MESSAGE,
                        null,
                        options,
                        options[0]
                );

                if (choice == JOptionPane.NO_OPTION) {
                    this.dispose(); // 關閉目前查詢畫面
                    new HomeFrame().setVisible(true); // 回首頁
                }
                // 若選擇重新查詢（YES_OPTION），就什麼都不做
            }
        });

            // TODO: 這邊要執行查詢動作 ㄜ直接跳進下一個Frame再抓資料庫嗎
            // new TrainSearchInformationFrame(...);

        formPanel.add(searchButton);
        formPanel.add(Box.createVerticalGlue());  // 空間底部推開 查詢就不會擠在最下面

        contentPane.add(formPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private JPanel labeledRow(String labelText, JComboBox<String> comboBox) {
        JPanel row = new JPanel();
        row.setOpaque(false);
        row.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.PLAIN, 18));
        comboBox.setPreferredSize(new Dimension(200, 30));
        row.add(label);
        row.add(comboBox);
        return row;
    }

    private void styleComboBox(JComboBox<String> box) {
        box.setFont(new Font("SansSerif", Font.PLAIN, 16));
    }

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


    // 粉紅色漸層背景（仿 AddTrainFrame）
    class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            Color color1 = new Color(255, 228, 225);
            Color color2 = new Color(255, 182, 193);
            GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}
