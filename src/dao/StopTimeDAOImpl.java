package dao;

import model.Station;
import model.StopTime;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * StopTimeDAO 接口的實現類，用於管理 StopTime 數據的數據庫訪問操作。
 * 此類提供與數據庫中 StopTime 表交互的方法，包括添加車次停靠時間和根據車次號查詢停靠時間。
 */
public class StopTimeDAOImpl implements StopTimeDAO {
    private Connection conn;

    /**
     * 構造函數，初始化數據庫連接。
     *
     * @param conn 數據庫連接對象
     */
    public StopTimeDAOImpl(Connection conn) {
        this.conn = conn;
    }

    /**
     * 為指定車次添加一組停靠時間信息到數據庫。
     * 每個停靠時間按順序插入，並設置停靠順序 (stop_order)。
     *
     * @param trainNumber 車次號
     * @param stopTimes   包含停靠時間信息的 {@link StopTime} 對象列表
     * @throws SQLException 如果數據庫操作失敗
     */
    @Override
    public void addStopTimes(int trainNumber, List<StopTime> stopTimes) {
        try {
            String sql = "INSERT INTO StopTime (train_number, station_id, arrival_time, departure_time, stop_order) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);

            for (int i = 0; i < stopTimes.size(); i++) {
                StopTime st = stopTimes.get(i);
                ps.setInt(1, trainNumber);
                ps.setInt(2, st.getStation().getStationId());
                ps.setTime(3, st.getArrivalTime() != null ? java.sql.Time.valueOf(st.getArrivalTime()) : null);
                ps.setTime(4, st.getDepartureTime() != null ? java.sql.Time.valueOf(st.getDepartureTime()) : null);
                ps.setInt(5, i + 1);
                ps.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根據車次號從數據庫中檢索該車次的所有停靠時間信息。
     * 停靠時間按 stop_order 排序，並包含對應車站信息。
     *
     * @param trainNumber 車次號
     * @return 包含該車次所有停靠時間的 {@link StopTime} 對象列表。如果未找到停靠時間或發生錯誤，則返回空列表
     * @throws SQLException 如果數據庫操作失敗
     */
    @Override
    public List<StopTime> getStopTimesByTrain(int trainNumber) {
        List<StopTime> stopTimes = new ArrayList<>();
        StationDAOImpl sta = new StationDAOImpl(conn);

        String sql = "SELECT station_id, arrival_time, departure_time FROM StopTime WHERE train_number = ? ORDER BY stop_order";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, trainNumber);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int stationId = rs.getInt("station_id");
                LocalTime arrival = rs.getTime("arrival_time") != null ? rs.getTime("arrival_time").toLocalTime() : null;
                LocalTime departure = rs.getTime("departure_time") != null ? rs.getTime("departure_time").toLocalTime() : null;

                Station station = new Station(stationId, sta.getStationById(stationId).getStationName());
                StopTime stop = new StopTime(station, arrival, departure);
                stopTimes.add(stop);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return stopTimes;
    }
}