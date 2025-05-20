package ui.addtrain;

import javax.swing.*;
import java.awt.*;
import java.time.LocalTime;
import ui.*;

/**
 * 新增班次
 */
public class AddTrainFrame extends JFrame {
    private JComboBox<String> directionBox;
    private JTextField hourField;
    private JTextField minuteField;
    private JCheckBox[] stationChecks;
    private JTextField trainIdField;

    public AddTrainFrame() {
        setTitle("新增車次");
        setSize(500, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel contentPane = new GradientPanel();
        contentPane.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel();
        formPanel.setOpaque(false);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

        // 建立置中的 title label
        JLabel title = new JLabel("新增車次");
        title.setFont(new Font("SansSerif", Font.BOLD, 25));
        title.setForeground(Color.BLACK);

        // 用 BoxLayout 的 JPanel 讓 title 真正水平置中
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
        titlePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50)); // 確保panel佔了整個空間
        titlePanel.add(Box.createHorizontalGlue());
        titlePanel.add(title);
        titlePanel.add(Box.createHorizontalGlue());

        // ✕ 按鈕
        JButton closeButton = new JButton("✕");
        closeButton.setPreferredSize(new Dimension(40, 30)); // ✕ 按鈕邊框
        closeButton.setFocusPainted(false);
        closeButton.setBackground(new Color(255, 182, 193));
        closeButton.setForeground(Color.BLACK);
        closeButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        closeButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.setContentAreaFilled(true);
        closeButton.setOpaque(true);

