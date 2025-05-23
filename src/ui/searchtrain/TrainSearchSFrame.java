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

            // TODO: 我試看看假資料看一下顯示畫面
            // ✅ 用假資料取代資料庫查詢
            List<Train> result = new ArrayList<>();

            // ★ Demo 條件：如果是查詢「台北 → 台中」就顯示兩班車
            if (from.equals("台北") && to.equals("台中")) {
                Station taipei = new Station(1, "台北");
                Station taoyuan = new Station(2, "桃園");
                Station hsinchu = new Station(3, "新竹");
                Station taichung = new Station(4, "台中");
                Station lefttemp = new Station(5, "左營");

                // 第一班車（1234）
                List<StopTime> stops1 = new ArrayList<>();
                stops1.add(new StopTime(taipei, null, LocalTime.of(8, 0)));
                stops1.add(new StopTime(taoyuan, LocalTime.of(8, 30), LocalTime.of(8, 31)));
                stops1.add(new StopTime(taichung,LocalTime.of(9, 10), LocalTime.of(9, 11)));
                stops1.add(new StopTime(lefttemp, LocalTime.of(10, 30), null));
                Train train1234 = new Train(1234, stops1, false); // false 表示南下

                // 第二班車（5678）
                List<StopTime> stops2 = new ArrayList<>();
                stops2.add(new StopTime(taipei, null, LocalTime.of(9, 0)));
                stops2.add(new StopTime(hsinchu, LocalTime.of(9, 35), LocalTime.of(9, 36)));
                stops2.add(new StopTime(taichung, LocalTime.of(10, 15), null));
                Train train5678 = new Train(5678, stops2, false);

                // 第三班車（2468）
                List<StopTime> stops3 = new ArrayList<>();
                stops3.add(new StopTime(taipei, null, LocalTime.of(9, 0)));
                stops3.add(new StopTime(hsinchu, LocalTime.of(9, 35), LocalTime.of(9, 36)));
                stops3.add(new StopTime(taichung, LocalTime.of(10, 15), null));
                Train train2468 = new Train(2468, stops3, false);

                // 第三班車（1357）
                List<StopTime> stops4 = new ArrayList<>();
                stops4.add(new StopTime(taipei, null, LocalTime.of(9, 0)));
                stops4.add(new StopTime(hsinchu, LocalTime.of(9, 35), LocalTime.of(9, 36)));
                stops4.add(new StopTime(taichung, LocalTime.of(10, 15), null));
                Train train1357 = new Train(1357, stops4, false);

                result.add(train1234);
                result.add(train5678);
                result.add(train2468);
                result.add(train1357);
            }
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

            new TrainInformationFrame(result, from, to).setVisible(true);
            this.dispose();
            // TODO: Demo結束點

            /*
            try (Connection conn = DBConnection.getConnection()) {
                TrainDAO trainDAO = new TrainDAOImpl(conn);
                StationDAO stationDAO = new StationDAOImpl(conn);

                // findTrainsBetween找起訖站對應的車次們
                //  result 就是符合我起訖站的車次們, from是出發車站，to是抵達車站（型別是string 67、68行）
                List<Train> result = trainDAO.findTrainsBetween(stationDAO.getStationByName(from),stationDAO.getStationByName(to),null);

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
                new new TrainInformationFrame(result, from, to).setVisible(true);
                this.dispose(); // 關掉目前畫面

            } catch (SQLException ex) {
                ex.printStackTrace();

                String message = "查詢失敗，請稍後再試。";
                String errorDialogTitle = "錯誤";

                Object[] options = {"重新查詢", "回首頁"};
                int choice = JOptionPane.showOptionDialog(
                        this,
                        message,
                        errorDialogTitle,
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
            }*/ // 資料庫暫時註解掉
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


    // 粉紅色漸層背景
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
