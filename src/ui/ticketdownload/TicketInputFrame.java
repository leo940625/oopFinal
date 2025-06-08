package ui.ticketdownload;

import dao.*;
import model.Station;
import model.StopTime;
import model.Train;
import ui.GradientPanel;
import ui.HomeFrame;
import util.DBConnection;
import util.TrainUtils;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 輸入車票資訊
 */
public class TicketInputFrame extends JFrame {
    private JTextField trainNumberField;
    private JComboBox<String> departureBox;
    private JComboBox<String> arrivalBox;
    private static final Map<String, java.util.List<String>> trainStops = new HashMap<>();

    public TicketInputFrame() {
        setTitle("車票下載");
        setSize(600, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 背景面板
        JPanel contentPane = new GradientPanel();
        contentPane.setLayout(new BorderLayout());
        setContentPane(contentPane);

        // formPanel：整體表單內容
        JPanel formPanel = new JPanel();
        formPanel.setOpaque(false);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 20, 40));

        // 標題
        JLabel title = new JLabel("輸入車票資料", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 25));
        title.setForeground(Color.BLACK);

        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
        titlePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        titlePanel.add(Box.createHorizontalGlue());
        titlePanel.add(title);
        titlePanel.add(Box.createHorizontalGlue());

        formPanel.add(titlePanel);
        formPanel.add(Box.createVerticalStrut(50)); // 垂直空白 50 像素

        // 車次號碼輸入
        JPanel trainNumberPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        trainNumberPanel.setOpaque(false);
        JLabel trainNumberLabel = new JLabel("車次號碼");
        trainNumberLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        trainNumberField = new JTextField(10);
        trainNumberField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        trainNumberPanel.add(trainNumberLabel);
        trainNumberPanel.add(trainNumberField);
        formPanel.add(trainNumberPanel);

        formPanel.add(Box.createVerticalStrut(10));

        // 出發站選單
        JPanel departurePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        departurePanel.setOpaque(false);
        JLabel departureLabel = new JLabel("出發車站");
        departureLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        departureBox = new JComboBox<>(getAllStations());
        departureBox.setFont(new Font("SansSerif", Font.PLAIN, 16));
        departurePanel.add(departureLabel);
        departurePanel.add(departureBox);
        formPanel.add(departurePanel);

        formPanel.add(Box.createVerticalStrut(10));

        // 抵達站選單
        JPanel arrivalPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        arrivalPanel.setOpaque(false);
        JLabel arrivalLabel = new JLabel("抵達車站");
        arrivalLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        arrivalBox = new JComboBox<>(getAllStations());
        arrivalBox.setFont(new Font("SansSerif", Font.PLAIN, 16));
        arrivalPanel.add(arrivalLabel);
        arrivalPanel.add(arrivalBox);
        formPanel.add(arrivalPanel);

        formPanel.add(Box.createVerticalStrut(30));


        // 下方按鈕區：車票預覽 + 回首頁
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        buttonPanel.setOpaque(false);

        // 車票預覽按鈕
        JButton previewButton = createStyledButton("車票預覽");
        previewButton.setPreferredSize(new Dimension(160, 40));
        previewButton.addActionListener(e -> handlePreview());
        buttonPanel.add(previewButton);

        // 回首頁按鈕
        JButton homeButton = createStyledButton("回首頁");
        homeButton.setPreferredSize(new Dimension(160, 40));
        homeButton.addActionListener(e -> {
            dispose();             // 關閉目前畫面
            new HomeFrame();       // 開啟首頁
        });
        buttonPanel.add(homeButton);

        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(buttonPanel);

        contentPane.add(formPanel, BorderLayout.CENTER);
        setContentPane(contentPane);
        setVisible(true);
    }

    // TODO:資料庫版本
    private void handlePreview() {
        try (Connection conn = DBConnection.getConnection()) {
            // 初始化 DAO，確保一致性
            BlockSectionDAO sectionDAO = new BlockSectionDAOImpl(conn);
            StationDAO stationDAO = new StationDAOImpl(conn); // 傳入連線
            TrainDAO trainDAO = new TrainDAOImpl(conn);
            TrainUtils trainUtils = new TrainUtils();

            // 取得輸入並清理
            String trainNumber = trainNumberField.getText().trim();
            String departureStationName = (String) departureBox.getSelectedItem();
            String arrivalStationName = (String) arrivalBox.getSelectedItem();

            // 驗證車次編號格式
            if (!trainNumber.matches("\\d{4}")) {
                JOptionPane.showMessageDialog(this, "請輸入 4 位數字作為車次編號！", "錯誤", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 取得資訊
            int trainId = Integer.parseInt(trainNumber);

            // 查詢車次和站點
            Train train = trainDAO.getTrainByNumber(trainId);
            if (train == null) {
                JOptionPane.showMessageDialog(this, "查無此車次！", "錯誤", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Station departureStation = stationDAO.getStationByName(departureStationName);
            Station arrivalStation = stationDAO.getStationByName(arrivalStationName);

            if (departureStation == null || arrivalStation == null) {
                JOptionPane.showMessageDialog(this, "無效的出發站或到達站！", "錯誤", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 尋找出發和到達站的停靠時間
            StopTime deparr[] = trainUtils.findDepartureAndArrivalStops(train, departureStation, arrivalStation);
            StopTime departureStop = deparr[0];
            StopTime arrivalStop = deparr[1];

            // 驗證停靠站和方向
            if (departureStop == null || arrivalStop == null) {
                JOptionPane.showMessageDialog(this, "車次未停靠指定的出發站或到達站！", "錯誤", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int departureStationId = departureStation.getStationId();
            int arrivalStationId = arrivalStation.getStationId();
            int direction = train.getDirection() ? 1 : -1;
            if ((departureStationId - arrivalStationId) * direction <= 0) {
                JOptionPane.showMessageDialog(this, "車次方向或站點順序無效！", "錯誤", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 彈出新視窗顯示 TicketPanel
            JFrame previewFrame = new JFrame("預覽車票");
            previewFrame.setSize(700, 300);
            previewFrame.setLocationRelativeTo(null);
            previewFrame.add(new TicketPanel(trainId, departureStationName, arrivalStationName));
            previewFrame.setVisible(true);
            // TODO:考慮一下要不要關: 先不關
            // dispose();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "資料庫錯誤：" + e.getMessage(), "錯誤", JOptionPane.ERROR_MESSAGE);
        }
    }
    // 測試ui版本
    /*
    private void handlePreview() {
        // 🧪 假資料模式：不連資料庫
        String trainNumber = trainNumberField.getText().trim();
        String departureStationName = (String) departureBox.getSelectedItem();
        String arrivalStationName = (String) arrivalBox.getSelectedItem();

        if (!trainNumber.matches("\\d{4}")) {
            JOptionPane.showMessageDialog(this, "請輸入 4 位數字作為車次編號！", "錯誤", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int trainId = Integer.parseInt(trainNumber);

        // 你可以假設只允許以下搭配
        if ((trainId == 1234 && departureStationName.equals("台中") && arrivalStationName.equals("台南")) ||
                (trainId == 5678 && departureStationName.equals("台北") && arrivalStationName.equals("台中"))) {

            JFrame previewFrame = new JFrame("預覽車票");
            previewFrame.setSize(700, 300);
            previewFrame.setLocationRelativeTo(null);
            previewFrame.add(new TicketPanel(trainId, departureStationName, arrivalStationName));
            previewFrame.setVisible(true);

        } else {
            JOptionPane.showMessageDialog(this, "查無此車次與起訖站搭配（假資料模式）", "錯誤", JOptionPane.ERROR_MESSAGE);
        }
    }*/


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
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(255, 105, 180));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(255, 182, 193));
            }
        });

        return button;
    }


    private String[] getAllStations() {
        return new String[]{"南港", "台北", "板橋", "桃園", "新竹", "苗栗", "台中", "彰化", "雲林", "嘉義", "台南", "左營"};
    }
}
