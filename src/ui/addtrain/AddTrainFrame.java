package ui.addtrain;

import dao.*;
import model.Station;
import model.StopTime;
import model.Train;
import ui.GradientPanel;
import ui.HomeFrame;
import util.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 新增車次的圖形用戶界面框架。
 * 提供用戶輸入車次編號、方向、出發時間和停靠車站的功能，並將數據存入數據庫。
 */
public class AddTrainFrame extends JFrame {
    private JComboBox<String> directionBox;
    private JTextField hourField;
    private JTextField minuteField;
    private JCheckBox[] stationChecks;
    private JTextField trainIdField;

    /**
     * 檢查用戶是否已登錄，若已登錄則打開新增車次窗口，否則顯示錯誤提示。
     *
     * @param parent     父組件，用於顯示錯誤提示對話框
     * @param isLoggedIn 用戶是否已登錄的標誌
     */
    public static void openIfAuthenticated(Component parent, boolean isLoggedIn) {
        if (isLoggedIn) {
            new AddTrainFrame().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(parent, "請先登入員工帳號才能新增車次！", "權限不足", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 構造函數，初始化新增車次窗口的圖形界面。
     * 設置窗口標題、大小、佈局，並添加方向選擇、車次編號輸入、出發時間輸入和停靠車站勾選等組件。
     */
    public AddTrainFrame() {
        setTitle("新增車次");
        setSize(600, 600);
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
        titlePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        titlePanel.add(Box.createHorizontalGlue());
        titlePanel.add(title);
        titlePanel.add(Box.createHorizontalGlue());

        // ✕ 按鈕
        JButton closeButton = new JButton("✕");
        closeButton.setPreferredSize(new Dimension(40, 30));
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
        topPanel.add(titlePanel, BorderLayout.CENTER);
        topPanel.add(rightTopPanel, BorderLayout.EAST);
        contentPane.add(topPanel, BorderLayout.NORTH);

        // 方向選單
        JPanel directionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 20));
        directionPanel.setOpaque(false);
        JLabel directionLabel = new JLabel("方向");
        directionLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
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
        trainIdLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
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
        timeLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
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
        stationLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        stationTitlePanel.add(stationLabel);
        formPanel.add(stationTitlePanel);
        formPanel.add(Box.createVerticalStrut(5));

        // 勾選車站
        JPanel stationOuterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 10));
        stationOuterPanel.setOpaque(false);

        String[] stationNames = {"南港", "台北", "板橋", "桃園", "新竹", "苗栗",
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

    /**
     * 處理用戶提交的車次信息。
     * 驗證輸入的時間格式、車次編號和選擇的車站，然後將車次數據存入數據庫。
     * 若操作成功，提供繼續新增或返回首頁的選項；若失敗，顯示錯誤提示。
     */
    private void handleSubmit() {
        try (Connection conn = DBConnection.getConnection()) {
            BlockSectionDAO sectionDAO = new BlockSectionDAOImpl(conn);
            StationDAO stationDAO = new StationDAOImpl(conn);
            TrainDAO trainDAO = new TrainDAOImpl(conn);

            // 驗證時間格式與範圍
            String hourText = hourField.getText().trim();
            String minuteText = minuteField.getText().trim();

            if (!hourText.matches("\\d{1,2}") || !minuteText.matches("\\d{1,2}")) {
                JOptionPane.showMessageDialog(this,
                        "請輸入正確格式的時與分（兩欄皆須為數字）",
                        "錯誤", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int hour = Integer.parseInt(hourText);
            int minute = Integer.parseInt(minuteText);

            if (hour < 0 || hour > 23 || minute < 0 || minute > 59) {
                JOptionPane.showMessageDialog(this,
                        "小時請輸入 0~23，分鐘請輸入 0~59",
                        "錯誤", JOptionPane.ERROR_MESSAGE);
                return;
            }

            LocalTime departure = LocalTime.of(hour, minute);

            String direction = (String) directionBox.getSelectedItem();
            boolean isNorthbound = "北上".equals(direction);
            String trainId = trainIdField.getText().trim();
            List<Train> trains = trainDAO.getAllTrains();

            if (!trainId.matches("\\d{4}")) {
                JOptionPane.showMessageDialog(this,
                        "請輸入 4 位數字作為車次編號！",
                        "錯誤", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int intTrainId = Integer.valueOf(trainId);

            if (isTrainIdDuplicate(intTrainId, trains)) {
                JOptionPane.showMessageDialog(this,
                        "該車次編號已存在，請重新輸入！",
                        "錯誤", JOptionPane.ERROR_MESSAGE);
                return;
            }

            List<StopTime> stops = this.getSelectedStations();

            if (stops.size() < 2) {
                JOptionPane.showMessageDialog(this,
                        "請至少選擇兩個停靠車站！",
                        "錯誤", JOptionPane.ERROR_MESSAGE);
                return;
            }


            Train train = new Train(intTrainId, stops, isNorthbound);
            train.calculateSchedule(sectionDAO,stationDAO.getAllStations(),departure);
            trainDAO.addTrain(train);

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
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "發生未知錯誤，請稍後再試。",
                    "錯誤", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 檢查車次編號是否已存在於數據庫中。
     *
     * @param id     要檢查的車次編號
     * @param trains 現有的車次列表
     * @return 如果車次編號已存在則返回 {@code true}，否則返回 {@code false}
     */
    private boolean isTrainIdDuplicate(int id, List<Train> trains) {
        List<Integer> trainIds = new ArrayList<>();
        for (Train train : trains) {
            trainIds.add(train.getTrainNumber());
        }
        for (Integer trainId : trainIds) {
            if (id == trainId) {
                return true;
            }
        }
        return false;
    }

    /**
     * 創建一個具有自定義樣式的按鈕。
     *
     * @param text 按鈕顯示的文字
     * @return 配置了樣式和懸停效果的 {@link JButton} 對象
     */
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

    /**
     * 取得使用者勾選的停靠車站清單。
     *
     * @return 包含所有被選取之車站名稱的字串清單
     */
    private List<StopTime> getSelectedStations() {
        try (Connection conn = DBConnection.getConnection()) {
            StationDAO stationDAO = new StationDAOImpl(conn);
            List<StopTime> selected = new ArrayList<>();
            for (JCheckBox cb : stationChecks) {
                if (cb.isSelected()) {
                    Station state = stationDAO.getStationByName(cb.getText());
                    StopTime st = new StopTime(state,null,null);
                    selected.add(st);
                }
            }
            return selected;
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "發生未知錯誤，請稍後再試。",
                    "錯誤", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
}