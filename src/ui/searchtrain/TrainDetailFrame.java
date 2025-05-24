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

import java.sql.Connection;
import java.sql.SQLException;
import java.util.regex.Pattern;
import model.Train;
import model.StopTime;

public class TrainDetailFrame extends JFrame {

    /**
     * 建構子：載入一班列車的完整時刻表，並顯示在表格中
     */
    public TrainDetailFrame(Train train) {
        setTitle("車次 " + train.getTrainNumber() + " 詳細時刻表");
        setSize(600, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        // 上方標題
        JLabel title = new JLabel("車次 " + train.getTrainNumber(), SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        add(title, BorderLayout.NORTH);

        // 表格：標題列 + 每一站資訊（共三欄）
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new GridLayout(train.getStopTimes().size() + 1, 3, 5, 10));
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // 表頭
        tablePanel.add(createHeader("車站"));
        tablePanel.add(createHeader("抵達時間"));
        tablePanel.add(createHeader("出發時間"));

        List<StopTime> stops = train.getStopTimes();
        for (int i = 0; i < stops.size(); i++) {
            StopTime st = stops.get(i);
            String station = st.getStation().getStationName();
            LocalTime arrival = st.getArrivalTime();
            LocalTime departure = st.getDepartureTime();

            // 抵達／出發時間處理
            String arrText = (arrival != null) ? arrival.toString() : (i == 0 ? "起始站" : "");
            String depText = (departure != null) ? departure.toString() : (i == stops.size() - 1 ? "終點站" : "");

            tablePanel.add(createStationCell(station));  // ✅ 改這裡：車站欄改為粉底
            tablePanel.add(createCell(arrText));
            tablePanel.add(createCell(depText));
        }

        // 滾動區域包住表格
        JScrollPane scrollPane = new JScrollPane(tablePanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        // 下方按鈕列
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 20));
        buttonPanel.setBackground(Color.WHITE);

        JButton againButton = createStyledButton("繼續查詢");
        againButton.addActionListener(e -> {
            dispose();
            new TrainSearchChoiceFrame().setVisible(true);
        });

        JButton backButton = createStyledButton("回首頁");
        backButton.addActionListener(e -> {
            dispose();
            new HomeFrame().setVisible(true);
        });

        buttonPanel.add(againButton);
        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * 建立表格標題欄位（深粉色底，白字）
     */
    private JLabel createHeader(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setOpaque(true);
        label.setBackground(new Color(255, 105, 180)); // 深粉紅
        label.setForeground(Color.WHITE);
        label.setFont(new Font("SansSerif", Font.BOLD, 16));
        return label;
    }

    /**
     * 建立標準表格格子（白底＋粉紅邊框）
     */
    private JLabel createCell(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.PLAIN, 15));
        label.setOpaque(true);
        label.setBackground(Color.WHITE);
        label.setBorder(BorderFactory.createLineBorder(new Color(255, 182, 193)));
        return label;
    }

    /**
     * 建立車站欄位專用格子（淡粉紅底＋粉紅邊框）
     */
    private JLabel createStationCell(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.PLAIN, 15));
        label.setOpaque(true);
        label.setBackground(new Color(255, 228, 225)); // ⬅ 淡粉紅
        label.setBorder(BorderFactory.createLineBorder(new Color(255, 182, 193)));
        return label;
    }

    // 重載建構子（當我以起訖站查詢時想要看更多資訊）
    public TrainDetailFrame(Train train, String from, String to) {
        this(train); // 呼叫原始建構子初始化畫面

        // 取得內容面板（GridLayout 表格）
        JScrollPane scrollPane = (JScrollPane) getContentPane().getComponent(1);
        JPanel tablePanel = (JPanel) scrollPane.getViewport().getView();

        List<StopTime> stops = train.getStopTimes();
        boolean between = false;

        for (int i = 0; i < stops.size(); i++) {
            StopTime st = stops.get(i);
            String name = st.getStation().getStationName();

            if (name.equals(from)) between = true;

            if (between) { // 起訖站經過的車站名要用不同的粉色填滿突顯出來
                for (int j = 0; j < 3; j++) { // 每列三欄
                    Component cell = tablePanel.getComponent((i + 1) * 3); // +1 跳過標題
                    if (cell instanceof JLabel label) {
                        label.setBackground(new Color(255, 192, 203)); // 突出的粉紅
                    }
                }
            }

            if (name.equals(to)) break;
        }

        // 移除原本底部的兩個按鈕區域（注意：index = 2 是 SOUTH）
        getContentPane().remove(2);

        // 加入只有「關閉」按鈕的新按鈕區
        JPanel closePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        closePanel.setBackground(Color.WHITE);

        JButton closeButton = createStyledButton("關閉");
        closeButton.addActionListener(e -> dispose());

        closePanel.add(closeButton);
        add(closePanel, BorderLayout.SOUTH);
    }

    /**
     * 建立有粉紅 hover 效果的按鈕（給底部使用）
     */
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        Color baseColor = new Color(255, 182, 193);
        Color hoverColor = new Color(255, 105, 180);
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setBackground(baseColor);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(baseColor);
            }
        });
        return button;
    }
}
