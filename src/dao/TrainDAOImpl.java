package dao;

import util.DBConnection;
import java.util.*;
import model.*;
import java.sql.*;

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
            stopTimeDAO.addStopTimes(train.getTrainNumber(),stable);
            conn.commit(); // 提交交易
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Train getTrainByNumber(int trainNumber) {
        // 從 StopTime 資料表查詢指定車次的所有停靠時間
        List<StopTime> stopTimes = new StopTimeDAOImpl(conn).getStopTimesByTrain(trainNumber);

        // 回傳 Train 物件（此處方向 direction 預設為 true，實務上應該從資料庫一併查詢）
        return new Train(trainNumber, stopTimes, true);
    }

    @Override
    public List<StopTime> getStopTimesByTrain(int trainNumber) {
        List<StopTime> stopTimes = new ArrayList<>();

        // SQL 查詢語句，從 StopTime 表中 JOIN Station 表來取得車站資訊與時刻
        String sql = "SELECT s.station_id, s.station_name, st.arrival_time, st.departure_time " +
                "FROM StopTime st " +
                "JOIN Station s ON st.station_id = s.station_id " +
                "WHERE st.train_number = ? " +
                "ORDER BY st.stop_order";

        try (Connection conn = DBConnection.getConnection();  // 建立資料庫連線
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, trainNumber);  // 設定查詢參數
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int stationId = rs.getInt("station_id");
                    String stationName = rs.getString("station_name");
                    Time arrival = rs.getTime("arrival_time");
                    Time departure = rs.getTime("departure_time");

                    // 組合成 Station 和 StopTime 物件
                    Station station = new Station(stationId, stationName);
                    StopTime stopTime = new StopTime(
                            station,
                            arrival != null ? arrival.toLocalTime() : null,
                            departure != null ? departure.toLocalTime() : null
                    );

                    stopTimes.add(stopTime);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();  // 印出錯誤訊息
        }
        return stopTimes;  // 回傳查詢結果
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
