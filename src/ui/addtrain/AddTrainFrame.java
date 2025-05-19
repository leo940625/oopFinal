package ui.addtrain;

import javax.swing.*;
import java.awt.*;
import java.time.LocalTime;
import ui.addtrain.login.LoginChoiceFrame;

/**
 * 新增班次
 * @author meredithfang
 */
class AddTrainFrame extends JFrame {
    private JComboBox<String> directionBox;
    private JTextField hourField;
    private JTextField minuteField;
    private JCheckBox[] stationChecks;

    public AddTrainFrame() {
        setTitle("新增車次");
        setSize(500, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel contentPane = new GradientPanel();
        contentPane.setLayout(new BorderLayout());

        JLabel title = new JLabel("新增車次", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 25));
        title.setForeground(Color.BLACK);
        contentPane.add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel();
        formPanel.setOpaque(false);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

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
