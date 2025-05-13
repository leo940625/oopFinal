package dao;

import model.StopTime;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalTime;
import java.util.List;

public class StopTimeDAOImpl implements StopTimeDAO {
    private Connection conn;

    public StopTimeDAOImpl(Connection conn) {
        this.conn = conn;
    }

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
    @Override
    public List<StopTime> getStopTimesByTrain(int trainNumber) {
        // 可選功能：查詢某列車的所有停靠站
        return null;
    }
}
