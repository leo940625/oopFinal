package ui.ticketdownload;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import ui.HomeFrame;
import ui.ticketdownload.TicketPanel;

/**
 * 顯示車票
 */
public class TicketPreviewFrame extends JFrame {
    private String departureStation;
    private String arrivalStation;
    private String departureTime;
    private String arrivalTime;
    private String trainNumber;

    public TicketPreviewFrame(String departureStation, String arrivalStation,
                               String departureTime, String arrivalTime, String trainNumber) {
        this.departureStation = departureStation;
        this.arrivalStation = arrivalStation;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.trainNumber = trainNumber;

        setTitle("車票預覽");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBackground(Color.WHITE);

        TicketPanel ticketPanel = new TicketPanel(trainNumber, departureStation, arrivalStation, departureTime, arrivalTime);
        contentPane.add(ticketPanel, BorderLayout.CENTER);

        JButton homeButton = new JButton("回首頁");
        homeButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        homeButton.setFocusPainted(false);
        homeButton.addActionListener(e -> {
            new HomeFrame();
            dispose();
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(homeButton);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(contentPane);
        setVisible(true);
    }

    // === 車票顯示區域（內部類）===
    private class TicketPanel extends JPanel {
        private Image ticketImage;

        public TicketPanel() {
            setOpaque(false);
            ticketImage = new ImageIcon("/Users/meredithfang/NetBeansProjects/resources/ticket.jpg").getImage(); // 這邊記得改你的圖
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            int panelWidth = getWidth();
            int panelHeight = getHeight();

            // --- 畫背景圖片 ---
            int imgWidth = 600;
            int imgHeight = 300;
            int imgX = (panelWidth - imgWidth) / 2;
            int imgY = 30;
            g.drawImage(ticketImage, imgX, imgY, imgWidth, imgHeight, this);

            // --- 畫車站名稱 ---
            g.setColor(Color.BLACK);
            g.setFont(new Font("SansSerif", Font.BOLD, 20));
            int stationY = imgY + 80;
            int departureX = imgX + 80;
            int arrivalX = imgX + imgWidth - 180;
            int timeY = stationY + 40;
            int trainNumberY = imgY + imgHeight - 30;

            g.drawString(departureStation, departureX, stationY);
            g.drawString(arrivalStation, arrivalX, stationY);

            // --- 畫時間 ---
            g.setFont(new Font("SansSerif", Font.PLAIN, 18));
            g.drawString(departureTime, departureX, timeY);
            g.drawString(arrivalTime, arrivalX, timeY);

            // --- 畫車次號碼 ---
            g.setFont(new Font("SansSerif", Font.BOLD, 20));
            int trainTextWidth = g.getFontMetrics().stringWidth("車次 " + trainNumber);
            int trainX = (panelWidth - trainTextWidth) / 2;
            g.drawString("車次 " + trainNumber, trainX, trainNumberY);
        }
    }
}