        // 滑鼠移入hover
        closeButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                closeButton.setBackground(new Color(255, 105, 180));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                closeButton.setBackground(new Color(255, 182, 193));
            }
        });

        // 點擊返回首頁
        closeButton.addActionListener(e -> {
            new HomeFrame();
            dispose();
        });

        // 右上角的 FlowPanel（只放叉叉）
        JPanel rightTopPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightTopPanel.setOpaque(false);
        rightTopPanel.add(closeButton);

        // 將標題與右上角叉叉一起放入 topPanel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(titlePanel, BorderLayout.CENTER);   // title 置中
        topPanel.add(rightTopPanel, BorderLayout.EAST);  // 叉叉在右邊
        // 加入畫面
        contentPane.add(topPanel, BorderLayout.NORTH);

        // 方向選單
        JPanel directionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 20));
        directionPanel.setOpaque(false);
        JLabel directionLabel = new JLabel("方向");
        directionLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        directionBox = new JComboBox<>(new String[]{"南下", "北上"});
        directionBox.setFont(new Font("SansSerif", Font.PLAIN, 15));
        directionPanel.add(directionLabel);
        directionPanel.add(directionBox);
        formPanel.add(directionPanel);
        formPanel.add(Box.createVerticalStrut(10));

        // 車次編號輸入
        JPanel trainIdPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 14));
        trainIdPanel.setOpaque(false);
        JLabel trainIdLabel = new JLabel("車次編號");
        trainIdLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        trainIdField = new JTextField(6);
        trainIdField.setFont(new Font("SansSerif", Font.PLAIN, 15));
        trainIdPanel.add(trainIdLabel);
        trainIdPanel.add(trainIdField);
        formPanel.add(trainIdPanel);
        formPanel.add(Box.createVerticalStrut(10));


        // 出發時間
        JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 14));
        timePanel.setOpaque(false);
        JLabel timeLabel = new JLabel("出發時間");
        timeLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        hourField = new JTextField(2);
        minuteField = new JTextField(2);
        hourField.setFont(new Font("SansSerif", Font.PLAIN, 15));
        minuteField.setFont(new Font("SansSerif", Font.PLAIN, 15));
        timePanel.add(timeLabel);
        timePanel.add(hourField);
        timePanel.add(new JLabel(":"));
        timePanel.add(minuteField);
        formPanel.add(timePanel);
        formPanel.add(Box.createVerticalStrut(20));

        // 停靠車站標題
        JPanel stationTitlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 14));
        stationTitlePanel.setOpaque(false);
        JLabel stationLabel = new JLabel("停靠車站");
        stationLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        stationTitlePanel.add(stationLabel);
        formPanel.add(stationTitlePanel);
        formPanel.add(Box.createVerticalStrut(5));

        // 勾選車站
        JPanel stationOuterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 10));
        stationOuterPanel.setOpaque(false);

        // TODO: 可以引入資料庫就不用寫死車站
        String[] stationNames = {"板橋", "台北", "南港", "桃園", "新竹", "苗栗",
                                 "台中", "彰化", "雲林", "嘉義", "台南", "左營"};
        JPanel stationPanel = new JPanel(new GridLayout(3, 4, 15, 5));
        stationPanel.setOpaque(false);
        stationChecks = new JCheckBox[stationNames.length];
        for (int i = 0; i < stationNames.length; i++) {
            stationChecks[i] = new JCheckBox(stationNames[i]);
            stationChecks[i].setFont(new Font("SansSerif", Font.PLAIN, 15));
            stationChecks[i].setOpaque(false);
            stationPanel.add(stationChecks[i]);
        }
        stationOuterPanel.add(stationPanel);
        formPanel.add(stationOuterPanel);

        // 空白分隔
        JPanel spacer = new JPanel();
        spacer.setOpaque(false);
        formPanel.add(spacer);

        // 送出按鈕
        JPanel submitPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        submitPanel.setOpaque(false);

        JButton submitButton = createStyledButton("送出");
        submitButton.setPreferredSize(new Dimension(200, 40));
        submitButton.addActionListener(e -> handleSubmit());
        submitPanel.add(submitButton);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(submitPanel);

        contentPane.add(formPanel, BorderLayout.CENTER);
        contentPane.setBorder(BorderFactory.createEmptyBorder(30, 40, 20, 40));

        setContentPane(contentPane);
        setVisible(true);
    }
    private void handleSubmit() {
        try {
            int hour = Integer.parseInt(hourField.getText());
            int minute = Integer.parseInt(minuteField.getText());
            LocalTime departure = LocalTime.of(hour, minute);
            String direction = (String) directionBox.getSelectedItem();
            boolean isNorthbound = "北上".equals(direction);
            //TODO:TrainID是車次但是是string,isNorthbound是方向(boolean),intTraindId(int),departure是出發時間(Localtime)
            String trainId = trainIdField.getText().trim();
            if (!trainId.matches("\\d{4}")) {
                JOptionPane.showMessageDialog(this,
                        "請輸入 4 位數字作為車次編號！",
                        "錯誤", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int intTraindId = Integer.valueOf(trainId);
            if (isTrainIdDuplicate(trainId)) {
                JOptionPane.showMessageDialog(this,
                        "該車次編號已存在，請重新輸入！",
                        "錯誤", JOptionPane.ERROR_MESSAGE);
                return;
            }


            int selectedStations = 0;
            StringBuilder stations = new StringBuilder();
            for (JCheckBox cb : stationChecks) {
                if (cb.isSelected()) {
                    stations.append(cb.getText()).append(" ");
                    selectedStations++;
                }
            }

            if (selectedStations < 2) {
                JOptionPane.showMessageDialog(this,
                        "請至少選擇兩個停靠車站！",
                        "錯誤", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int choice = JOptionPane.showOptionDialog(this,
                    "新增成功！請選擇接下來的操作：",
                    "成功",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    new String[]{"新增其他車次", "回首頁"},
                    "新增其他車次");

            if (choice == JOptionPane.YES_OPTION) {
                new AddTrainFrame();
                dispose();
            } else if (choice == JOptionPane.NO_OPTION) {
                new HomeFrame();
                dispose();
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "請正確輸入小時和分鐘（0-23 小時, 0-59 分鐘）",
                    "錯誤", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isTrainIdDuplicate(String id) {
        // TODO: 這邊要連接你的資料庫進行查詢，檢查該車次是否已存在
        // 這裡暫時用假資料模擬：
        String[] existingIds = {"1234", "5678", "9999"};
        for (String s : existingIds) {
            if (s.equals(id)) return true;
        }
        return false;
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
