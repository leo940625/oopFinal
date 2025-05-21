package ui.searchtrain;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

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

            // TODO: 這邊要執行查詢動作 ㄜ直接跳進下一個Frame再抓資料庫嗎
            // new TrainSearchInformationFrame(...);
        });

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
