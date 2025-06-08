package ui.ticketdownload;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.awt.geom.*;
import java.sql.*;
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

    private static final int CORNER_RADIUS = 30;
    private static final int SIDE_RADIUS = 18;

    public TicketPanel(int trainId, String fromStation, String toStation) {
        this.trainId = trainId;
        this.fromStation = fromStation;
        this.toStation = toStation;
        loadTrainData();  // <-- 從 TrainDAO 拿 Train 物件，再取時間
        loadQRCodeImage(); // 載入 QR Code 圖片
        //setPreferredSize(new Dimension(800, 150));
        setBackground(Color.WHITE);
    }

    //
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
    }
    /*
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
        }
    }*/

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
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        // Draw ticket shape
        Area ticket = new Area(new Rectangle2D.Double(0, 0, width, height));
        int r = CORNER_RADIUS, d = r * 2;
        ticket.subtract(new Area(new Ellipse2D.Double(-r, -r, d, d)));
        ticket.subtract(new Area(new Ellipse2D.Double(width - r, -r, d, d)));
        ticket.subtract(new Area(new Ellipse2D.Double(-r, height - r, d, d)));
        ticket.subtract(new Area(new Ellipse2D.Double(width - r, height - r, d, d)));

        int count  = (height - 60) / (50 + 36); // 用count算拉長的話要多少個半圓
        int sr = SIDE_RADIUS, sd = sr * 2, baseY = 62, spacing = 50;
        for (int i = 0; i < 4; i++) {
            int y = baseY + i * spacing;
            ticket.subtract(new Area(new Ellipse2D.Double(-sr, y - sr, sd, sd)));
            ticket.subtract(new Area(new Ellipse2D.Double(width - sr, y - sr, sd, sd)));
        }

        // 填白底
        g2.setColor(Color.WHITE);
        g2.fill(ticket);

        // 畫外框
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(3));
        g2.draw(ticket);

        // 畫左側粉紅塊
        Shape oldClip = g2.getClip();
        g2.setClip(ticket);
        g2.setColor(new Color(255, 192, 203));
        g2.fillRect(2, 2, 150, height-4);
        g2.setClip(oldClip);

        // 左上文字
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 20));
        g2.drawString("車票", 60, 50);
        g2.setFont(new Font("SansSerif", Font.PLAIN, 14));
        g2.drawString("Ticket", 60, 75);

        // 主區內容
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("SansSerif", Font.BOLD, 18));
        g2.drawString("車次 " + trainId, 260, 55);
        g2.drawString(fromStation + "      →      " + toStation, 220, 120);
        g2.setFont(new Font("SansSerif", Font.BOLD, 14));
        g2.drawString(departureTime + "                 " + arrivalTime, 220, 150);

        // 畫虛線
        g2.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{5}, 0));
        g2.setColor(Color.GRAY);
        g2.drawLine(475, 20, 475, height - 20);

        // 更多資訊
        g2.setFont(new Font("SansSerif", Font.BOLD, 18));
        g2.setColor(Color.BLACK);
        g2.drawString("更多資訊", 550, 65);

        // 畫 QR code
        if (qrImage != null) {
            int qrSize = 100;
            g2.drawImage(qrImage, 535, 90, qrSize, qrSize, this);
        }
        g2.dispose();
    }
}