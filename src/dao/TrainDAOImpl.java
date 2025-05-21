package dao;

import model.StopTime;
import model.Train;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TrainDAOImpl implements TrainDAO {
    private Connection conn;

    public TrainDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void addTrain(Train train) {
        // 取得資料庫連線
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false); // 啟用交易控制

            // 1. 插入 Train 資料
            String sqlTrain = "INSERT INTO Train (train_number, direction) VALUES (?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sqlTrain)) {
                stmt.setInt(1, train.getTrainNumber());
                stmt.setBoolean(2, train.getDirection());
                stmt.executeUpdate();
            }
            // 2. 插入對應的 StopTime 資料（呼叫 StopTimeDAO）
            StopTimeDAO stopTimeDAO = new StopTimeDAOImpl(conn);
            List<StopTime> stable = new ArrayList<>();
            for (StopTime st : train.getStopTimes()) {
                stable.add(st);
            }
            stopTimeDAO.addStopTimes(train.getTrainNumber(), stable);
            conn.commit(); // 提交交易
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Train getTrainByNumber(int trainNumber) {
        Train train = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            // 查詢 Train 基本資料（方向）
            String sql = "SELECT direction FROM Train WHERE train_number = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, trainNumber);
            rs = stmt.executeQuery();

            if (rs.next()) {
                boolean direction = rs.getBoolean("direction");

                // 查詢該車次的 StopTime 資料
                StopTimeDAO stopTimeDAO = new StopTimeDAOImpl(conn);  // 請確認此建構子存在
                List<StopTime> stopTimes = stopTimeDAO.getStopTimesByTrain(trainNumber);

                // 建立 Train 物件
                train = new Train(trainNumber, stopTimes, direction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
            } catch (Exception ignored) {
            }
            try {
                if (stmt != null) stmt.close();
            } catch (Exception ignored) {
            }
            try {
                if (conn != null) conn.close();
            } catch (Exception ignored) {
            }
        }
        return train;
    }

    @Override
    public List<Train> getAllTrains() {
        List<Train> trains = new ArrayList<>();
        String sql = "SELECT train_number, direction FROM Train";

        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int trainNumber = rs.getInt("train_number");
                boolean direction = rs.getBoolean("direction");

                // 透過 StopTimeDAO 取得對應的停靠資料
                List<StopTime> stopTimes = new StopTimeDAOImpl(conn).getStopTimesByTrain(trainNumber);
                Train train = new Train(trainNumber, stopTimes, direction);
                trains.add(train);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trains;
    }

}
