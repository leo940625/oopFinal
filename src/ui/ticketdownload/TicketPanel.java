package ui.ticketdownload;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.awt.geom.*;
import dao.TrainDAO;
import dao.TrainDAOImpl;
import util.DBConnection;

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
            Train train = trainDAO.getTrainByNumber(trainId);

            // 根據 fromStation 與 toStation 比對，取得時間
            this.departureTime = train.getTimeByStation(fromStation); // 自訂方法
            this.arrivalTime = train.getTimeByStation(toStation);     // 自訂方法
        } catch (SQLException e) {
            e.printStackTrace();
            this.departureTime = "??:??";
            this.arrivalTime = "??:??";
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
