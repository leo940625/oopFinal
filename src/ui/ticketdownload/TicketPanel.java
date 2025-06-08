package ui.ticketdownload;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.*;
import java.net.URL;
import dao.TrainDAO;
import dao.TrainDAOImpl;
import model.*;
import util.DBConnection;
import util.TrainUtils;
import dao.*;

public class TicketPanel extends JPanel {
    private int trainId;
    private String fromStation;
    private String toStation;
    private String departureTime;
    private String arrivalTime;
    private BufferedImage qrImage;

    public TicketPanel(int trainId, String fromStation, String toStation) {
        this.trainId = trainId;
        this.fromStation = fromStation;
        this.toStation = toStation;

        loadTrainData();  // <-- 從 TrainDAO 拿 Train 物件，再取時間
        loadQRCodeImage(); // 載入 QR Code 圖片
        //setPreferredSize(new Dimension(800, 150));
        setBackground(Color.WHITE);
    }

    /* TODO:資料庫版本
    private void loadTrainData() {
        try (Connection conn = DBConnection.getConnection()) {
            TrainDAO trainDAO = new TrainDAOImpl(conn);
            StationDAO stationDAO = new StationDAOImpl(conn);
            TrainUtils trainUtils = new TrainUtils();

            Train train = trainDAO.getTrainByNumber(trainId);
            Station from = stationDAO.getStationByName(fromStation);
            Station to = stationDAO.getStationByName(toStation);

            if (train == null || from == null || to == null) {
                throw new IllegalArgumentException("傳入的車次或站點無效。");
            }

            StopTime[] stops = trainUtils.findDepartureAndArrivalStops(train, from, to);
            StopTime departureStop = stops[0];
            StopTime arrivalStop = stops[1];

            if (departureStop == null || arrivalStop == null
                    || departureStop.getDepartureTime() == null
                    || arrivalStop.getArrivalTime() == null) {
                throw new IllegalStateException("找不到停靠時間，請檢查車次資料是否完整。");
            }

            this.departureTime = departureStop.getDepartureTime().toString();
            this.arrivalTime = arrivalStop.getArrivalTime().toString();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "無法載入車票資訊：" + e.getMessage(), "錯誤", JOptionPane.ERROR_MESSAGE);
        }
    }*/
    // 測試ui
    private void loadTrainData() {
        // 🧪 假資料模式
        // System.out.println("[DEBUG] 使用假資料載入車票時間");

        // 根據車次與站名給固定時間
        if (trainId == 1234 && fromStation.equals("台中") && toStation.equals("台南")) {
            this.departureTime = "09:12";
            this.arrivalTime = "10:28";
        } else if (trainId == 5678 && fromStation.equals("台北") && toStation.equals("台中")) {
            this.departureTime = "07:00";
            this.arrivalTime = "08:45";
        } else {
            // 找不到匹配 → 給錯誤提示
            this.departureTime = "??:??";
            this.arrivalTime = "??:??";
            System.out.println("[WARN] 無對應假資料，請確認車次與站名是否輸入正確");
        }
    }

    private void loadQRCodeImage() {
        try {
            URL url = getClass().getResource("/resources/qr-code.png");
            if (url != null) {
                qrImage = ImageIO.read(url);
            } else {
                System.out.println("[WARN] 找不到 QRCode 資源");
                qrImage = null;
            }
        } catch (IOException e) {
            qrImage = null;
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // 左側粉色區塊
        // g.setColor(new Color(255, 228, 225)); // 淺粉色
        g2.setColor(new Color(255, 192, 203)); // 粉紅
        g2.fillRect(0, 0, 150, getHeight());

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 20));
        g2.drawString("車票", 50, 40);
        g2.setFont(new Font("SansSerif", Font.PLAIN, 14));
        g2.drawString("Ticket", 50, 65);

        // 中間主要白色區塊（文字）
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("SansSerif", Font.BOLD, 18));
        g2.drawString("車次 " + trainId, 260, 55); // 120->200
        g2.drawString(fromStation + "      →      " + toStation, 220, 120);

        g2.setFont(new Font("SansSerif", Font.BOLD, 14)); // TODO:調整一下
        g2.drawString(departureTime + "                 " + arrivalTime, 220, 150);

        // 垂直虛線
        Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
                0, new float[]{5}, 0);
        g2.setStroke(dashed);
        g2.setColor(Color.GRAY);
        g2.drawLine(480, 20, 480, getHeight() - 20);

        // 更多資訊 + QRCode 區域
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("SansSerif", Font.BOLD, 18));
        g2.drawString("更多資訊", 560, 65);

        if (qrImage != null) {
            g2.drawImage(qrImage, 550, 90, 100, 100, this);
        } else {
            g2.drawRect(550, 70, 60, 60);
            g2.drawString("[QR]", 570, 85);
        }
    }
}