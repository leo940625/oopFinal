package ui.ticketdownload;

import javax.swing.*;
import java.awt.*;
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

    private final int PANEL_WIDTH = 400;
    private final int PANEL_HEIGHT = 200;
    private final int CORNER_RADIUS = 30;
    private final int SIDE_RADIUS = 12;
    private final int SIDE_COUNT = 3;

    public TicketPanel(int trainId, String fromStation, String toStation) {
        this.trainId = trainId;
        this.fromStation = fromStation;
        this.toStation = toStation;

        loadTrainData();  // <-- 從 TrainDAO 拿 Train 物件，再取時間
        setPreferredSize(new Dimension(600, 200));
        setBackground(Color.WHITE);
    }

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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 畫左邊藍色區塊
        g.setColor(new Color(100, 149, 237)); // 淺藍色
        g.fillRect(0, 0, 100, getHeight());

        g.setColor(Color.WHITE);
        g.setFont(new Font("SansSerif", Font.BOLD, 20));
        g.drawString("車票", 25, 40);
        g.setFont(new Font("SansSerif", Font.PLAIN, 14));
        g.drawString("Ticket", 25, 65);

        // 主要白色區塊
        g.setColor(Color.BLACK);
        g.setFont(new Font("SansSerif", Font.BOLD, 18));
        g.drawString("車次：" + trainId, 120, 40);
        g.drawString(fromStation + " → " + toStation, 120, 75);

        g.setFont(new Font("SansSerif", Font.PLAIN, 16));
        g.drawString("出發時間：" + departureTime, 120, 110);
        g.drawString("抵達時間：" + arrivalTime, 120, 140);

    }
}
