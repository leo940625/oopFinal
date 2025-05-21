package dao;

import model.Station;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * StationDAO 接口的實現類，用於管理 Station 數據的訪問操作。
 * 此類提供了與數據庫中 Station 表交互的方法。
 */
public class StationDAOImpl implements StationDAO {
    private Connection conn;
    public StationDAOImpl(Connection conn) {
        this.conn = conn;
    }
    /**
     * 從數據庫中檢索所有車站。
     *
     * @return 包含數據庫中所有車站的 Station 對象列表。
     * 如果未找到車站或發生錯誤，則返回空列表。
     */
    @Override
    public List<Station> getAllStations() {
        List<Station> list = new ArrayList<>();
        String sql = "SELECT * FROM Station";

        try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("station_id");
                String name = rs.getString("station_name");
                list.add(new Station(id, name));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Station getStationById(int stationId) {
        String sql = "SELECT station_id, station_name FROM Station WHERE station_id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, stationId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("station_id");
                String name = rs.getString("station_name");
                return new Station(id, name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // 若找不到對應車站則回傳 null
    }

    @Override
    public Station getStationByName(String stationName) {
        String sql = "SELECT station_id, station_name FROM Station WHERE station_name = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, stationName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("station_id");
                String name = rs.getString("station_name");
                return new Station(id, name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // 找不到該站名時回傳 null
    }
}