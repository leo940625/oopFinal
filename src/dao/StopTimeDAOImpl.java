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

                Station station = new Station(stationId, sta.getStationById(stationId).getStationName()); // 你可以查 StationDAO 或暫時給個名稱
                StopTime stop = new StopTime(station, arrival, departure);
                stopTimes.add(stop);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return stopTimes;
    }

}
