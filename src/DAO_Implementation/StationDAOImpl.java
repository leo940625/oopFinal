package DAO_Implementation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StationDAOImpl implements StationDAO {
    @Override
    public List<Station> getAllStations() {
        List<Station> stations = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Station")) {

            while (rs.next()) {
                int id = rs.getInt("station_id");
                String name = rs.getString("station_name");
                stations.add(new Station(id, name));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stations;
    }

    @Override
    public Station getStationById(int id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM Station WHERE station_id = ?")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Station(rs.getInt("station_id"), rs.getString("station_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void addStation(Station station) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO Station (station_id, station_name) VALUES (?, ?)")) {
            ps.setInt(1, station.getStationId());
            ps.setString(2, station.getStationName());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateStation(Station station) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE Station SET station_name = ? WHERE station_id = ?")) {
            ps.setString(1, station.getStationName());
            ps.setInt(2, station.getStationId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteStation(int id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM Station WHERE station_id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

