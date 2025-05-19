/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.trainscheduler;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;

/**
 * 輸入車票資訊
 * @author meredithfang
 */
class TicketInputFrame extends JFrame {
    private JTextField trainNumberField;
    private JComboBox<String> departureBox;
    private JComboBox<String> arrivalBox;
    private static final Map<String, java.util.List<String>> trainStops = new HashMap<>();

    public TicketInputFrame() {
        setTitle("車票下載");
        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initializeTrainStops();

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
        String trainNumber = trainNumberField.getText();
        String departureStation = (String) departureBox.getSelectedItem();
        String arrivalStation = (String) arrivalBox.getSelectedItem();

        if (!trainStops.containsKey(trainNumber)) {
            JOptionPane.showMessageDialog(this, "查無此車次！", "錯誤", JOptionPane.ERROR_MESSAGE);
            return;
        }

        java.util.List<String> stops = trainStops.get(trainNumber);
        int depIdx = stops.indexOf(departureStation);
        int arrIdx = stops.indexOf(arrivalStation);

        if (depIdx == -1 || arrIdx == -1) {
            JOptionPane.showMessageDialog(this, "資料錯誤！", "錯誤", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (depIdx >= arrIdx) {
            JOptionPane.showMessageDialog(this, "資料錯誤！", "錯誤", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 假資料，這裡可以之後連後端
        String departureTime = "08:00";
        String arrivalTime = "10:30";

        new TicketPreviewFrame(departureStation, arrivalStation, departureTime, arrivalTime, trainNumber);
        dispose();
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

    private void initializeTrainStops() {
        trainStops.put("1234", Arrays.asList("板橋", "台北", "桃園", "新竹", "台中", "嘉義", "台南", "左營"));
        trainStops.put("5678", Arrays.asList("台北", "南港", "桃園", "新竹", "苗栗", "台中", "彰化"));
        trainStops.put("4321", Arrays.asList("左營", "台南", "嘉義", "台中", "新竹", "桃園", "台北", "板橋"));
    }

    private String[] getAllStations() {
        return new String[]{"板橋", "台北", "南港", "桃園", "新竹", "苗栗", "台中", "彰化", "雲林", "嘉義", "台南", "左營"};
    }
}
