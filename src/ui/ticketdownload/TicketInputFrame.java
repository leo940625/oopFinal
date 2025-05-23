package ui.ticketdownload;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;

import model.*;
import ui.GradientPanel;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import dao.*;
import util.DBConnection;
import util.TrainUtils;

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
        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel contentPane = new GradientPanel();
        contentPane.setLayout(new BorderLayout());

        JLabel title = new JLabel("輸入車票資料", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 25));
        title.setForeground(Color.BLACK);
        contentPane.add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel();
        formPanel.setOpaque(false);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        // 車次號碼輸入
        JPanel trainNumberPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        trainNumberPanel.setOpaque(false);
        JLabel trainNumberLabel = new JLabel("車次號碼");
        trainNumberLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        trainNumberField = new JTextField(10);
        trainNumberField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        trainNumberPanel.add(trainNumberLabel);
        trainNumberPanel.add(trainNumberField);
        formPanel.add(trainNumberPanel);

        formPanel.add(Box.createVerticalStrut(20));

        // 出發站選單
        JPanel departurePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        departurePanel.setOpaque(false);
        JLabel departureLabel = new JLabel("出發車站");
        departureLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        departureBox = new JComboBox<>(getAllStations());
        departureBox.setFont(new Font("SansSerif", Font.PLAIN, 16));
        departurePanel.add(departureLabel);
        departurePanel.add(departureBox);
        formPanel.add(departurePanel);

        formPanel.add(Box.createVerticalStrut(20));

        // 抵達站選單
        JPanel arrivalPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        arrivalPanel.setOpaque(false);
        JLabel arrivalLabel = new JLabel("抵達車站");
        arrivalLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        arrivalBox = new JComboBox<>(getAllStations());
        arrivalBox.setFont(new Font("SansSerif", Font.PLAIN, 16));
        arrivalPanel.add(arrivalLabel);
        arrivalPanel.add(arrivalBox);
        formPanel.add(arrivalPanel);

        formPanel.add(Box.createVerticalStrut(30));

        // 送出按鈕
        JButton previewButton = createStyledButton("車票預覽");
        previewButton.addActionListener(e -> handlePreview());
        formPanel.add(previewButton);

        contentPane.add(formPanel, BorderLayout.CENTER);
        setContentPane(contentPane);
        setVisible(true);
    }

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

            // 解析車次編號
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

            // 取得時間
            String departureTime = departureStop.getDepartureTime().toString();
            String arrivalTime = arrivalStop.getArrivalTime().toString();

            // 開啟預覽視窗並關閉當前視窗
            new TicketPreviewFrame(departureStationName, arrivalStationName, departureTime, arrivalTime, trainNumber);
            dispose();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "資料庫錯誤：" + e.getMessage(), "錯誤", JOptionPane.ERROR_MESSAGE);
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

    // TODO: 跟資料庫確認輸入的火車班次是不是確實有停輸入的兩個車站

    private String[] getAllStations() {
        return new String[]{"南港", "台北", "板橋", "桃園", "新竹", "苗栗", "台中", "彰化", "雲林", "嘉義", "台南", "左營"};
    }
}
