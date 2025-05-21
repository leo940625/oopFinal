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

/**
 * TrainDAO 接口的實現類，用於管理 Train 數據的數據庫訪問操作。
 * 此類提供與數據庫中 Train 表交互的方法，包括添加車次、根據車次號查詢車次以及檢索所有車次。
 */
public class TrainDAOImpl implements TrainDAO {
    private Connection conn;

    /**
     * 構造函數，初始化數據庫連接。
     *
     * @param conn 數據庫連接對象
     */
    public TrainDAOImpl(Connection conn) {
        this.conn = conn;
    }

    /**
     * 向數據庫中添加一個新的車次及其相關的停靠時間信息。
     * 此方法使用事務控制，確保車次及其停靠時間的數據一致性。
     *
     * @param train 要添加的 {@link Train} 對象，包含車次號、方向和停靠時間列表
     * @throws SQLException 如果數據庫操作失敗
     */
    @Override
    public void addTrain(Train train) {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false); // 啟用交易控制

            // 插入 Train 資料
            String sqlTrain = "INSERT INTO Train (train_number, direction) VALUES (?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sqlTrain)) {
                stmt.setInt(1, train.getTrainNumber());
                stmt.setBoolean(2, train.getDirection());
                stmt.executeUpdate();
            }
            // 插入對應的 StopTime 資料
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

    /**
     * 根據車次號從數據庫中檢索單個車次信息，包括其停靠時間。
     *
     * @param trainNumber 車次號
     * @return 對應車次號的 {@link Train} 對象，如果未找到則返回 {@code null}
     * @throws SQLException 如果數據庫操作失敗
     */
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
                StopTimeDAO stopTimeDAO = new StopTimeDAOImpl(conn);
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

    /**
     * 從數據庫中檢索所有車次信息，包括每個車次的停靠時間。
     *
     * @return 包含所有車次的 {@link Train} 對象列表。如果未找到車次或發生錯誤，則返回空列表
     * @throws SQLException 如果數據庫操作失敗
     */
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